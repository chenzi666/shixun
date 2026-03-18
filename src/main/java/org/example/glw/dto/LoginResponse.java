package org.example.glw.dto;

import org.example.glw.entity.User;

/**
 * 登录响应数据
 */
public class LoginResponse {
    private String token;
    private UserInfo user;
    
    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = new UserInfo(user);
    }
    
    // 用户信息内部类
    public static class UserInfo {
        private Long userId;
        private String username;
        private String nickname;
        private String avatar;
        private String role;
        
        public UserInfo(User user) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.avatar = user.getAvatar();
            this.role = user.getRoleId() != null ? (user.getRoleId() == 1 ? "admin" : "user") : "user";
        }
        
        // Getters and Setters
        public Long getUserId() {
            return userId;
        }
        
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getNickname() {
            return nickname;
        }
        
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        
        public String getAvatar() {
            return avatar;
        }
        
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserInfo getUser() {
        return user;
    }
    
    public void setUser(UserInfo user) {
        this.user = user;
    }
}