package com.drmejia.core.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drmejia.core.domain.models.Comprobant;
import com.drmejia.core.domain.services.interfaces.ComprobantService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/comprobants")
public class ComprobantRestController {
    
    @Autowired
    private ComprobantService comprobantService;

    @GetMapping
    public ResponseEntity<?> getComprobants() {
        return ResponseEntity.ok(comprobantService.getAllComprobants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return comprobantService.getAllComprobants()
            .stream()
            .filter(comprobant -> comprobant.getIdComprobant().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(()-> new ResourceNotFoundException("Comprobant: " + id + " Not Found"));
    }
    
    @PostMapping
    public ResponseEntity<?> postComprobant(@RequestBody Comprobant comprobant){
        if(comprobant.hasNullAttributes()){
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nId: " + comprobant.getIdComprobant()
                + "\nName: " + comprobant.getName()
            );
        }
        comprobantService.saveComprobant(comprobant);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(comprobant.getIdComprobant())
            .toUri();
        return ResponseEntity.created(location).body(comprobant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putComprobant(@PathVariable("id") Long id, @RequestBody Comprobant comprobant){
        comprobant.setIdComprobant(id);
        try {
            comprobantService.updateComprobant(comprobant);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(comprobant);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchComprobant(@PathVariable("id") Long id, @RequestBody Comprobant comprobant){
        comprobant.setIdComprobant(id);
        try {
            comprobantService.modifyComprobant(comprobant);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(comprobant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComprobant(@PathVariable("id") Long id){
        comprobantService.deleteComprobant(id);
        return ResponseEntity.noContent().build();
    }



}
