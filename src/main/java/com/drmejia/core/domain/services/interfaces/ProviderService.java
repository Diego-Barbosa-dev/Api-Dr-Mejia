package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Provider;

public interface ProviderService {
    List<Provider> getAllProviders();
    void saveProvider(Provider provider);
    void modifyProvider(Provider provider);
    void updateProvider(Provider provider);
    void deleteProvider(Long idProvider);
}
