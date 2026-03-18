package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Comment;
import java.util.List;

@Mapper
public interface CommentMapper {
    // 添加评价
    int insert(Comment comment);
    
    // 获取产品评价列表
    List<Comment> selectByProduct(@Param("productType") String productType, 
                                 @Param("productId") Long productId, 
                                 @Param("offset") int offset, 
                                 @Param("size") int size, 
                                 @Param("score") Integer score);
    
    // 统计产品评价数量
    Long countByProduct(@Param("productType") String productType, 
                       @Param("productId") Long productId, 
                       @Param("score") Integer score);
    
    // 获取用户评价列表
    List<Comment> selectByUser(@Param("userId") Long userId, 
                              @Param("offset") int offset, 
                              @Param("size") int size);
    
    // 统计用户评价数量
    Long countByUser(@Param("userId") Long userId);
    
    // 删除评价
    int deleteById(@Param("commentId") Long commentId, @Param("userId") Long userId);
    
    // 根据ID获取评价
    Comment selectById(Long commentId);
}