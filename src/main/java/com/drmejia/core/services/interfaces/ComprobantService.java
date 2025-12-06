package com.drmejia.core.services.interfaces;

import java.util.List;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.models.Comprobant;

public interface ComprobantService {
    void saveComprobant(Comprobant comprobant);
    List<Comprobant> getAllComprobants();
    void modifyComprobant(Comprobant comprobant) throws BadRequestException;
    void updateComprobant(Comprobant comprobant) throws BadRequestException;
    void deleteComprobant(Long idComprobant);
}
