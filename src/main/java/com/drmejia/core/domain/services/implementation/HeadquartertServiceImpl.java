package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Headquarter;
import com.drmejia.core.domain.services.interfaces.HeadquarterService;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.HeadquarterEntity;
import com.drmejia.core.persistence.repository.HeadquaterRepository;

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
        headquarter.setName(headquarter.getName());

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
    public void modifyHeadquarter(@NonNull Long idHeadquarter, String name) {
        HeadquarterEntity headquarterEntity = headquaterRepository.findById(idHeadquarter).orElseThrow(this::nonExistingHeadquarter);
        headquarterEntity.setName(name);

        headquaterRepository.save(headquarterEntity);
    }

    /* DELETE METHOD */
    @Override
    public void deleteHeadquarter(@NonNull Long idHeadquarter) {
        headquaterRepository.deleteById(idHeadquarter);
    }
    
}
