package org.example.glw.utils;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码工具类
 */
public class VerificationCodeUtils {
    // 存储验证码，key为target（邮箱或手机号），value为验证码
    private static final Map<String, String> CODE_CACHE = new ConcurrentHashMap<>();
    // 验证码有效期（5分钟）
    private static final long CODE_EXPIRATION = 5 * 60 * 1000;
    // 存储验证码生成时间
    private static final Map<String, Long> CODE_TIMESTAMP = new ConcurrentHashMap<>();
    
    /**
     * 生成6位数字验证码
     */
    public static String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    /**
     * 保存验证码
     */
    public static void saveCode(String target, String code) {
        CODE_CACHE.put(target, code);
        CODE_TIMESTAMP.put(target, System.currentTimeMillis());
    }
    
    /**
     * 获取验证码
     */
    public static String getCode(String target) {
        // 检查是否过期
        Long timestamp = CODE_TIMESTAMP.get(target);
        if (timestamp != null && System.currentTimeMillis() - timestamp > CODE_EXPIRATION) {
            // 过期则移除
            CODE_CACHE.remove(target);
            CODE_TIMESTAMP.remove(target);
            return null;
        }
        return CODE_CACHE.get(target);
    }
    
    /**
     * 验证验证码
     */
    public static boolean validateCode(String target, String code) {
        String cachedCode = getCode(target);
        if (cachedCode != null && cachedCode.equals(code)) {
            // 验证成功后移除验证码
            CODE_CACHE.remove(target);
            CODE_TIMESTAMP.remove(target);
            return true;
        }
        return false;
    }
    
    /**
     * 移除验证码
     */
    public static void removeCode(String target) {
        CODE_CACHE.remove(target);
        CODE_TIMESTAMP.remove(target);
    }
}