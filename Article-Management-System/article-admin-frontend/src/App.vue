<template>
  <el-container style="height: 100vh;">
    <el-aside width="200px" style="background: #f5f6fa;">
      <div style="padding: 20px; font-weight: bold;">文章信息管理</div>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/article">文章管理</el-menu-item>
        <el-menu-item index="/category">分类管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: #fff; text-align: center; font-size: 24px; font-weight: bold;">
        文章管理系统
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
const route = useRoute()
const activeMenu = computed(() => route.path)
</script> 