package org.example.glw.service.impl;

import org.example.glw.entity.Comment;
import org.example.glw.mapper.CommentMapper;
import org.example.glw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Override
    public int addComment(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setStatus(1); // 默认状态为已审核
        return commentMapper.insert(comment);
    }
    
    @Override
    public List<Comment> getProductComments(String productType, Long productId, Integer page, Integer size, Integer score) {
        int offset = (page - 1) * size;
        return commentMapper.selectByProduct(productType, productId, offset, size, score);
    }
    
    @Override
    public Long countProductComments(String productType, Long productId, Integer score) {
        return commentMapper.countByProduct(productType, productId, score);
    }
    
    @Override
    public List<Comment> getUserComments(Long userId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        return commentMapper.selectByUser(userId, offset, size);
    }
    
    @Override
    public Long countUserComments(Long userId) {
        return commentMapper.countByUser(userId);
    }
    
    @Override
    public boolean deleteComment(Long userId, Long commentId) {
        return commentMapper.deleteById(commentId, userId) > 0;
    }
    
    @Override
    public Comment getCommentById(Long commentId) {
        return commentMapper.selectById(commentId);
    }
}