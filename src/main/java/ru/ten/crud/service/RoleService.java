package ru.ten.crud.service;

import ru.ten.crud.model.Role;

import java.util.List;

public interface RoleService {

    void addRole(Role role);

    Role getRoleByName(String roleName);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    void updateRole(Role role);

    void deleteRoleById(Long id);
}
