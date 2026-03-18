package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Orders;
import org.example.glw.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    // 创建订单
    int insertOrder(Orders order);
    
    // 创建订单商品项
    int insertOrderItem(OrderItem orderItem);
    
    // 获取用户订单列表
    List<Orders> selectUserOrders(@Param("userId") Long userId, 
                                 @Param("offset") int offset, 
                                 @Param("size") int size, 
                                 @Param("status") Integer status, 
                                 @Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);
    
    // 统计用户订单数量
    Long countUserOrders(@Param("userId") Long userId, 
                        @Param("status") Integer status, 
                        @Param("startDate") LocalDateTime startDate, 
                        @Param("endDate") LocalDateTime endDate);
    
    // 获取订单详情
    Orders selectById(Long orderId);
    
    // 更新订单
    int updateOrder(Orders order);
    
    // 获取订单商品项列表
    List<OrderItem> selectOrderItemsByOrderId(Long orderId);
    
    // 获取管理订单列表
    List<Orders> selectManageOrders(@Param("offset") int offset, 
                                   @Param("size") int size, 
                                   @Param("orderNo") String orderNo, 
                                   @Param("username") String username, 
                                   @Param("orderStatus") Integer orderStatus, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    // 统计管理订单数量
    Long countManageOrders(@Param("orderNo") String orderNo, 
                          @Param("username") String username, 
                          @Param("orderStatus") Integer orderStatus, 
                          @Param("startDate") LocalDateTime startDate, 
                          @Param("endDate") LocalDateTime endDate);
}