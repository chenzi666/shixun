package org.example.glw.dto.order;

/**
 * 支付订单响应数据
 */
public class PayOrderResponse {
    private String payUrl;
    private String qrCode;

    // Getters and Setters
    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}