package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

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
        .findById(user.getRol())
        .orElseThrow(this::nonExistingUser);

        userEntity.setNit(user.getNit());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getMail());
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
            user.setMail(userEntity.getEmail());
            user.setPassword(userEntity.getPassword());
            user.setRol(userEntity.getRole().getIdRol());
            users.add(user);
        }
        return users;
    }



    /* UPDATE Methods */
    @Override
    public void modifyUser(User user, @NonNull Long role) {
        
        UserEntity userEntity = userRepository.findByNit(user.getNit())
        .orElseThrow(this::nonExistingUser);

        userEntity.setName(user.getName()); 
        userEntity.setEmail(user.getMail());
        userEntity.setPassword(user.getPassword());

        RoleEntity roleEntity = roleRepository.findById(role)
        .orElseThrow(this::nonExistingRole);
        userEntity.setRole(roleEntity);
        userRepository.save(userEntity);
    }

    public void modifyUserNit(@NonNull String newId, @NonNull String oldNit){
        
        UserEntity userEntity = userRepository.findByNit(oldNit)
        .orElseThrow(this::nonExistingUser);

        userEntity.setNit(newId);
        userRepository.save(userEntity);
    }

    public void modifyUserName(@NonNull String nit, @NonNull String name){
        UserEntity userEntity = userRepository.findByNit(nit)
        .orElseThrow(this::nonExistingUser);

        userEntity.setName(name);
        userRepository.save(userEntity);
    }

    public void modifyUserMail(@NonNull String nit, @NonNull String mail){
        UserEntity userEntity = userRepository.findByNit(nit)
        .orElseThrow(this::nonExistingUser);
        
        userEntity.setEmail(mail);
        userRepository.save(userEntity);
    }

    public void modifyUserPassword(@NonNull String nit, @NonNull String password){
        UserEntity userEntity = userRepository.findByNit(nit)
        .orElseThrow(this::nonExistingUser);

        userEntity.setPassword(password);
        userRepository.save(userEntity);
    }
    
    /* DELETE METHOD */
    @Override
    public void deleteUser(@NonNull String nit) {
        userRepository.deleteByNit(nit);
    }
}

