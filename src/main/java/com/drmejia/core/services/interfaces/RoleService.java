package com.drmejia.core.services.interfaces;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.drmejia.core.models.Role;

public interface RoleService {
    List<Role> getAllRoles();
    void modifyRole(Role role) throws BadRequestException;
    void updateRole(Role role) throws BadRequestException;
    void saveRole(Role role);
    void deleteRole(Long id);
}
