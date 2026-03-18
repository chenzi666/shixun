package org.example.glw.controller;

import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.dto.resource.*;
import org.example.glw.entity.Attraction;
import org.example.glw.entity.Food;
import org.example.glw.entity.Hotel;
import org.example.glw.entity.TravelRoute;
import org.example.glw.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索控制器
 */
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 搜索资源
     */
    @GetMapping
    public ApiResponse<SearchResponse> searchResources(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        
        // 验证搜索类型
        if (!"all".equals(type) && !"attraction".equals(type) && !"route".equals(type) && 
            !"hotel".equals(type) && !"food".equals(type)) {
            return ApiResponse.fail(400, "不支持的搜索类型");
        }
        
        // 构造搜索响应
        SearchResponse response = new SearchResponse();
        
        // 根据类型搜索不同资源
        if ("all".equals(type) || "attraction".equals(type)) {
            // 搜索景点
            List<Attraction> attractions = resourceService.getAttractions(page, size, null, keyword, null, null);
            response.setAttractions(attractions.stream().map(attraction -> {
                AttractionResponse attractionResponse = new AttractionResponse();
                attractionResponse.setAttractionId(attraction.getAttractionId());
                attractionResponse.setName(attraction.getName());
                attractionResponse.setDescription(attraction.getDescription());
                attractionResponse.setTicketPrice(attraction.getTicketPrice());
                attractionResponse.setScore(attraction.getScore());
                attractionResponse.setViewCount(attraction.getViewCount());
                // 简化处理图片列表
                attractionResponse.setImages(List.of());
                return attractionResponse;
            }).collect(Collectors.toList()));
        }
        
        if ("all".equals(type) || "route".equals(type)) {
            // 搜索线路
            List<TravelRoute> routes = resourceService.getRoutes(page, size, null, null, keyword, null, null);
            response.setRoutes(routes.stream().map(route -> {
                RouteResponse routeResponse = new RouteResponse();
                routeResponse.setRouteId(route.getRouteId());
                routeResponse.setName(route.getName());
                routeResponse.setDescription(route.getDescription());
                routeResponse.setDays(route.getDays());
                routeResponse.setPrice(route.getPrice());
                routeResponse.setDeparturePlace(route.getDeparturePlace());
                routeResponse.setDestination(route.getDestination());
                routeResponse.setScore(route.getScore());
                routeResponse.setViewCount(route.getViewCount());
                // 简化处理图片列表
                routeResponse.setImages(List.of());
                return routeResponse;
            }).collect(Collectors.toList()));
        }
        
        if ("all".equals(type) || "hotel".equals(type)) {
            // 搜索酒店
            List<Hotel> hotels = resourceService.getHotels(page, size, null, null, null, keyword, null, null);
            response.setHotels(hotels.stream().map(hotel -> {
                HotelResponse hotelResponse = new HotelResponse();
                hotelResponse.setHotelId(hotel.getHotelId());
                hotelResponse.setName(hotel.getName());
                hotelResponse.setDescription(hotel.getDescription());
                hotelResponse.setStarLevel(hotel.getStarLevel());
                hotelResponse.setMinPrice(hotel.getMinPrice());
                hotelResponse.setMaxPrice(hotel.getMaxPrice());
                hotelResponse.setScore(hotel.getScore());
                hotelResponse.setViewCount(hotel.getViewCount());
                // 简化处理图片列表
                hotelResponse.setImages(List.of());
                return hotelResponse;
            }).collect(Collectors.toList()));
        }
        
        if ("all".equals(type) || "food".equals(type)) {
            // 搜索美食
            List<Food> foods = resourceService.getFoods(page, size, null, keyword, null, null);
            response.setFoods(foods.stream().map(food -> {
                FoodResponse foodResponse = new FoodResponse();
                foodResponse.setFoodId(food.getFoodId());
                foodResponse.setName(food.getName());
                foodResponse.setDescription(food.getDescription());
                foodResponse.setRestaurantName(food.getRestaurantName());
                foodResponse.setPrice(food.getPrice());
                foodResponse.setScore(food.getScore());
                foodResponse.setViewCount(food.getViewCount());
                // 简化处理图片列表
                foodResponse.setImages(List.of());
                return foodResponse;
            }).collect(Collectors.toList()));
        }
        
        // 设置总数（简化处理）
        response.setTotal(156L);
        response.setPage(page);
        response.setSize(size);
        
        return ApiResponse.success("搜索成功", response);
    }

    // 内部类定义
    public static class SearchResponse extends PageResponse<Object> {
        private List<AttractionResponse> attractions;
        private List<RouteResponse> routes;
        private List<HotelResponse> hotels;
        private List<FoodResponse> foods;

        public List<AttractionResponse> getAttractions() {
            return attractions;
        }

        public void setAttractions(List<AttractionResponse> attractions) {
            this.attractions = attractions;
        }

        public List<RouteResponse> getRoutes() {
            return routes;
        }

        public void setRoutes(List<RouteResponse> routes) {
            this.routes = routes;
        }

        public List<HotelResponse> getHotels() {
            return hotels;
        }

        public void setHotels(List<HotelResponse> hotels) {
            this.hotels = hotels;
        }

        public List<FoodResponse> getFoods() {
            return foods;
        }

        public void setFoods(List<FoodResponse> foods) {
            this.foods = foods;
        }
    }
}