<template>
  <el-card>
    <div style="margin-bottom: 20px; display: flex; align-items: center;">
      <el-input v-model="searchTitle" placeholder="标题" style="width: 200px; margin-right: 10px;" />
      <el-select v-model="searchCategoryId" placeholder="全部分类" clearable style="width: 160px; margin-right: 10px;">
        <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
      </el-select>
      <el-button type="primary" @click="loadArticles">查询</el-button>
      <el-button type="success" style="margin-left:auto;" @click="openAddDialog">发布文章</el-button>
    </div>
    <el-table :data="articles" style="width: 100%">
      <el-table-column prop="title" label="文章标题" />
      <el-table-column prop="category.name" label="分类" />
      <el-table-column prop="publishTime" label="发表时间" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button
            size="small"
            type="info"
            :icon="View"
            @click="handleView(scope.row)"
            title="查看"
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
    <el-pagination
      v-model:current-page="page"
      :page-size="size"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @current-change="loadArticles"
      @size-change="handleSizeChange"
      :page-sizes="[10, 20, 50, 100]"
      style="margin-top: 20px; text-align: right;"
    />
  </el-card>
  <el-dialog v-model="dialogVisible" :title="dialogMode === 'add' ? '发布文章' : '编辑文章'">
    <el-form :model="form" label-width="80px">
      <el-form-item label="标题">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.categoryId" placeholder="请选择分类">
          <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="内容">
        <el-input v-model="form.content" type="textarea" rows="4" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择状态">
          <el-option label="草稿" value="draft" />
          <el-option label="已发布" value="published" />
        </el-select>
      </el-form-item>
      <el-form-item label="发表时间">
        <el-date-picker v-model="form.publishTime" type="datetime" placeholder="选择发表时间" style="width: 100%;" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
  <el-dialog v-model="detailDialogVisible" title="文章详情" width="600px">
    <div><b>标题：</b>{{ detailData.title }}</div>
    <div><b>分类：</b>{{ detailData.category?.name }}</div>
    <div><b>状态：</b>{{ detailData.status }}</div>
    <div><b>发表时间：</b>{{ detailData.publishTime }}</div>
    <div style="margin-top: 10px;"><b>内容：</b></div>
    <div style="white-space: pre-wrap;">{{ detailData.content }}</div>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchArticles, addArticle, updateArticle, deleteArticle } from '../api/article'
import { fetchCategories } from '../api/category'
import { Edit, Delete, View } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const articles = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const searchTitle = ref('')
const searchCategoryId = ref('')

const categories = ref([])
const dialogVisible = ref(false)
const dialogMode = ref('add') // 'add' or 'edit'
const form = ref({ id: null, title: '', categoryId: '', content: '', status: 'draft', publishTime: '' })
const detailDialogVisible = ref(false)
const detailData = ref({})

function loadArticles() {
  fetchArticles({
    page: page.value - 1,
    size: size.value,
    title: searchTitle.value,
    categoryId: searchCategoryId.value || undefined
  }).then(res => {
    articles.value = res.data.data.content
    total.value = res.data.data.totalElements
  })
}

function loadCategories() {
  fetchCategories().then(res => {
    categories.value = res.data.data
  })
}

function handleSizeChange(val) {
  size.value = val
  loadArticles()
}

function openAddDialog() {
  dialogMode.value = 'add'
  form.value = { id: null, title: '', categoryId: '', content: '', status: 'draft', publishTime: '' }
  dialogVisible.value = true
  loadCategories()
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  form.value = {
    id: row.id,
    title: row.title,
    categoryId: row.category.id,
    content: row.content,
    status: row.status,
    publishTime: row.publishTime
  }
  dialogVisible.value = true
  loadCategories()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定要删除该文章吗？', '提示', { type: 'warning' })
    .then(() => {
      deleteArticle(row.id).then(() => {
        ElMessage.success('删除成功')
        loadArticles()
      })
    })
    .catch(() => {})
}

function handleView(row) {
  detailData.value = row
  detailDialogVisible.value = true
}

function submitForm() {
  const payload = {
    title: form.value.title,
    categoryId: form.value.categoryId,
    content: form.value.content,
    status: form.value.status,
    publishTime: form.value.publishTime
  }
  if (dialogMode.value === 'add') {
    addArticle(payload).then(() => {
      ElMessage.success('添加成功')
      dialogVisible.value = false
      loadArticles()
    })
  } else {
    updateArticle(form.value.id, payload).then(() => {
      ElMessage.success('修改成功')
      dialogVisible.value = false
      loadArticles()
    })
  }
}

onMounted(() => {
  loadCategories()
  loadArticles()
})
</script> 