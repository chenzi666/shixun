package org.example.glw.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.glw.dto.ApiResponse;
import org.example.glw.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    @Autowired
    private JwtUtils jwtUtils;

    // 文件存储路径
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @Value("${file.upload.image.path}")
    private String imageUploadPath;
    
    @Value("${file.upload.video.path}")
    private String videoUploadPath;

    /**
     * 上传图片
     */
    @PostMapping("/image")
    public ApiResponse<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 检查文件是否为空
        if (file.isEmpty()) {
            return ApiResponse.fail(400, "上传文件不能为空");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.fail(400, "文件类型不正确，请上传图片文件");
        }

        // 创建上传目录
        File imageDir = new File(imageUploadPath);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        // 构造文件路径
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        File targetDir = new File(imageDir, datePath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        try {
            // 保存文件
            Path targetPath = Paths.get(targetDir.getPath(), newFilename);
            Files.write(targetPath, file.getBytes());

            // 构造访问URL
            String url = "/uploads/images/" + datePath + "/" + newFilename;

            UploadResponse response = new UploadResponse();
            response.setUrl(url);
            return ApiResponse.success("上传成功", response);
        } catch (IOException e) {
            return ApiResponse.fail(500, "文件上传失败");
        }
    }

    /**
     * 上传视频
     */
    @PostMapping("/video")
    public ApiResponse<UploadResponse> uploadVideo(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest) {
        // 从请求头中获取token并解析用户ID
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return ApiResponse.fail(401, "未授权，请先登录");
        }

        // 检查文件是否为空
        if (file.isEmpty()) {
            return ApiResponse.fail(400, "上传文件不能为空");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            return ApiResponse.fail(400, "文件类型不正确，请上传视频文件");
        }

        // 检查文件大小（限制为100MB）
        if (file.getSize() > 100 * 1024 * 1024) {
            return ApiResponse.fail(400, "文件大小不能超过100MB");
        }

        // 创建上传目录
        File videoDir = new File(videoUploadPath);
        if (!videoDir.exists()) {
            videoDir.mkdirs();
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        // 构造文件路径
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        File targetDir = new File(videoDir, datePath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        try {
            // 保存文件
            Path targetPath = Paths.get(targetDir.getPath(), newFilename);
            Files.write(targetPath, file.getBytes());

            // 构造访问URL
            String url = "/uploads/videos/" + datePath + "/" + newFilename;

            UploadResponse response = new UploadResponse();
            response.setUrl(url);
            return ApiResponse.success("上传成功", response);
        } catch (IOException e) {
            return ApiResponse.fail(500, "文件上传失败");
        }
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
    public static class UploadResponse {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}