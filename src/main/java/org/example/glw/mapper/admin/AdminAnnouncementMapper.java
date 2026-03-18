package org.example.glw.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Announcement;
import java.util.List;

@Mapper
public interface AdminAnnouncementMapper {
    // 获取公告列表（管理员）
    List<Announcement> selectAnnouncements(@Param("offset") int offset, @Param("size") int size);
    
    // 添加公告（管理员）
    int insert(Announcement announcement);
    
    // 更新公告（管理员）
    int updateById(Announcement announcement);
    
    // 删除公告（管理员）
    int deleteById(Long announcementId);
}