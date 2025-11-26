package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Role;
import com.drmejia.core.domain.services.interfaces.RoleService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.RoleEntity;
import com.drmejia.core.persistence.repository.RoleRepository;

import lombok.NonNull;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    private ResourceNotFoundException nonExistingRole(){
        return new ResourceNotFoundException("Role Not Found In DataBase");
    }

    /*<--------- CRUD SECTION ----------->*/

    /*
     * Create method, save role
     */
    @Override
    public void saveRole(Role role) {
        RoleEntity roleEntity = new RoleEntity(
            role.getIdRole(),
            role.getName()
        );

        roleRepository.save(roleEntity);
    }

    /*
     * Read Method, get all roles
     */
    @Override
    public List<Role> getAllRoles() {
       List<RoleEntity> roleEntities = roleRepository.findAll();
        List<Role> roles = new ArrayList<>();

       for (RoleEntity roleEntity : roleEntities){
            
            Role role = new Role(
            roleEntity.getIdRol(),
            roleEntity.getName()
            );

            roles.add(role);
       }
       return roles;
    }


    /*
     * UPDATE method
     */
    @Override
    public void modifyRole(Role role) throws BadRequestException{
        /* PATCH HTTP  */
        if(role.getIdRole() == null){
            throw new BadRequestException("Role Id Can't Be Null");
        }
        RoleEntity roleEntity = roleRepository.findById(role.getIdRole()).orElseThrow(this::nonExistingRole);
        if(!role.getName().isBlank() && role.getName() != null){
            roleEntity.setName(role.getName());
        }
        roleRepository.save(roleEntity);
    }

    @Override
    public void updateRole(Role role) throws BadRequestException{
        /* PUT METHOD */
        if(role.getIdRole() == null){
            throw new BadRequestException("Role Id Can't Be Null");
        }
        if (role.getName() == null || role.getName().isBlank()){
            throw new BadRequestException("Role Name Can't Be Null");
        }
        RoleEntity roleEntity = roleRepository.findById(role.getIdRole()).orElseThrow(this::nonExistingRole);
        roleEntity.setName(role.getName());
        roleRepository.save(roleEntity);
    }
    
    /* Delete method */ 

    @Override
    public void deleteRole(@NonNull Long id) {
        if (!roleRepository.existsById(id)) {
            throw nonExistingRole();
        }
        roleRepository.deleteById(id);
    }
    
}
