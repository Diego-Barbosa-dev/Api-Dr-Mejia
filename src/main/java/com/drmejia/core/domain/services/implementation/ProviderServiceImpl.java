package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Provider;
import com.drmejia.core.domain.services.interfaces.ProviderService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.ProviderEntity;
import com.drmejia.core.persistence.repository.ProviderRepository;

import lombok.NonNull;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    /*This method returns a ResourceNotFoundException, it was made for DRY */
    private ResourceNotFoundException nonExistingProvider(){
        return new ResourceNotFoundException("Provider Not Found In DataBase");
    }
    /*
     * ----------------------------------------------------------------
     *                   CRUD METHODS
     * ----------------------------------------------------------------
     */
    /* Method for searching a Provider */
    private ProviderEntity searchProviderEntity(@NonNull Long id){
        return providerRepository
        .findById(id)
        .orElseThrow(this::nonExistingProvider);
    }

    /* Create a Provider in DB */
    @Override
    public void saveProvider(Provider provider) {
        ProviderEntity providerEntity = new ProviderEntity(
        provider.getIdProvider(),
        provider.getNit(), 
        provider.getName(), 
        provider.getAddress(), 
        provider.getEmail());
        providerRepository.save(providerEntity);
    }


    /* Read all Providers from DB*/
    @Override
    public List<Provider> getAllProviders() {
        List<ProviderEntity> providersEntities = providerRepository.findAll();
        List<Provider> providers = new ArrayList<>();

        for (ProviderEntity providerEntity : providersEntities) {
            Provider provider = new Provider(
            providerEntity.getIdProvider(),
            providerEntity.getNit(),
            providerEntity.getName(),
            providerEntity.getAddress(),
            providerEntity.getEmail());

            providers.add(provider);
        }

        return providers;
    }
    
    /* Modifiers (UPDATE)*/
    @Override
    public void modifyProvider(Provider provider) {
        if (provider.getIdProvider() == null) {
            throw new BadRequestException("El ID del proveedor no puede ser nulo para modificarlo");
        }

        ProviderEntity providerEntity = searchProviderEntity(provider.getIdProvider());
        providerEntity.setNit(provider.getNit());
        providerEntity.setName(provider.getName());
        providerEntity.setAddress(provider.getAddress());
        providerEntity.setEmail(provider.getEmail());

        providerRepository.save(providerEntity);

    }

    public void modifyProviderId(@NonNull Long newId, @NonNull Long oldId){
        ProviderEntity providerEntity = searchProviderEntity(oldId);

        providerEntity.setIdProvider(newId);
        providerRepository.save(providerEntity);
    }

    public void modifyProviderNit(@NonNull Long idProvider, @NonNull String nit){
        ProviderEntity providerEntity = searchProviderEntity(idProvider);

        providerEntity.setNit(nit);
        providerRepository.save(providerEntity);
    }

    public void modifyProviderName(@NonNull Long idProvider, @NonNull String name){
        ProviderEntity providerEntity = searchProviderEntity(idProvider);

        providerEntity.setName(name);
        providerRepository.save(providerEntity);
    }
    
    public void modifyProviderAddress(@NonNull Long idProvider, @NonNull String address){
        ProviderEntity providerEntity = searchProviderEntity(idProvider);

        providerEntity.setAddress(address);
        providerRepository.save(providerEntity);
    }

    public void modifyProviderEmail(@NonNull Long idProvider, @NonNull String email){
        ProviderEntity providerEntity = searchProviderEntity(idProvider);

        providerEntity.setEmail(email);
        providerRepository.save(providerEntity);
    }

    /*Delete Provider from DB */
    @Override
    public void deleteProvider(@NonNull Long idProvider) {
        providerRepository.deleteById(idProvider);
    }
    
}
