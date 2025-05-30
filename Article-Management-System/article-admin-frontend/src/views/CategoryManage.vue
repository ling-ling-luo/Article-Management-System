<template>
  <el-card>
    <div style="margin-bottom: 20px; display: flex; align-items: center;">
      <el-button type="primary" @click="loadCategories">刷新</el-button>
      <el-button type="success" style="margin-left:auto;" @click="openAddDialog">新增分类</el-button>
    </div>
    <el-table :data="categories" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button
            size="small"
            type="info"
            :icon="View"
            @click="handleViewArticles(scope.row)"
            title="查看文章"
            circle
            style="margin-right: 8px;"
          />
          <el-button
            size="small"
            type="primary"
            :icon="Edit"
            @click="handleEdit(scope.row)"
            title="编辑"
            circle
          />
          <el-button
            size="small"
            type="danger"
            :icon="Delete"
            @click="handleDelete(scope.row)"
            title="删除"
            circle
            style="margin-left: 8px;"
          />
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog v-model="dialogVisible" :title="dialogMode === 'add' ? '新增分类' : '编辑分类'">
    <el-form :model="form" label-width="80px">
      <el-form-item label="分类名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" rows="3" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
  <el-dialog v-model="articleDialogVisible" :title="`分类【${currentCategory?.name}】下的文章`" width="600px">
    <el-table :data="categoryArticles" style="width: 100%">
      <el-table-column prop="title" label="文章标题" />
      <el-table-column prop="createTime" label="发布时间" />
    </el-table>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchCategories, addCategory, updateCategory, deleteCategory } from '../api/category'
import { fetchArticles } from '../api/article'
import { Edit, Delete, View } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const categories = ref([])
const dialogVisible = ref(false)
const dialogMode = ref('add') // 'add' or 'edit'
const form = ref({ id: null, name: '', description: '' })
const articleDialogVisible = ref(false)
const categoryArticles = ref([])
const currentCategory = ref(null)

function loadCategories() {
  fetchCategories().then(res => {
    categories.value = res.data.data
  })
}

function openAddDialog() {
  dialogMode.value = 'add'
  form.value = { id: null, name: '', description: '' }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  form.value = { id: row.id, name: row.name, description: row.description }
  dialogVisible.value = true
}

function handleDelete(row) {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', { type: 'warning' })
    .then(() => {
      deleteCategory(row.id).then(() => {
        ElMessage.success('删除成功')
        loadCategories()
      })
    })
    .catch(() => {})
}

function submitForm() {
  if (dialogMode.value === 'add') {
    addCategory({
      name: form.value.name,
      description: form.value.description
    }).then(() => {
      ElMessage.success('添加成功')
      dialogVisible.value = false
      loadCategories()
    })
  } else {
    updateCategory(form.value.id, {
      name: form.value.name,
      description: form.value.description
    }).then(() => {
      ElMessage.success('修改成功')
      dialogVisible.value = false
      loadCategories()
    })
  }
}

function handleViewArticles(row) {
  currentCategory.value = row
  fetchArticles({ categoryId: row.id, page: 0, size: 100 }).then(res => {
    categoryArticles.value = res.data.data.content || []
    articleDialogVisible.value = true
  })
}

onMounted(loadCategories)
</script> 