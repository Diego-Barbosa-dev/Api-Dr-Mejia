package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Patient;
import com.drmejia.core.domain.services.interfaces.PatientService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.PatientEntity;
import com.drmejia.core.persistence.repository.PatientRepository;

import jakarta.transaction.Transactional;
import lombok.NonNull;



@Service 
/*
 * Los servicios deben marcarse para reconocer que lo son
 */
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    private ResourceNotFoundException nonExistingPatient(){
        return new ResourceNotFoundException("Patient Not Found In DataBase");
    }

    /* Create a new patient for the database */

    @Override
    public void savePatient(Patient patient) {
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setName(patient.getName());
        patientEntity.setEmail(patient.getEmail());
        patientEntity.setDocument(patient.getDocument());
        patientEntity.setAddress(patient.getAddress());

        patientRepository.save(patientEntity);
    }

    /* Read all patients from database */

    @Override
    public List<Patient> getAllPatients() {
        List<PatientEntity> patientEntities = patientRepository.findAll();
        List<Patient> patients = new ArrayList<>();

        for(PatientEntity patientEntity : patientEntities ){
            Patient patient = new Patient(
            patientEntity.getDocument(),
            patientEntity.getName(),
            patientEntity.getEmail(),
            patientEntity.getAddress(),
            patientEntity.getNotes()
            );

            patients.add(patient);
        }
        return patients;
    }

    /* 
     * Modify an existing patient or update a patient
     * these all are modify methods until next comment
     */
    @Override
    public void modifyPatient(Patient patient) throws BadRequestException {
        /* PATCH HTTP METHOD */
        if(patient.getDocument() == null || patient.getDocument().isBlank()){
            throw new BadRequestException("Patient Document Can't Be Null Or Blank");
        }
        PatientEntity patientEntity = patientRepository
        .findByDocument(patient.getDocument())
        .orElseThrow(this::nonExistingPatient);
        
        if(patient.getName() != null && !patient.getName().isBlank()){
            patientEntity.setName(patient.getName());
        }
        if(patient.getEmail() != null && !patient.getEmail().isBlank()){
            patientEntity.setEmail(patient.getEmail());
        }
        if(patient.getAddress() != null && !patient.getAddress().isBlank()){
            patientEntity.setAddress(patient.getAddress());
        }
        if(patient.getNotes() != null && !patient.getNotes().isBlank()){
            patientEntity.setNotes(patient.getNotes());
        }

        if (patientEntity == null) {
            throw new BadRequestException("No fields to update");
        }
        
        patientRepository.save(patientEntity);
    }

    @Override
    public void updatePatient(Patient patient) throws BadRequestException {
        /* PUT HTTP METHOD */
        if(patient.getDocument() == null || patient.getDocument().isBlank()){
            throw new BadRequestException("Patient Document Can't Be Null Or Blank");
        }
        if(patient.getName() == null || patient.getName().isBlank()){
            throw new BadRequestException("Patient Name Can't Be Null Or Blank");
        }
        if(patient.getEmail() == null || patient.getEmail().isBlank()){
            throw new BadRequestException("Patient Email Can't Be Null Or Blank");
        }
        if(patient.getAddress() == null || patient.getAddress().isBlank()){
            throw new BadRequestException("Patient Address Can't Be Null Or Blank");
        }

        PatientEntity patientEntity = patientRepository
        .findByDocument(patient.getDocument())
        .orElseThrow(this::nonExistingPatient);
        
        patientEntity.setDocument(patient.getDocument());
        patientEntity.setEmail(patient.getEmail());
        patientEntity.setAddress(patient.getAddress());
        patientEntity.setName(patient.getName());
        patientEntity.setNotes(patient.getNotes());
        
        patientRepository.save(patientEntity);
    }


    /* Delete an existing patient from the database */
    @Transactional
    @Override
    public void deletePatient(@NonNull String document) {
        if(!patientRepository.existsByDocument(document)){
            throw nonExistingPatient();
        }
        patientRepository.deleteByDocument(document);
    }


}
