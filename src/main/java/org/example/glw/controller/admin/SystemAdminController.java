package org.example.glw.controller.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.dto.system.SystemLogResponse;
import org.example.glw.dto.system.SystemStatsResponse;
import org.example.glw.entity.Role;
import org.example.glw.entity.SystemLog;
import org.example.glw.entity.User;
import org.example.glw.service.RoleService;
import org.example.glw.service.SystemLogService;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统管理控制器（管理员）
 */
@RestController
@RequestMapping("/api/v1/system")
public class SystemAdminController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SystemLogService systemLogService;
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取系统统计数据（管理员）
     */
    @GetMapping("/stats")
    public ApiResponse<SystemStatsResponse> getSystemStats(HttpServletRequest httpRequest) {
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

        // 构造系统统计数据响应
        SystemStatsResponse response = new SystemStatsResponse();
        // TODO: 实际项目中应该从数据库查询真实数据
        response.setTotalUsers(1000L);
        response.setTotalOrders(5000L);
        response.setTotalSales(new BigDecimal("1000000"));
        response.setTodayOrders(100L);
        response.setTodaySales(new BigDecimal("20000"));
        response.setUserGrowth(10);
        response.setOrderTrend(List.of(100, 120, 90, 150, 180, 160, 200));

        return ApiResponse.success("获取成功", response);
    }

    /**
     * 获取操作日志（管理员）
     */
    @GetMapping("/logs")
    public ApiResponse<PageResponse<SystemLogResponse>> getSystemLogs(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
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

        // 解析日期参数
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        try {
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDate.parse(startDate).atStartOfDay();
            }
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDate.parse(endDate).atTime(23, 59, 59);
            }
        } catch (Exception e) {
            return ApiResponse.fail(400, "日期格式错误");
        }

        // 获取系统日志列表
        List<SystemLog> systemLogs = systemLogService.getSystemLogs(page, size, username, startDateTime, endDateTime);
        Long total = systemLogService.countSystemLogs(username, startDateTime, endDateTime);

        // 转换为响应对象
        List<SystemLogResponse> responseList = systemLogs.stream().map(log -> {
            SystemLogResponse response = new SystemLogResponse();
            response.setLogId(log.getLogId());
            response.setUsername(log.getUsername());
            response.setOperation(log.getOperation());
            response.setIp(log.getIp());
            response.setCreateTime(log.getCreateTime());
            response.setStatus(log.getStatus());
            return response;
        }).collect(Collectors.toList());

        PageResponse<SystemLogResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(responseList);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);

        return ApiResponse.success("获取成功", pageResponse);
    }
    
    /**
     * 获取角色列表（管理员）
     */
    @GetMapping("/roles")
    public ApiResponse<List<RoleResponse>> getRoles(HttpServletRequest httpRequest) {
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

        // 获取角色列表
        List<Role> roles = roleService.getRoles();
        
        // 转换为响应对象
        List<RoleResponse> responseList = roles.stream().map(role -> {
            RoleResponse response = new RoleResponse();
            response.setRoleId(role.getRoleId());
            response.setRoleName(role.getRoleName());
            response.setDescription(role.getDescription());
            return response;
        }).collect(Collectors.toList());

        return ApiResponse.success("获取成功", responseList);
    }
    
    /**
     * 获取权限列表（管理员）
     */
    @GetMapping("/permissions")
    public ApiResponse<List<PermissionResponse>> getPermissions(HttpServletRequest httpRequest) {
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

        // TODO: 实际项目中应该根据角色ID获取权限列表
        // 这里简化处理，直接返回所有权限
        
        // 构造响应数据
        List<PermissionResponse> responseList = List.of(); // 空列表，实际应从数据库查询

        return ApiResponse.success("获取成功", responseList);
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
    
    // 内部类定义
    public static class RoleResponse {
        private Long roleId;
        private String roleName;
        private String description;

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
    
    public static class PermissionResponse {
        private Long permissionId;
        private String permissionName;
        private String permissionCode;
        private String description;

        public Long getPermissionId() {
            return permissionId;
        }

        public void setPermissionId(Long permissionId) {
            this.permissionId = permissionId;
        }

        public String getPermissionName() {
            return permissionName;
        }

        public void setPermissionName(String permissionName) {
            this.permissionName = permissionName;
        }

        public String getPermissionCode() {
            return permissionCode;
        }

        public void setPermissionCode(String permissionCode) {
            this.permissionCode = permissionCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}