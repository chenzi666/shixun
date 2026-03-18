-- MySQL数据库脚本 - 桂林旅游门户网站
-- 创建时间: 2024-01-01
-- 数据库版本: MySQL 8.0+

-- 1. 创建数据库并设置字符集
CREATE DATABASE IF NOT EXISTS gl_travel_portal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE gl_travel_portal;

-- 2. 创建表结构

-- 2.1 角色表 (role)
CREATE TABLE IF NOT EXISTS role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2.2 权限表 (permission)
CREATE TABLE IF NOT EXISTS permission (
    permission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(50) NOT NULL,
    permission_code VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2.3 角色权限关联表 (role_permission)
CREATE TABLE IF NOT EXISTS role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(permission_id) ON DELETE CASCADE
);

-- 2.4 用户表 (user)
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0,
    register_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_time DATETIME,
    status TINYINT DEFAULT 1,
    role_id BIGINT DEFAULT 2,
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

-- 2.5 产品分类表 (category)
CREATE TABLE IF NOT EXISTS category (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2.6 旅游景点表 (attraction)
CREATE TABLE IF NOT EXISTS attraction (
    attraction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    address VARCHAR(255),
    phone VARCHAR(20),
    opening_hours VARCHAR(100),
    ticket_price DECIMAL(10,2),
    images JSON,
    video_url VARCHAR(255),
    longitude DECIMAL(10,6),
    latitude DECIMAL(10,6),
    score DECIMAL(3,1) DEFAULT 0,
    view_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 2.7 旅游线路表 (travel_route)
CREATE TABLE IF NOT EXISTS travel_route (
    route_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    days INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    departure_place VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    includes TEXT,
    excludes TEXT,
    notice TEXT,
    images JSON,
    video_url VARCHAR(255),
    score DECIMAL(3,1) DEFAULT 0,
    view_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 2.8 酒店表 (hotel)
CREATE TABLE IF NOT EXISTS hotel (
    hotel_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    address VARCHAR(255),
    phone VARCHAR(20),
    star_level TINYINT,
    min_price DECIMAL(10,2),
    max_price DECIMAL(10,2),
    facilities JSON,
    images JSON,
    video_url VARCHAR(255),
    longitude DECIMAL(10,6),
    latitude DECIMAL(10,6),
    score DECIMAL(3,1) DEFAULT 0,
    view_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 2.9 酒店房型表 (hotel_room)
CREATE TABLE IF NOT EXISTS hotel_room (
    room_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    bed_type VARCHAR(50),
    area DECIMAL(8,2),
    max_occupancy INT DEFAULT 2,
    facilities JSON,
    images JSON,
    stock INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id) ON DELETE CASCADE
);

-- 2.10 美食表 (food)
CREATE TABLE IF NOT EXISTS food (
    food_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    restaurant_name VARCHAR(100),
    price DECIMAL(10,2),
    images JSON,
    score DECIMAL(3,1) DEFAULT 0,
    view_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 2.11 订单表 (orders)
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    actual_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20),
    payment_status TINYINT DEFAULT 0,
    order_status TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    pay_time DATETIME,
    complete_time DATETIME,
    contact_name VARCHAR(50) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    remark VARCHAR(255),
    delete_flag TINYINT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 2.12 订单详情表 (order_item)
CREATE TABLE IF NOT EXISTS order_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_type VARCHAR(20) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    total_price DECIMAL(10,2) NOT NULL,
    use_date DATE,
    item_status TINYINT DEFAULT 0,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- 2.13 评价表 (comment)
CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_type VARCHAR(20) NOT NULL,
    score TINYINT NOT NULL,
    content TEXT,
    images JSON,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    order_item_id BIGINT,
    status TINYINT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (order_item_id) REFERENCES order_item(item_id)
);

-- 2.14 收藏表 (favorite)
CREATE TABLE IF NOT EXISTS favorite (
    favorite_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_type VARCHAR(20) NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 2.15 行程表 (travel_plan)
CREATE TABLE IF NOT EXISTS travel_plan (
    plan_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    plan_name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    plan_content JSON NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_public TINYINT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 2.16 公告表 (announcement)
CREATE TABLE IF NOT EXISTS announcement (
    announcement_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 1,
    create_by BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (create_by) REFERENCES user(user_id)
);

-- 2.17 Banner表 (banner)
CREATE TABLE IF NOT EXISTS banner (
    banner_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    image_url VARCHAR(255) NOT NULL,
    link_url VARCHAR(255),
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2.18 系统日志表 (sys_log)
CREATE TABLE IF NOT EXISTS sys_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    username VARCHAR(50),
    operation VARCHAR(255),
    ip VARCHAR(50),
    method VARCHAR(255),
    params TEXT,
    time BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 0,
    error_msg TEXT,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 3. 创建索引

-- 3.1 用户表索引
CREATE INDEX idx_user_status ON user(status);
CREATE INDEX idx_user_role_id ON user(role_id);

-- 3.2 订单表索引
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_order_status ON orders(order_status);
CREATE INDEX idx_orders_create_time ON orders(create_time);
CREATE INDEX idx_orders_user_status ON orders(user_id, order_status);

-- 3.3 订单详情表索引
CREATE INDEX idx_order_item_order_id ON order_item(order_id);
CREATE INDEX idx_order_item_product ON order_item(product_id, product_type);

-- 3.4 评价表索引
CREATE INDEX idx_comment_product ON comment(product_id, product_type);
CREATE INDEX idx_comment_user_id ON comment(user_id);
CREATE INDEX idx_comment_score ON comment(score);

-- 3.5 收藏表索引
CREATE INDEX idx_favorite_user ON favorite(user_id);
CREATE INDEX idx_favorite_product ON favorite(product_id, product_type);
CREATE UNIQUE INDEX idx_favorite_user_product ON favorite(user_id, product_id, product_type);

-- 3.6 行程表索引
CREATE INDEX idx_travel_plan_user ON travel_plan(user_id);
CREATE INDEX idx_travel_plan_public ON travel_plan(is_public);

-- 3.7 产品表索引
CREATE INDEX idx_attraction_name ON attraction(name);
CREATE INDEX idx_attraction_category ON attraction(category_id);
CREATE INDEX idx_attraction_status ON attraction(status);
CREATE INDEX idx_attraction_score ON attraction(score);

CREATE INDEX idx_travel_route_name ON travel_route(name);
CREATE INDEX idx_travel_route_category ON travel_route(category_id);
CREATE INDEX idx_travel_route_status ON travel_route(status);
CREATE INDEX idx_travel_route_score ON travel_route(score);

CREATE INDEX idx_hotel_name ON hotel(name);
CREATE INDEX idx_hotel_category ON hotel(category_id);
CREATE INDEX idx_hotel_status ON hotel(status);
CREATE INDEX idx_hotel_score ON hotel(score);

CREATE INDEX idx_food_name ON food(name);
CREATE INDEX idx_food_category ON food(category_id);
CREATE INDEX idx_food_status ON food(status);
CREATE INDEX idx_food_score ON food(score);

-- 3.8 酒店房型表索引
CREATE INDEX idx_hotel_room_hotel ON hotel_room(hotel_id);
CREATE INDEX idx_hotel_room_status ON hotel_room(status);

-- 3.9 分类表索引
CREATE INDEX idx_category_type ON category(type);
CREATE INDEX idx_category_parent ON category(parent_id);

-- 3.10 Banner表索引
CREATE INDEX idx_banner_status ON banner(status);
CREATE INDEX idx_banner_sort ON banner(sort_order);

-- 4. 创建全文索引（MySQL 8.0+支持）
ALTER TABLE attraction ADD FULLTEXT INDEX ft_attraction_name_desc (name, description);
ALTER TABLE travel_route ADD FULLTEXT INDEX ft_route_name_desc (name, description);
ALTER TABLE hotel ADD FULLTEXT INDEX ft_hotel_name_desc (name, description);
ALTER TABLE food ADD FULLTEXT INDEX ft_food_name_desc (name, description);

-- 5. 插入初始数据

-- 5.1 插入角色数据
INSERT INTO role (role_id, role_name, description) VALUES
(1, 'admin', '系统管理员'),
(2, 'user', '普通用户');

-- 5.2 插入基础权限数据
INSERT INTO permission (permission_name, permission_code, description) VALUES
('查看首页', 'view_home', '查看系统首页'),
('搜索旅游资源', 'search_resource', '搜索旅游资源'),
('查看详情', 'view_detail', '查看旅游资源详情'),
('添加收藏', 'add_favorite', '添加收藏'),
('创建订单', 'create_order', '创建订单'),
('支付订单', 'pay_order', '支付订单'),
('查看订单', 'view_order', '查看订单'),
('评价订单', 'comment_order', '评价订单'),
('管理用户', 'manage_user', '管理用户信息'),
('管理角色', 'manage_role', '管理角色权限'),
('管理景点', 'manage_attraction', '管理旅游景点'),
('管理线路', 'manage_route', '管理旅游线路'),
('管理酒店', 'manage_hotel', '管理酒店信息'),
('管理美食', 'manage_food', '管理美食信息'),
('管理订单', 'manage_order', '管理所有订单'),
('管理公告', 'manage_announcement', '管理系统公告'),
('管理Banner', 'manage_banner', '管理Banner图片');

-- 5.3 为角色分配权限
-- 管理员权限
INSERT INTO role_permission (role_id, permission_id) 
SELECT 1, permission_id FROM permission;

-- 用户权限
INSERT INTO role_permission (role_id, permission_id) 
SELECT 2, permission_id FROM permission 
WHERE permission_code IN ('view_home', 'search_resource', 'view_detail', 'add_favorite', 
                          'create_order', 'pay_order', 'view_order', 'comment_order');

-- 5.4 插入默认管理员用户（密码：admin123，已加密）
-- 注意：实际应用中请使用更安全的加密方式
INSERT INTO user (username, password, nickname, avatar, status, role_id) VALUES
('admin', '$2a$10$e1a7L1HfKt2u1I7c4L6eK.7O3dR0q3Q8O3q7N0d7K6e7r9U8c9d3s', '系统管理员', 
 'https://via.placeholder.com/150', 1, 1);

-- 5.5 插入默认产品分类
INSERT INTO category (name, type, parent_id, sort_order) VALUES
('自然景观', 'attraction', 0, 1),
('人文景观', 'attraction', 0, 2),
('主题公园', 'attraction', 0, 3),
('一日游', 'route', 0, 1),
('多日游', 'route', 0, 2),
('定制游', 'route', 0, 3),
('五星级酒店', 'hotel', 0, 1),
('四星级酒店', 'hotel', 0, 2),
('特色民宿', 'hotel', 0, 3),
('本地特色菜', 'food', 0, 1),
('小吃', 'food', 0, 2),
('甜品', 'food', 0, 3);

-- 5.6 插入默认Banner数据
INSERT INTO banner (title, image_url, link_url, sort_order, status) VALUES
('桂林山水甲天下', 'https://via.placeholder.com/1200x400', '/attraction', 1, 1),
('漓江风光', 'https://via.placeholder.com/1200x400', '/route?theme=lijiang', 2, 1),
('阳朔西街', 'https://via.placeholder.com/1200x400', '/attraction?area=yangshuo', 3, 1);

-- 5.7 插入默认公告
INSERT INTO announcement (title, content, status) VALUES
('欢迎使用桂林旅游门户网', '尊敬的用户，欢迎您使用桂林旅游门户网，祝您旅途愉快！', 1),
('网站使用须知', '为了给您提供更好的服务，请遵守网站相关规定，文明旅游，理性消费。', 1);

-- 6. 创建视图（方便查询）

-- 6.1 创建用户订单统计视图
CREATE VIEW v_user_order_stats AS
SELECT 
    u.user_id,
    u.username,
    u.nickname,
    COUNT(o.order_id) AS total_orders,
    SUM(CASE WHEN o.order_status = 1 THEN 1 ELSE 0 END) AS pending_orders,
    SUM(CASE WHEN o.order_status = 2 THEN 1 ELSE 0 END) AS completed_orders,
    SUM(CASE WHEN o.order_status = 3 THEN 1 ELSE 0 END) AS cancelled_orders,
    SUM(o.actual_amount) AS total_spent
FROM user u
LEFT JOIN orders o ON u.user_id = o.user_id AND o.delete_flag = 0
GROUP BY u.user_id, u.username, u.nickname;

-- 6.2 创建产品评价统计视图
CREATE VIEW v_product_comment_stats AS
SELECT 
    product_id,
    product_type,
    COUNT(comment_id) AS comment_count,
    AVG(score) AS avg_score
FROM comment
WHERE status = 1
GROUP BY product_id, product_type;

-- 7. 创建存储过程（示例）

-- 7.1 产品浏览量增加存储过程
DELIMITER //
CREATE PROCEDURE increment_view_count(IN p_product_id BIGINT, IN p_product_type VARCHAR(20))
BEGIN
    IF p_product_type = 'attraction' THEN
        UPDATE attraction SET view_count = view_count + 1 WHERE attraction_id = p_product_id;
    ELSEIF p_product_type = 'route' THEN
        UPDATE travel_route SET view_count = view_count + 1 WHERE route_id = p_product_id;
    ELSEIF p_product_type = 'hotel' THEN
        UPDATE hotel SET view_count = view_count + 1 WHERE hotel_id = p_product_id;
    ELSEIF p_product_type = 'food' THEN
        UPDATE food SET view_count = view_count + 1 WHERE food_id = p_product_id;
    END IF;
END //
DELIMITER ;

-- 7.2 计算产品平均评分存储过程
DELIMITER //
CREATE PROCEDURE update_product_score(IN p_product_id BIGINT, IN p_product_type VARCHAR(20))
BEGIN
    DECLARE avg_score_val DECIMAL(3,1);
    
    SELECT AVG(score) INTO avg_score_val 
    FROM comment 
    WHERE product_id = p_product_id AND product_type = p_product_type AND status = 1;
    
    IF avg_score_val IS NULL THEN
        SET avg_score_val = 0;
    END IF;
    
    IF p_product_type = 'attraction' THEN
        UPDATE attraction SET score = avg_score_val WHERE attraction_id = p_product_id;
    ELSEIF p_product_type = 'route' THEN
        UPDATE travel_route SET score = avg_score_val WHERE route_id = p_product_id;
    ELSEIF p_product_type = 'hotel' THEN
        UPDATE hotel SET score = avg_score_val WHERE hotel_id = p_product_id;
    ELSEIF p_product_type = 'food' THEN
        UPDATE food SET score = avg_score_val WHERE food_id = p_product_id;
    END IF;
END //
DELIMITER ;

-- 8. 设置权限（示例）
-- 实际部署时根据需要设置数据库用户权限
-- CREATE USER 'travel_user'@'%' IDENTIFIED BY 'password';
-- GRANT ALL PRIVILEGES ON gl_travel_portal.* TO 'travel_user'@'%';
-- FLUSH PRIVILEGES;

-- 数据库初始化完成
SELECT '桂林旅游门户网站数据库初始化完成' AS 'Status';