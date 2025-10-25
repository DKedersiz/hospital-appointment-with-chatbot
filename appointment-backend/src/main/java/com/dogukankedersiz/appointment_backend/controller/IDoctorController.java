package com.dogukankedersiz.appointment_backend.controller;

import com.dogukankedersiz.appointment_backend.dto.DtoDoctor;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDoctorController {

    ResponseEntity<List<DtoDoctor>> getAllDoctors();
    ResponseEntity<DtoDoctor> getDoctorById(Long id);
    ResponseEntity<DtoDoctor> createDoctor(DtoDoctor doctorDTO);
    ResponseEntity<DtoDoctor> updateDoctor(Long id, DtoDoctor doctorDTO);
    ResponseEntity<Void> deleteDoctor(Long id);
    ResponseEntity<List<String>> getAvailableHours(Long id);

}
