<!-- src/components/LeftPanel.vue -->
<template>
  <el-card class="h-full">
    <template #header>
      <div class="text-xl font-bold text-gray-800 text-center">部门销售进度</div>
    </template>
    
    <div v-if="loading" class="flex justify-center items-center h-64">
      <el-icon :size="30" class="is-loading"><Loading /></el-icon>
    </div>
    
    <div v-else class="grid grid-cols-2 gap-4">
      <div v-for="(department, index) in departments" :key="index" class="flex flex-col items-center p-2">
        
        
        <!-- 进度条容器 -->
        <div class="chart-container">
          <div :id="'chart-'+index" class="chart"></div>
        </div>

        <!-- 部门名称 - 在进度条下方 -->
        <div class="text-center font-medium mb-2 truncate w-full" style="text-align: center">
          {{ department.departmentName }}
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import axios from 'axios';
import * as echarts from 'echarts';
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { Loading } from '@element-plus/icons-vue';

export default {
  name: 'LeftPanel',
  components: {
    Loading
  },
  setup() {
    const departments = ref([]);
    const loading = ref(true);
    const charts = ref([]);

    const getColor = (rate) => {
      if (rate > 15) return '#36D88E'; // 绿色
      if (rate > 10) return '#4A90E2'; // 蓝色
      if (rate > 5) return '#FFC107'; // 黄色
      return '#FF6B6B'; // 红色
    };

    const renderChart = (index) => {
      const department = departments.value[index];
      const chartDom = document.getElementById('chart-'+index);
      if (!chartDom) return;
      
      const chart = echarts.init(chartDom);
      charts.value[index] = chart;
      
      const color = getColor(department.achievementRate);
      
      // 优化图表配置
      const option = {
        tooltip: {
          formatter: '{c}%'
        },
        series: [{
          type: 'gauge',
          radius: '90%',
          center: ['50%', '50%'],
          startAngle: 225,
          endAngle: -45,
          min: 0,
          max: 100,
          splitNumber: 0, // 移除刻度
          axisLine: {
            lineStyle: {
              width: 12,
              color: [
                [department.achievementRate / 100, color],
                [1, '#f0f2f5']
              ]
            }
          },
          pointer: {
            show: false // 移除指针
          },
          axisTick: {
            show: false // 隐藏刻度线
          },
          splitLine: {
            show: false // 隐藏分割线
          },
          axisLabel: {
            show: false // 隐藏刻度标签
          },
          detail: {
            valueAnimation: true,
            formatter: function(value) {
              return value.toFixed(2) + '%';
            },
            fontSize: 16,
            fontWeight: 'bold',
            color: color,
            offsetCenter: [0, '0'] // 居中显示百分比
          },
          data: [{
            value: department.achievementRate
          }]
        }]
      };
      
      chart.setOption(option);
    };

    const resizeCharts = () => {
      charts.value.forEach(chart => chart && chart.resize());
    };

    onMounted(async () => {
      window.addEventListener('resize', resizeCharts);
      
      try {
        loading.value = true;
        const response = await axios.get('/api/departments');
        departments.value = response.data;
        
        // 等待DOM更新
        setTimeout(() => {
          departments.value.forEach((_, index) => {
            renderChart(index);
          });
        }, 0);
      } catch (error) {
        console.error('获取部门数据失败:', error);
      } finally {
        loading.value = false;
      }
    });

    onBeforeUnmount(() => {
      window.removeEventListener('resize', resizeCharts);
      charts.value.forEach(chart => chart && chart.dispose());
    });

    return {
      departments,
      loading,
      getColor
    };
  }
};
</script>

<style scoped>
/* 整个卡片内部无边界线 */
.el-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 部门项目样式 */
.flex-col {
  border: none !important;
  box-shadow: none !important;
}

/* 图表容器 */
.chart-container {
  position: relative;
  width: 120px;
  height: 120px;
  margin: 0 auto;
}

.chart {
  width: 100%;
  height: 100%;
}

/* 网格布局 - 两列 */
.grid-cols-2 {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
}


</style>