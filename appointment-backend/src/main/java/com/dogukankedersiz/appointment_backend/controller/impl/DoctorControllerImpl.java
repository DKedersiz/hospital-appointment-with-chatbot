package com.dogukankedersiz.appointment_backend.controller.impl;

import com.dogukankedersiz.appointment_backend.controller.IDoctorController;
import com.dogukankedersiz.appointment_backend.dto.DtoDoctor;
import com.dogukankedersiz.appointment_backend.repository.DoctorRepository;
import com.dogukankedersiz.appointment_backend.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/api/doctor")
public class DoctorControllerImpl implements IDoctorController {


    private final  IDoctorService doctorService;

    @Autowired
    public DoctorControllerImpl(IDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    @GetMapping(path = "/getAllDoctors")
    public ResponseEntity<List<DtoDoctor>> getAllDoctors() {
        List<DtoDoctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @Override
    @GetMapping(path = "/getDoctorById/{id}")
    public ResponseEntity<DtoDoctor> getDoctorById(@PathVariable(name = "id") Long id) {
        DtoDoctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @Override
    @PostMapping(path = "/create")
    public ResponseEntity<DtoDoctor> createDoctor(@RequestBody DtoDoctor doctor) {
        DtoDoctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    }

    @Override
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<DtoDoctor> updateDoctor(@PathVariable Long id, @RequestBody DtoDoctor doctor) {
        DtoDoctor updatedDoctor = doctorService.updateDoctor(id, doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    @Override
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable(name = "id") Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping(path = "/getAvailableHours/{id}")
    public ResponseEntity<List<String>> getAvailableHours(@PathVariable(name = "id") Long id) {
        List<String> availableHours = doctorService.getAvailableHours(id);
        return ResponseEntity.ok(availableHours);
    }
}
