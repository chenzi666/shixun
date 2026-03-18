package org.example.glw.service;

import org.example.glw.entity.Announcement;
import java.util.List;

public interface AnnouncementService {
    // 获取公告列表
    List<Announcement> getAnnouncements(Integer page, Integer size);
    
    // 根据ID获取公告详情
    Announcement getAnnouncementById(Long announcementId);
    
    // 添加公告
    int addAnnouncement(Announcement announcement);
    
    // 更新公告
    int updateAnnouncement(Announcement announcement);
    
    // 删除公告
    int deleteAnnouncement(Long announcementId);
}