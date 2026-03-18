package org.example.glw.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.entity.TravelPlan;
import org.example.glw.entity.User;
import org.example.glw.service.TravelPlanService;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行程管理控制器
 */
@RestController
@RequestMapping("/api/v1/travel-plan")
public class TravelPlanController {

    @Autowired
    private TravelPlanService travelPlanService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 创建行程
     */
    @PostMapping("/create")
    public ApiResponse<TravelPlanCreateResponse> createTravelPlan(@RequestBody TravelPlanCreateRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 创建行程实体
        TravelPlan travelPlan = new TravelPlan();
        travelPlan.setUserId(userId);
        travelPlan.setPlanName(request.getPlanName());
        travelPlan.setStartDate(request.getStartDate());
        travelPlan.setEndDate(request.getEndDate());
        // 将planContent转换为JSON字符串存储
        if (request.getPlanContent() != null) {
            travelPlan.setPlanContent(request.getPlanContent().toString());
        }
        travelPlan.setIsPublic(request.getPublic() != null && request.getPublic() ? 1 : 0);

        // 保存行程
        int result = travelPlanService.createTravelPlan(travelPlan);
        if (result > 0) {
            TravelPlanCreateResponse response = new TravelPlanCreateResponse();
            response.setPlanId(travelPlan.getPlanId());
            return ApiResponse.success("行程创建成功", response);
        } else {
            return ApiResponse.fail(500, "行程创建失败");
        }
    }

    /**
     * 更新行程
     */
    @PutMapping("/update/{planId}")
    public ApiResponse<Void> updateTravelPlan(@PathVariable Long planId, @RequestBody TravelPlanUpdateRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 获取行程详情
        TravelPlan travelPlan = travelPlanService.getTravelPlanById(planId);
        if (travelPlan == null) {
            return ApiResponse.fail(404, "行程不存在");
        }

        // 检查是否为本人操作
        if (!travelPlan.getUserId().equals(userId)) {
            return ApiResponse.fail(403, "权限不足，无法执行此操作");
        }

        // 更新行程信息
        travelPlan.setPlanName(request.getPlanName());
        travelPlan.setStartDate(request.getStartDate());
        travelPlan.setEndDate(request.getEndDate());
        if (request.getPlanContent() != null) {
            travelPlan.setPlanContent(request.getPlanContent().toString());
        }
        travelPlan.setIsPublic(request.getPublic() != null && request.getPublic() ? 1 : 0);

        // 更新行程
        int result = travelPlanService.updateTravelPlan(travelPlan);
        if (result > 0) {
            return ApiResponse.success("行程更新成功", null);
        } else {
            return ApiResponse.fail(500, "行程更新失败");
        }
    }

    /**
     * 获取行程列表
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<TravelPlanResponse>> getTravelPlanList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 获取行程列表
        List<TravelPlan> travelPlans = travelPlanService.getTravelPlans(userId, page, size);
        Long total = travelPlanService.countTravelPlans(userId);

        // 转换为响应对象
        List<TravelPlanResponse> responseList = travelPlans.stream().map(plan -> {
            TravelPlanResponse response = new TravelPlanResponse();
            response.setPlanId(plan.getPlanId());
            response.setPlanName(plan.getPlanName());
            response.setStartDate(plan.getStartDate());
            response.setEndDate(plan.getEndDate());
            // 计算天数
            if (plan.getStartDate() != null && plan.getEndDate() != null) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate()) + 1;
                response.setDays((int) days);
            }
            response.setPublic(plan.getIsPublic() != null && plan.getIsPublic() == 1);
            response.setCreateTime(plan.getCreateTime());
            response.setUpdateTime(plan.getUpdateTime());
            return response;
        }).collect(Collectors.toList());

        PageResponse<TravelPlanResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(responseList);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 获取行程详情
     */
    @GetMapping("/detail/{planId}")
    public ApiResponse<TravelPlanDetailResponse> getTravelPlanDetail(@PathVariable Long planId, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 获取行程详情
        TravelPlan travelPlan = travelPlanService.getTravelPlanById(planId);
        if (travelPlan == null) {
            return ApiResponse.fail(404, "行程不存在");
        }

        // 检查是否为本人操作或行程是否公开
        if (!travelPlan.getUserId().equals(userId) && (travelPlan.getIsPublic() == null || travelPlan.getIsPublic() != 1)) {
            return ApiResponse.fail(403, "权限不足，无法执行此操作");
        }

        TravelPlanDetailResponse response = new TravelPlanDetailResponse();
        response.setPlanId(travelPlan.getPlanId());
        response.setPlanName(travelPlan.getPlanName());
        response.setStartDate(travelPlan.getStartDate());
        response.setEndDate(travelPlan.getEndDate());
        // 解析planContent
        if (travelPlan.getPlanContent() != null) {
            response.setPlanContent(travelPlan.getPlanContent());
        }
        response.setPublic(travelPlan.getIsPublic() != null && travelPlan.getIsPublic() == 1);
        response.setCreateTime(travelPlan.getCreateTime());
        response.setUpdateTime(travelPlan.getUpdateTime());

        return ApiResponse.success("获取成功", response);
    }

