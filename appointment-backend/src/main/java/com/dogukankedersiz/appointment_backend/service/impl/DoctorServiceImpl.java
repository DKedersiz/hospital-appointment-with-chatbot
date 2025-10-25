package com.dogukankedersiz.appointment_backend.service.impl;

import com.dogukankedersiz.appointment_backend.dto.DtoDoctor;
import com.dogukankedersiz.appointment_backend.entities.Doctor;
import com.dogukankedersiz.appointment_backend.repository.DoctorRepository;
import com.dogukankedersiz.appointment_backend.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements IDoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    private DtoDoctor toDto(Doctor doctor) {
        return new DtoDoctor(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialty(),
                doctor.getEmail(),
                doctor.getAvailableHours()
        );
    }

    @Override
    public List<DtoDoctor> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctor -> new DtoDoctor(doctor.getId(),doctor.getFirstName(),doctor.getLastName(),doctor.getSpecialty(), doctor.getAvailableHours()))
                .collect(Collectors.toList());
    }

    @Override
    public DtoDoctor getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
        return toDto(doctor);

    }

    @Override
    public DtoDoctor createDoctor(DtoDoctor dtoDoctor) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(dtoDoctor.getFirstName());
        doctor.setLastName(dtoDoctor.getLastName());
        doctor.setSpecialty(dtoDoctor.getSpecialty());
        doctor.setAvailableHours(dtoDoctor.getAvailableHours());
        doctor.setEmail(dtoDoctor.getEmail() != null ? dtoDoctor.getEmail() : "default@example.com");

        Doctor savedDoctor = doctorRepository.save(doctor);
        return toDto(savedDoctor);
    }

    @Transactional
    @Override
    public DtoDoctor updateDoctor(Long id, DtoDoctor doctorDto) {
        try {
            Doctor doctor = doctorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Doktor bulunamadı: " + id));

            if (doctorDto.getFirstName() != null) {
                doctor.setFirstName(doctorDto.getFirstName());
            }
            if (doctorDto.getLastName() != null) {
                doctor.setLastName(doctorDto.getLastName());
            }
            if (doctorDto.getSpecialty() != null) {
                doctor.setSpecialty(doctorDto.getSpecialty());
            }
            if (doctorDto.getEmail() != null) {
                doctor.setEmail(doctorDto.getEmail());
            }
            if (doctorDto.getAvailableHours() != null) {
                doctor.setAvailableHours(doctorDto.getAvailableHours());
            }

            Doctor updatedDoctor = doctorRepository.save(doctor);
            return toDto(updatedDoctor);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Güncelleme başarısız: Benzersiz alan çakışması (örneğin email veya isim zaten kullanılıyor).", e);
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()-> new RuntimeException("Doktor bulunamadı"));
        doctorRepository.delete(doctor);
    }

    @Override
    public List<String> getAvailableHours(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()-> new RuntimeException("Doktor bulunamadı"));
        return doctor.getAvailableHours();
    }

    @Override
    public List<DtoDoctor> getDoctorsBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return doctors.stream().map(this::toDto).collect(Collectors.toList());
    }
}
