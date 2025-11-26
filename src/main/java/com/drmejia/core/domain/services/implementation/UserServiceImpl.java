package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.drmejia.core.domain.models.User;
import com.drmejia.core.domain.services.interfaces.UserService;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.RoleEntity;
import com.drmejia.core.persistence.entities.UserEntity;
import com.drmejia.core.persistence.repository.RoleRepository;
import com.drmejia.core.persistence.repository.UserRepository;

import lombok.NonNull;

@Service
public class UserServiceImpl implements UserService{

    
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private ResourceNotFoundException nonExistingUser(){
        return new ResourceNotFoundException("User Not Found In DataBase");
    }

    private ResourceNotFoundException nonExistingRole(){
        return new ResourceNotFoundException("Role Not Found In DataBase");
    }


    /*
     * --------------------------------------
     *              CRUD METHODS
     * --------------------------------------
     */


    /* CREATE an USER  */
    @Override
    public void saveUser(User user){
        UserEntity userEntity = new UserEntity();
        RoleEntity roleEntity = roleRepository
        .findById(user.getRole())
        .orElseThrow(this::nonExistingUser);

        userEntity.setNit(user.getNit());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(roleEntity);
        userRepository.save(userEntity);
        
    }

    /* READ the USERS */
    @Override
    public List<User> getAllUsers(){
        
        List<UserEntity> userEntities = userRepository.findAll();
        List<User> users = new ArrayList<>();

        for(UserEntity userEntity : userEntities){
            User user = new User();
            user.setNit(userEntity.getNit());
            user.setName(userEntity.getName());
            user.setEmail(userEntity.getEmail());
            user.setPassword(userEntity.getPassword());
            user.setRole(userEntity.getRole().getIdRol());
            users.add(user);
        }
        return users;
    }

    /* UPDATE Methods */
    @Override
    public void modifyUser(User user) {
        /* PATCH HTTP METHOD */
        if (user.getNit().isBlank() && user.getNit() == null ) {
        }
        UserEntity userEntity = userRepository.findByNit(user.getNit()).orElseThrow(this::nonExistingUser);
        if (!user.getName().isBlank() && user.getName() != null) {
            userEntity.setName(user.getName());
        }
        if (!user.getEmail().isBlank() && user.getEmail() != null) {
            userEntity.setEmail(user.getEmail());
        }
        if(!user.getPassword().isBlank() && user.getPassword() != null){
            userEntity.setPassword(user.getPassword());
        }
        if(user.getRole() != null){
            userEntity.setRole(roleRepository.findById(user.getRole()).orElseThrow(this::nonExistingRole));
        }
        if (userEntity != null){
            userRepository.save(userEntity);
        }

    }

    @Override
    public void updateUser(User user) throws BadRequestException{
        /* PUT HTTP METHOD */
        if (user.getNit() == null || user.getNit().isBlank()){
            throw new BadRequestException("Nit Can't Be Null Or Empty");
        }
        if(user.getName() == null || user.getEmail().isBlank()){
            throw new BadRequestException("Name Can't Be Null Or Empty");
        }
        if(user.getEmail() == null || user.getEmail().isBlank()){
            throw new BadRequestException("Email Can't Be Null Or Empty");
        }
        if(user.getPassword() == null || user.getPassword().isBlank()){
            throw new BadRequestException("Password Can't Be Null Or Empty");
        }
        if(user.getRole() == null){
            throw new BadRequestException("Role Can't Be Null");
        }
        UserEntity userEntity = userRepository.findByNit(user.getNit()).orElseThrow(this::nonExistingUser);
        userEntity.setNit(user.getNit());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(roleRepository.findById(user.getRole()).orElseThrow(this::nonExistingRole));

    }
        
    /* DELETE METHOD */
    @Override
    public void deleteUser(@NonNull String nit) {
        if (!userRepository.existsByNit(nit)){
            throw nonExistingUser();
        }
        userRepository.deleteByNit(nit);
    }
}

