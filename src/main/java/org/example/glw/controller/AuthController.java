package org.example.glw.controller;

import org.example.glw.dto.*;
import org.example.glw.entity.User;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.example.glw.utils.PasswordUtils;
import org.example.glw.utils.VerificationCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        // 验证参数
        if (request.getUsername() == null || request.getPassword() == null) {
            return ApiResponse.fail(400, "用户名或密码不能为空");
        }
        
        // 用户登录
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ApiResponse.fail(401, "用户名或密码错误");
        }
        
        // 更新最后登录时间
        userService.updateLastLoginTime(user.getUserId());
        
        // 生成Token
        String role = user.getRoleId() != null ? (user.getRoleId() == 1 ? "admin" : "user") : "user";
        String token = jwtUtils.generateToken(user.getUserId(), user.getUsername(), role);
        
        // 构建响应
        LoginResponse loginResponse = new LoginResponse(token, user);
        return ApiResponse.success("登录成功", loginResponse);
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest request) {
        // 验证参数
        if (request.getUsername() == null || request.getPassword() == null) {
            return ApiResponse.fail(400, "用户名或密码不能为空");
        }
        
        // 检查用户名是否已存在
        if (userService.findByUsername(request.getUsername()) != null) {
            return ApiResponse.fail(400, "用户名已存在");
        }
        
        // 检查邮箱是否已存在（如果提供了邮箱）
        if (request.getEmail() != null && userService.findByEmail(request.getEmail()) != null) {
            return ApiResponse.fail(400, "邮箱已存在");
        }
        
        // 检查手机号是否已存在（如果提供了手机号）
        if (request.getPhone() != null && userService.findByPhone(request.getPhone()) != null) {
            return ApiResponse.fail(400, "手机号已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtils.encodePassword(request.getPassword())); // 加密密码
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        
        // 注册用户
        int result = userService.register(user);
        if (result <= 0) {
            return ApiResponse.fail(500, "注册失败，请稍后重试");
        }
        
        // 构建响应
        RegisterResponse registerResponse = new RegisterResponse(user.getUserId());
        return ApiResponse.success("注册成功", registerResponse);
    }
    
    /**
     * 发送验证码
     */
    @PostMapping("/send-code")
    public ApiResponse<Void> sendCode(@RequestBody SendCodeRequest request) {
        // 验证参数
        if (request.getType() == null || request.getTarget() == null) {
            return ApiResponse.fail(400, "参数错误");
        }
        
        // 验证type是否为email或phone
        if (!"email".equals(request.getType()) && !"phone".equals(request.getType())) {
            return ApiResponse.fail(400, "类型错误，支持email或phone");
        }
        
        // 生成验证码
        String code = VerificationCodeUtils.generateCode();
        
        // 保存验证码
        VerificationCodeUtils.saveCode(request.getTarget(), code);
        
        // TODO: 实际项目中这里应该调用邮件或短信发送服务发送验证码
        // 这里为了演示，只是打印验证码到控制台
        System.out.println("向" + request.getType() + "=" + request.getTarget() + "发送验证码: " + code);
        
        return ApiResponse.success("验证码发送成功", null);
    }
    
    /**
     * 忘记密码/重置密码
     */
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        // 验证参数
        if (request.getUsername() == null || request.getVerificationCode() == null || request.getNewPassword() == null) {
            return ApiResponse.fail(400, "参数错误");
        }
        
        // 查找用户
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }
        
        // 确定验证目标（优先使用邮箱，其次使用手机号）
        String target = null;
        if (user.getEmail() != null) {
            target = user.getEmail();
        } else if (user.getPhone() != null) {
            target = user.getPhone();
        } else {
            return ApiResponse.fail(400, "用户未设置邮箱或手机号，无法重置密码");
        }
        
        // 验证验证码
        if (!VerificationCodeUtils.validateCode(target, request.getVerificationCode())) {
            return ApiResponse.fail(400, "验证码错误或已过期");
        }
        
        // 更新密码
        user.setPassword(PasswordUtils.encodePassword(request.getNewPassword()));
        int result = userService.update(user);
        if (result <= 0) {
            return ApiResponse.fail(500, "密码重置失败，请稍后重试");
        }
        
        return ApiResponse.success("密码重置成功", null);
    }
    
    /**
     * 注册响应数据类
     */
    public static class RegisterResponse {
        private Long userId;
        
        public RegisterResponse(Long userId) {
            this.userId = userId;
        }
        
        public Long getUserId() {
            return userId;
        }
        
        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}