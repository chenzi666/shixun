package org.example.glw.dto;

/**
 * 更新用户状态请求参数
 */
public class UpdateUserStatusRequest {
    private Integer status;

    // Getters and Setters
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}