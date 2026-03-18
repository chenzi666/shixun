package org.example.glw.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类，用于生成和验证Token
 */
@Component
public class JwtUtils {
    // 从配置文件读取密钥
    @Value("${jwt.secret}")
    private String secretKeyString;
    
    private SecretKey secretKey;
    
    // 从配置文件读取过期时间
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    /**
     * 初始化SecretKey
     */
    @PostConstruct
    public void init() {
        // 将字符串密钥转换为SecretKey对象
        secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }
    
    /**
     * 生成Token
     */
    public String generateToken(Long userId, String username, String role) {
        // 设置Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        
        // 创建Token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 从Token中获取Claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("JWT解析失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                System.out.println("JWT Claims为空");
                return false;
            }
            // 检查是否过期
            boolean expired = claims.getExpiration().before(new Date());
            if (expired) {
                System.out.println("JWT Token已过期");
            }
            return !expired;
        } catch (Exception e) {
            System.out.println("JWT验证失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 从Token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.get("userId", Long.class);
        }
        return null;
    }
    
    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.get("username", String.class);
        }
        return null;
    }
    
    /**
     * 从Token中获取角色
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.get("role", String.class);
        }
        return null;
    }
}