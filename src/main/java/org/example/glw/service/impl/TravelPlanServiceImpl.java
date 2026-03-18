package org.example.glw.service.impl;

import org.example.glw.entity.TravelPlan;
import org.example.glw.mapper.TravelPlanMapper;
import org.example.glw.service.TravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TravelPlanServiceImpl implements TravelPlanService {
    
    @Autowired
    private TravelPlanMapper travelPlanMapper;
    
    @Override
    public int createTravelPlan(TravelPlan travelPlan) {
        travelPlan.setCreateTime(LocalDateTime.now());
        travelPlan.setUpdateTime(LocalDateTime.now());
        return travelPlanMapper.insert(travelPlan);
    }
    
    @Override
    public int updateTravelPlan(TravelPlan travelPlan) {
        travelPlan.setUpdateTime(LocalDateTime.now());
        return travelPlanMapper.updateById(travelPlan);
    }
    
    @Override
    public TravelPlan getTravelPlanById(Long planId) {
        return travelPlanMapper.selectById(planId);
    }
    
    @Override
    public List<TravelPlan> getTravelPlans(Long userId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        return travelPlanMapper.selectByUser(userId, offset, size);
    }
    
    @Override
    public Long countTravelPlans(Long userId) {
        return travelPlanMapper.countByUser(userId);
    }
    
    @Override
    public boolean deleteTravelPlan(Long userId, Long planId) {
        return travelPlanMapper.deleteById(planId, userId) > 0;
    }
    
    @Override
    public List<TravelPlan> getPublicTravelPlans(Long userId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        return travelPlanMapper.selectPublicPlans(userId, offset, size);
    }
    
    @Override
    public Long countPublicTravelPlans(Long userId) {
        return travelPlanMapper.countPublicPlans(userId);
    }
}