package org.example.glw.controller.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.entity.Announcement;
import org.example.glw.entity.Banner;
import org.example.glw.entity.User;
import org.example.glw.service.AnnouncementService;
import org.example.glw.service.BannerService;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 内容管理控制器（管理员）
 */
@RestController
@RequestMapping("/api/v1/content/manage")
public class ContentAdminController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AnnouncementService announcementService;
    
    @Autowired
    private BannerService bannerService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 添加公告（管理员）
     */
    @PostMapping("/announcement/add")
    public ApiResponse<Void> addAnnouncement(
            @RequestBody org.example.glw.dto.content.AddAnnouncementRequest request,
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

        // 创建公告实体
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setStatus(request.getStatus());
        announcement.setCreateBy(userId);

        // 添加公告
        int result = announcementService.addAnnouncement(announcement);
        if (result > 0) {
            return ApiResponse.success("公告发布成功", null);
        } else {
            return ApiResponse.fail(500, "公告发布失败");
        }
    }

    /**
     * 更新公告（管理员）
     */
    @PutMapping("/announcement/update/{announcementId}")
    public ApiResponse<Void> updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody org.example.glw.dto.content.UpdateAnnouncementRequest request,
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

        // 获取公告详情
        Announcement announcement = announcementService.getAnnouncementById(announcementId);
        if (announcement == null) {
            return ApiResponse.fail(404, "公告不存在");
        }

        // 更新公告信息
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setStatus(request.getStatus());

        // 更新公告
        int result = announcementService.updateAnnouncement(announcement);
        if (result > 0) {
            return ApiResponse.success("公告更新成功", null);
        } else {
            return ApiResponse.fail(500, "公告更新失败");
        }
    }

    /**
     * 删除公告（管理员）
     */
    @DeleteMapping("/announcement/delete/{announcementId}")
    public ApiResponse<Void> deleteAnnouncement(
            @PathVariable Long announcementId,
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

        // 删除公告
        int result = announcementService.deleteAnnouncement(announcementId);
        if (result > 0) {
            return ApiResponse.success("公告删除成功", null);
        } else {
            return ApiResponse.fail(500, "公告删除失败");
        }
    }

    /**
     * 添加Banner（管理员）
     */
    @PostMapping("/banner/add")
    public ApiResponse<Void> addBanner(
            @RequestBody org.example.glw.dto.content.AddBannerRequest request,
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

        // 创建Banner实体
        Banner banner = new Banner();
        banner.setTitle(request.getTitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLinkUrl(request.getLinkUrl());
        banner.setSortOrder(request.getSortOrder());
        banner.setStatus(request.getStatus());

        // 添加Banner
        int result = bannerService.addBanner(banner);
        if (result > 0) {
            return ApiResponse.success("Banner添加成功", null);
        } else {
            return ApiResponse.fail(500, "Banner添加失败");
        }
    }

    /**
     * 更新Banner（管理员）
     */
    @PutMapping("/banner/update/{bannerId}")
    public ApiResponse<Void> updateBanner(
            @PathVariable Long bannerId,
            @RequestBody org.example.glw.dto.content.UpdateBannerRequest request,
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

        // 获取Banner详情
        Banner banner = bannerService.getBannerById(bannerId);
        if (banner == null) {
            return ApiResponse.fail(404, "Banner不存在");
        }

        // 更新Banner信息
        banner.setTitle(request.getTitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLinkUrl(request.getLinkUrl());
        banner.setSortOrder(request.getSortOrder());
        banner.setStatus(request.getStatus());

        // 更新Banner
        int result = bannerService.updateBanner(banner);
        if (result > 0) {
            return ApiResponse.success("Banner更新成功", null);
        } else {
            return ApiResponse.fail(500, "Banner更新失败");
        }
    }

    /**
     * 删除Banner（管理员）
     */
    @DeleteMapping("/banner/delete/{bannerId}")
    public ApiResponse<Void> deleteBanner(
            @PathVariable Long bannerId,
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

        // 删除Banner
        int result = bannerService.deleteBanner(bannerId);
        if (result > 0) {
            return ApiResponse.success("Banner删除成功", null);
        } else {
            return ApiResponse.fail(500, "Banner删除失败");
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
}