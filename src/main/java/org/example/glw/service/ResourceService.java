package org.example.glw.service;

import org.example.glw.entity.Attraction;
import org.example.glw.entity.Food;
import org.example.glw.entity.Hotel;
import org.example.glw.entity.TravelRoute;

import java.math.BigDecimal;
import java.util.List;

/**
 * 旅游资源服务接口
 */
public interface ResourceService {
    
    // 景点相关接口
    List<Attraction> getAttractions(Integer page, Integer size, Long categoryId, String keyword, String sort, String order);
    Long countAttractions(Long categoryId, String keyword);
    Attraction getAttractionById(Long attractionId);
    
    // 线路相关接口
    List<TravelRoute> getRoutes(Integer page, Integer size, Long categoryId, Integer days, String keyword, String sort, String order);
    Long countRoutes(Long categoryId, Integer days, String keyword);
    TravelRoute getRouteById(Long routeId);
    
    // 酒店相关接口
    List<Hotel> getHotels(Integer page, Integer size, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String keyword, String sort, String order);
    Long countHotels(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String keyword);
    Hotel getHotelById(Long hotelId);
    
    // 美食相关接口
    List<Food> getFoods(Integer page, Integer size, Long categoryId, String keyword, String sort, String order);
    Long countFoods(Long categoryId, String keyword);
    Food getFoodById(Long foodId);
}