package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Patient;


public interface PatientService {

    public List<Patient> getAllPatients();
    public void modifyPatient(Patient patient);
    public void savePatient(Patient patient);
    public void deletePatient(String document);
}