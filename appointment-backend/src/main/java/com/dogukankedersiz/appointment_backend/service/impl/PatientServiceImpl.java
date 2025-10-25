package com.dogukankedersiz.appointment_backend.service.impl;

import com.dogukankedersiz.appointment_backend.dto.DtoPatient;
import com.dogukankedersiz.appointment_backend.entities.Patient;
import com.dogukankedersiz.appointment_backend.entities.User;
import com.dogukankedersiz.appointment_backend.repository.PatientRepository;
import com.dogukankedersiz.appointment_backend.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    private DtoPatient toDto(Patient patient) {
        return new DtoPatient(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getUser() != null ? patient.getUser().getEmail() : null,
                patient.getPhoneNumber(),
                patient.getBirthDate()
        );
    }

    @Override
    public List<DtoPatient> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DtoPatient getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aranan hasta bulunamadı"));
        return toDto(patient);
    }

    @Override
    public DtoPatient getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı userId ile: " + userId));
        return toDto(patient);
    }

    @Override
    public void deletePatientById(Long id) {
        Patient deletedPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aranan hasta bulunamadı"));
        patientRepository.delete(deletedPatient);
    }

    @Override
    @Transactional
    public DtoPatient createPatient(DtoPatient dtoPatient) {
        try {
            if (dtoPatient.getPassword() == null || dtoPatient.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Şifre boş olamaz.");
            }
            String encodedPassword = passwordEncoder.encode(dtoPatient.getPassword());
            User user = userService.createUser(dtoPatient.getEmail(), encodedPassword);
            System.out.println("User oluşturuldu: " + user.getEmail() + ", ID: " + user.getId());

            Patient patient = new Patient();
            patient.setFirstName(dtoPatient.getFirstName());
            patient.setLastName(dtoPatient.getLastName());
            patient.setPhoneNumber(dtoPatient.getPhoneNumber());
            patient.setBirthDate(dtoPatient.getBirthDate());
            patient.setUser(user);

            Patient savedPatient = patientRepository.save(patient);
            System.out.println("Patient kaydedildi: " + savedPatient.getId());
            return toDto(savedPatient);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Bu e-posta adresi zaten kayıtlı: " + dtoPatient.getEmail(), e);
        } catch (Exception e) {
            System.err.println("Patient oluşturma hatası: " + e.getMessage());
            throw new RuntimeException("Patient oluşturma başarısız: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public DtoPatient updatePatient(Long id, DtoPatient dtoPatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı: " + id));

        if (dtoPatient.getFirstName() != null) {
            patient.setFirstName(dtoPatient.getFirstName());
        }
        if (dtoPatient.getLastName() != null) {
            patient.setLastName(dtoPatient.getLastName());
        }
        if (dtoPatient.getPhoneNumber() != null) {
            patient.setPhoneNumber(dtoPatient.getPhoneNumber());
        }
        if (dtoPatient.getBirthDate() != null) {
            patient.setBirthDate(dtoPatient.getBirthDate());
        }

        Patient updatedPatient = patientRepository.save(patient);
        return toDto(updatedPatient);
    }
}