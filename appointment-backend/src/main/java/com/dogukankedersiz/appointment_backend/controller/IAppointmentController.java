package com.dogukankedersiz.appointment_backend.controller;

import com.dogukankedersiz.appointment_backend.dto.DtoAppointment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAppointmentController {
    ResponseEntity<List<DtoAppointment>> getAllAppointments();
    ResponseEntity<DtoAppointment> getAppointmentById(Long id);
    ResponseEntity<DtoAppointment> createAppointment(DtoAppointment dtoAppointment);
    ResponseEntity<DtoAppointment> updateAppointment(Long id, DtoAppointment dtoAppointment);
    ResponseEntity<Void> deleteAppointment(Long id);
    ResponseEntity<List<DtoAppointment>> getAppointmentsByDoctor(Long doctorId);
    ResponseEntity<List<DtoAppointment>> getAppointmentsByPatient(Long patientId);
}