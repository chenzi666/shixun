package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.glw.entity.Role;
import java.util.List;

@Mapper
public interface RoleMapper {
    // 获取角色列表
    List<Role> selectRoles();
    
    // 根据ID获取角色
    Role selectById(Long roleId);
}