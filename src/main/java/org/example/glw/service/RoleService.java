package org.example.glw.service;

import org.example.glw.entity.Role;
import java.util.List;

public interface RoleService {
    // 获取角色列表
    List<Role> getRoles();
    
    // 根据ID获取角色
    Role getRoleById(Long roleId);
}