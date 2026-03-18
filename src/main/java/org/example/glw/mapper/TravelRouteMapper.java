package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.TravelRoute;

import java.util.List;

@Mapper
public interface TravelRouteMapper {
    // 查询线路列表
    List<TravelRoute> selectRoutes(@Param("offset") int offset, 
                                 @Param("size") int size, 
                                 @Param("categoryId") Long categoryId, 
                                 @Param("days") Integer days, 
                                 @Param("keyword") String keyword, 
                                 @Param("sort") String sort, 
                                 @Param("order") String order);
    
    // 统计线路数量
    Long countRoutes(@Param("categoryId") Long categoryId, 
                     @Param("days") Integer days, 
                     @Param("keyword") String keyword);
    
    // 根据ID查询线路详情
    TravelRoute selectById(Long routeId);
}