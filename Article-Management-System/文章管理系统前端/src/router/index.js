import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/ArticleList.vue'),
    meta: { title: '文章管理' }
  },
  {
    path: '/categories',
    name: 'Categories',
    component: () => import('../views/CategoryManage.vue'),
    meta: { title: '分类管理' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫，设置页面标题
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '文章管理系统'
  next()
})

export default router 