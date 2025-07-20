const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {  // 代理所有以/api开头的请求
        target: 'http://localhost:9090', // 后端地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/back/api' // 重写路径
        }
      }
    },
    port: 8080
  }
})