package org.example.glw.dto.order;

/**
 * 处理退款申请请求参数
 */
public class ProcessRefundRequest {
    private Boolean agree;
    private String remark;

    // Getters and Setters
    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}