package com.dogukankedersiz.appointment_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoPatient {
    private Long id;
    @NotBlank(message = "İsim boş olamaz")
    private String firstName;
    @NotBlank(message = "Soyisim boş olamaz")
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Date birthDate;
    private List<DtoAppointment> appointments;


    public DtoPatient(Long id, String firstName, String lastName, String email, String phoneNumber, Date birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }
}