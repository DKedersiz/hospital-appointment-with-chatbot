package com.dogukankedersiz.appointment_backend.controller.impl;

import com.dogukankedersiz.appointment_backend.controller.IPatientController;
import com.dogukankedersiz.appointment_backend.dto.DtoPatient;
import com.dogukankedersiz.appointment_backend.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/api/patient")
public class PatientControllerImpl implements IPatientController {

    private final IPatientService patientService;

    @Autowired
    public PatientControllerImpl(IPatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    @GetMapping(path = "/all")
    public ResponseEntity<List<DtoPatient>> getAllPatients() {
        List<DtoPatient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<DtoPatient> getPatientById(@PathVariable(name = "id") Long id) {
        DtoPatient dtoPatient = patientService.getPatientById(id);
        return ResponseEntity.ok(dtoPatient);
    }

    @GetMapping(path = "/byUserId")
    public ResponseEntity<DtoPatient> getPatientByUserId(@RequestParam Long userId) {
        DtoPatient dtoPatient = patientService.getPatientByUserId(userId);
        return ResponseEntity.ok(dtoPatient);
    }

    @Override
    @PostMapping(path = "/create")
    public ResponseEntity<DtoPatient> createPatient(@RequestBody DtoPatient dtoPatient) {
        DtoPatient createdPatient = patientService.createPatient(dtoPatient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @Override
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<DtoPatient> updatePatient(@PathVariable(name = "id") Long id, @RequestBody DtoPatient patientDto) {
        DtoPatient updatedPatient = patientService.updatePatient(id, patientDto);
        return ResponseEntity.ok(updatedPatient);
    }

    @Override
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable(name = "id") Long id) {
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }
}