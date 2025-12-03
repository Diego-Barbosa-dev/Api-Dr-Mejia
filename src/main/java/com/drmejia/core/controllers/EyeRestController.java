package com.drmejia.core.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drmejia.core.domain.models.Eye;
import com.drmejia.core.domain.services.interfaces.EyeService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("api/eyes")
public class EyeRestController {
    
    @Autowired
    private EyeService eyeService;

    
    @GetMapping
    public ResponseEntity<?> getEyes() {
        return ResponseEntity.ok(eyeService.getAllEyes());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return eyeService.getAllEyes().stream()
            .filter(eye -> eye.getIdEye().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Eye: " + id + " Not Found"));
    }


    @PostMapping
    public ResponseEntity<?> postEye(@RequestBody Eye eye) {
        if (eye.hasNullAttributes()) {
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nId: " + eye.getIdEye()
                + "\nOrder: " + eye.getOrder()
                + "\nType: " + eye.getType()
                + "\nEsf: " + eye.getEsf()
                + "\nCil: " + eye.getCil()
                + "\nAxis: " + eye.getAxis()
                + "\nAddition: " + eye.getAddition()
                + "\nDp: " + eye.getDp()
                + "\nHigh: " + eye.getHigh()
                + "\nAvl: " + eye.getAvl()
                + "\nAvp: " + eye.getAvp()
            );
        }

        if (eye.getType() == null) {
            throw new BadRequestException("Eye Type Cannot Be Null");
        }

        eyeService.saveEye(eye);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(eye.getIdEye())
            .toUri();
        return ResponseEntity.created(location).body(eye);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> putEye(@PathVariable("id") Long id, @RequestBody Eye eye) {
        eye.setIdEye(id);
        try {
            eyeService.updateEye(eye);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(eye);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> patchEye(@PathVariable("id") Long id, @RequestBody Eye eye) {
        eye.setIdEye(id);
        try {
            eyeService.modifyEye(eye);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(eye);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEye(@PathVariable("id") Long id) {
        eyeService.deleteEye(id);
        return ResponseEntity.noContent().build();
    }
    
}