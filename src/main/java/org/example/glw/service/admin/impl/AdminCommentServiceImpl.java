package org.example.glw.service.admin.impl;

import org.example.glw.entity.Comment;
import org.example.glw.mapper.admin.AdminCommentMapper;
import org.example.glw.service.admin.AdminCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminCommentServiceImpl implements AdminCommentService {
    
    @Autowired
    private AdminCommentMapper adminCommentMapper;
    
    @Override
    public List<Comment> getManageComments(Integer page, Integer size, String productType, Integer status) {
        int offset = (page - 1) * size;
        return adminCommentMapper.selectManageComments(offset, size, productType, status);
    }
    
    @Override
    public Long countManageComments(String productType, Integer status) {
        return adminCommentMapper.countManageComments(productType, status);
    }
    
    @Override
    public boolean auditComment(Long commentId, Integer status) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setStatus(status);
        return adminCommentMapper.updateById(comment) > 0;
    }
    
    @Override
    public Comment getCommentById(Long commentId) {
        return adminCommentMapper.selectById(commentId);
    }
}