package com.drmejia.core.services.interfaces;

import java.util.List;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.models.Eye;

public interface EyeService {
    void saveEye(Eye eye);
    List<Eye> getAllEyes();
    void modifyEye(Eye eye) throws BadRequestException;
    void updateEye(Eye eye) throws BadRequestException;
    void deleteEye(Long idEye);
    
}