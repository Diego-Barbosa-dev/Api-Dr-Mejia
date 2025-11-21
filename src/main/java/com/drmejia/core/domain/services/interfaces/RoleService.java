package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Role;

public interface RoleService {
    List<Role> getAllRoles();
    void modifyRole(Role role);
    void saveRole(Role role);
    void eliminateRole(Role role);
}
