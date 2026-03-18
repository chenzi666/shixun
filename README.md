# GLW - 桂林旅游门户系统后端

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![MyBatis](https://img.shields.io/badge/MyBatis-3.0.3-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)
![JWT](https://img.shields.io/badge/JWT-0.11.5-red.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

一个功能完善的桂林旅游信息服务与管理平台

[📖 项目文档](#-项目文档) • [🚀 快速开始](#-快速开始) • [📁 项目结构](#-项目结构) • [🔧 技术栈](#-技术栈) • [📝 API 接口](#-api-接口)

</div>

---

## 📋 项目简介

GLW（Guilin Travel Portal）是一个基于 Spring Boot 3.x 的桂林旅游门户系统后端服务项目。该系统提供完整的旅游信息管理、用户服务、订单处理等功能，支持景点、酒店、美食、旅游路线等核心业务模块，并集成了 JWT 认证、权限管理、系统日志等企业级功能。

### ✨ 主要特性

- 🔐 **安全认证**：基于 JWT 的用户认证与授权机制
- 📱 **RESTful API**：规范的 RESTful 接口设计
- 🏨 **业务全面**：覆盖景点、酒店、美食、路线规划等旅游场景
- 🛒 **订单系统**：完整的购物车与订单管理流程
- 💬 **互动功能**：评论、收藏等社交互动功能
- 📊 **后台管理**：完善的后台管理系统支持
- 📝 **系统日志**：操作日志记录与追踪
- 🗂️ **文件上传**：支持图片、视频等多种资源上传

---

## 🔧 技术栈

### 核心框架
- **Spring Boot** 3.5.7 - 快速应用开发框架
- **Spring Security** - 安全认证框架
- **MyBatis** 3.0.3 - ORM 持久层框架

### 安全与认证
- **JWT (io.jsonwebtoken)** 0.11.5 - Token 认证
- **Spring Security** - 权限控制

### 数据库
- **MySQL** 8.0+ - 关系型数据库
- **MySQL Connector/J** - JDBC 驱动

### 开发环境
- **Java** 21
- **Maven** - 项目构建与依赖管理
- **Lombok** - 简化代码（可选）

---

## 📁 项目结构

```
glw/
├── src/main/java/org/example/glw/
│   ├── config/                      # 配置类
│   │   ├── JwtAuthenticationFilter.java    # JWT 认证过滤器
│   │   ├── SecurityConfig.java             # Spring Security 配置
│   │   └── WebMvcConfig.java               # Web MVC 配置
│   ├── controller/                  # 控制器层
│   │   ├── admin/                   # 后台管理接口
│   │   ├── AuthController.java      # 认证接口
│   │   ├── ContentController.java   # 内容管理接口
│   │   ├── OrderController.java     # 订单接口
│   │   ├── TravelPlanController.java # 旅游计划接口
│   │   ├── UserController.java      # 用户接口
│   │   └── ...                      # 其他控制器
│   ├── service/                     # 业务逻辑层
│   │   ├── impl/                    # 服务实现
│   │   └── admin/                   # 后台管理服务
│   ├── mapper/                      # 数据访问层
│   ├── entity/                      # 实体类
│   │   ├── User.java                # 用户实体
│   │   ├── Attraction.java          # 景点实体
│   │   ├── Hotel.java               # 酒店实体
│   │   ├── Food.java                # 美食实体
│   │   ├── TravelRoute.java         # 旅游路线
│   │   ├── Orders.java              # 订单实体
│   │   ├── Comment.java             # 评论实体
│   │   └── ...                      # 其他实体
│   ├── dto/                         # 数据传输对象
│   │   ├── ApiResponse.java         # 统一响应
│   │   ├── PageResponse.java        # 分页响应
│   │   └── ...                      # 其他 DTO
│   ├── utils/                       # 工具类
│   └── GlwApplication.java          # 启动类
├── src/main/resources/
│   ├── mapper/                      # MyBatis XML 映射文件
│   └── application.properties       # 应用配置文件
└── pom.xml                          # Maven 配置文件
```

---

## 🚀 快速开始

### 环境要求

- JDK 21+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

#### 1. 克隆项目

```bash
git clone https://github.com/your-username/glw.git
cd glw
```

#### 2. 配置数据库

创建 MySQL 数据库并导入 SQL 脚本：

```sql
CREATE DATABASE gl_travel_portal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gl_travel_portal;
```

导入 `gl_travel_portal.sql` 文件：

```bash
mysql -u root -p gl_travel_portal < gl_travel_portal.sql
```

#### 3. 修改配置

编辑 `src/main/resources/application.properties`：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/gl_travel_portal?useUnicode=true&characterEncoding=utf-8&...
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT 密钥（建议修改为自己的密钥）
jwt.secret=your_secret_key_here

# 文件上传路径
file.upload.path=/your/upload/path/
```

#### 4. 构建项目

```bash
mvn clean install
```

#### 5. 运行应用

```bash
mvn spring-boot:run
```

应用启动后，默认访问地址：`http://localhost:8080`

---

## 📝 API 接口

### 认证接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/login` | POST | 用户登录 |
| `/api/auth/logout` | POST | 用户登出 |
| `/api/auth/password/change` | POST | 修改密码 |
| `/api/auth/password/reset` | POST | 重置密码 |

### 用户接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/user/info` | GET | 获取用户信息 |
| `/api/user/update` | POST | 更新用户信息 |
| `/api/user/list` | GET | 用户列表（管理员） |

### 内容接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/content/attractions` | GET | 景点列表 |
| `/api/content/hotels` | GET | 酒店列表 |
| `/api/content/foods` | GET | 美食列表 |
| `/api/content/categories` | GET | 分类列表 |

### 旅游计划接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/travel-plan/create` | POST | 创建行程 |
| `/api/travel-plan/list` | GET | 行程列表 |
| `/api/travel-plan/detail` | GET | 行程详情 |
| `/api/travel-plan/update` | PUT | 更新行程 |
| `/api/travel-plan/delete` | DELETE | 删除行程 |

### 订单接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/order/create` | POST | 创建订单 |
| `/api/order/list` | GET | 订单列表 |
| `/api/order/detail` | GET | 订单详情 |
| `/api/order/cancel` | POST | 取消订单 |

### 评论接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/comment/create` | POST | 发表评论 |
| `/api/comment/list` | GET | 评论列表 |
| `/api/comment/delete` | DELETE | 删除评论 |

### 收藏接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/favorite/add` | POST | 添加收藏 |
| `/api/favorite/list` | GET | 收藏列表 |
| `/api/favorite/remove` | DELETE | 取消收藏 |

### 文件上传接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/upload/image` | POST | 上传图片 |
| `/api/upload/video` | POST | 上传视频 |
| `/api/upload/file` | POST | 上传文件 |

> 📌 详细 API 文档请参考项目 Wiki 或使用 Postman 查看接口集合

---

## 🔐 认证说明

系统采用 **JWT (JSON Web Token)** 进行身份认证：

1. 用户登录后获取 token
2. 后续请求在 Header 中携带 token：`Authorization: Bearer <token>`
3. Token 有效期默认为 7 天（604800000ms）
4. 过期需重新登录

---

## 📊 数据库设计

系统包含以下核心数据表：

- **用户相关**：`user`, `role`, `permission`, `role_permission`
- **旅游资源**：`attraction`, `hotel`, `hotel_room`, `food`, `category`
- **业务相关**：`travel_plan`, `travel_route`, `orders`, `order_item`
- **互动功能**：`comment`, `favorite`
- **系统管理**：`banner`, `announcement`, `system_log`

完整表结构请查看 `gl_travel_portal.sql` 文件。

---

## 🛠️ 开发指南

### 添加新的 Controller

```java
@RestController
@RequestMapping("/api/your-module")
public class YourController {
    
    @Autowired
    private YourService yourService;
    
    @GetMapping("/list")
    public ApiResponse<?> list() {
        return ApiResponse.success(yourService.getList());
    }
}
```

### 添加新的 Service

```java
@Service
public class YourServiceImpl implements YourService {
    
    @Autowired
    private YourMapper yourMapper;
    
    @Override
    public List<YourEntity> getList() {
        return yourMapper.selectList();
    }
}
```

### Mapper XML 配置

在 `src/main/resources/mapper/` 下创建对应的 XML 映射文件。

---

## 🧪 测试

运行单元测试：

```bash
mvn test
```

---

## 📦 部署

### 打包应用

```bash
mvn clean package -DskipTests
```

### 运行 JAR 包

```bash
java -jar target/glw-0.0.1-SNAPSHOT.jar
```

### 生产环境配置

建议修改以下配置：

```properties
# 修改端口
server.port=80

# 生产环境数据库
spring.datasource.url=jdbc:mysql://prod-db-host:3306/gl_travel_portal...

# 强化 JWT 密钥
jwt.secret=your-strong-production-secret-key-here

# 日志级别调整
logging.level.org.example.glw=warn
```

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📄 开源协议

本项目采用 MIT 协议开源 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

## 👥 开发团队

- **开发者**：未诚
- **联系方式**：2897895906@qq.com

---

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis](https://mybatis.org/mybatis-3/)
- [JWT](https://jwt.io/)
- [MySQL](https://www.mysql.com/)

---

## 📞 问题反馈

如遇到问题或有更好的建议，请通过以下方式联系我们：

- 提交 [Issue](https://github.com/your-username/glw/issues)
- 发送邮件至：2897895906@qq.com

---

<div align="center">

**⭐ 如果这个项目对您有帮助，请给个 Star 支持一下！**

Made with ❤️ by GLW Team

</div>
