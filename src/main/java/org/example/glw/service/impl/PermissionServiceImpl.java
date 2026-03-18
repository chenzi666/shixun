package org.example.glw.service.impl;

import org.example.glw.entity.Permission;
import org.example.glw.mapper.PermissionMapper;
import org.example.glw.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Override
    public List<Permission> getPermissions() {
        return permissionMapper.selectPermissions();
    }
    
    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }
}