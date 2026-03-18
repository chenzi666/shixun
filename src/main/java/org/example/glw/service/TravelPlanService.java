package org.example.glw.service;

import org.example.glw.entity.TravelPlan;
import java.util.List;

public interface TravelPlanService {
    // 创建行程
    int createTravelPlan(TravelPlan travelPlan);
    
    // 更新行程
    int updateTravelPlan(TravelPlan travelPlan);
    
    // 获取行程详情
    TravelPlan getTravelPlanById(Long planId);
    
    // 获取行程列表
    List<TravelPlan> getTravelPlans(Long userId, Integer page, Integer size);
    
    // 统计行程数量
    Long countTravelPlans(Long userId);
    
    // 删除行程
    boolean deleteTravelPlan(Long userId, Long planId);
    
    // 获取公开行程列表
    List<TravelPlan> getPublicTravelPlans(Long userId, Integer page, Integer size);
    
    // 统计公开行程数量
    Long countPublicTravelPlans(Long userId);
}