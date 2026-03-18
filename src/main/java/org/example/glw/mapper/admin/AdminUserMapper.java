package org.example.glw.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.User;
import java.util.List;

@Mapper
public interface AdminUserMapper {
    // 查询用户列表（管理员）
    List<User> selectUsers(@Param("offset") int offset, @Param("size") int size, 
                          @Param("username") String username, @Param("status") Integer status);
    
    // 查询用户总数（管理员）
    Long countUsers(@Param("username") String username, @Param("status") Integer status);
    
    // 更新用户信息
    int updateById(User user);
}