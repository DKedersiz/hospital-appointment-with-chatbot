package com.dogukankedersiz.appointment_backend.service.impl;

import com.dogukankedersiz.appointment_backend.dto.DtoAppointment;
import com.dogukankedersiz.appointment_backend.entities.Appointment;
import com.dogukankedersiz.appointment_backend.entities.Doctor;
import com.dogukankedersiz.appointment_backend.entities.Patient;
import com.dogukankedersiz.appointment_backend.entities.User;
import com.dogukankedersiz.appointment_backend.repository.AppointmentRepository;
import com.dogukankedersiz.appointment_backend.repository.DoctorRepository;
import com.dogukankedersiz.appointment_backend.repository.PatientRepository;
import com.dogukankedersiz.appointment_backend.repository.UserRepository;
import com.dogukankedersiz.appointment_backend.service.impl.EmailService;
import com.dogukankedersiz.appointment_backend.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, UserRepository userRepository, EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    private DtoAppointment toDto(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Randevu nesnesi null olamaz.");
        }
        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();
        if (doctor == null) {
            throw new IllegalStateException("Randevu için doktor bilgisi eksik: " + appointment.getId());
        }
        if (patient == null) {
            throw new IllegalStateException("Randevu için hasta bilgisi eksik: " + appointment.getId());
        }
        return new DtoAppointment(
                appointment.getId(),
                appointment.getAppointmentDate(),
                doctor,
                patient,
                appointment.getStatus() != null ? appointment.getStatus() : "Bilinmeyen Durum"
        );
    }

    @Override
    public List<DtoAppointment> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public DtoAppointment getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı: " + id));
        return toDto(appointment);
    }

    @Override
    @Transactional
    public DtoAppointment createAppointment(DtoAppointment dtoAppointment) {
        Doctor doctor = doctorRepository.findById(dtoAppointment.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı: " + dtoAppointment.getDoctorId()));
        Patient patient = patientRepository.findById(dtoAppointment.getPatientId())
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı: " + dtoAppointment.getPatientId()));

        if (patient.getFirstName() == null || patient.getLastName() == null) {
            throw new IllegalArgumentException("Hasta kaydında firstName ve lastName alanları zorunludur.");
        }

        LocalDateTime localDateTime = LocalDateTime.parse(dtoAppointment.getAppointmentDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String appointmentHour = localDateTime.format(formatter);


        if (!doctor.getAvailableHours().contains(appointmentHour)) {
            throw new RuntimeException("Doktor bu saatte müsait değil: " + appointmentHour);
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(localDateTime);
        appointment.setStatus(dtoAppointment.getStatus() != null ? dtoAppointment.getStatus() : "ONAYLANDI");

        Appointment savedAppointment = appointmentRepository.save(appointment);

        if (savedAppointment.getDoctor().getAppointments() == null) {
            savedAppointment.getDoctor().setAppointments(new ArrayList<>());
        }
        savedAppointment.getDoctor().getAppointments().add(savedAppointment);

        doctor.getAvailableHours().remove(appointmentHour);
        doctorRepository.save(doctor);


        String patientEmail = patient.getUser().getEmail();
        String subject = "Randevu Onayınız";
        String body = String.format(
                "Sayın %s %s,\n\nRandevunuz başarıyla oluşturulmuştur:\n" +
                        "- Doktor: %s %s\n" +
                        "- Tarih: %s\n" +
                        "- Durum: %s\n\n" +
                        "İyi günler dileriz!\n" +
                        "Appointment Sistemi",
                patient.getFirstName(), patient.getLastName(),
                doctor.getFirstName(), doctor.getLastName(),
                localDateTime.toString(),
                savedAppointment.getStatus()
        );
        emailService.sendAppointmentConfirmation(patientEmail, subject, body);

        DtoAppointment responseDto = new DtoAppointment();
        responseDto.setId(savedAppointment.getId());
        responseDto.setDoctorId(savedAppointment.getDoctor().getId());
        responseDto.setDoctorName(savedAppointment.getDoctor().getFirstName() + " " + savedAppointment.getDoctor().getLastName());
        responseDto.setPatientId(savedAppointment.getPatient().getId());
        responseDto.setPatientName(savedAppointment.getPatient().getFirstName() + " " + savedAppointment.getPatient().getLastName());
        responseDto.setAppointmentDate(savedAppointment.getAppointmentDate().toString());
        responseDto.setStatus(savedAppointment.getStatus());

        return responseDto;
    }

    @Override
    @Transactional
    public DtoAppointment updateAppointment(DtoAppointment dtoAppointment, Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı: " + id));

        if (dtoAppointment.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(dtoAppointment.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doktor bulunamadı: " + dtoAppointment.getDoctorId()));
            appointment.setDoctor(doctor);
        }

        if (dtoAppointment.getPatientId() != null) {
            Patient patient = patientRepository.findById(dtoAppointment.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Hasta bulunamadı: " + dtoAppointment.getPatientId()));
            appointment.setPatient(patient);
        }

        if (dtoAppointment.getAppointmentDate() != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(dtoAppointment.getAppointmentDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String appointmentHour = localDateTime.format(formatter);
            if (!appointment.getDoctor().getAvailableHours().contains(appointmentHour)) {
                throw new RuntimeException("Doktor bu saatte müsait değil: " + appointmentHour);
            }
            appointment.setAppointmentDate(localDateTime);
        }
        if (dtoAppointment.getStatus() != null) {
            appointment.setStatus(dtoAppointment.getStatus());
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return toDto(updatedAppointment);
    }

    @Transactional
    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı: " + id));


        Doctor doctor = appointment.getDoctor();
        if (doctor == null) {
            throw new RuntimeException("Randevu ile ilişkili doktor bulunamadı.");
        }


        LocalDateTime appointmentDateTime = appointment.getAppointmentDate();
        String hour = appointmentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));


        List<String> availableHours = doctor.getAvailableHours();
        if (!availableHours.contains(hour)) {
            availableHours.add(hour);
            availableHours.sort(String::compareTo);
            doctor.setAvailableHours(availableHours);
            doctorRepository.save(doctor);
        }


        appointmentRepository.delete(appointment);
    }

    @Override
    public List<DtoAppointment> getAppointmentByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DtoAppointment> getAppointmentByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DtoAppointment> getAppointmentsByPatientEmail(String email) {
        System.out.println("getAppointmentsByPatientEmail called with email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));
        System.out.println("User found: " + user.getId() + ", Email: " + user.getEmail());
        Patient patient = user.getPatient();
        if (patient == null) {
            throw new RuntimeException("Kullanıcıya bağlı hasta bulunamadı: " + email);
        }
        System.out.println("Patient found: " + patient.getId());
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());
        System.out.println("Appointments found: " + appointments.size());
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}