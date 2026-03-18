package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.TravelPlan;
import java.util.List;

@Mapper
public interface TravelPlanMapper {
    // 创建行程
    int insert(TravelPlan travelPlan);
    
    // 更新行程
    int updateById(TravelPlan travelPlan);
    
    // 获取行程详情
    TravelPlan selectById(Long planId);
    
    // 获取行程列表
    List<TravelPlan> selectByUser(@Param("userId") Long userId, 
                                 @Param("offset") int offset, 
                                 @Param("size") int size);
    
    // 统计行程数量
    Long countByUser(@Param("userId") Long userId);
    
    // 删除行程
    int deleteById(@Param("planId") Long planId, @Param("userId") Long userId);
    
    // 获取公开行程列表
    List<TravelPlan> selectPublicPlans(@Param("userId") Long userId,
                                      @Param("offset") int offset,
                                      @Param("size") int size);
    
    // 统计公开行程数量
    Long countPublicPlans(@Param("userId") Long userId);
}