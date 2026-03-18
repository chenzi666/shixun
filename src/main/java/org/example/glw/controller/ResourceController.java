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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 旅游资源控制器
 */
@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 获取首页推荐
     */
    @GetMapping("/recommend")
    public ApiResponse<RecommendResponse> getRecommend() {
        // TODO: 实现获取首页推荐逻辑
        RecommendResponse recommendResponse = new RecommendResponse();
        
        // 示例数据
        recommendResponse.setBanners(new ArrayList<>());
        recommendResponse.setPopularAttractions(new ArrayList<>());
        recommendResponse.setRecommendedRoutes(new ArrayList<>());
        recommendResponse.setHotels(new ArrayList<>());
        recommendResponse.setFoods(new ArrayList<>());
        
        return ApiResponse.success("获取成功", recommendResponse);
    }

    /**
     * 获取景点列表
     */
    @GetMapping("/attractions")
    public ApiResponse<PageResponse<AttractionResponse>> getAttractions(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order) {
        
        // 查询景点列表
        List<Attraction> attractions = resourceService.getAttractions(page, size, categoryId, keyword, sort, order);
        Long total = resourceService.countAttractions(categoryId, keyword);
        
        // 转换为响应数据
        List<AttractionResponse> attractionResponses = attractions.stream().map(attraction -> {
            AttractionResponse response = new AttractionResponse();
            response.setAttractionId(attraction.getAttractionId());
            response.setName(attraction.getName());
            response.setDescription(attraction.getDescription());
            response.setAddress(attraction.getAddress());
            response.setPhone(attraction.getPhone());
            response.setOpeningHours(attraction.getOpeningHours());
            response.setTicketPrice(attraction.getTicketPrice());
            // 简化处理图片列表，实际应解析JSON
            response.setImages(List.of());
            response.setVideoUrl(attraction.getVideoUrl());
            response.setLongitude(attraction.getLongitude());
            response.setLatitude(attraction.getLatitude());
            response.setScore(attraction.getScore());
            response.setViewCount(attraction.getViewCount());
            return response;
        }).collect(Collectors.toList());
        
        PageResponse<AttractionResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(attractionResponses);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 获取景点详情
     */
    @GetMapping("/attractions/{attractionId}")
    public ApiResponse<AttractionDetailResponse> getAttractionDetail(@PathVariable Long attractionId) {
        // 查询景点详情
        Attraction attraction = resourceService.getAttractionById(attractionId);
        if (attraction == null) {
            return ApiResponse.fail(404, "景点不存在");
        }
        
        // 转换为响应数据
        AttractionDetailResponse detailResponse = new AttractionDetailResponse();
        detailResponse.setAttractionId(attraction.getAttractionId());
        detailResponse.setName(attraction.getName());
        detailResponse.setDescription(attraction.getDescription());
        detailResponse.setAddress(attraction.getAddress());
        detailResponse.setPhone(attraction.getPhone());
        detailResponse.setOpeningHours(attraction.getOpeningHours());
        detailResponse.setTicketPrice(attraction.getTicketPrice());
        // 简化处理图片列表，实际应解析JSON
        detailResponse.setImages(List.of());
        detailResponse.setVideoUrl(attraction.getVideoUrl());
        detailResponse.setLongitude(attraction.getLongitude());
        detailResponse.setLatitude(attraction.getLatitude());
        detailResponse.setScore(attraction.getScore());
        detailResponse.setViewCount(attraction.getViewCount());
        detailResponse.setIsFavorite(false); // 简化处理，实际应根据用户是否收藏判断
        detailResponse.setComments(new ArrayList<>());
        
        return ApiResponse.success("获取成功", detailResponse);
    }

    /**
     * 获取线路列表
     */
    @GetMapping("/routes")
    public ApiResponse<PageResponse<RouteResponse>> getRoutes(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order) {
        
        // 查询线路列表
        List<TravelRoute> routes = resourceService.getRoutes(page, size, categoryId, days, keyword, sort, order);
        Long total = resourceService.countRoutes(categoryId, days, keyword);
        
        // 转换为响应数据
        List<RouteResponse> routeResponses = routes.stream().map(route -> {
            RouteResponse response = new RouteResponse();
            response.setRouteId(route.getRouteId());
            response.setName(route.getName());
            response.setDescription(route.getDescription());
            response.setDays(route.getDays());
            response.setPrice(route.getPrice());
            response.setDeparturePlace(route.getDeparturePlace());
            response.setDestination(route.getDestination());
            // 简化处理图片列表，实际应解析JSON
            response.setImages(List.of());
            response.setScore(route.getScore());
            response.setViewCount(route.getViewCount());
            return response;
        }).collect(Collectors.toList());
        
        PageResponse<RouteResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(routeResponses);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 获取线路详情
     */
    @GetMapping("/routes/{routeId}")
    public ApiResponse<RouteDetailResponse> getRouteDetail(@PathVariable Long routeId) {
        // 查询线路详情
        TravelRoute route = resourceService.getRouteById(routeId);
        if (route == null) {
            return ApiResponse.fail(404, "线路不存在");
        }
        
        // 转换为响应数据
        RouteDetailResponse detailResponse = new RouteDetailResponse();
        detailResponse.setRouteId(route.getRouteId());
        detailResponse.setName(route.getName());
        detailResponse.setDescription(route.getDescription());
        detailResponse.setDays(route.getDays());
        detailResponse.setPrice(route.getPrice());
        detailResponse.setDeparturePlace(route.getDeparturePlace());
        detailResponse.setDestination(route.getDestination());
        detailResponse.setIncludes(route.getIncludes());
        detailResponse.setExcludes(route.getExcludes());
        detailResponse.setNotice(route.getNotice());
        // 简化处理图片列表，实际应解析JSON
        detailResponse.setImages(List.of());
        detailResponse.setVideoUrl(route.getVideoUrl());
        detailResponse.setScore(route.getScore());
        detailResponse.setViewCount(route.getViewCount());
        detailResponse.setIsFavorite(false); // 简化处理，实际应根据用户是否收藏判断
        detailResponse.setComments(new ArrayList<>());
        
        return ApiResponse.success("获取成功", detailResponse);
    }

    /**
     * 获取酒店列表
     */
    @GetMapping("/hotels")
    public ApiResponse<PageResponse<HotelResponse>> getHotels(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order) {
        
        // 查询酒店列表
        List<Hotel> hotels = resourceService.getHotels(page, size, categoryId, minPrice, maxPrice, keyword, sort, order);
        Long total = resourceService.countHotels(categoryId, minPrice, maxPrice, keyword);
        
        // 转换为响应数据
        List<HotelResponse> hotelResponses = hotels.stream().map(hotel -> {
            HotelResponse response = new HotelResponse();
            response.setHotelId(hotel.getHotelId());
            response.setName(hotel.getName());
            response.setDescription(hotel.getDescription());
            response.setAddress(hotel.getAddress());
            response.setStarLevel(hotel.getStarLevel());
            response.setMinPrice(hotel.getMinPrice());
            response.setMaxPrice(hotel.getMaxPrice());
            // 简化处理图片列表，实际应解析JSON
            response.setImages(List.of());
            response.setScore(hotel.getScore());
            response.setViewCount(hotel.getViewCount());
            return response;
        }).collect(Collectors.toList());
        
        PageResponse<HotelResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(hotelResponses);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 获取酒店详情
     */
    @GetMapping("/hotels/{hotelId}")
    public ApiResponse<HotelDetailResponse> getHotelDetail(@PathVariable Long hotelId) {
        // 查询酒店详情
        Hotel hotel = resourceService.getHotelById(hotelId);
        if (hotel == null) {
            return ApiResponse.fail(404, "酒店不存在");
        }
        
        // 转换为响应数据
        HotelDetailResponse detailResponse = new HotelDetailResponse();
        detailResponse.setHotelId(hotel.getHotelId());
        detailResponse.setName(hotel.getName());
        detailResponse.setDescription(hotel.getDescription());
        detailResponse.setAddress(hotel.getAddress());
        detailResponse.setPhone(hotel.getPhone());
        detailResponse.setStarLevel(hotel.getStarLevel());
        detailResponse.setMinPrice(hotel.getMinPrice());
        detailResponse.setMaxPrice(hotel.getMaxPrice());
        // 简化处理设施列表，实际应解析JSON
        detailResponse.setFacilities(List.of());
        // 简化处理图片列表，实际应解析JSON
        detailResponse.setImages(List.of());
        detailResponse.setVideoUrl(hotel.getVideoUrl());
        detailResponse.setLongitude(hotel.getLongitude());
        detailResponse.setLatitude(hotel.getLatitude());
        detailResponse.setScore(hotel.getScore());
        detailResponse.setViewCount(hotel.getViewCount());
        detailResponse.setIsFavorite(false); // 简化处理，实际应根据用户是否收藏判断
        detailResponse.setComments(new ArrayList<>());
        detailResponse.setRooms(new ArrayList<>());
        
        return ApiResponse.success("获取成功", detailResponse);
    }

    /**
     * 获取房型详情
     */
    @GetMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ApiResponse<HotelRoomResponse> getRoomDetail(@PathVariable Long hotelId, @PathVariable Long roomId) {
        // TODO: 实现获取房型详情逻辑
        HotelRoomResponse roomResponse = new HotelRoomResponse();
        roomResponse.setRoomId(roomId);
        
        return ApiResponse.success("获取成功", roomResponse);
    }

    /**
     * 获取美食列表
     */
    @GetMapping("/foods")
    public ApiResponse<PageResponse<FoodResponse>> getFoods(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order) {
        
        // 查询美食列表
        List<Food> foods = resourceService.getFoods(page, size, categoryId, keyword, sort, order);
        Long total = resourceService.countFoods(categoryId, keyword);
        
        // 转换为响应数据
        List<FoodResponse> foodResponses = foods.stream().map(food -> {
            FoodResponse response = new FoodResponse();
            response.setFoodId(food.getFoodId());
            response.setName(food.getName());
            response.setDescription(food.getDescription());
            response.setRestaurantName(food.getRestaurantName());
            response.setPrice(food.getPrice());
            // 简化处理图片列表，实际应解析JSON
            response.setImages(List.of());
            response.setScore(food.getScore());
            response.setViewCount(food.getViewCount());
            return response;
        }).collect(Collectors.toList());
        
        PageResponse<FoodResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(foodResponses);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 获取美食详情
     */
    @GetMapping("/foods/{foodId}")
    public ApiResponse<FoodDetailResponse> getFoodDetail(@PathVariable Long foodId) {
        // 查询美食详情
        Food food = resourceService.getFoodById(foodId);
        if (food == null) {
            return ApiResponse.fail(404, "美食不存在");
        }
        
        // 转换为响应数据
        FoodDetailResponse detailResponse = new FoodDetailResponse();
        detailResponse.setFoodId(food.getFoodId());
        detailResponse.setName(food.getName());
        detailResponse.setDescription(food.getDescription());
        detailResponse.setRestaurantName(food.getRestaurantName());
        detailResponse.setPrice(food.getPrice());
        // 简化处理图片列表，实际应解析JSON
        detailResponse.setImages(List.of());
        detailResponse.setScore(food.getScore());
        detailResponse.setViewCount(food.getViewCount());
        detailResponse.setIsFavorite(false); // 简化处理，实际应根据用户是否收藏判断
        detailResponse.setComments(new ArrayList<>());
        
        return ApiResponse.success("获取成功", detailResponse);
    }

    /**
     * 搜索资源
     */
    @GetMapping("/search")
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
        
        // TODO: 实际项目中应该实现具体的搜索逻辑
        // 这里简化处理，构造示例数据
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