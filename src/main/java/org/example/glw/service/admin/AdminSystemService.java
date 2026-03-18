package org.example.glw.service.admin;

import org.example.glw.dto.system.SystemStatsResponse;
import org.example.glw.entity.SystemLog;
import java.util.List;

public interface AdminSystemService {
    // 获取系统统计数据
    SystemStatsResponse getSystemStats();
    
    // 获取操作日志列表
    List<SystemLog> getSystemLogs(Integer page, Integer size, String username, String startDate, String endDate);
    
    // 统计操作日志数量
    Long countSystemLogs(String username, String startDate, String endDate);
}