    /**
     * 删除行程
     */
    @DeleteMapping("/delete/{planId}")
    public ApiResponse<Void> deleteTravelPlan(@PathVariable Long planId, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 删除行程
        boolean result = travelPlanService.deleteTravelPlan(userId, planId);
        if (result) {
            return ApiResponse.success("行程删除成功", null);
        } else {
            return ApiResponse.fail(500, "行程删除失败");
        }
    }

    /**
     * 获取公开行程列表
     */
    @GetMapping("/public-list")
    public ApiResponse<PageResponse<PublicTravelPlanResponse>> getPublicTravelPlanList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            HttpServletRequest httpRequest) {
        
        // 获取公开行程列表
        List<TravelPlan> travelPlans = travelPlanService.getPublicTravelPlans(userId, page, size);
        Long total = travelPlanService.countPublicTravelPlans(userId);

        // 转换为响应对象
        List<PublicTravelPlanResponse> responseList = travelPlans.stream().map(plan -> {
            PublicTravelPlanResponse response = new PublicTravelPlanResponse();
            response.setPlanId(plan.getPlanId());
            response.setPlanName(plan.getPlanName());
            response.setStartDate(plan.getStartDate());
            response.setEndDate(plan.getEndDate());
            // 计算天数
            if (plan.getStartDate() != null && plan.getEndDate() != null) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate()) + 1;
                response.setDays((int) days);
            }
            
            // 获取用户名和头像
            User user = userService.findById(plan.getUserId());
            if (user != null) {
                response.setUsername(user.getNickname());
                response.setAvatar(user.getAvatar());
            }
            
            response.setCreateTime(plan.getCreateTime());
            return response;
        }).collect(Collectors.toList());

        PageResponse<PublicTravelPlanResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(responseList);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 从JWT token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.getClaimsFromToken(token);
            if (claims != null) {
                return Long.valueOf(claims.get("userId").toString());
            }
        }
        return null;
    }

    // 内部类定义
    public static class TravelPlanCreateRequest {
        private String planName;
        private LocalDate startDate;
        private LocalDate endDate;
        private Object planContent;
        private Boolean isPublic;

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Object getPlanContent() {
            return planContent;
        }

        public void setPlanContent(Object planContent) {
            this.planContent = planContent;
        }

        public Boolean getPublic() {
            return isPublic;
        }

        public void setPublic(Boolean aPublic) {
            isPublic = aPublic;
        }
    }

    public static class TravelPlanCreateResponse {
        private Long planId;

        public Long getPlanId() {
            return planId;
        }

        public void setPlanId(Long planId) {
            this.planId = planId;
        }
    }

    public static class TravelPlanUpdateRequest {
        private String planName;
        private LocalDate startDate;
        private LocalDate endDate;
        private Object planContent;
        private Boolean isPublic;

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Object getPlanContent() {
            return planContent;
        }

        public void setPlanContent(Object planContent) {
            this.planContent = planContent;
        }

        public Boolean getPublic() {
            return isPublic;
        }

        public void setPublic(Boolean aPublic) {
            isPublic = aPublic;
        }
    }

    public static class TravelPlanResponse {
        private Long planId;
        private String planName;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer days;
        private Boolean isPublic;
        private java.time.LocalDateTime createTime;
        private java.time.LocalDateTime updateTime;

        public Long getPlanId() {
            return planId;
        }

        public void setPlanId(Long planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Integer getDays() {
            return days;
        }

        public void setDays(Integer days) {
            this.days = days;
        }

        public Boolean getPublic() {
            return isPublic;
        }

        public void setPublic(Boolean aPublic) {
            isPublic = aPublic;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }

        public java.time.LocalDateTime getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(java.time.LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class TravelPlanDetailResponse {
        private Long planId;
        private String planName;
        private LocalDate startDate;
        private LocalDate endDate;
        private Object planContent;
        private Boolean isPublic;
        private java.time.LocalDateTime createTime;
        private java.time.LocalDateTime updateTime;

        public Long getPlanId() {
            return planId;
        }

        public void setPlanId(Long planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Object getPlanContent() {
            return planContent;
        }

        public void setPlanContent(Object planContent) {
            this.planContent = planContent;
        }

        public Boolean getPublic() {
            return isPublic;
        }

        public void setPublic(Boolean aPublic) {
            isPublic = aPublic;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }

        public java.time.LocalDateTime getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(java.time.LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class PublicTravelPlanResponse {
        private Long planId;
        private String planName;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer days;
        private String username;
        private String avatar;
        private java.time.LocalDateTime createTime;

        public Long getPlanId() {
            return planId;
        }

        public void setPlanId(Long planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Integer getDays() {
            return days;
        }

        public void setDays(Integer days) {
            this.days = days;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }
}