package org.example.glw.service.admin.impl;

import org.example.glw.entity.Announcement;
import org.example.glw.entity.Banner;
import org.example.glw.mapper.admin.AdminAnnouncementMapper;
import org.example.glw.mapper.admin.AdminBannerMapper;
import org.example.glw.service.admin.AdminContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminContentServiceImpl implements AdminContentService {
    
    @Autowired
    private AdminAnnouncementMapper adminAnnouncementMapper;
    
    @Autowired
    private AdminBannerMapper adminBannerMapper;
    
    @Override
    public List<Announcement> getAnnouncements(Integer page, Integer size) {
        int offset = (page - 1) * size;
        return adminAnnouncementMapper.selectAnnouncements(offset, size);
    }
    
    @Override
    public int addAnnouncement(Announcement announcement) {
        announcement.setPublishTime(LocalDateTime.now());
        announcement.setCreateTime(LocalDateTime.now());
        return adminAnnouncementMapper.insert(announcement);
    }
    
    @Override
    public int updateAnnouncement(Announcement announcement) {
        announcement.setUpdateTime(LocalDateTime.now());
        return adminAnnouncementMapper.updateById(announcement);
    }
    
    @Override
    public int deleteAnnouncement(Long announcementId) {
        return adminAnnouncementMapper.deleteById(announcementId);
    }
    
    @Override
    public List<Banner> getBanners() {
        return adminBannerMapper.selectBanners();
    }
    
    @Override
    public int addBanner(Banner banner) {
        banner.setCreateTime(LocalDateTime.now());
        banner.setUpdateTime(LocalDateTime.now());
        return adminBannerMapper.insert(banner);
    }
    
    @Override
    public int updateBanner(Banner banner) {
        banner.setUpdateTime(LocalDateTime.now());
        return adminBannerMapper.updateById(banner);
    }
    
    @Override
    public int deleteBanner(Long bannerId) {
        return adminBannerMapper.deleteById(bannerId);
    }
}