package org.example.glw.service;

import org.example.glw.entity.Banner;
import java.util.List;

public interface BannerService {
    // 获取Banner列表
    List<Banner> getBanners();
    
    // 添加Banner
    int addBanner(Banner banner);
    
    // 更新Banner
    int updateBanner(Banner banner);
    
    // 删除Banner
    int deleteBanner(Long bannerId);
    
    // 根据ID获取Banner
    Banner getBannerById(Long bannerId);
}