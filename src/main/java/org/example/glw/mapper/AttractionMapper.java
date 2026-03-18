package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Attraction;

import java.util.List;

@Mapper
public interface AttractionMapper {
    // 查询景点列表
    List<Attraction> selectAttractions(@Param("offset") int offset, 
                                     @Param("size") int size, 
                                     @Param("categoryId") Long categoryId, 
                                     @Param("keyword") String keyword, 
                                     @Param("sort") String sort, 
                                     @Param("order") String order);
    
    // 统计景点数量
    Long countAttractions(@Param("categoryId") Long categoryId, 
                         @Param("keyword") String keyword);
    
    // 根据ID查询景点详情
    Attraction selectById(Long attractionId);
}