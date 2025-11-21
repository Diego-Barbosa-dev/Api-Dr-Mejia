package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Eye;

public interface EyeService {
    void saveEye(Eye eye);
    List<Eye> getAllEyes();
    void modifyEye(Eye eye);
    void deleteEye(Long idEye);
    
}