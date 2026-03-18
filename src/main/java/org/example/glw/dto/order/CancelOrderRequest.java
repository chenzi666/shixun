package org.example.glw.dto.order;

/**
 * 取消订单请求参数
 */
public class CancelOrderRequest {
    private String reason;

    // Getters and Setters
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}