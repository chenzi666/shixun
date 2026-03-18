package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.glw.entity.Permission;
import java.util.List;

@Mapper
public interface PermissionMapper {
    // 获取权限列表
    List<Permission> selectPermissions();
    
    // 根据角色ID获取权限列表
    List<Permission> selectPermissionsByRoleId(Long roleId);
}