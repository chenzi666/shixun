package org.example.glw.controller;

import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.entity.Announcement;
import org.example.glw.entity.Banner;
import org.example.glw.service.AnnouncementService;
import org.example.glw.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容管理控制器
 */
@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    @Autowired
    private AnnouncementService announcementService;
    
    @Autowired
    private BannerService bannerService;

    /**
     * 获取公告列表
     */
    @GetMapping("/announcements")
    public ApiResponse<PageResponse<AnnouncementResponse>> getAnnouncements(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        
        // 获取公告列表
        List<Announcement> announcements = announcementService.getAnnouncements(page, size);
        // 获取公告总数（这里简化处理，实际应该有统计方法）
        long total = 0; // 简化处理，实际应查询数据库获取总数
        
        // 转换为响应对象
        List<AnnouncementResponse> responseList = announcements.stream().map(announcement -> {
            AnnouncementResponse response = new AnnouncementResponse();
            response.setAnnouncementId(announcement.getAnnouncementId());
            response.setTitle(announcement.getTitle());
            response.setPublishTime(announcement.getPublishTime());
            return response;
        }).collect(Collectors.toList());

        PageResponse<AnnouncementResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(responseList);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 获取公告详情
     */
    @GetMapping("/announcements/{announcementId}")
    public ApiResponse<AnnouncementDetailResponse> getAnnouncementDetail(@PathVariable Long announcementId) {
        // 获取公告详情
        Announcement announcement = announcementService.getAnnouncementById(announcementId);
        if (announcement == null) {
            return ApiResponse.fail(404, "公告不存在");
        }

        AnnouncementDetailResponse response = new AnnouncementDetailResponse();
        response.setAnnouncementId(announcement.getAnnouncementId());
        response.setTitle(announcement.getTitle());
        response.setContent(announcement.getContent());
        response.setPublishTime(announcement.getPublishTime());

        return ApiResponse.success("获取成功", response);
    }

    /**
     * 获取Banner列表
     */
    @GetMapping("/banners")
    public ApiResponse<java.util.List<BannerResponse>> getBanners() {
        // 获取Banner列表
        List<Banner> banners = bannerService.getBanners();
        
        // 转换为响应对象
        List<BannerResponse> responseList = banners.stream().map(banner -> {
            BannerResponse response = new BannerResponse();
            response.setBannerId(banner.getBannerId());
            response.setTitle(banner.getTitle());
            response.setImageUrl(banner.getImageUrl());
            response.setLinkUrl(banner.getLinkUrl());
            return response;
        }).collect(Collectors.toList());

        return ApiResponse.success("获取成功", responseList);
    }

    // 内部类定义
    public static class AnnouncementResponse {
        private Long announcementId;
        private String title;
        private java.time.LocalDateTime publishTime;

        public Long getAnnouncementId() {
            return announcementId;
        }

        public void setAnnouncementId(Long announcementId) {
            this.announcementId = announcementId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public java.time.LocalDateTime getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(java.time.LocalDateTime publishTime) {
            this.publishTime = publishTime;
        }
    }

    public static class AnnouncementDetailResponse {
        private Long announcementId;
        private String title;
        private String content;
        private java.time.LocalDateTime publishTime;

        public Long getAnnouncementId() {
            return announcementId;
        }

        public void setAnnouncementId(Long announcementId) {
            this.announcementId = announcementId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public java.time.LocalDateTime getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(java.time.LocalDateTime publishTime) {
            this.publishTime = publishTime;
        }
    }

    public static class BannerResponse {
        private Long bannerId;
        private String title;
        private String imageUrl;
        private String linkUrl;

        public Long getBannerId() {
            return bannerId;
        }

        public void setBannerId(Long bannerId) {
            this.bannerId = bannerId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }
}