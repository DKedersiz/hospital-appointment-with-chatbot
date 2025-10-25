package com.dogukankedersiz.appointment_backend.service;

import com.dogukankedersiz.appointment_backend.dto.DtoAppointment;

import java.util.List;

public interface IAppointmentService {
    List<DtoAppointment> getAllAppointments();
    DtoAppointment getAppointmentById(Long id);
    DtoAppointment createAppointment(DtoAppointment dtoAppointment);
    DtoAppointment updateAppointment(DtoAppointment dtoAppointment, Long id);
    void deleteAppointment(Long id);
    List<DtoAppointment> getAppointmentByDoctor(Long doctorId);
    List<DtoAppointment> getAppointmentByPatient(Long patientId);
    List<DtoAppointment> getAppointmentsByPatientEmail(String email); // Yeni metod
}