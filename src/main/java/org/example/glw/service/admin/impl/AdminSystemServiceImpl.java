package org.example.glw.service.admin.impl;

import org.example.glw.dto.system.SystemStatsResponse;
import org.example.glw.entity.SystemLog;
import org.example.glw.mapper.admin.AdminSystemMapper;
import org.example.glw.service.admin.AdminSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdminSystemServiceImpl implements AdminSystemService {
    
    @Autowired
    private AdminSystemMapper adminSystemMapper;
    
    @Override
    public SystemStatsResponse getSystemStats() {
        SystemStatsResponse response = new SystemStatsResponse();
        
        // 获取总用户数
        response.setTotalUsers(adminSystemMapper.countTotalUsers());
        
        // 获取总订单数
        response.setTotalOrders(adminSystemMapper.countTotalOrders());
        
        // 获取总销售额
        response.setTotalSales(adminSystemMapper.sumTotalSales());
        
        // 获取今日订单数
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(23, 59, 59);
        response.setTodayOrders(adminSystemMapper.countTodayOrders(startOfToday, endOfToday));
        
        // 获取今日销售额
        response.setTodaySales(adminSystemMapper.sumTodaySales(startOfToday, endOfToday));
        
        // 获取用户增长数（最近7天）
        LocalDateTime weekAgo = LocalDate.now().minusDays(7).atStartOfDay();
        Long userCountWeekAgo = adminSystemMapper.countUsersBeforeDate(weekAgo);
        Long currentUserCount = adminSystemMapper.countTotalUsers();
        response.setUserGrowth(Math.toIntExact(currentUserCount - userCountWeekAgo));
        
        // 获取订单趋势（最近7天）
        response.setOrderTrend(adminSystemMapper.getOrderTrend());
        
        return response;
    }
    
    @Override
    public List<SystemLog> getSystemLogs(Integer page, Integer size, String username, String startDate, String endDate) {
        int offset = (page - 1) * size;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        
        try {
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDate.parse(startDate).atStartOfDay();
            }
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDate.parse(endDate).atTime(23, 59, 59);
            }
        } catch (Exception e) {
            // 日期格式错误，使用默认值
        }
        
        return adminSystemMapper.selectSystemLogs(offset, size, username, startDateTime, endDateTime);
    }
    
    @Override
    public Long countSystemLogs(String username, String startDate, String endDate) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        
        try {
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDate.parse(startDate).atStartOfDay();
            }
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDate.parse(endDate).atTime(23, 59, 59);
            }
        } catch (Exception e) {
            // 日期格式错误，使用默认值
        }
        
        return adminSystemMapper.countSystemLogs(username, startDateTime, endDateTime);
    }
}