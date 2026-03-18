package org.example.glw.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.dto.PageResponse;
import org.example.glw.dto.order.*;
import org.example.glw.entity.Orders;
import org.example.glw.entity.OrderItem;
import org.example.glw.service.OrderService;
import org.example.glw.service.UserService;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理控制器（普通用户）
 */
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public ApiResponse<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 创建订单
        Orders order = orderService.createOrder(userId, request);
        if (order == null) {
            return ApiResponse.fail(500, "订单创建失败");
        }
        
        // 构建响应数据
        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderNo(order.getOrderNo());
        response.setTotalAmount(order.getTotalAmount());
        
        return ApiResponse.success("订单创建成功", response);
    }

    /**
     * 获取订单列表
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<OrderListResponse>> getOrderList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletRequest httpRequest) {
        
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
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
        
        // 查询订单列表
        List<Orders> orders = orderService.getUserOrders(userId, page, size, status, startDateTime, endDateTime);
        Long total = orderService.countUserOrders(userId, status, startDateTime, endDateTime);
        
        // 转换为响应数据
        List<OrderListResponse> orderResponses = orders.stream().map(order -> {
            OrderListResponse response = new OrderListResponse();
            response.setOrderId(order.getOrderId());
            response.setOrderNo(order.getOrderNo());
            response.setTotalAmount(order.getTotalAmount());
            response.setActualAmount(order.getActualAmount());
            response.setOrderStatus(order.getOrderStatus());
            response.setOrderStatusText(getOrderStatusText(order.getOrderStatus()));
            response.setCreateTime(order.getCreateTime());
            
            // 获取订单商品项
            List<OrderItem> items = orderService.getOrderItems(order.getOrderId());
            List<OrderListResponse.OrderItemResponse> itemResponses = items.stream().map(item -> {
                OrderListResponse.OrderItemResponse itemResponse = new OrderListResponse.OrderItemResponse();
                itemResponse.setItemId(item.getItemId());
                itemResponse.setProductId(item.getProductId());
                itemResponse.setProductType(item.getProductType());
                itemResponse.setProductName(item.getProductName());
                itemResponse.setPrice(item.getPrice());
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setTotalPrice(item.getTotalPrice());
                itemResponse.setUseDate(item.getUseDate());
                itemResponse.setItemStatus(item.getItemStatus());
                return itemResponse;
            }).collect(Collectors.toList());
            
            response.setItems(itemResponses);
            return response;
        }).collect(Collectors.toList());
        
        PageResponse<OrderListResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(orderResponses);
        pageResponse.setTotal(total);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        
        return ApiResponse.success("获取成功", pageResponse);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/detail/{orderId}")
    public ApiResponse<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 查询订单详情
        Orders order = orderService.getOrderDetail(userId, orderId);
        if (order == null) {
            return ApiResponse.fail(404, "订单不存在");
        }
        
        // 转换为响应数据
        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderNo(order.getOrderNo());
        response.setTotalAmount(order.getTotalAmount());
        response.setActualAmount(order.getActualAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setOrderStatus(order.getOrderStatus());
        response.setCreateTime(order.getCreateTime());
        response.setPayTime(order.getPayTime());
        response.setContactName(order.getContactName());
        response.setContactPhone(order.getContactPhone());
        response.setRemark(order.getRemark());
        
        // 获取订单商品项
        List<OrderItem> items = orderService.getOrderItems(order.getOrderId());
        List<OrderDetailResponse.OrderItemResponse> itemResponses = items.stream().map(item -> {
            OrderDetailResponse.OrderItemResponse itemResponse = new OrderDetailResponse.OrderItemResponse();
            itemResponse.setItemId(item.getItemId());
            itemResponse.setProductId(item.getProductId());
            itemResponse.setProductType(item.getProductType());
            itemResponse.setProductName(item.getProductName());
            itemResponse.setPrice(item.getPrice());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setTotalPrice(item.getTotalPrice());
            itemResponse.setUseDate(item.getUseDate());
            itemResponse.setItemStatus(item.getItemStatus());
            return itemResponse;
        }).collect(Collectors.toList());
        
        response.setItems(itemResponses);
        
        return ApiResponse.success("获取成功", response);
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{orderId}")
    public ApiResponse<Void> cancelOrder(@PathVariable Long orderId, @RequestBody CancelOrderRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 取消订单
        boolean result = orderService.cancelOrder(userId, orderId, request.getReason());
        if (!result) {
            return ApiResponse.fail(500, "订单取消失败");
        }
        
        return ApiResponse.success("订单取消成功", null);
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay/{orderId}")
    public ApiResponse<PayOrderResponse> payOrder(@PathVariable Long orderId, @RequestBody PayOrderRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 支付订单
        boolean result = orderService.payOrder(userId, orderId, request.getPaymentMethod());
        if (!result) {
            return ApiResponse.fail(500, "支付失败");
        }
        
        // 构建响应数据
        PayOrderResponse response = new PayOrderResponse();
        response.setPayUrl("https://payment.example.com/pay/" + orderId); // 示例支付链接
        response.setQrCode("https://payment.example.com/qrcode/" + orderId); // 示例二维码链接
        
        return ApiResponse.success("获取支付链接成功", response);
    }

    /**
     * 查询订单支付状态
     */
    @GetMapping("/pay-status/{orderId}")
    public ApiResponse<PaymentStatusResponse> getOrderPaymentStatus(@PathVariable Long orderId, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 查询订单支付状态
        Integer paymentStatus = orderService.getOrderPaymentStatus(userId, orderId);
        if (paymentStatus == null) {
            return ApiResponse.fail(404, "订单不存在");
        }
        
        // 构建响应数据
        PaymentStatusResponse response = new PaymentStatusResponse();
        response.setOrderId(orderId);
        response.setPaymentStatus(paymentStatus);
        response.setPaymentStatusText(getPaymentStatusText(paymentStatus));
        
        return ApiResponse.success("查询成功", response);
    }

    /**
     * 申请退款
     */
    @PostMapping("/refund/{orderId}")
    public ApiResponse<RefundResponse> applyRefund(@PathVariable Long orderId, @RequestBody RefundRequest request, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }
        
        // 申请退款
        String refundId = orderService.applyRefund(userId, orderId, request.getRefundAmount(), request.getReason(), request.getItemIds());
        if (refundId == null) {
            return ApiResponse.fail(500, "退款申请提交失败");
        }
        
        // 构建响应数据
        RefundResponse response = new RefundResponse();
        response.setRefundId(refundId);
        
        return ApiResponse.success("退款申请提交成功", response);
    }

    /**
     * 从JWT token中获取用户ID
     */
    protected Long getUserIdFromToken(HttpServletRequest request) {
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
    protected String getOrderStatusText(Integer status) {
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
    
    /**
     * 获取支付状态文本
     */
    protected String getPaymentStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "未支付";
            case 1: return "已支付";
            case 2: return "支付失败";
            default: return "未知";
        }
    }
}