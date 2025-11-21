package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Eye;
import com.drmejia.core.domain.services.interfaces.EyeService;
import com.drmejia.core.enums.EyeType;
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
    public void modifyEye(Eye eye) {
        EyeEntity eyeEntity = eyeRepository.findById(eye.getIdEye()).orElseThrow(this::nonExistingEye);

        eyeEntity.setOrden(orderRepository.findById(eye.getOrder()).orElseThrow(this::nonExistingEye));
        eyeEntity.setType(eye.getType());
        eyeEntity.setAxis(eye.getAxis());
        eyeEntity.setCil(eye.getCil());
        eyeEntity.setAddition(eye.getAddition());
        eyeEntity.setDp(eye.getDp());
        eyeEntity.setHigh(eye.getHigh());
        eyeEntity.setAvl(eye.getAvl());
        eyeEntity.setAvp(eye.getAvp());
        
        eyeRepository.save(eyeEntity);
    }

    // Métodos individuales para modificar cada atributo de EyeEntity
    public void modifyEyeType(@NonNull Long idEye, EyeType type) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setType(type);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeEsf(@NonNull Long idEye, String esf) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setEsf(esf);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeCil(@NonNull Long idEye, String cil) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setCil(cil);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeAxis(@NonNull Long idEye, String axis) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setAxis(axis);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeAddition(@NonNull Long idEye, String addition) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setAddition(addition);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeDp(@NonNull Long idEye, String dp) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setDp(dp);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeHigh(@NonNull Long idEye, String high) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setHigh(high);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeAvl(@NonNull Long idEye, String avl) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setAvl(avl);
        eyeRepository.save(eyeEntity);
    }

    public void modifyEyeAvp(@NonNull Long idEye, String avp) {
        EyeEntity eyeEntity = eyeRepository.findById(idEye).orElseThrow(this::nonExistingEye);
        eyeEntity.setAvp(avp);
        eyeRepository.save(eyeEntity);
    }

    /* DELETE METHOD */
    @Override
    public void deleteEye(@NonNull Long idEye) {
        eyeRepository.deleteById(idEye);
    }
    
}
