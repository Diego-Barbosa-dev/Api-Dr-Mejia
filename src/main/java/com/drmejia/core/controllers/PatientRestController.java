package com.drmejia.core.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drmejia.core.domain.models.Patient;
import com.drmejia.core.domain.services.interfaces.PatientService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/patients")
public class PatientRestController {

    @Autowired //Autowired para que se asigne la dependencia de manera automática
    private PatientService patientService; //Inyectamos la dependencia para que las clases no queden acopladas
    
    @GetMapping
    public ResponseEntity<?> getPatients(){
        List<?> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{document}")
    public ResponseEntity<?> getByDocument(@PathVariable("document") String document) {
        return patientService.getAllPatients().stream()
            .filter(patient -> patient.getDocument().equalsIgnoreCase(document))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Patient: " + document + " Not Found"));
    }
    

    @PostMapping
    public ResponseEntity<?> postPatient(@RequestBody Patient patient) {
        if(patient.hasNullAttributes()){
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nDocument: " + patient.getDocument()
                + "\nName: " + patient.getName()
                + "\nAddress: " + patient.getAddress()
                + "\nEmail: " + patient.getEmail()
            );
        }
        if(patient.getDocument().length() <3){
            throw new BadRequestException("Document Length Is Not Valid");
        }
        if (!patient.getEmail().contains("@")) {
            throw new BadRequestException("Email Is Not Valid");
        }
        patientService.savePatient(patient);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{document}")
            .buildAndExpand(patient.getDocument())
            .toUri();
        return ResponseEntity.created(location).body(patient);
    }

    @PutMapping("/{document}")
    public ResponseEntity<?> putPatient(@PathVariable("document") String document, @RequestBody Patient patient) {
        
        patient.setDocument(document);

        patientService.updatePatient(patient);
        
        return null;
    }

    @DeleteMapping("/{document}")
    public ResponseEntity<?> deletePatient(@PathVariable("document") String document){
        patientService.deletePatient(document);
        return ResponseEntity.noContent().build();
    }
    
}
