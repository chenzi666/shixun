package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Favorite;
import java.util.List;

@Mapper
public interface FavoriteMapper {
    // 添加收藏
    int insert(Favorite favorite);
    
    // 取消收藏
    int deleteById(@Param("favoriteId") Long favoriteId, @Param("userId") Long userId);
    
    // 获取收藏列表
    List<Favorite> selectByUser(@Param("userId") Long userId, 
                               @Param("offset") int offset, 
                               @Param("size") int size,
                               @Param("type") String type);
    
    // 统计收藏数量
    Long countByUser(@Param("userId") Long userId, @Param("type") String type);
    
    // 检查是否已收藏
    Favorite selectByUserAndProduct(@Param("userId") Long userId, 
                                   @Param("productId") Long productId, 
                                   @Param("productType") String productType);
}