<template>
  <div class="article-container">
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
        <div class="article-header">
          <h2>文章管理</h2>
          <el-button type="primary" @click="showAddDialog">添加文章</el-button>
        </div>
        <div class="filter-bar">
          <el-form :inline="true" :model="searchForm" class="filter-form">
            <el-form-item label="分类">
              <el-select v-model="searchForm.category" placeholder="选择分类">
                <el-option label="全部" value=""></el-option>
                <el-option label="时事" value="时事"></el-option>
                <el-option label="篮球" value="篮球"></el-option>
                <el-option label="旅游" value="旅游"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="searchForm.state" placeholder="选择状态">
                <el-option label="全部" value=""></el-option>
                <el-option label="已发布" value="已发布"></el-option>
                <el-option label="草稿" value="草稿"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchArticles">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table :data="articleList" border style="width: 100%">
          <el-table-column prop="title" label="文章标题" width="250"></el-table-column>
          <el-table-column prop="category" label="分类" width="100"></el-table-column>
          <el-table-column prop="time" label="发表时间" width="150"></el-table-column>
          <el-table-column prop="state" label="状态" width="100"></el-table-column>
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
        <div class="pagination">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="10"
            @current-change="handleCurrentChange"
          ></el-pagination>
          <div class="pagination-info">
            Total {{ total }}
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加文章对话框 -->
    <el-dialog v-model="dialogVisible" title="添加文章" width="50%">
      <el-form :model="articleForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="articleForm.title" placeholder="请输入文章标题"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="articleForm.category" placeholder="请选择分类">
            <el-option label="时事" value="时事"></el-option>
            <el-option label="篮球" value="篮球"></el-option>
            <el-option label="旅游" value="旅游"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="articleForm.state" placeholder="请选择状态">
            <el-option label="已发布" value="已发布"></el-option>
            <el-option label="草稿" value="草稿"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addArticle">确定</el-button>
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
  name: 'ArticleList',
  components: {
    Document,
    Folder,
    Edit,
    Delete
  },
  setup() {
    const activeIndex = ref('/')
    const articleList = ref([])
    const total = ref(0)
    const dialogVisible = ref(false)
    const currentPage = ref(1)
    
    const searchForm = reactive({
      category: '',
      state: ''
    })
    
    const articleForm = reactive({
      title: '',
      category: '时事',
      state: '已发布',
      time: new Date().toISOString().split('T')[0]
    })
    
    // 获取所有文章
    const getAllArticles = async () => {
      try {
        const response = await axios.get('/article/getAll')
        articleList.value = response.data
        total.value = response.data.length
      } catch (error) {
        console.error('获取文章列表失败:', error)
        ElMessage.error('获取文章列表失败')
      }
    }
    
    // 搜索文章
    const searchArticles = async () => {
      // 如果没有选择分类和状态，则获取所有文章
      if (!searchForm.category && !searchForm.state) {
        getAllArticles()
        return
      }
      
      try {
        const response = await axios.get('/article/search', {
          params: {
            category: searchForm.category || '',
            state: searchForm.state || ''
          }
        })
        articleList.value = response.data
        total.value = response.data.length
      } catch (error) {
        console.error('搜索文章失败:', error)
        ElMessage.error('搜索文章失败')
      }
    }
    
    // 添加文章
    const addArticle = async () => {
      if (!articleForm.title || !articleForm.category || !articleForm.state) {
        ElMessage.warning('请填写完整的文章信息')
        return
      }
      
      try {
        // 设置当前日期
        articleForm.time = new Date().toISOString().split('T')[0]
        
        const response = await axios.post('/article/add', articleForm)
        if (response.data) {
          ElMessage.success('添加文章成功')
          dialogVisible.value = false
          getAllArticles()
          
          // 重置表单
          articleForm.title = ''
          articleForm.category = '时事'
          articleForm.state = '已发布'
        }
      } catch (error) {
        console.error('添加文章失败:', error)
        ElMessage.error('添加文章失败')
      }
    }
    
    // 删除文章（模拟，因为后端没有提供删除接口）
    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除这篇文章吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 模拟删除成功
        const index = articleList.value.findIndex(item => item.title === row.title)
        if (index !== -1) {
          articleList.value.splice(index, 1)
          total.value--
          ElMessage.success('删除成功')
        }
      }).catch(() => {
        // 用户取消删除
      })
    }
    
    // 分页
    const handleCurrentChange = (page) => {
      currentPage.value = page
      // 因为是前端分页，这里不需要重新请求数据
    }
    
    // 显示添加对话框
    const showAddDialog = () => {
      dialogVisible.value = true
    }
    
    // 页面加载时获取文章列表
    onMounted(() => {
      getAllArticles()
    })
    
    return {
      activeIndex,
      articleList,
      total,
      searchForm,
      articleForm,
      dialogVisible,
      currentPage,
      searchArticles,
      addArticle,
      handleDelete,
      handleCurrentChange,
      showAddDialog
    }
  }
}
</script>

<style scoped>
.article-container {
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

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 