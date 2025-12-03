package com.drmejia.core.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drmejia.core.domain.models.Provider;
import com.drmejia.core.domain.services.interfaces.ProviderService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/providers")
public class ProviderRestController {
    
    @Autowired
    private ProviderService providerService;


    @GetMapping
    public ResponseEntity<?> getProviders(){
        /* Devuelve una lista de proveedores */
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    @GetMapping("/{nit}")
    public ResponseEntity<?> getByNit(@PathVariable String nit) {
        return providerService.getAllProviders()
            .stream()
            .filter(provider -> provider.getNit().contains(nit))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Provider " + nit + " Not Found"));
    }

    @PostMapping
    public ResponseEntity<?> createProvider(@RequestBody Provider provider) throws BadRequestException {

        if(provider.hasNullAttributes()){
            throw new BadRequestException("Empty Attributes Are Not Allowed: "
                + "\nNit: " + provider.getNit()
                + "\nName: " + provider.getName()
                + "\nAddress: " + provider.getAddress()
                + "\nEmail: " + provider.getEmail()
            );
        }

        if (provider.getName().length() < 3 || provider.getName().isBlank()){
            throw new BadRequestException("Provider Name: " + provider.getName() + " Is Not Valid");
        }
        if(provider.getAddress().length() < 3 || provider.getAddress().isBlank()){
            throw new BadRequestException("Provider Address: " + provider.getAddress() + "Is Not Valid");
        }

        if (!provider.getEmail().contains("@") || provider.getEmail().isBlank()){
            throw new BadRequestException("Privider Email: " + provider.getEmail() + " Is Not Valid");
        }

        if (provider.getNit().isBlank()){
            throw new BadRequestException("Provider Nit Is Empty, Empty Nit Not Valid");
        }

        providerService.saveProvider(provider);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{nit}")
            .buildAndExpand(provider.getNit())
            .toUri();
        return ResponseEntity.created(location).body(provider);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvider(@PathVariable Long id, @RequestBody Provider provider){
        provider.setIdProvider(id);
        providerService.updateProvider(provider);
        return ResponseEntity.ok(provider);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> modifyProvider(@PathVariable Long id, @RequestBody Provider provider){
        provider.setIdProvider(id);
        providerService.modifyProvider(provider);
        return ResponseEntity.ok().body(provider);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return ResponseEntity.ok().body("Deleted Id:" + id);
}

}

