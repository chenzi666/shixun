package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.SystemLog;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SystemLogMapper {
    // 插入系统日志
    int insert(SystemLog systemLog);
    
    // 获取系统日志列表
    List<SystemLog> selectSystemLogs(@Param("offset") int offset, 
                                    @Param("size") int size,
                                    @Param("username") String username,
                                    @Param("startDateTime") LocalDateTime startDateTime,
                                    @Param("endDateTime") LocalDateTime endDateTime);
    
    // 统计系统日志数量
    Long countSystemLogs(@Param("username") String username,
                        @Param("startDateTime") LocalDateTime startDateTime,
                        @Param("endDateTime") LocalDateTime endDateTime);
}