package org.example.glw.service.impl;

import org.example.glw.entity.User;
import org.example.glw.mapper.UserMapper;
import org.example.glw.service.UserService;
import org.example.glw.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    @Override
    public User findByEmail(String email) {
        return userMapper.selectByEmail(email);
    }
    
    @Override
    public User findByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }
    
    @Override
    public User findById(Long userId) {
        return userMapper.selectById(userId);
    }
    
    @Override
    public int save(User user) {
        return userMapper.insert(user);
    }
    
    @Override
    public int update(User user) {
        return userMapper.updateById(user);
    }
    
    @Override
    public User login(String username, String password) {
        // 根据用户名查询用户
        User user = findByUsername(username);
        if (user == null) {
            return null;
        }
        
        // 验证密码
        if (!PasswordUtils.matches(password, user.getPassword())) {
            return null;
        }
        
        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            return null; // 用户被禁用
        }
        
        return user;
    }
    
    @Override
    public int register(User user) {
        // 设置默认值
        if (user.getGender() == null) {
            user.setGender(0); // 0表示未知
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 1表示启用
        }
        if (user.getRoleId() == null) {
            user.setRoleId(2L); // 默认角色为普通用户
        }
        if (user.getRegisterTime() == null) {
            user.setRegisterTime(LocalDateTime.now());
        }
        
        return userMapper.insert(user);
    }
    
    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
    }
    
    @Override
    public List<User> findUsers(Integer page, Integer size, String username, Integer status) {
        int offset = (page - 1) * size;
        return userMapper.selectUsers(offset, size, username, status);
    }
    
    @Override
    public Long countUsers(String username, Integer status) {
        return userMapper.countUsers(username, status);
    }
}