package com.drmejia.core.services.interfaces;

import java.util.List;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.models.Patient;


public interface PatientService {

    public List<Patient> getAllPatients();
    public void modifyPatient(Patient patient) throws BadRequestException;
    public void updatePatient(Patient patient) throws BadRequestException;
    public void savePatient(Patient patient);
    public void deletePatient(String document);
}