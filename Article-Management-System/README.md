# 文章管理系统

这是一个基于Spring Boot的文章管理系统后端，提供了文章和分类的CRUD操作。

## 技术栈

- Java 8
- Spring Boot 2.7.10
- Spring Data JPA
- MySQL
- Lombok

## 功能特性

- 文章管理：创建、更新、删除、查询文章
- 分类管理：创建、更新、删除、查询分类
- 根据分类查询文章
- 文章搜索功能
- 分页查询支持

## 如何运行

### 数据库配置

1. 确保已安装MySQL，并已启动服务
2. 默认配置：
   - 数据库: articledb
   - 用户名: root
   - 密码: 123456
   - 主机: localhost
   - 端口: 3306

如需更改配置，请修改 `src/main/resources/application-dev.yml` 文件中的数据库连接信息。

### 初始化数据库

方法一：使用脚本初始化
1. 运行项目根目录中的 `init-database.bat` 脚本

方法二：通过应用自动初始化
1. 确保 `application-dev.yml` 中的 `app.db.init.enabled` 设置为 `true`
2. 启动应用，应用会自动创建数据库和表，并插入初始数据

### 启动应用

1. 克隆代码到本地
2. 使用Maven构建项目：`mvn clean install`
3. 运行应用：`mvn spring-boot:run`

应用启动后，可以通过 http://localhost:8080 访问API。

## API接口

### 文章API

- `POST /api/articles` - 创建文章
- `PUT /api/articles/{id}` - 更新文章
- `DELETE /api/articles/{id}` - 删除文章
- `GET /api/articles/{id}` - 获取文章详情
- `GET /api/articles` - 获取所有文章（支持分页）
- `GET /api/articles/category/{categoryId}` - 获取指定分类的文章
- `GET /api/articles/search?keyword=xxx` - 搜索文章

### 分类API

- `POST /api/categories` - 创建分类
- `PUT /api/categories/{id}` - 更新分类
- `DELETE /api/categories/{id}` - 删除分类
- `GET /api/categories/{id}` - 获取分类详情
- `GET /api/categories` - 获取所有分类
- `GET /api/categories/check?name=xxx` - 检查分类名称是否存在