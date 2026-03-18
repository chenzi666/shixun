package org.example.glw.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类，用于密码加密和验证
 */
public class PasswordUtils {

    // 密码编码器实例
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 对密码进行加密
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 验证密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}