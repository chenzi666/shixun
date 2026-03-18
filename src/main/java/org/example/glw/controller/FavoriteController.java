package org.example.glw.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.entity.Favorite;
import org.example.glw.service.FavoriteService;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏管理控制器
 */
@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 添加收藏
     */
    @PostMapping("/add")
    public ApiResponse<FavoriteAddResponse> addFavorite(@RequestBody FavoriteAddRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 检查是否已收藏
        Favorite existingFavorite = favoriteService.getFavoriteByUserAndProduct(userId, request.getProductId(), request.getProductType());
        if (existingFavorite != null) {
            FavoriteAddResponse response = new FavoriteAddResponse();
            response.setFavoriteId(existingFavorite.getFavoriteId());
            return ApiResponse.success("收藏成功", response);
        }

        // 创建收藏实体
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(request.getProductId());
        favorite.setProductType(request.getProductType());
        
        // 保存收藏
        int result = favoriteService.addFavorite(favorite);
        if (result > 0) {
            FavoriteAddResponse response = new FavoriteAddResponse();
            response.setFavoriteId(favorite.getFavoriteId());
            return ApiResponse.success("收藏成功", response);
        } else {
            return ApiResponse.fail(500, "收藏失败");
        }
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/delete/{favoriteId}")
    public ApiResponse<Void> deleteFavorite(@PathVariable Long favoriteId, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 取消收藏
        boolean result = favoriteService.deleteFavorite(userId, favoriteId);
        if (result) {
            return ApiResponse.success("取消收藏成功", null);
        } else {
            return ApiResponse.fail(500, "取消收藏失败");
        }
    }

    /**
     * 获取收藏列表
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<FavoriteResponse>> getFavoriteList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String type,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 获取收藏列表
        List<Favorite> favorites = favoriteService.getFavorites(userId, page, size, type);
        Long total = favoriteService.countFavorites(userId, type);
        
        // 转换为响应对象
        List<FavoriteResponse> responseList = favorites.stream().map(favorite -> {
            FavoriteResponse response = new FavoriteResponse();
            response.setFavoriteId(favorite.getFavoriteId());
            response.setProductId(favorite.getProductId());
            response.setProductType(favorite.getProductType());
            response.setProductName(getProductName(favorite.getProductType(), favorite.getProductId()));
            response.setProductImage(getProductImage(favorite.getProductType(), favorite.getProductId()));
            response.setPrice(getProductPrice(favorite.getProductType(), favorite.getProductId()));
            response.setCreateTime(favorite.getCreateTime());
            return response;
        }).collect(Collectors.toList());

        PageResponse<FavoriteResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(responseList);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check")
    public ApiResponse<FavoriteCheckResponse> checkFavorite(
            @RequestParam Long productId,
            @RequestParam String productType,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 检查是否已收藏
        Favorite favorite = favoriteService.getFavoriteByUserAndProduct(userId, productId, productType);
        
        FavoriteCheckResponse response = new FavoriteCheckResponse();
        if (favorite != null) {
            response.setFavorite(true);
            response.setFavoriteId(favorite.getFavoriteId());
        } else {
            response.setFavorite(false);
            response.setFavoriteId(null);
        }
        return ApiResponse.success("查询成功", response);
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
        return productType + "名称";
    }
    
    /**
     * 根据产品类型和ID获取产品图片（简化实现）
     */
    private String getProductImage(String productType, Long productId) {
        // 这里应该根据产品类型查询对应的产品图片
        // 简化实现，直接返回固定值
        return "https://example.com/image.jpg";
    }
    
    /**
     * 根据产品类型和ID获取产品价格（简化实现）
     */
    private java.math.BigDecimal getProductPrice(String productType, Long productId) {
        // 这里应该根据产品类型查询对应的产品价格
        // 简化实现，直接返回固定值
        return new java.math.BigDecimal("99.99");
    }

    // 内部类定义
    public static class FavoriteAddRequest {
        private Long productId;
        private String productType;

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
    }

    public static class FavoriteAddResponse {
        private Long favoriteId;

        public Long getFavoriteId() {
            return favoriteId;
        }

        public void setFavoriteId(Long favoriteId) {
            this.favoriteId = favoriteId;
        }
    }

    public static class FavoriteResponse {
        private Long favoriteId;
        private Long productId;
        private String productType;
        private String productName;
        private String productImage;
        private java.math.BigDecimal price;
        private java.time.LocalDateTime createTime;

        public Long getFavoriteId() {
            return favoriteId;
        }

        public void setFavoriteId(Long favoriteId) {
            this.favoriteId = favoriteId;
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

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public java.math.BigDecimal getPrice() {
            return price;
        }

        public void setPrice(java.math.BigDecimal price) {
            this.price = price;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }

    public static class FavoriteCheckResponse {
        private Boolean isFavorite;
        private Long favoriteId;

        public Boolean getFavorite() {
            return isFavorite;
        }

        public void setFavorite(Boolean favorite) {
            isFavorite = favorite;
        }

        public Long getFavoriteId() {
            return favoriteId;
        }

        public void setFavoriteId(Long favoriteId) {
            this.favoriteId = favoriteId;
        }
    }
}