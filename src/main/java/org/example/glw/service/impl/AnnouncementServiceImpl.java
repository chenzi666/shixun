package org.example.glw.service.impl;

import org.example.glw.entity.Announcement;
import org.example.glw.mapper.AnnouncementMapper;
import org.example.glw.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    
    @Autowired
    private AnnouncementMapper announcementMapper;
    
    @Override
    public List<Announcement> getAnnouncements(Integer page, Integer size) {
        int offset = (page - 1) * size;
        return announcementMapper.selectAnnouncements(offset, size);
    }
    
    @Override
    public Announcement getAnnouncementById(Long announcementId) {
        return announcementMapper.selectById(announcementId);
    }
    
    @Override
    public int addAnnouncement(Announcement announcement) {
        announcement.setPublishTime(LocalDateTime.now());
        announcement.setCreateTime(LocalDateTime.now());
        return announcementMapper.insert(announcement);
    }
    
    @Override
    public int updateAnnouncement(Announcement announcement) {
        announcement.setUpdateTime(LocalDateTime.now());
        return announcementMapper.updateById(announcement);
    }
    
    @Override
    public int deleteAnnouncement(Long announcementId) {
        return announcementMapper.deleteById(announcementId);
    }
}