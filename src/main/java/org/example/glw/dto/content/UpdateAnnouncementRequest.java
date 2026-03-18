package org.example.glw.dto.content;

/**
 * 更新公告请求参数
 */
public class UpdateAnnouncementRequest {
    private String title;
    private String content;
    private Integer status;

    // Getters and Setters
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}