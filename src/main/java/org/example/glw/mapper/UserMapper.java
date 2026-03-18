package org.example.glw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.glw.entity.User;
import java.util.List;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    User selectByUsername(String username);
    
    // 根据邮箱查询用户
    User selectByEmail(String email);
    
    // 根据手机号查询用户
    User selectByPhone(String phone);
    
    // 新增用户
    int insert(User user);
    
    // 更新用户信息
    int updateById(User user);
    
    // 根据用户ID查询用户
    User selectById(Long userId);
    
    // 查询用户列表（管理员）
    List<User> selectUsers(@Param("offset") int offset, @Param("size") int size, 
                          @Param("username") String username, @Param("status") Integer status);
    
    // 查询用户总数（管理员）
    Long countUsers(@Param("username") String username, @Param("status") Integer status);
}