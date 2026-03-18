package org.example.glw.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderItem {
    private Long itemId;
    private Long orderId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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