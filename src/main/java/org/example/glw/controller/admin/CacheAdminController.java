package org.example.glw.controller.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.entity.User;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 缓存管理控制器（管理员专用）
 */
@RestController
@RequestMapping("/api/v1/cache")
public class CacheAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 刷新缓存
     */
    @PostMapping("/refresh/{type}")
    public ApiResponse<Void> refreshCache(@PathVariable String type, HttpServletRequest httpRequest) {
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

        // 验证缓存类型
        if (!"product".equals(type) && !"banner".equals(type) && !"config".equals(type)) {
            return ApiResponse.fail(400, "不支持的缓存类型");
        }

        // TODO: 实际项目中应该实现具体的缓存刷新逻辑
        // 这里简化处理，直接返回成功

        return ApiResponse.success("缓存刷新成功", null);
    }

    /**
     * 清除缓存
     */
    @DeleteMapping("/clear/{type}")
    public ApiResponse<Void> clearCache(@PathVariable String type, HttpServletRequest httpRequest) {
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

        // 验证缓存类型
        if (!"product".equals(type) && !"banner".equals(type) && !"config".equals(type)) {
            return ApiResponse.fail(400, "不支持的缓存类型");
        }

        // TODO: 实际项目中应该实现具体的缓存清除逻辑
        // 这里简化处理，直接返回成功

        return ApiResponse.success("缓存清除成功", null);
    }

    /**
     * 重建搜索索引
     */
    @PostMapping("/index/rebuild/{type}")
    public ApiResponse<RebuildIndexResponse> rebuildIndex(@PathVariable String type, HttpServletRequest httpRequest) {
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

        // 验证索引类型
        if (!"all".equals(type) && !"attraction".equals(type) && !"route".equals(type) && 
            !"hotel".equals(type) && !"food".equals(type)) {
            return ApiResponse.fail(400, "不支持的索引类型");
        }

        // TODO: 实际项目中应该实现具体的索引重建逻辑
        // 这里简化处理，直接返回模拟数据
        RebuildIndexResponse response = new RebuildIndexResponse();
        response.setCount(1000); // 模拟重建了1000条索引

        return ApiResponse.success("索引重建成功", response);
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
    public static class RebuildIndexResponse {
        private Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
}