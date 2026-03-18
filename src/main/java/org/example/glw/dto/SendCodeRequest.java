package org.example.glw.dto;

/**
 * 发送验证码请求参数
 */
public class SendCodeRequest {
    private String type; // email 或 phone
    private String target; // 邮箱地址或手机号
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
}