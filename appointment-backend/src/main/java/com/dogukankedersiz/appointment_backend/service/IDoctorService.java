package com.dogukankedersiz.appointment_backend.service;

import com.dogukankedersiz.appointment_backend.dto.DtoDoctor;
import com.dogukankedersiz.appointment_backend.entities.Doctor;

import java.util.List;
import java.util.Optional;

public interface IDoctorService {

    public List<DtoDoctor> getAllDoctors();
    public DtoDoctor getDoctorById(Long id);
    public DtoDoctor createDoctor(DtoDoctor doctor);
    public DtoDoctor updateDoctor(Long id,DtoDoctor doctor);
    public void deleteDoctor(Long id);
    public List<String> getAvailableHours(Long id);
    public List<DtoDoctor> getDoctorsBySpecialty(String specialty);


 }
