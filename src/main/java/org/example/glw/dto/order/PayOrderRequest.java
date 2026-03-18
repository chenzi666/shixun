package org.example.glw.dto.order;

/**
 * 支付订单请求参数
 */
public class PayOrderRequest {
    private String paymentMethod;

    // Getters and Setters
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}