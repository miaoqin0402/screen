<!-- src/App.vue -->
<template>
  <div class="min-h-screen bg-gray-100">
    <header class="bg-blue-600 text-white p-4 flex items-center">
      <img src="@/assets/screen.jpg" alt="Logo" class="h-10 w-10 mr-3">
      <!-- <h1 class="text-xl font-bold">销售管理系统</h1> -->
    </header>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>