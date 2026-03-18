package org.example.glw.dto.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * 申请退款请求参数
 */
public class RefundRequest {
    private BigDecimal refundAmount;
    private String reason;
    private List<Long> itemIds;

    // Getters and Setters
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}