import { createRouter, createWebHistory } from 'vue-router'
import ArticleManage from '../views/ArticleManage.vue'
import CategoryManage from '../views/CategoryManage.vue'

const routes = [
  { path: '/', redirect: '/article' },
  { path: '/article', component: ArticleManage },
  { path: '/category', component: CategoryManage }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 