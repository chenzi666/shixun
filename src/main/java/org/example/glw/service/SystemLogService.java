package org.example.glw.service;

import org.example.glw.entity.SystemLog;
import java.time.LocalDateTime;
import java.util.List;

public interface SystemLogService {
    // 添加系统日志
    int addSystemLog(SystemLog systemLog);
    
    // 获取系统日志列表
    List<SystemLog> getSystemLogs(Integer page, Integer size, String username, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    // 统计系统日志数量
    Long countSystemLogs(String username, LocalDateTime startDateTime, LocalDateTime endDateTime);
}