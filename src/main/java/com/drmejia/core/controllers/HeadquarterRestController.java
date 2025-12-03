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

import com.drmejia.core.domain.models.Headquarter;
import com.drmejia.core.domain.services.interfaces.HeadquarterService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("api/headquarters")
public class HeadquarterRestController {
    
    @Autowired
    private HeadquarterService headquarterService;


    @GetMapping
    public ResponseEntity<?> getHeadquarters() {
        return ResponseEntity.ok(headquarterService.getAllHeadquarters());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return headquarterService.getAllHeadquarters().stream()
            .filter(headquarter -> headquarter.getIdHeadquarter().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Headquarter: " + id + " Not Found"));
    }


    @PostMapping
    public ResponseEntity<?> postHeadquarter(@RequestBody Headquarter headquarter) {
        if (headquarter.hasNullAttributes()) {
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nId: " + headquarter.getIdHeadquarter()
                + "\nName: " + headquarter.getName()
            );
        }

        if (headquarter.getName().length() < 2) {
            throw new BadRequestException("Headquarter Name Must Have At Least 2 Characters");
        }

        headquarterService.saveHeadquarter(headquarter);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(headquarter.getIdHeadquarter())
            .toUri();
        return ResponseEntity.created(location).body(headquarter);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> putHeadquarter(@PathVariable("id") Long id, @RequestBody Headquarter headquarter) {
        headquarter.setIdHeadquarter(id);
        try {
            headquarterService.updateHeadquarter(headquarter);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(headquarter);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> patchHeadquarter(@PathVariable("id") Long id, @RequestBody Headquarter headquarter) {
        headquarter.setIdHeadquarter(id);
        try {
            headquarterService.modifyHeadquarter(headquarter);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(headquarter);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHeadquarter(@PathVariable("id") Long id) {
        headquarterService.deleteHeadquarter(id);
        return ResponseEntity.noContent().build();
    }
    
}