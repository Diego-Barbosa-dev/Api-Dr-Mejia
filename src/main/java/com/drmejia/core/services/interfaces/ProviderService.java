package com.drmejia.core.services.interfaces;

import java.util.List;

import com.drmejia.core.models.Provider;

public interface ProviderService {
    List<Provider> getAllProviders();
    void saveProvider(Provider provider);
    void modifyProvider(Provider provider);
    void updateProvider(Provider provider);
    void deleteProvider(Long idProvider);
}
