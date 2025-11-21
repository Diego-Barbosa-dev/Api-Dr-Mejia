package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Comprobant;
import com.drmejia.core.domain.services.interfaces.ComprobantService;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.ComprobantEntity;
import com.drmejia.core.persistence.repository.ComprobantRepository;

import lombok.NonNull;

@Service
public class ComprobantServiceImpl implements ComprobantService{
    @Autowired
    private ComprobantRepository comprobantRepository;

    private ResourceNotFoundException nonExistingComprobant(){
        return new ResourceNotFoundException("Comprobant Not Found In DataBase");
    }

    /*
     *----------------------------------------------
     *               CRUD SECTION
     *----------------------------------------------
     */


    /* CREATE METHOD */
    @Override
    public void saveComprobant(Comprobant comprobant){
        ComprobantEntity comprobantEntity = new ComprobantEntity(
            null,
            comprobant.getName()
        );

        comprobantRepository.save(comprobantEntity);
        
    }

    /* READ METHOD */
    @Override
    public List<Comprobant> getAllComprobants(){
        List<ComprobantEntity> comprobantEntities = comprobantRepository.findAll();
        List<Comprobant> comprobants = new ArrayList<>();

        for(ComprobantEntity comprobantEntity : comprobantEntities){
            Comprobant comprobant = new Comprobant(
                comprobantEntity.getIdComprobant(),
                comprobantEntity.getName()
            );
            comprobants.add(comprobant);
        }
        return comprobants;
    }

    /* UPDATE METHODS*/
    @Override
    public void modifyComprobant(Comprobant comprobant){
        ComprobantEntity comprobantEntity = comprobantRepository
        .findById(comprobant.getIdComprobant())
        .orElseThrow(this::nonExistingComprobant);

        comprobantEntity.setName(comprobant.getName());
        comprobantRepository.save(comprobantEntity);
    }

    public void modifyComprobantComprobantId(@NonNull Long newId, @NonNull Long OldId){
        ComprobantEntity comprobantEntity = comprobantRepository
        .findById(OldId)
        .orElseThrow(this::nonExistingComprobant);

        comprobantEntity.setIdComprobant(newId);
        comprobantRepository.save(comprobantEntity);
    }

    @Override
    public void deleteComprobant(Long idComprobant){
        comprobantRepository.deleteById(idComprobant);
    }
}
