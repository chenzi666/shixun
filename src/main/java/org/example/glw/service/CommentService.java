package org.example.glw.service;

import org.example.glw.entity.Comment;
import java.util.List;

public interface CommentService {
    // 添加评价
    int addComment(Comment comment);
    
    // 获取产品评价列表
    List<Comment> getProductComments(String productType, Long productId, Integer page, Integer size, Integer score);
    
    // 统计产品评价数量
    Long countProductComments(String productType, Long productId, Integer score);
    
    // 获取用户评价列表
    List<Comment> getUserComments(Long userId, Integer page, Integer size);
    
    // 统计用户评价数量
    Long countUserComments(Long userId);
    
    // 删除评价
    boolean deleteComment(Long userId, Long commentId);
    
    // 根据ID获取评价
    Comment getCommentById(Long commentId);
}