package com.dogukankedersiz.appointment_backend.repository;

import com.dogukankedersiz.appointment_backend.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    @Query("SELECT a FROM Appointment a JOIN a.patient p JOIN p.user u WHERE u.email = :email")
    List<Appointment> findByPatientEmail(String email);
}