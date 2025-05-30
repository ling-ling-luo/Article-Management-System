# 文章管理系统前端

这是一个基于Vue 3和Element Plus开发的文章管理系统前端项目。

## 功能特性

- 文章列表显示
- 文章分类管理
- 添加新文章
- 文章搜索
- 分页显示

## 技术栈

- Vue 3 (组合式API)
- Vue Router
- Element Plus
- Axios

## 项目安装

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

## 项目结构

```
src/
  ├── assets/       # 静态资源
  ├── components/   # 组件
  ├── router/       # 路由配置
  ├── views/        # 页面视图
  ├── App.vue       # 根组件
  └── main.js       # 入口文件
```

## 后端接口

项目使用SpringBoot后端提供的API：

- `GET /article/getAll` - 获取所有文章
- `GET /article/search` - 根据分类和状态搜索文章
- `POST /article/add` - 新增文章

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).
