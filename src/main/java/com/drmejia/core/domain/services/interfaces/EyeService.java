package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Eye;
import com.drmejia.core.exceptions.BadRequestException;

public interface EyeService {
    void saveEye(Eye eye);
    List<Eye> getAllEyes();
    void modifyEye(Eye eye) throws BadRequestException;
    void updateEye(Eye eye) throws BadRequestException;
    void deleteEye(Long idEye);
    
}