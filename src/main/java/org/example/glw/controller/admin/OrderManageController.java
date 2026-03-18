package org.example.glw.controller.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.dto.order.OrderManageListResponse;
import org.example.glw.dto.order.ProcessRefundRequest;
import org.example.glw.entity.Orders;
import org.example.glw.service.OrderService;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理控制器（管理员）
 */
@RestController
@RequestMapping("/api/v1/order/manage")
public class OrderManageController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 管理订单列表（管理员）
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<OrderManageListResponse>> getManageOrderList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 检查是否为管理员
        org.example.glw.entity.User currentUser = userService.findById(userId);
        if (currentUser == null || currentUser.getRoleId() != 1) { // 1为管理员角色ID
            return ApiResponse.fail(403, "权限不足，无法执行此操作");
        }
        
        // 解析日期参数
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        try {
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDate.parse(startDate).atStartOfDay();
            }
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDate.parse(endDate).atTime(23, 59, 59);
            }
        } catch (Exception e) {
            return ApiResponse.fail(400, "日期格式错误");
        }
        
        // 查询管理订单列表
        List<Orders> orders = orderService.getManageOrders(page, size, orderNo, username, orderStatus, startDateTime, endDateTime);
        Long total = orderService.countManageOrders(orderNo, username, orderStatus, startDateTime, endDateTime);
        
        // 转换为响应数据
        List<OrderManageListResponse> orderResponses = orders.stream().map(order -> {
            OrderManageListResponse response = new OrderManageListResponse();
            response.setOrderId(order.getOrderId());
            response.setOrderNo(order.getOrderNo());
            response.setUsername("用户" + order.getUserId()); // 简化处理，实际应查询用户表获取用户名
            response.setTotalAmount(order.getTotalAmount());
            response.setActualAmount(order.getActualAmount());
            response.setOrderStatus(order.getOrderStatus());
            response.setOrderStatusText(getOrderStatusText(order.getOrderStatus()));
            response.setCreateTime(order.getCreateTime());
            return response;
        }).collect(Collectors.toList());
        
        PageResponse<OrderManageListResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(orderResponses);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 处理退款申请（管理员）
     */
    @PutMapping("/refund/{orderId}")
    public ApiResponse<Void> processRefund(@PathVariable Long orderId, @RequestBody ProcessRefundRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 检查是否为管理员
        org.example.glw.entity.User currentUser = userService.findById(userId);
        if (currentUser == null || currentUser.getRoleId() != 1) { // 1为管理员角色ID
            return ApiResponse.fail(403, "权限不足，无法执行此操作");
        }
        
        // 处理退款申请
        boolean result = orderService.processRefund(orderId, request.getAgree(), request.getRemark());
        if (!result) {
            return ApiResponse.fail(500, "退款处理失败");
        }
        
        return ApiResponse.success("退款处理成功", null);
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
    
    /**
     * 获取订单状态文本
     */
    private String getOrderStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待支付";
            case 1: return "待使用";
            case 2: return "已使用";
            case 3: return "已取消";
            case 4: return "已完成";
            case 5: return "已退款";
            default: return "未知";
        }
    }
}