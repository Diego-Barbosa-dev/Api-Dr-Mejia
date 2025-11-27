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
    public void modifyProvider(Provider provider) throws BadRequestException{
        /* PATCH HTTP method */
        if (provider.getIdProvider() == null) {
            throw new BadRequestException("Provider Id Can't Be Null");
        }
        ProviderEntity providerEntity = searchProviderEntity(provider.getIdProvider());
        if (provider.getNit() != null && !provider.getNit().isBlank()) {
            providerEntity.setNit(provider.getNit());
        }
        if (provider.getName()!= null && !provider.getName().isBlank()){
            providerEntity.setName(provider.getName());
        }
        if(provider.getAddress() != null && !provider.getAddress().isBlank()){
            providerEntity.setAddress(provider.getAddress());
        }
        if(provider.getEmail() != null && !provider.getEmail().isBlank()){
            providerEntity.setEmail(provider.getEmail());
        }
        if (providerEntity == null) {
            throw nonExistingProvider();
        }
        providerRepository.save(providerEntity);

    }

    @Override
    public void updateProvider(Provider provider) throws BadRequestException {
        /* PUT HTTP method */
        if (provider.getIdProvider() == null) {
            throw new BadRequestException("Provider Id Can't Be Null");
        }

        ProviderEntity providerEntity = searchProviderEntity(provider.getIdProvider());

        if (provider.getNit() == null || provider.getNit().isBlank()) {
            throw new BadRequestException("Provider NIT Can't Be Null Or Blank");
        }
        if (provider.getName() == null || provider.getName().isBlank()) {
            throw new BadRequestException("Provider Name Can't Be Null Or Blank");
        }
        if (provider.getAddress() == null || provider.getAddress().isBlank()) {
            throw new BadRequestException("Provider Address Can't Be Null Or Blank");
        }
        if (provider.getEmail() == null || provider.getEmail().isBlank()) {
            throw new BadRequestException("Provider Email Can't Be Null Or Blank");
        }
        providerEntity.setNit(provider.getNit());
        providerEntity.setName(provider.getName());
        providerEntity.setAddress(provider.getAddress());
        providerEntity.setEmail(provider.getEmail());

        providerRepository.save(providerEntity);
    }

    /*Delete Provider from DB */
    @Override
    public void deleteProvider(@NonNull Long idProvider) {
        if(!providerRepository.existsById(idProvider)){
            throw nonExistingProvider();
        }
        providerRepository.deleteById(idProvider);
    }
    
}
