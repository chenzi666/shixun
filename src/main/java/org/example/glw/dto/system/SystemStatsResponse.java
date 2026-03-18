package org.example.glw.dto.system;

import java.math.BigDecimal;
import java.util.List;

/**
 * 系统统计数据响应
 */
public class SystemStatsResponse {
    private Long totalUsers;
    private Long totalOrders;
    private BigDecimal totalSales;
    private Long todayOrders;
    private BigDecimal todaySales;
    private Integer userGrowth;
    private List<Integer> orderTrend;

    // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public Long getTodayOrders() {
        return todayOrders;
    }

    public void setTodayOrders(Long todayOrders) {
        this.todayOrders = todayOrders;
    }

    public BigDecimal getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(BigDecimal todaySales) {
        this.todaySales = todaySales;
    }

    public Integer getUserGrowth() {
        return userGrowth;
    }

    public void setUserGrowth(Integer userGrowth) {
        this.userGrowth = userGrowth;
    }

    public List<Integer> getOrderTrend() {
        return orderTrend;
    }

    public void setOrderTrend(List<Integer> orderTrend) {
        this.orderTrend = orderTrend;
    }
}