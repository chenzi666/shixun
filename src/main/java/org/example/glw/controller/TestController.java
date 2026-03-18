package org.example.glw.controller;

import io.jsonwebtoken.Claims;
import org.example.glw.dto.ApiResponse;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @GetMapping("/token")
    public ApiResponse<String> testToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ApiResponse.fail(400, "Authorization header missing or invalid format");
        }
        
        String token = authHeader.substring(7);
        System.out.println("Received token: " + token);
        
        Claims claims = jwtUtils.getClaimsFromToken(token);
        if (claims == null) {
            return ApiResponse.fail(400, "Invalid token");
        }
        
        boolean valid = jwtUtils.validateToken(token);
        return ApiResponse.success("Token is " + (valid ? "valid" : "invalid"), 
            "User: " + claims.getSubject() + ", Role: " + claims.get("role"));
    }
}