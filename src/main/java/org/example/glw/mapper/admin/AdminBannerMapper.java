package org.example.glw.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.example.glw.entity.Banner;
import java.util.List;

@Mapper
public interface AdminBannerMapper {
    // 获取Banner列表（管理员）
    List<Banner> selectBanners();
    
    // 添加Banner（管理员）
    int insert(Banner banner);
    
    // 更新Banner（管理员）
    int updateById(Banner banner);
    
    // 删除Banner（管理员）
    int deleteById(Long bannerId);
}