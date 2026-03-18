package org.example.glw.dto.resource;

import java.math.BigDecimal;
import java.util.List;

/**
 * 线路详情响应数据
 */
public class RouteDetailResponse extends RouteResponse {
    private String includes;
    private String excludes;
    private String notice;
    private String videoUrl;
    private Boolean isFavorite;
    private List<CommentResponse> comments;

    // Getters and Setters
    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }
}