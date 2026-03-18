package org.example.glw.config;

import io.jsonwebtoken.Claims;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT认证过滤器，用于拦截请求并验证JWT token
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 从请求头中获取token
            String token = extractJwtFromRequest(request);
            
            System.out.println("Extracted token: " + token);

            // 如果token不为空且有效
            if (token != null && jwtUtils.validateToken(token)) {
                // 获取token中的用户信息
                Claims claims = jwtUtils.getClaimsFromToken(token);
                String username = claims.getSubject();
                
                System.out.println("Token is valid for user: " + username);
                
                // 获取用户角色
                String role = (String) claims.get("role");
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (role != null) {
                    // 根据JWT中的角色添加适当的权限前缀
                    if ("admin".equals(role)) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    } else if ("user".equals(role)) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                    }
                } else {
                    // 默认角色
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }

                // 创建UserDetails对象
                UserDetails userDetails = new User(username, "", authorities);

                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 设置认证信息到Spring Security上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                System.out.println("Authentication set for user: " + username + " with authorities: " + authorities);
            } else {
                System.out.println("Token is null or invalid");
            }
        } catch (Exception ex) {
            System.out.println("Authentication failed: " + ex.getMessage());
            ex.printStackTrace();
            // 认证失败，清除Security上下文
            SecurityContextHolder.clearContext();
        }

        // 继续执行过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中提取JWT token
     * @param request HTTP请求
     * @return JWT token
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}