package org.example.glw.service.impl;

import org.example.glw.entity.SystemLog;
import org.example.glw.mapper.SystemLogMapper;
import org.example.glw.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemLogServiceImpl implements SystemLogService {
    
    @Autowired
    private SystemLogMapper systemLogMapper;
    
    @Override
    public int addSystemLog(SystemLog systemLog) {
        if (systemLog.getCreateTime() == null) {
            systemLog.setCreateTime(LocalDateTime.now());
        }
        return systemLogMapper.insert(systemLog);
    }
    
    @Override
    public List<SystemLog> getSystemLogs(Integer page, Integer size, String username, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int offset = (page - 1) * size;
        return systemLogMapper.selectSystemLogs(offset, size, username, startDateTime, endDateTime);
    }
    
    @Override
    public Long countSystemLogs(String username, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return systemLogMapper.countSystemLogs(username, startDateTime, endDateTime);
    }
}