package org.example.glw.service.impl;

import org.example.glw.entity.Banner;
import org.example.glw.mapper.BannerMapper;
import org.example.glw.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {
    
    @Autowired
    private BannerMapper bannerMapper;
    
    @Override
    public List<Banner> getBanners() {
        return bannerMapper.selectBanners();
    }
    
    @Override
    public int addBanner(Banner banner) {
        banner.setCreateTime(LocalDateTime.now());
        banner.setUpdateTime(LocalDateTime.now());
        return bannerMapper.insert(banner);
    }
    
    @Override
    public int updateBanner(Banner banner) {
        banner.setUpdateTime(LocalDateTime.now());
        return bannerMapper.updateById(banner);
    }
    
    @Override
    public int deleteBanner(Long bannerId) {
        return bannerMapper.deleteById(bannerId);
    }
    
    @Override
    public Banner getBannerById(Long bannerId) {
        return bannerMapper.selectById(bannerId);
    }
}