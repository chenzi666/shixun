package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Hotel;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface HotelMapper {
    // 查询酒店列表
    List<Hotel> selectHotels(@Param("offset") int offset, 
                          @Param("size") int size, 
                          @Param("categoryId") Long categoryId, 
                          @Param("minPrice") BigDecimal minPrice, 
                          @Param("maxPrice") BigDecimal maxPrice, 
                          @Param("keyword") String keyword, 
                          @Param("sort") String sort, 
                          @Param("order") String order);
    
    // 统计酒店数量
    Long countHotels(@Param("categoryId") Long categoryId, 
                     @Param("minPrice") BigDecimal minPrice, 
                     @Param("maxPrice") BigDecimal maxPrice, 
                     @Param("keyword") String keyword);
    
    // 根据ID查询酒店详情
    Hotel selectById(Long hotelId);
}