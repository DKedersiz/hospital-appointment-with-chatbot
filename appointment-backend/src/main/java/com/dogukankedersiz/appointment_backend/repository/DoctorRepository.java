package com.dogukankedersiz.appointment_backend.repository;

import com.dogukankedersiz.appointment_backend.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d WHERE d.firstName = :firstName AND d.lastName = :lastName")
    Optional<Doctor> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

}
