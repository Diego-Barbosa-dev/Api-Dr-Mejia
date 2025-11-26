package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Headquarter;
import com.drmejia.core.exceptions.BadRequestException;

public interface HeadquarterService {
    void saveHeadquarter(Headquarter headquarter);
    List<Headquarter> getAllHeadquarters();
    void modifyHeadquarter(Headquarter headquarter) throws BadRequestException;
    void updateHeadquarter(Headquarter headquarter) throws BadRequestException;
    void deleteHeadquarter(Long idHeadquarter);
}
