package org.example.glw.service.impl;

import org.example.glw.entity.Favorite;
import org.example.glw.mapper.FavoriteMapper;
import org.example.glw.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Override
    public int addFavorite(Favorite favorite) {
        favorite.setCreateTime(LocalDateTime.now());
        return favoriteMapper.insert(favorite);
    }
    
    @Override
    public boolean deleteFavorite(Long userId, Long favoriteId) {
        return favoriteMapper.deleteById(favoriteId, userId) > 0;
    }
    
    @Override
    public List<Favorite> getFavorites(Long userId, Integer page, Integer size, String type) {
        int offset = (page - 1) * size;
        return favoriteMapper.selectByUser(userId, offset, size, type);
    }
    
    @Override
    public Long countFavorites(Long userId, String type) {
        return favoriteMapper.countByUser(userId, type);
    }
    
    @Override
    public Favorite getFavoriteByUserAndProduct(Long userId, Long productId, String productType) {
        return favoriteMapper.selectByUserAndProduct(userId, productId, productType);
    }
}