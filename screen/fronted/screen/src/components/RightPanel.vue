<template>
  <el-card class="h-full">
    <template #header>
      <div class="text-xl font-bold text-gray-800 text-center">
        本月逾期任务（{{ totalOverdueTasks }}项）
      </div>
    </template>
    
    <div v-if="loading" class="flex justify-center items-center" :style="{ height: tableHeight }">
      <el-icon :size="30" class="is-loading"><Loading /></el-icon>
    </div>
    
    <div v-else-if="error" class="flex justify-center items-center" :style="{ height: tableHeight }">
      <el-empty :description="errorMessage" :image-size="100" />
    </div>
    
    <div v-else-if="overdueTasks.length === 0" class="flex justify-center items-center" :style="{ height: tableHeight }">
      <el-empty description="暂无逾期任务" :image-size="100" />
    </div>
    
    <el-table 
      v-else 
      :data="overdueTasks" 
      style="width: 100%" 
      :height="tableHeight"
      :row-class-name="setRowClassName"
    >
      <el-table-column label="责任人" width="180" align="center">
        <template #default="scope">
          <div class="flex items-center justify-start">
            <el-avatar
              :size="36"
              :src="getAvatarUrl(scope.row.avatarUrl)"
              class="mr-2"
              style="margin-right: 12px;"
            />
            <div class="font-medium">{{ scope.row.responsiblePerson }}</div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="检查人" width="120">
        <template #default="scope">
          <div class="font-medium">{{ scope.row.inspector }}</div>
        </template>
      </el-table-column>
      
      <el-table-column label="逾期天数" width="120">
        <template #default="scope">
          <el-tag type="danger" size="small">
            逾期 {{ scope.row.overdueDays }} 天
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="行动计划" prop="actionPlan" min-width="300">
        <template #default="scope">
          <div class="action-plan-text">
            {{ scope.row.actionPlan }}
          </div>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import axios from 'axios';
import { Loading } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

export default {
  name: 'RightPanel',
  components: {
    Loading
  },
  data() {
    return {
      loading: true,
      error: false,
      errorMessage: '',
      totalOverdueTasks: 0,
      overdueTasks: [],
      tableHeight: 'calc(100vh - 300px)',
      baseUrl: 'http://localhost:9090/back/', // 后端服务基础 URL
      defaultAvatar: '/default-avatar.png' // 默认头像路径
    };
  },
  methods: {
    setRowClassName({ rowIndex }) {
      return `custom-row-${rowIndex}`;
    },
    getAvatarUrl(avatarPath) {
      if (avatarPath) {
        return `${this.baseUrl}avatar/${avatarPath}`;
      }
      return this.defaultAvatar;
    }
  },
  async mounted() {
    try {
      const response = await axios.get('/api/overdue-tasks');
      this.totalOverdueTasks = response.data.totalOverdueTasks || 0;
      this.overdueTasks = response.data.tasks || [];
    } catch (error) {
      console.error('获取逾期任务失败:', error);
      this.error = true;
      this.errorMessage = `加载失败: ${error.message}`;
      ElMessage.error('加载逾期任务失败');
    } finally {
      this.loading = false;
    }
  }
};
</script>

<style scoped>
.action-plan-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
  color: #606266;
  padding: 4px 0;
}
:deep(.el-table__row) {
  height: 60px !important;
}
:deep(.el-table__cell) {
  padding: 8px !important;
  vertical-align: middle;
}
.el-tag {
  margin: 0;
}
.flex.items-center {
  display: flex;
  align-items: center;
}
</style>