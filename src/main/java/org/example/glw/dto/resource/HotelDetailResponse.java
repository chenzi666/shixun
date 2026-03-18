package org.example.glw.dto.resource;

import java.math.BigDecimal;
import java.util.List;

/**
 * 酒店详情响应数据
 */
public class HotelDetailResponse extends HotelResponse {
    private String phone;
    private List<String> facilities;
    private String videoUrl;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Boolean isFavorite;
    private List<CommentResponse> comments;
    private List<HotelRoomResponse> rooms;

    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
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

    public List<HotelRoomResponse> getRooms() {
        return rooms;
    }

    public void setRooms(List<HotelRoomResponse> rooms) {
        this.rooms = rooms;
    }
}