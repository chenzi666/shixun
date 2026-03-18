package org.example.glw.dto.resource;

import java.math.BigDecimal;
import java.util.List;

/**
 * 景点详情响应数据
 */
public class AttractionDetailResponse extends AttractionResponse {
    private Boolean isFavorite;
    private List<CommentResponse> comments;

    // Getters and Setters
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