package org.example.glw.service;

import org.example.glw.dto.order.CreateOrderRequest;
import org.example.glw.entity.OrderItem;
import org.example.glw.entity.Orders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    // 创建订单
    Orders createOrder(Long userId, CreateOrderRequest request);
    
    // 获取用户订单列表
    List<Orders> getUserOrders(Long userId, Integer page, Integer size, Integer status, LocalDateTime startDate, LocalDateTime endDate);
    
    // 统计用户订单数量
    Long countUserOrders(Long userId, Integer status, LocalDateTime startDate, LocalDateTime endDate);
    
    // 获取订单详情
    Orders getOrderDetail(Long userId, Long orderId);
    
    // 获取订单详情（管理员）
    Orders getOrderDetailByAdmin(Long orderId);
    
    // 取消订单
    boolean cancelOrder(Long userId, Long orderId, String reason);
    
    // 支付订单
    boolean payOrder(Long userId, Long orderId, String paymentMethod);
    
    // 查询订单支付状态
    Integer getOrderPaymentStatus(Long userId, Long orderId);
    
    // 申请退款
    String applyRefund(Long userId, Long orderId, BigDecimal refundAmount, String reason, List<Long> itemIds);
    
    // 获取管理订单列表
    List<Orders> getManageOrders(Integer page, Integer size, String orderNo, String username, Integer orderStatus, LocalDateTime startDate, LocalDateTime endDate);
    
    // 统计管理订单数量
    Long countManageOrders(String orderNo, String username, Integer orderStatus, LocalDateTime startDate, LocalDateTime endDate);
    
    // 处理退款申请
    boolean processRefund(Long orderId, boolean agree, String remark);
    
    // 获取订单商品项
    List<OrderItem> getOrderItems(Long orderId);
    
    // 根据ID获取订单
    Orders getOrderById(Long orderId);
}