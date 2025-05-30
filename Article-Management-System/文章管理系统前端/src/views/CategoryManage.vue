<template>
  <div class="category-container">
    <div class="header">
      <h1>文章管理系统</h1>
    </div>
    <div class="content">
      <div class="sidebar">
        <el-menu
          :default-active="activeIndex"
          class="menu"
          router
        >
          <el-menu-item index="/">
            <el-icon><document /></el-icon>
            <span>文章管理</span>
          </el-menu-item>
          <el-menu-item index="/categories">
            <el-icon><folder /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
        </el-menu>
      </div>
      <div class="main">
        <div class="category-header">
          <h2>分类管理</h2>
          <el-button type="primary" @click="showAddDialog">添加分类</el-button>
        </div>
        
        <el-table :data="categoryList" border style="width: 100%">
          <el-table-column prop="name" label="分类名称"></el-table-column>
          <el-table-column prop="count" label="文章数量"></el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button type="primary" size="small" circle>
                <el-icon><edit /></el-icon>
              </el-button>
              <el-button type="danger" size="small" circle @click="handleDelete(scope.row)">
                <el-icon><delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    
    <!-- 添加分类对话框 -->
    <el-dialog v-model="dialogVisible" title="添加分类" width="40%">
      <el-form :model="categoryForm" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addCategory">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { Document, Folder, Edit, Delete } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CategoryManage',
  components: {
    Document,
    Folder,
    Edit,
    Delete
  },
  setup() {
    const activeIndex = ref('/categories')
    const categoryList = ref([])
    const dialogVisible = ref(false)
    
    const categoryForm = reactive({
      name: ''
    })
    
    // 模拟获取分类数据
    const fetchCategories = async () => {
      try {
        // 使用新的分类API
        const response = await axios.get('/category/getAll')
        categoryList.value = response.data.map(item => ({
          name: item.name,
          count: 0 // 暂不统计数量
        }))
      } catch (error) {
        console.error('获取分类列表失败:', error)
        ElMessage.error('获取分类列表失败')
        
        // 模拟数据，以防API调用失败
        categoryList.value = [
          { name: '时事', count: 1 },
          { name: '篮球', count: 1 },
          { name: '旅游', count: 1 }
        ]
      }
    }
    
    // 添加分类（使用新API）
    const addCategory = async () => {
      if (!categoryForm.name) {
        ElMessage.warning('请输入分类名称')
        return
      }
      
      // 检查分类是否已存在
      const exists = categoryList.value.some(item => item.name === categoryForm.name)
      if (exists) {
        ElMessage.warning('该分类已存在')
        return
      }
      
      try {
        // 调用添加分类API
        await axios.post('/category/add', {
          name: categoryForm.name
        })
        
        ElMessage.success('添加分类成功')
        dialogVisible.value = false
        categoryForm.name = ''
        
        // 刷新分类列表
        fetchCategories()
      } catch (error) {
        console.error('添加分类失败:', error)
        ElMessage.error('添加分类失败')
      }
    }
    
    // 删除分类（模拟）
    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除该分类吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = categoryList.value.findIndex(item => item.name === row.name)
        if (index !== -1) {
          categoryList.value.splice(index, 1)
          ElMessage.success('删除成功')
        }
      }).catch(() => {
        // 用户取消删除
      })
    }
    
    // 显示添加对话框
    const showAddDialog = () => {
      dialogVisible.value = true
    }
    
    // 页面加载时获取分类列表
    onMounted(() => {
      fetchCategories()
    })
    
    return {
      activeIndex,
      categoryList,
      categoryForm,
      dialogVisible,
      addCategory,
      handleDelete,
      showAddDialog
    }
  }
}
</script>

<style scoped>
.category-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  background-color: #409eff;
  color: white;
  padding: 10px 20px;
  text-align: center;
}

.content {
  display: flex;
  flex: 1;
}

.sidebar {
  width: 200px;
  background-color: #f5f7fa;
  border-right: 1px solid #e6e6e6;
}

.main {
  flex: 1;
  padding: 20px;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style> 