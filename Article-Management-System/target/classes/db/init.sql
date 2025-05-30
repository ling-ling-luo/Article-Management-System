-- 创建数据库
CREATE DATABASE IF NOT EXISTS Article CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE Article;

-- 清空表并重置自增主键，避免外键约束问题
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE articles;
TRUNCATE TABLE categories;
SET FOREIGN_KEY_CHECKS = 1;

-- 创建分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建文章表
CREATE TABLE IF NOT EXISTS articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT,
    author VARCHAR(50),
    category_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'draft',
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入初始分类数据
INSERT INTO categories (name, description) VALUES 
('技术', '技术相关文章'),
('生活', '生活相关文章'),
('旅游', '旅游相关文章'),
('美食', '美食相关文章');

-- 插入初始文章数据
INSERT INTO articles (title, content, author, category_id, status, publish_time) VALUES 
('Java编程入门', 'Java是一种广泛使用的计算机编程语言，拥有跨平台、面向对象、泛型编程的特性...', '张三', 1, 'published', '2024-05-20 10:00:00'),
('Spring Boot实战', 'Spring Boot是由Pivotal团队提供的全新框架，其设计目的是用来简化Spring应用的初始搭建以及开发过程...', '李四', 1, 'published', '2024-05-21 14:30:00'),
('周末旅行计划', '周末可以去附近的山区游玩，感受大自然的美妙...', '王五', 3, 'draft', '2024-05-22 09:15:00'),
('家常菜谱分享', '今天分享一道简单易做的红烧肉做法...', '赵六', 4, 'published', '2024-05-23 18:00:00'),
('工作与生活的平衡', '如何在忙碌的工作中找到生活的平衡点...', '孙七', 2, 'draft', '2024-05-24 20:45:00');

-- 插入带有发表时间的文章数据
INSERT INTO articles (title, content, author, category_id, status, publish_time) VALUES 
('AI技术前沿', '人工智能正在改变世界...', '小明', 1, 'published', '2024-05-27 09:00:00'),
('健康生活指南', '每天锻炼有助于身心健康...', '小红', 2, 'published', '2024-05-26 15:30:00'),
('美食新发现', '这家餐厅的菜品非常有特色...', '小刚', 4, 'draft', '2024-05-25 18:45:00');