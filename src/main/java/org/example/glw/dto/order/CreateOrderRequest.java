package org.example.glw.dto.order;

import java.time.LocalDate;
import java.util.List;

/**
 * 创建订单请求参数
 */
public class CreateOrderRequest {
    private List<OrderItemRequest> items;
    private String contactName;
    private String contactPhone;
    private String remark;

    /**
     * 订单商品项
     */
    public static class OrderItemRequest {
        private Long productId;
        private String productType;
        private String productName;
        private java.math.BigDecimal price;
        private Integer quantity;
        private LocalDate useDate;

        // Getters and Setters
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

        public java.math.BigDecimal getPrice() {
            return price;
        }

        public void setPrice(java.math.BigDecimal price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public LocalDate getUseDate() {
            return useDate;
        }

        public void setUseDate(LocalDate useDate) {
            this.useDate = useDate;
        }
    }

    // Getters and Setters
    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}