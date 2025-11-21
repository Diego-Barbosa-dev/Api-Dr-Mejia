package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Role;
import com.drmejia.core.domain.services.interfaces.RoleService;
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
     * Update method, modify role
     * 
     * There are also modifyRoleId and modifyRoleName
     */
    @Override
    public void modifyRole(Role role) {
        RoleEntity roleEntity = roleRepository
        .findById(role.getIdRole())
        .orElseThrow(this::nonExistingRole);

        roleEntity.setName(role.getName());

        roleRepository.save(roleEntity);
    }

    
    public void modifyRoleId(Long newId, @NonNull Long oldId) {
        RoleEntity roleEntity = roleRepository
        .findById(oldId)
        .orElseThrow(this::nonExistingRole);

        roleEntity.setIdRol(newId);

        roleRepository.save(roleEntity);
        
    }

    public void modifyRoleName(@NonNull Long id, @NonNull String roleNewName){
        RoleEntity roleEntity = roleRepository
        .findById(id)
        .orElseThrow(this::nonExistingRole);

        roleEntity.setName(roleNewName);

        roleRepository.save(roleEntity);
    }

    /* Delete method */
    @Override
    public void eliminateRole(Role role) {
        RoleEntity roleEntity = new RoleEntity(role.getIdRole(), role.getName());
        roleRepository.delete(roleEntity);
    }
    
}
