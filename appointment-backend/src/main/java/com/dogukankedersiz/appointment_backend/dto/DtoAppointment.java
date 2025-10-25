package com.dogukankedersiz.appointment_backend.dto;

import com.dogukankedersiz.appointment_backend.entities.Doctor;
import com.dogukankedersiz.appointment_backend.entities.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DtoAppointment {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String appointmentDate;
    private String status;

    public DtoAppointment(Long id, LocalDateTime appointmentDate, Doctor doctor, Patient patient, String status) {
        this.id = id;
        this.appointmentDate = appointmentDate.toString();
        this.doctorId = doctor.getId();
        this.doctorName = doctor.getFirstName() + " " + doctor.getLastName();
        this.patientId = patient.getId();
        this.patientName = patient.getFirstName() + " " + patient.getLastName();
        this.status = status;
    }
}