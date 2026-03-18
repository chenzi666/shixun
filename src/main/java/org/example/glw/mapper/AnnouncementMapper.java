package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Announcement;
import java.util.List;

@Mapper
public interface AnnouncementMapper {
    // 获取公告列表
    List<Announcement> selectAnnouncements(@Param("offset") int offset, @Param("size") int size);
    
    // 根据ID获取公告详情
    Announcement selectById(Long announcementId);
    
    // 添加公告
    int insert(Announcement announcement);
    
    // 更新公告
    int updateById(Announcement announcement);
    
    // 删除公告
    int deleteById(Long announcementId);
}