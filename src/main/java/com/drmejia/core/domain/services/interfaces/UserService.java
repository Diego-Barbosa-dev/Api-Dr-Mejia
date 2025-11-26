package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.drmejia.core.domain.models.User;

public interface UserService {

    List<User> getAllUsers();
    void saveUser(User user);
    void modifyUser(User use);
    void updateUser(User user) throws BadRequestException;
    void deleteUser(String nit);

}
