package org.example.glw.service;

import org.example.glw.entity.Favorite;
import java.util.List;

public interface FavoriteService {
    // 添加收藏
    int addFavorite(Favorite favorite);
    
    // 取消收藏
    boolean deleteFavorite(Long userId, Long favoriteId);
    
    // 获取收藏列表
    List<Favorite> getFavorites(Long userId, Integer page, Integer size, String type);
    
    // 统计收藏数量
    Long countFavorites(Long userId, String type);
    
    // 检查是否已收藏
    Favorite getFavoriteByUserAndProduct(Long userId, Long productId, String productType);
}