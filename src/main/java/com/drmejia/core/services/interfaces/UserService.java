package com.drmejia.core.services.interfaces;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.drmejia.core.models.User;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();
    void saveUser(User user);
    void modifyUser(User use) throws BadRequestException;
    void updateUser(User user) throws BadRequestException;
    void deleteUser(String nit);

}
