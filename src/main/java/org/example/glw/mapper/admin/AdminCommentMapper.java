package org.example.glw.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.Comment;
import java.util.List;

@Mapper
public interface AdminCommentMapper {
    // 获取管理评价列表
    List<Comment> selectManageComments(@Param("offset") int offset, 
                                      @Param("size") int size, 
                                      @Param("productType") String productType, 
                                      @Param("status") Integer status);
    
    // 统计管理评价数量
    Long countManageComments(@Param("productType") String productType, 
                            @Param("status") Integer status);
    
    // 更新评价
    int updateById(Comment comment);
    
    // 根据ID获取评价
    Comment selectById(Long commentId);
}