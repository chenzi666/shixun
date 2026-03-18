package org.example.glw.service.admin;

import org.example.glw.entity.Orders;
import org.example.glw.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员订单服务接口
 */
public interface AdminOrderService {
    
    // 获取管理订单列表
    List<Orders> getManageOrders(Integer page, Integer size, String orderNo, String username, Integer orderStatus, LocalDateTime startDate, LocalDateTime endDate);
    
    // 统计管理订单数量
    Long countManageOrders(String orderNo, String username, Integer orderStatus, LocalDateTime startDate, LocalDateTime endDate);
    
    // 获取订单详情（管理员）
    Orders getOrderDetailByAdmin(Long orderId);
    
    // 处理退款申请
    boolean processRefund(Long orderId, boolean agree, String remark);
    
    // 获取订单商品项
    List<OrderItem> getOrderItems(Long orderId);
    
    // 根据ID获取订单
    Orders getOrderById(Long orderId);
}