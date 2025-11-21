package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.User;

public interface UserService {

    public List<User> getAllUsers();
    public void saveUser(User user);
    public void modifyUser(User user, Long role);
    public void deleteUser(String nit);

}
