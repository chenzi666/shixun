package org.example.glw.controller;

import org.example.glw.dto.*;
import org.example.glw.entity.User;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public ApiResponse<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 根据用户ID查询用户信息
        User user = userService.findById(userId);
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }

        // 构建响应数据
        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setGender(user.getGender());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setRegisterTime(user.getRegisterTime());

        return ApiResponse.success("获取成功", userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public ApiResponse<Void> updateUserInfo(@RequestBody UpdateUserRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 根据用户ID查询用户信息
        User user = userService.findById(userId);
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }

        // 更新用户信息
        user.setNickname(request.getNickname());
        user.setAvatar(request.getAvatar());
        user.setGender(request.getGender());

        // 保存更新
        int result = userService.update(user);
        if (result <= 0) {
            return ApiResponse.fail(500, "更新失败");
        }

        return ApiResponse.success("更新成功", null);
    }

    /**
     * 修改密码
     */
    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 根据用户ID查询用户信息
        User user = userService.findById(userId);
        if (user == null) {
            return ApiResponse.fail(404, "用户不存在");
        }

        // 验证旧密码
        if (!org.example.glw.utils.PasswordUtils.matches(request.getOldPassword(), user.getPassword())) {
            return ApiResponse.fail(400, "旧密码错误");
        }

        // 更新密码
        user.setPassword(org.example.glw.utils.PasswordUtils.encodePassword(request.getNewPassword()));
        int result = userService.update(user);
        if (result <= 0) {
            return ApiResponse.fail(500, "密码修改失败");
        }

        return ApiResponse.success("密码修改成功", null);
    }

    /**
     * 从JWT token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.getClaimsFromToken(token);
            if (claims != null) {
                return Long.valueOf(claims.get("userId").toString());
            }
        }
        return null;
    }
}