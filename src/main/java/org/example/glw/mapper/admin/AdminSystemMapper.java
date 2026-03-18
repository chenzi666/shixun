package org.example.glw.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.SystemLog;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AdminSystemMapper {
    // 获取总用户数
    Long countTotalUsers();
    
    // 获取总订单数
    Long countTotalOrders();
    
    // 获取总销售额
    BigDecimal sumTotalSales();
    
    // 获取今日订单数
    Long countTodayOrders(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // 获取今日销售额
    BigDecimal sumTodaySales(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // 获取指定日期前的用户数
    Long countUsersBeforeDate(LocalDateTime date);
    
    // 获取订单趋势（最近7天）
    List<Integer> getOrderTrend();
    
    // 获取操作日志列表
    List<SystemLog> selectSystemLogs(@Param("offset") int offset, 
                                   @Param("size") int size, 
                                   @Param("username") String username, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    // 统计操作日志数量
    Long countSystemLogs(@Param("username") String username, 
                        @Param("startDate") LocalDateTime startDate, 
                        @Param("endDate") LocalDateTime endDate);
}