package com.drmejia.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drmejia.core.domain.services.interfaces.PatientService;

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


}
