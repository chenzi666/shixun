package org.example.glw.dto.order;

import java.math.BigDecimal;

/**
 * 创建订单响应数据
 */
public class CreateOrderResponse {
    private Long orderId;
    private String orderNo;
    private BigDecimal totalAmount;

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}