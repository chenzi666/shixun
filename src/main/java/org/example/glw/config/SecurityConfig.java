package org.example.glw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 密码编码器配置
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置HTTP安全规则
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护，便于开发测试
            .csrf(csrf -> csrf.disable())
            // 设置会话创建策略为无状态，不使用session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求授权规则
            .authorizeHttpRequests(authorize -> authorize
                // 允许所有用户访问登录、注册、发送验证码和重置密码接口
                .requestMatchers("/api/v1/auth/**").permitAll()
                // 允许访问用户信息接口（需要认证）
                .requestMatchers("/api/v1/user/info").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // 允许访问静态资源
                .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            // 允许跨域请求
            .cors(cors -> {})
            // 禁用表单登录
            .formLogin(form -> form.disable())
            // 禁用基本认证
            .httpBasic(basic -> basic.disable());

        // 添加JWT认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}