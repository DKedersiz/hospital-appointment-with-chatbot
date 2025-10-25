package com.dogukankedersiz.appointment_backend.controller.impl;

import com.dogukankedersiz.appointment_backend.controller.IAppointmentController;
import com.dogukankedersiz.appointment_backend.dto.DtoAppointment;
import com.dogukankedersiz.appointment_backend.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/appointment")
public class AppointmentControllerImpl implements IAppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<DtoAppointment> createAppointment(@RequestBody DtoAppointment dtoAppointment) {
        DtoAppointment createdAppointment = appointmentService.createAppointment(dtoAppointment);
        return ResponseEntity.ok(createdAppointment);
    }

    @GetMapping("/getByPatientEmail")
    public ResponseEntity<List<DtoAppointment>> getAppointmentsByPatientEmail(@RequestParam String email) {
        List<DtoAppointment> appointments = appointmentService.getAppointmentsByPatientEmail(email);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getByPatientId")
    public ResponseEntity<List<DtoAppointment>> getAppointmentsByPatient(@RequestParam Long patientId) {
        List<DtoAppointment> appointments = appointmentService.getAppointmentByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getByDoctorId")
    public ResponseEntity<List<DtoAppointment>> getAppointmentsByDoctor(@RequestParam Long doctorId) {
        List<DtoAppointment> appointments = appointmentService.getAppointmentByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DtoAppointment>> getAllAppointments() {
        List<DtoAppointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoAppointment> getAppointmentById(@PathVariable Long id) {
        DtoAppointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoAppointment> updateAppointment(@PathVariable Long id, @RequestBody DtoAppointment dtoAppointment) {
        DtoAppointment updatedAppointment = appointmentService.updateAppointment(dtoAppointment, id);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/delete/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}