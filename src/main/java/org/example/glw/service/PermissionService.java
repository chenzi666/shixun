package org.example.glw.service;

import org.example.glw.entity.Permission;
import java.util.List;

public interface PermissionService {
    // 获取权限列表
    List<Permission> getPermissions();
    
    // 根据角色ID获取权限列表
    List<Permission> getPermissionsByRoleId(Long roleId);
}