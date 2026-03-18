package org.example.glw.service.impl;

import org.example.glw.entity.User;
import org.example.glw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查找用户
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 创建权限列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        String role = user.getRoleId() != null ? (user.getRoleId() == 1 ? "ROLE_ADMIN" : "ROLE_USER") : "ROLE_USER";
        authorities.add(new SimpleGrantedAuthority(role));

        // 返回UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus() != null && user.getStatus() == 1, // 账户是否启用
                true, // 账户是否过期
                true, // 凭证是否过期
                true, // 账户是否锁定
                authorities
        );
    }
}