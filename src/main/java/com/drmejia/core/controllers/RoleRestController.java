package com.drmejia.core.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drmejia.core.domain.models.Role;
import com.drmejia.core.domain.services.interfaces.RoleService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/roles")
public class RoleRestController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return roleService.getAllRoles().stream()
            .filter(role -> role.getIdRole().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Role: " + id + " Not Found"));
    }
    
    @PostMapping
    public ResponseEntity<?> postRole(@RequestBody Role role) throws BadRequestException{
        if(role.hasNullAttributes()){
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nId: " + role.getIdRole()
                + "\nname: " + role.getName()
            );
        }

        roleService.saveRole(role);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(role.getIdRole())
            .toUri();
        return ResponseEntity.created(location).body(role);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> putRole(@PathVariable Long id, @RequestBody Role role) {
        role.setIdRole(id);

        try {
            roleService.updateRole(role);
        } catch (BadRequestException | org.apache.coyote.BadRequestException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(role);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchRole(@PathVariable Long id, @RequestBody Role role) throws BadRequestException  {
        role.setIdRole(id);
        try {
            roleService.modifyRole(role);
        } catch (org.apache.coyote.BadRequestException | BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id){
        
        roleService.deleteRole(id);

        return ResponseEntity.noContent().build();
    }
}
