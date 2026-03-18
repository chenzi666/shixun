package org.example.glw.service.admin;

import org.example.glw.entity.Announcement;
import org.example.glw.entity.Banner;
import java.util.List;

public interface AdminContentService {
    // 获取公告列表（管理员）
    List<Announcement> getAnnouncements(Integer page, Integer size);
    
    // 添加公告（管理员）
    int addAnnouncement(Announcement announcement);
    
    // 更新公告（管理员）
    int updateAnnouncement(Announcement announcement);
    
    // 删除公告（管理员）
    int deleteAnnouncement(Long announcementId);
    
    // 获取Banner列表（管理员）
    List<Banner> getBanners();
    
    // 添加Banner（管理员）
    int addBanner(Banner banner);
    
    // 更新Banner（管理员）
    int updateBanner(Banner banner);
    
    // 删除Banner（管理员）
    int deleteBanner(Long bannerId);
}