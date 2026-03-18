package org.example.glw.dto;

/**
 * Banner响应数据
 */
public class BannerResponse {
    private Long bannerId;
    private String title;
    private String imageUrl;
    private String linkUrl;

    // Getters and Setters
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