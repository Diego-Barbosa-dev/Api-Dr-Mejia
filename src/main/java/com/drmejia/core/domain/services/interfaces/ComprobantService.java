package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Comprobant;
import com.drmejia.core.exceptions.BadRequestException;

public interface ComprobantService {
    void saveComprobant(Comprobant comprobant);
    List<Comprobant> getAllComprobants();
    void modifyComprobant(Comprobant comprobant) throws BadRequestException;
    void updateComprobant(Comprobant comprobant) throws BadRequestException;
    void deleteComprobant(Long idComprobant);
}
