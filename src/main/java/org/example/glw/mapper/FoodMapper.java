package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Food;

import java.util.List;

@Mapper
public interface FoodMapper {
    // 查询美食列表
    List<Food> selectFoods(@Param("offset") int offset, 
                        @Param("size") int size, 
                        @Param("categoryId") Long categoryId, 
                        @Param("keyword") String keyword, 
                        @Param("sort") String sort, 
                        @Param("order") String order);
    
    // 统计美食数量
    Long countFoods(@Param("categoryId") Long categoryId, 
                   @Param("keyword") String keyword);
    
    // 根据ID查询美食详情
    Food selectById(Long foodId);
}