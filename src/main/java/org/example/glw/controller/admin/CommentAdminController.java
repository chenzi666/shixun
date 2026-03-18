package org.example.glw.controller.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.entity.Comment;
import org.example.glw.entity.User;
import org.example.glw.service.CommentService;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价管理控制器（管理员）
 */
@RestController
@RequestMapping("/api/v1/comment/manage")
public class CommentAdminController {

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 管理评价列表（管理员）
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<ManageCommentResponse>> getManageCommentList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String productType,
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

        // TODO: 实际实现中应该根据productType和status进行筛选
        // 这里简化实现，获取所有评价
        // 注意：实际项目中应该在CommentMapper中添加对应的查询方法
        
        // 模拟数据
        PageResponse<ManageCommentResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(List.of()); // 空列表
        pageResponse.setTotal(0L);
        pageResponse.setPage(page);
        pageResponse.setSize(size);

        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 审核评价（管理员）
     */
    @PutMapping("/audit/{commentId}")
    public ApiResponse<Void> auditComment(
            @PathVariable Long commentId,
            @RequestBody org.example.glw.dto.comment.AuditCommentRequest request,
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

        // 获取评价
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ApiResponse.fail(404, "评价不存在");
        }
        
        // 更新评价状态
        comment.setStatus(request.getStatus());
        // 注意：实际项目中应该在CommentMapper中添加更新方法
        
        return ApiResponse.success("审核成功", null);
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

    // 响应类定义
    public static class ManageCommentResponse {
        private Long commentId;
        private Long userId;
        private String username;
        private Long productId;
        private String productType;
        private String productName;
        private Integer score;
        private String content;
        private java.time.LocalDateTime createTime;
        private Integer status;

        // Getters and Setters
        public Long getCommentId() {
            return commentId;
        }

        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }

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

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}