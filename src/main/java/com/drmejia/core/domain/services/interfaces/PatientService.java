package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Patient;
import com.drmejia.core.exceptions.BadRequestException;


public interface PatientService {

    public List<Patient> getAllPatients();
    public void modifyPatient(Patient patient) throws BadRequestException;
    public void updatePatient(Patient patient) throws BadRequestException;
    public void savePatient(Patient patient);
    public void deletePatient(String document);
}