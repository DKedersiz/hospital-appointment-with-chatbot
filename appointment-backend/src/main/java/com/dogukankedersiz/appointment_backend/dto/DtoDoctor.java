package com.dogukankedersiz.appointment_backend.dto;

import com.dogukankedersiz.appointment_backend.entities.Appointment;
import com.dogukankedersiz.appointment_backend.entities.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DtoDoctor {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String email;
    private List<String> availableHours;

    public DtoDoctor(String firstName, List<String> availableHours) {
        this.firstName = firstName;
        this.availableHours = availableHours;
    }

    public DtoDoctor(String firstName, String lastName, String specialty, List<String> availableHours) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.availableHours = availableHours;
    }

    public DtoDoctor(Long id, String firstName, String lastName, String specialty, List<String> availableHours) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.availableHours = availableHours;
    }




    public Doctor toDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(this.id);
        doctor.setFirstName(this.firstName);
        doctor.setLastName(this.lastName);
        doctor.setSpecialty(this.specialty);
        doctor.setEmail(this.email);
        doctor.setAvailableHours(this.availableHours);
        return doctor;
    }
}