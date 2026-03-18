package org.example.glw.service.admin.impl;

import org.example.glw.entity.Orders;
import org.example.glw.entity.OrderItem;
import org.example.glw.mapper.admin.AdminOrderMapper;
import org.example.glw.service.admin.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员订单服务实现类
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService {
    
    @Autowired
    private AdminOrderMapper adminOrderMapper;
    
    @Override
    public List<Orders> getManageOrders(Integer page, Integer size, String orderNo, String username, Integer orderStatus, LocalDateTime startDate, LocalDateTime endDate) {
        int offset = (page - 1) * size;
        return adminOrderMapper.selectManageOrders(offset, size, orderNo, username, orderStatus, startDate, endDate);
    }
    
    @Override
    public Long countManageOrders(String orderNo, String username, Integer orderStatus, LocalDateTime startDate, LocalDateTime endDate) {
        return adminOrderMapper.countManageOrders(orderNo, username, orderStatus, startDate, endDate);
    }
    
    @Override
    public Orders getOrderDetailByAdmin(Long orderId) {
        return adminOrderMapper.selectById(orderId);
    }
    
    @Override
    public boolean processRefund(Long orderId, boolean agree, String remark) {
        Orders order = new Orders();
        order.setOrderId(orderId);
        if (agree) {
            // 同意退款
            order.setOrderStatus(6); // 6: 已退款
        } else {
            // 拒绝退款
            order.setOrderStatus(5); // 5: 退款拒绝
        }
        order.setRemark(remark);
        return adminOrderMapper.updateOrder(order) > 0;
    }
    
    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return adminOrderMapper.selectOrderItemsByOrderId(orderId);
    }
    
    @Override
    public Orders getOrderById(Long orderId) {
        return adminOrderMapper.selectById(orderId);
    }
}