package com.drmejia.core.services.interfaces;

import java.util.List;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.models.Headquarter;

public interface HeadquarterService {
    void saveHeadquarter(Headquarter headquarter);
    List<Headquarter> getAllHeadquarters();
    void modifyHeadquarter(Headquarter headquarter) throws BadRequestException;
    void updateHeadquarter(Headquarter headquarter) throws BadRequestException;
    void deleteHeadquarter(Long idHeadquarter);
}
