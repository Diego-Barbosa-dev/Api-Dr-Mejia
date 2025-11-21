package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Headquarter;

public interface HeadquarterService {
    void saveHeadquarter(Headquarter headquarter);
    List<Headquarter> getAllHeadquarters();
    void modifyHeadquarter(Long idHeadquarter, String name);
    void deleteHeadquarter(Long idHeadquarter);
}
