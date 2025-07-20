<template>
  <el-card class="h-full">
    <template #header>
      <div class="text-xl font-bold text-gray-800 text-center">
        本周待完成计划（{{ totalPending }}项）
      </div>
    </template>
    
    <div v-if="loading" class="flex justify-center items-center" :style="{ height: tableHeight }">
      <el-icon :size="30" class="is-loading"><Loading /></el-icon>
    </div>
    
    <div v-else-if="error" class="flex justify-center items-center" :style="{ height: tableHeight }">
      <el-empty :description="errorMessage" :image-size="100" />
    </div>
    
    <div v-else-if="plans.length === 0" class="flex justify-center items-center" :style="{ height: tableHeight }">
      <el-empty description="暂无待完成计划" :image-size="100" />
    </div>
    
    <el-table v-else :data="plans" style="width: 100%" :height="tableHeight">
      <el-table-column label="责任人" width="150" align="center">
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
      
      <el-table-column prop="actionPlan" label="行动计划" />
      
      <el-table-column prop="inspector" label="检查人" width="150" align="center" />
      <el-table-column prop="completionDate" label="完成时间" width="150" align="center" />
    </el-table>
  </el-card>
</template>

<script>
import axios from 'axios';
import { Loading } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

export default {
  name: 'CenterPanel',
  components: {
    Loading
  },
  data() {
    return {
      loading: true,
      error: false,
      errorMessage: '',
      totalPending: 0,
      plans: [],
      tableHeight: 'calc(100vh - 300px)',
      baseUrl: 'http://localhost:9090/back/', // 后端服务基础 URL
      defaultAvatar: '/default-avatar.png' // 默认头像路径
    };
  },
  methods: {
    getAvatarUrl(avatarPath) {
      if (avatarPath) {
        return `${this.baseUrl}avatar/${avatarPath}`;
      }
      return this.defaultAvatar;
    }
  },
  async mounted() {
    try {
      const response = await axios.get('/api/plans');
      this.totalPending = response.data.totalPending || 0;
      this.plans = response.data.plans || [];
    } catch (error) {
      console.error('获取计划数据失败:', error);
      this.error = true;
      this.errorMessage = `加载失败: ${error.message}`;
      ElMessage.error('加载计划数据失败');
    } finally {
      this.loading = false;
    }
  }
};
</script>

<style scoped>
:deep(.el-table__row) {
  height: 60px !important;
}
.flex.items-center {
  display: flex;
  align-items: center;
}
</style>