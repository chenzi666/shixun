package org.example.glw.service;

import org.example.glw.entity.User;
import java.util.List;

public interface UserService {
    // 根据用户名查询用户
    User findByUsername(String username);
    
    // 根据邮箱查询用户
    User findByEmail(String email);
    
    // 根据手机号查询用户
    User findByPhone(String phone);
    
    // 根据用户ID查询用户
    User findById(Long userId);
    
    // 新增用户
    int save(User user);
    
    // 更新用户信息
    int update(User user);
    
    // 用户登录
    User login(String username, String password);
    
    // 用户注册
    int register(User user);
    
    // 更新用户最后登录时间
    void updateLastLoginTime(Long userId);
    
    // 查询用户列表（管理员）
    List<User> findUsers(Integer page, Integer size, String username, Integer status);
    
    // 查询用户总数（管理员）
    Long countUsers(String username, Integer status);
}