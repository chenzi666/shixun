package org.example.glw.service.admin.impl;

import org.example.glw.entity.User;
import org.example.glw.mapper.admin.AdminUserMapper;
import org.example.glw.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    
    @Autowired
    private AdminUserMapper adminUserMapper;
    
    @Override
    public List<User> findUsers(Integer page, Integer size, String username, Integer status) {
        int offset = (page - 1) * size;
        return adminUserMapper.selectUsers(offset, size, username, status);
    }
    
    @Override
    public Long countUsers(String username, Integer status) {
        return adminUserMapper.countUsers(username, status);
    }
    
    @Override
    public int updateUserStatus(Long userId, Integer status) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(status);
        return adminUserMapper.updateById(user);
    }
}