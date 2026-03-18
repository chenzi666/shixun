package org.example.glw.service.admin;

import org.example.glw.entity.User;
import java.util.List;

public interface AdminUserService {
    // 查询用户列表（管理员）
    List<User> findUsers(Integer page, Integer size, String username, Integer status);
    
    // 查询用户总数（管理员）
    Long countUsers(String username, Integer status);
    
    // 禁用/启用用户（管理员）
    int updateUserStatus(Long userId, Integer status);
}