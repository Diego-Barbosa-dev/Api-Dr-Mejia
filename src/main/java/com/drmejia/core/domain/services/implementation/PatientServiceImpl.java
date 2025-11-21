package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Patient;
import com.drmejia.core.domain.services.interfaces.PatientService;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.PatientEntity;
import com.drmejia.core.persistence.repository.PatientRepository;

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
            patientEntity.getAddress() 
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
    public void modifyPatient(Patient patient) {
        PatientEntity patientEntity = patientRepository
        .findByDocument(patient.getDocument())
        .orElseThrow(this::nonExistingPatient);
        
        patientEntity.setDocument(patient.getDocument());
        patientEntity.setEmail(patient.getEmail());
        patientEntity.setAddress(patient.getAddress());
        patientEntity.setName(patient.getName());
        
        patientRepository.save(patientEntity);
    }

    public void modifyPatientId(Long newId, Long oldId){
        PatientEntity patientEntity = patientRepository.findById(oldId).
        orElseThrow(this::nonExistingPatient);

        patientEntity.setIdPatient(newId);
        patientRepository.save(patientEntity);
    }

    public void modifyPatientDocument(String newDocument, String oldDocument){
        PatientEntity patientEntity = patientRepository.findByDocument(oldDocument)
        .orElseThrow(this::nonExistingPatient);

        patientEntity.setDocument(newDocument);
        patientRepository.save(patientEntity);
    }

    public void modifyPatientName(@NonNull String document, @NonNull String name){
        PatientEntity patientEntity = patientRepository.findByDocument(document)
        .orElseThrow(this::nonExistingPatient);

        patientEntity.setName(name);
        patientRepository.save(patientEntity);
    }

    public void modifyPatientAddress(@NonNull String document, @NonNull String address){
        PatientEntity patientEntity = patientRepository.findByDocument(document)
        .orElseThrow(this::nonExistingPatient);

        patientEntity.setAddress(address);
        patientRepository.save(patientEntity);
    }



    /* Delete an existing patient from the database */

    @Override
    public void deletePatient(String document) {
        patientRepository.findByDocument(document)
        .orElseThrow(this::nonExistingPatient);
        patientRepository.deleteByDocument(document);
    }


}
