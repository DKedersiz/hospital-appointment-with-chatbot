package com.dogukankedersiz.appointment_backend.service;

import com.dogukankedersiz.appointment_backend.dto.DtoPatient;

import java.util.List;

public interface IPatientService {

    public List<DtoPatient> getAllPatients();
    public DtoPatient getPatientById(Long id);
    public void deletePatientById(Long id);
    public DtoPatient createPatient(DtoPatient patient);
    public DtoPatient updatePatient(Long id, DtoPatient dtoPatient);

    public DtoPatient getPatientByUserId(Long userId);
}
