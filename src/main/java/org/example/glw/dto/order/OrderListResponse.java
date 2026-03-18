package org.example.glw.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单列表响应数据
 */
public class OrderListResponse {
    private Long orderId;
    private String orderNo;
    private BigDecimal totalAmount;
    private BigDecimal actualAmount;
    private Integer orderStatus;
    private String orderStatusText;
    private LocalDateTime createTime;
    private List<OrderItemResponse> items;

    /**
     * 订单商品项
     */
    public static class OrderItemResponse {
        private Long itemId;
        private Long productId;
        private String productType;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal totalPrice;
        private LocalDate useDate;
        private Integer itemStatus;

        // Getters and Setters
        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public LocalDate getUseDate() {
            return useDate;
        }

        public void setUseDate(LocalDate useDate) {
            this.useDate = useDate;
        }

        public Integer getItemStatus() {
            return itemStatus;
        }

        public void setItemStatus(Integer itemStatus) {
            this.itemStatus = itemStatus;
        }
    }

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

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusText() {
        return orderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        this.orderStatusText = orderStatusText;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}