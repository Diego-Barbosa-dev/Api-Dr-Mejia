package com.drmejia.core.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.models.Headquarter;
import com.drmejia.core.persistence.entities.HeadquarterEntity;
import com.drmejia.core.persistence.repository.HeadquaterRepository;
import com.drmejia.core.services.interfaces.HeadquarterService;

import lombok.NonNull;



@Service
public class HeadquartertServiceImpl implements HeadquarterService {

    @Autowired
    private HeadquaterRepository headquaterRepository;

    private ResourceNotFoundException nonExistingHeadquarter(){
        return new ResourceNotFoundException("Headquarter Not Found In DataBase");
    }

    /* 
    *
    *       CRUD METHOD
    *
    */


    /*  CREATE METHOD  */
    @Override
    public void saveHeadquarter(Headquarter headquarter) {
        HeadquarterEntity headquarterEntity = new HeadquarterEntity();
        headquarterEntity.setName(headquarter.getName());

        headquaterRepository.save(headquarterEntity);
    }


    /* READ METHOD */
    @Override
    public List<Headquarter> getAllHeadquarters() {
        List<Headquarter> headquarters = new ArrayList<>();
        List<HeadquarterEntity> headquarterEntities = headquaterRepository.findAll();

        for (HeadquarterEntity headquarterEntity : headquarterEntities) {
            Headquarter headquarter = new Headquarter();
            headquarter.setIdHeadquarter(headquarterEntity.getIdHeadquarters());
            headquarter.setName(headquarterEntity.getName());

            headquarters.add(headquarter);
        }
        return headquarters;
    }


    /* UPDATE METHOD */
    @Override
    public void modifyHeadquarter(Headquarter headquarter) throws BadRequestException {
        /* PATCH HTTP METHOD */
        if(headquarter.getIdHeadquarter() == null){
            throw new BadRequestException("Headquarter Id Can't Be Null");
        }
        HeadquarterEntity headquarterEntity = headquaterRepository.findById(headquarter.getIdHeadquarter()).orElseThrow(this::nonExistingHeadquarter);
        
        if(headquarter.getName() != null && !headquarter.getName().isBlank()){
            headquarterEntity.setName(headquarter.getName());
        }

        headquaterRepository.save(headquarterEntity);
    }

    @Override
    public void updateHeadquarter(Headquarter headquarter) throws BadRequestException {
        /* PUT HTTP METHOD */
        if(headquarter.getIdHeadquarter() == null){
            throw new BadRequestException("Headquarter Id Can't Be Null");
        }
        if(headquarter.getName() == null || headquarter.getName().isBlank()){
            throw new BadRequestException("Headquarter Name Can't Be Null Or Blank");
        }

        HeadquarterEntity headquarterEntity = headquaterRepository.findById(headquarter.getIdHeadquarter()).orElseThrow(this::nonExistingHeadquarter);
        headquarterEntity.setName(headquarter.getName());

        headquaterRepository.save(headquarterEntity);
    }

    /* DELETE METHOD */
    @Override
    public void deleteHeadquarter(@NonNull Long idHeadquarter) {
        if (!headquaterRepository.existsById(idHeadquarter)) {
            throw nonExistingHeadquarter();
        }
        headquaterRepository.deleteById(idHeadquarter);
    }
    
}
