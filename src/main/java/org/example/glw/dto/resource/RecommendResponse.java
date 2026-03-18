package org.example.glw.dto.resource;

import org.example.glw.dto.BannerResponse;

import java.util.List;

/**
 * 首页推荐响应数据
 */
public class RecommendResponse {
    private List<BannerResponse> banners;
    private List<AttractionResponse> popularAttractions;
    private List<RouteResponse> recommendedRoutes;
    private List<HotelResponse> hotels;
    private List<FoodResponse> foods;

    // Getters and Setters
    public List<BannerResponse> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerResponse> banners) {
        this.banners = banners;
    }

    public List<AttractionResponse> getPopularAttractions() {
        return popularAttractions;
    }

    public void setPopularAttractions(List<AttractionResponse> popularAttractions) {
        this.popularAttractions = popularAttractions;
    }

    public List<RouteResponse> getRecommendedRoutes() {
        return recommendedRoutes;
    }

    public void setRecommendedRoutes(List<RouteResponse> recommendedRoutes) {
        this.recommendedRoutes = recommendedRoutes;
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