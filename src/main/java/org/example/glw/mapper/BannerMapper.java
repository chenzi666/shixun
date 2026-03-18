package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.glw.entity.Banner;
import java.util.List;

@Mapper
public interface BannerMapper {
    // 获取Banner列表
    List<Banner> selectBanners();
    
    // 添加Banner
    int insert(Banner banner);
    
    // 更新Banner
    int updateById(Banner banner);
    
    // 删除Banner
    int deleteById(Long bannerId);
    
    // 根据ID获取Banner
    Banner selectById(Long bannerId);
}