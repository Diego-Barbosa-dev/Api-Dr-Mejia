package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Eye;
import com.drmejia.core.domain.services.interfaces.EyeService;
import com.drmejia.core.enums.EyeType;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.EyeEntity;
import com.drmejia.core.persistence.repository.EyeRepository;
import com.drmejia.core.persistence.repository.OrderRepository;

import lombok.NonNull;

@Service
public class EyeServiceImpl implements EyeService{

    @Autowired
    EyeRepository eyeRepository;

    @Autowired
    OrderRepository orderRepository;

    private ResourceNotFoundException nonExistingEye(){
        return new ResourceNotFoundException("Eye Not Found In DataBase");
    }

    /*
     * ***********************************************************
     *                        CRUD METHODS
     * ***********************************************************
     */


    /* CREATE METHOD     */
    @Override
    public void saveEye(Eye eye) {
        EyeEntity eyeEntity = new EyeEntity();
        eyeEntity.setIdEye(eye.getIdEye());
        eyeEntity.setOrden(orderRepository.findById(eye.getOrder()).orElseThrow(this::nonExistingEye));
        eyeEntity.setType(eye.getType());
        eyeEntity.setEsf(eye.getEsf());
        eyeEntity.setCil(eye.getCil());
        eyeEntity.setAxis(eye.getAxis());
        eyeEntity.setAddition(eye.getAddition());
        eyeEntity.setDp(eye.getDp());
        eyeEntity.setHigh(eye.getHigh());
        eyeEntity.setAvl(eye.getAvl());
        eyeEntity.setAvp(eye.getAvp());

        eyeRepository.save(eyeEntity);
    }
    /* READ METHOD */
    @Override
    public List<Eye> getAllEyes() {
        List<EyeEntity> eyeEntities = eyeRepository.findAll();
        List<Eye> eyes = new ArrayList<>();

        for (EyeEntity eyeEntity : eyeEntities) {
            Eye eye = new Eye(
                eyeEntity.getIdEye(),
                eyeEntity.getOrden().getIdOrder(),
                eyeEntity.getType(),
                eyeEntity.getEsf(),
                eyeEntity.getCil(),
                eyeEntity.getAxis(),
                eyeEntity.getAddition(),
                eyeEntity.getDp(),
                eyeEntity.getHigh(),
                eyeEntity.getAvl(),
                eyeEntity.getAvp()
            );

            eyes.add(eye);
        }
        return eyes;
    }


    /* MODIFY METHOD */
    @Override
    public void modifyEye(Eye eye) throws BadRequestException {
        /* PATCH HTTP METHOD */
        if(eye.getIdEye() == null){
            throw new BadRequestException("Eye Id Can't Be Null");
        }
        EyeEntity eyeEntity = eyeRepository.findById(eye.getIdEye()).orElseThrow(this::nonExistingEye);

        if(eye.getOrder() != null){
            eyeEntity.setOrden(orderRepository.findById(eye.getOrder()).orElseThrow(this::nonExistingEye));
        }
        if(eye.getType() != null){
            eyeEntity.setType(eye.getType());
        }
        if(eye.getAxis() != null && !eye.getAxis().isBlank()){
            eyeEntity.setAxis(eye.getAxis());
        }
        if(eye.getCil() != null && !eye.getCil().isBlank()){
            eyeEntity.setCil(eye.getCil());
        }
        if(eye.getEsf() != null && !eye.getEsf().isBlank()){
            eyeEntity.setEsf(eye.getEsf());
        }
        if(eye.getAddition() != null && !eye.getAddition().isBlank()){
            eyeEntity.setAddition(eye.getAddition());
        }
        if(eye.getDp() != null && !eye.getDp().isBlank()){
            eyeEntity.setDp(eye.getDp());
        }
        if(eye.getHigh() != null && !eye.getHigh().isBlank()){
            eyeEntity.setHigh(eye.getHigh());
        }
        if(eye.getAvl() != null && !eye.getAvl().isBlank()){
            eyeEntity.setAvl(eye.getAvl());
        }
        if(eye.getAvp() != null && !eye.getAvp().isBlank()){
            eyeEntity.setAvp(eye.getAvp());
        }
        
        eyeRepository.save(eyeEntity);
    }

    @Override
    public void updateEye(Eye eye) throws BadRequestException {
        /* PUT HTTP METHOD */
        if(eye.getIdEye() == null){
            throw new BadRequestException("Eye Id Can't Be Null");
        }
        if(eye.getOrder() == null){
            throw new BadRequestException("Eye Order Can't Be Null");
        }
        if(eye.getType() == null){
            throw new BadRequestException("Eye Type Can't Be Null");
        }
        if(eye.getEsf() == null || eye.getEsf().isBlank()){
            throw new BadRequestException("Eye Esf Can't Be Null Or Blank");
        }
        if(eye.getCil() == null || eye.getCil().isBlank()){
            throw new BadRequestException("Eye Cil Can't Be Null Or Blank");
        }
        if(eye.getAxis() == null || eye.getAxis().isBlank()){
            throw new BadRequestException("Eye Axis Can't Be Null Or Blank");
        }
        if(eye.getAddition() == null || eye.getAddition().isBlank()){
            throw new BadRequestException("Eye Addition Can't Be Null Or Blank");
        }
        if(eye.getDp() == null || eye.getDp().isBlank()){
            throw new BadRequestException("Eye Dp Can't Be Null Or Blank");
        }
        if(eye.getHigh() == null || eye.getHigh().isBlank()){
            throw new BadRequestException("Eye High Can't Be Null Or Blank");
        }
        if(eye.getAvl() == null || eye.getAvl().isBlank()){
            throw new BadRequestException("Eye Avl Can't Be Null Or Blank");
        }
        if(eye.getAvp() == null || eye.getAvp().isBlank()){
            throw new BadRequestException("Eye Avp Can't Be Null Or Blank");
        }

        EyeEntity eyeEntity = eyeRepository.findById(eye.getIdEye()).orElseThrow(this::nonExistingEye);

        eyeEntity.setOrden(orderRepository.findById(eye.getOrder()).orElseThrow(this::nonExistingEye));
        eyeEntity.setType(eye.getType());
        eyeEntity.setAxis(eye.getAxis());
        eyeEntity.setCil(eye.getCil());
        eyeEntity.setEsf(eye.getEsf());
        eyeEntity.setAddition(eye.getAddition());
        eyeEntity.setDp(eye.getDp());
        eyeEntity.setHigh(eye.getHigh());
        eyeEntity.setAvl(eye.getAvl());
        eyeEntity.setAvp(eye.getAvp());
        
        eyeRepository.save(eyeEntity);
    }

    /* DELETE METHOD */
    @Override
    public void deleteEye(@NonNull Long idEye) {
        eyeRepository.deleteById(idEye);
    }
    
}
