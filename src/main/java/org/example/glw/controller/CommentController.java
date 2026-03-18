package org.example.glw.controller;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价管理控制器
 */
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 添加评价
     */
    @PostMapping("/add")
    public ApiResponse<CommentAddResponse> addComment(@RequestBody CommentAddRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 创建评价实体
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setOrderItemId(request.getOrderItemId());
        comment.setProductId(request.getProductId());
        comment.setProductType(request.getProductType());
        comment.setScore(request.getScore());
        comment.setContent(request.getContent());
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            comment.setImages(String.join(",", request.getImages()));
        }
        
        // 保存评价
        int result = commentService.addComment(comment);
        if (result > 0) {
            CommentAddResponse response = new CommentAddResponse();
            response.setCommentId(comment.getCommentId());
            return ApiResponse.success("评价发布成功", response);
        } else {
            return ApiResponse.fail(500, "评价发布失败");
        }
    }

    /**
     * 获取产品评价列表
     */
    @GetMapping("/list/{productType}/{productId}")
    public ApiResponse<ProductCommentListResponse> getProductCommentList(
            @PathVariable String productType,
            @PathVariable Long productId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer score) {
        
        // 获取评价列表
        List<Comment> comments = commentService.getProductComments(productType, productId, page, size, score);
        Long total = commentService.countProductComments(productType, productId, score);
        
        // 转换为响应对象
        List<ProductCommentResponse> responseList = comments.stream().map(comment -> {
            ProductCommentResponse response = new ProductCommentResponse();
            response.setCommentId(comment.getCommentId());
            response.setUserId(comment.getUserId());
            
            // 获取用户名和头像
            User user = userService.findById(comment.getUserId());
            if (user != null) {
                response.setUsername(user.getNickname());
                response.setAvatar(user.getAvatar());
            }
            
            response.setScore(comment.getScore());
            response.setContent(comment.getContent());
            if (comment.getImages() != null && !comment.getImages().isEmpty()) {
                response.setImages(List.of(comment.getImages().split(",")));
            }
            response.setCreateTime(comment.getCreateTime());
            return response;
        }).collect(Collectors.toList());
        
        // 构造评分分布（简化实现）
        ScoreDistribution scoreDistribution = new ScoreDistribution();
        scoreDistribution.setFive(0);
        scoreDistribution.setFour(0);
        scoreDistribution.setThree(0);
        scoreDistribution.setTwo(0);
        scoreDistribution.setOne(0);
        
        ProductCommentListResponse response = new ProductCommentListResponse();
        response.setList(responseList);
        response.setTotal(total);
        response.setPage(page);
        response.setSize(size);
        response.setScoreDistribution(scoreDistribution);
        return ApiResponse.success("获取成功", response);
    }

    /**
     * 获取用户评价列表
     */
    @GetMapping("/my-list")
    public ApiResponse<PageResponse<UserCommentResponse>> getMyCommentList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 获取用户评价列表
        List<Comment> comments = commentService.getUserComments(userId, page, size);
        Long total = commentService.countUserComments(userId);
        
        // 转换为响应对象
        List<UserCommentResponse> responseList = comments.stream().map(comment -> {
            UserCommentResponse response = new UserCommentResponse();
            response.setCommentId(comment.getCommentId());
            response.setProductId(comment.getProductId());
            response.setProductType(comment.getProductType());
            response.setProductName(getProductName(comment.getProductType(), comment.getProductId()));
            response.setScore(comment.getScore());
            response.setContent(comment.getContent());
            response.setCreateTime(comment.getCreateTime());
            return response;
        }).collect(Collectors.toList());
        
        PageResponse<UserCommentResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(responseList);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 删除评价
     */
    @DeleteMapping("/delete/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 删除评价
        boolean result = commentService.deleteComment(userId, commentId);
        if (result) {
            return ApiResponse.success("评价删除成功", null);
        } else {
            return ApiResponse.fail(500, "评价删除失败");
        }
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
    
    /**
     * 根据产品类型和ID获取产品名称（简化实现）
     */
    private String getProductName(String productType, Long productId) {
        // 这里应该根据产品类型查询对应的产品名称
        // 简化实现，直接返回固定值
        return "产品名称";
    }

    // 内部类定义
    public static class CommentAddRequest {
        private Long orderItemId;
        private Long productId;
        private String productType;
        private Integer score;
        private String content;
        private java.util.List<String> images;

        public Long getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(Long orderItemId) {
            this.orderItemId = orderItemId;
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

        public java.util.List<String> getImages() {
            return images;
        }

        public void setImages(java.util.List<String> images) {
            this.images = images;
        }
    }

    public static class CommentAddResponse {
        private Long commentId;

        public Long getCommentId() {
            return commentId;
        }

        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }
    }

    public static class ProductCommentResponse {
        private Long commentId;
        private Long userId;
        private String username;
        private String avatar;
        private Integer score;
        private String content;
        private java.util.List<String> images;
        private java.time.LocalDateTime createTime;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public java.util.List<String> getImages() {
            return images;
        }

        public void setImages(java.util.List<String> images) {
            this.images = images;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }

    public static class ProductCommentListResponse extends PageResponse<ProductCommentResponse> {
        private ScoreDistribution scoreDistribution;

        public ScoreDistribution getScoreDistribution() {
            return scoreDistribution;
        }

        public void setScoreDistribution(ScoreDistribution scoreDistribution) {
            this.scoreDistribution = scoreDistribution;
        }
    }

    public static class ScoreDistribution {
        private Integer five;
        private Integer four;
        private Integer three;
        private Integer two;
        private Integer one;

        public Integer getFive() {
            return five;
        }

        public void setFive(Integer five) {
            this.five = five;
        }

        public Integer getFour() {
            return four;
        }

        public void setFour(Integer four) {
            this.four = four;
        }

        public Integer getThree() {
            return three;
        }

        public void setThree(Integer three) {
            this.three = three;
        }

        public Integer getTwo() {
            return two;
        }

        public void setTwo(Integer two) {
            this.two = two;
        }

        public Integer getOne() {
            return one;
        }

        public void setOne(Integer one) {
            this.one = one;
        }
    }

    public static class UserCommentResponse {
        private Long commentId;
        private Long productId;
        private String productType;
        private String productName;
        private Integer score;
        private String content;
        private java.time.LocalDateTime createTime;

        public Long getCommentId() {
            return commentId;
        }

        public void setCommentId(Long commentId) {
            this.commentId = commentId;
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
    }
}