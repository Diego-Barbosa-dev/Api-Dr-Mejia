package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Comprobant;

public interface ComprobantService {
    void saveComprobant(Comprobant comprobant);
    List<Comprobant> getAllComprobants();
    void modifyComprobant(Comprobant comprobant);
    void deleteComprobant(Long idComprobant);
}
