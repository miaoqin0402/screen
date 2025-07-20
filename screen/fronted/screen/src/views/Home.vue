<!-- src/views/Home.vue -->
<template>
  <div class="min-h-screen bg-gray-50">
    <div class="container mx-auto px-4 py-8">
      <!-- 顶部标题区域 -->
      <el-card class="mb-8">
        <div class="flex items-center">
        </div>
      </el-card>

      <!-- 三列布局 - 确保水平排列 -->
      <div class="flex flex-col lg:flex-row gap-6">
        <!-- 左侧部门进度卡片 -->
        <div class="w-full lg:w-1/4" style="width:20%; float: left; margin-left:2%">
          <LeftPanel />
        </div>
        
        <!-- 中间本周计划 -->
        <div class="w-full lg:w-2/4" style="width:50%; display: inline-block; margin-left:1%; margin-right:1%">
          <CenterPanel />
        </div>
        
        <!-- 右侧逾期行动 -->
        <div class="w-full lg:w-1/4" style="width:25%; display: inline-block">
          <RightPanel />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LeftPanel from '@/components/LeftPanel.vue';
import CenterPanel from '@/components/CenterPanel.vue';
import RightPanel from '@/components/RightPanel.vue';

export default {
  name: 'HomeView',
  components: {
    LeftPanel,
    CenterPanel,
    RightPanel
  }
};
</script>

<style scoped>
.container {
  max-width: 1600px;
}
</style>