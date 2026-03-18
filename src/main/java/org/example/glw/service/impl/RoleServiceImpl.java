package org.example.glw.service.impl;

import org.example.glw.entity.Role;
import org.example.glw.mapper.RoleMapper;
import org.example.glw.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public List<Role> getRoles() {
        return roleMapper.selectRoles();
    }
    
    @Override
    public Role getRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }
}