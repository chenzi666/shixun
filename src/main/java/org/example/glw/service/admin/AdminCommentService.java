package org.example.glw.service.admin;

import org.example.glw.entity.Comment;
import java.util.List;

public interface AdminCommentService {
    // 获取管理评价列表
    List<Comment> getManageComments(Integer page, Integer size, String productType, Integer status);
    
    // 统计管理评价数量
    Long countManageComments(String productType, Integer status);
    
    // 审核评价
    boolean auditComment(Long commentId, Integer status);
    
    // 根据ID获取评价
    Comment getCommentById(Long commentId);
}