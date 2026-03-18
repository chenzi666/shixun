package org.example.glw.controller.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.dto.UpdateUserStatusRequest;
import org.example.glw.dto.UserListResponse;
import org.example.glw.entity.User;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理控制器（管理员）
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 管理用户列表（管理员）
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<UserListResponse>> getUserList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 检查是否为管理员
        User currentUser = userService.findById(userId);
        if (currentUser == null || currentUser.getRoleId() != 1) { // 1为管理员角色ID
            return ApiResponse.fail(403, "权限不足，无法执行此操作");
        }

        // 查询用户列表
        List<User> users = userService.findUsers(page, size, username, status);
        Long total = userService.countUsers(username, status);
        
        // 转换为响应数据
        List<UserListResponse> userListResponses = users.stream().map(user -> {
            UserListResponse response = new UserListResponse();
            response.setUserId(user.getUserId());
            response.setUsername(user.getUsername());
            response.setNickname(user.getNickname());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());
            response.setStatus(user.getStatus());
            response.setRegisterTime(user.getRegisterTime());
            // 简化处理角色名称，实际应该查询角色表获取
            response.setRoleName(user.getRoleId() != null ? (user.getRoleId() == 1 ? "管理员" : "普通用户") : "未知");
            return response;
        }).collect(Collectors.toList());

        PageResponse<UserListResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(userListResponses);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);

        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 禁用/启用用户（管理员）
     */
    @PutMapping("/status/{userId}")
    public ApiResponse<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody UpdateUserStatusRequest request,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long currentUserId = getUserIdFromToken(httpRequest);
        if (currentUserId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 检查是否为管理员
        User currentUser = userService.findById(currentUserId);
        if (currentUser == null || currentUser.getRoleId() != 1) { // 1为管理员角色ID
            return ApiResponse.fail(403, "权限不足，无法执行此操作");
        }

        // 检查目标用户是否存在
        User targetUser = userService.findById(userId);
        if (targetUser == null) {
            return ApiResponse.fail(404, "用户不存在");
        }

        // 不能禁用自己
        if (currentUserId.equals(userId)) {
            return ApiResponse.fail(400, "不能禁用自己");
        }

        // 更新用户状态
        targetUser.setStatus(request.getStatus());
        int result = userService.update(targetUser);
        if (result <= 0) {
            return ApiResponse.fail(500, "操作失败");
        }

        return ApiResponse.success("操作成功", null);
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