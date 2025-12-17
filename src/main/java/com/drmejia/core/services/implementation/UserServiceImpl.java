package com.drmejia.core.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.models.User;
import com.drmejia.core.persistence.entities.HeadquarterEntity;
import com.drmejia.core.persistence.entities.RoleEntity;
import com.drmejia.core.persistence.entities.UserEntity;
import com.drmejia.core.persistence.repository.HeadquaterRepository;
import com.drmejia.core.persistence.repository.RoleRepository;
import com.drmejia.core.persistence.repository.UserRepository;
import com.drmejia.core.services.interfaces.UserService;

import jakarta.transaction.Transactional;
import lombok.NonNull;

@Service
public class UserServiceImpl implements UserService{

    
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private HeadquaterRepository headquarterRepository;
    

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
        
        if(user.getIdHeadquarter() != null){
            HeadquarterEntity hqEntity = headquarterRepository.findById(user.getIdHeadquarter()).orElse(null);
            userEntity.setHeadquarter(hqEntity);
        }
        
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
            user.setRole(userEntity.getRole().getId());
            user.setIdHeadquarter(userEntity.getHeadquarter() != null ? userEntity.getHeadquarter().getIdHeadquarters() : null);
            users.add(user);
        }
        return users;
    }

    /* UPDATE Methods */
    @Override
    public void modifyUser(User user) throws BadRequestException {

        /* PATCH HTTP METHOD */

        if (user.getNit() == null || user.getNit().isBlank()) {
            throw new BadRequestException("Nit Can't Be Null Or Empty");
        }

        UserEntity userEntity = userRepository.findByNit(user.getNit())
                .orElseThrow(this::nonExistingUser);

        if (user.getName() != null && !user.getName().isBlank()) {
            userEntity.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userEntity.setEmail(user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            userEntity.setPassword(user.getPassword());
        }

        if (user.getRole() != null) {
            userEntity.setRole(
                roleRepository.findById(user.getRole())
                        .orElseThrow(this::nonExistingRole)
            );
        }
        
        if (user.getIdHeadquarter() != null) {
            HeadquarterEntity hqEntity = headquarterRepository.findById(user.getIdHeadquarter()).orElse(null);
            userEntity.setHeadquarter(hqEntity);
        }

        userRepository.save(userEntity);

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

        userRepository.save(userEntity);

    }
        
    /* DELETE METHOD */
    @Transactional
    @Override
    public void deleteUser(@NonNull String nit) {
        if (!userRepository.existsByNit(nit)){
            throw nonExistingUser();
        }
        userRepository.deleteByNit(nit);
    }
    
    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(this::nonExistingUser);
    }
    
    @Override
    public UserEntity findByName(String name) {
        return userRepository.findByName(name).orElseThrow(this::nonExistingUser);
    }

    /* SECURITY METHODS*/
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        UserEntity userEntity;

        if (userName.contains("@")) {
            userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        } else {
            userEntity = userRepository.findByName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        }

        return new org.springframework.security.core.userdetails.User(
                userEntity.getName().toLowerCase(),
                userEntity.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().getName().toUpperCase()))
        );

    } 
}

