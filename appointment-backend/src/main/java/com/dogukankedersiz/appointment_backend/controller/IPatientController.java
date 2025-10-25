package com.dogukankedersiz.appointment_backend.controller;

import com.dogukankedersiz.appointment_backend.dto.DtoDoctor;
import com.dogukankedersiz.appointment_backend.dto.DtoPatient;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPatientController {
    ResponseEntity<List<DtoPatient>> getAllPatients();
    ResponseEntity<DtoPatient> getPatientById(Long id);
    ResponseEntity<DtoPatient> createPatient(DtoPatient doctorDTO);
    ResponseEntity<DtoPatient> updatePatient(Long id, DtoPatient patientDto);
    ResponseEntity<Void> deletePatientById(Long id);
}
