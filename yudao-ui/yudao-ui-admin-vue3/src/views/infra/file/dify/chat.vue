<template>
  <ContentWrap>
    <el-card>
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-bold">AI 智能助手</span>
          <!-- <el-button type="primary" size="small" @click="refreshChat">
            <Icon icon="ep:refresh" class="mr-5px" /> 刷新对话
          </el-button> -->
        </div>
      </template>
      <div class="chat-container">
        
        <iframe
          ref="chatIframe"
          src="https://udify.app/chatbot/wjuJfNA3QAQorgKp"
          style="width: 100%; height: 700px; border: none;"
          frameborder="0"
          allow="microphone"
        ></iframe>
      </div>
    </el-card>
  </ContentWrap>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

defineOptions({ name: 'InfraFileDifyChat' })

const chatIframe = ref<HTMLIFrameElement | null>(null)

// 刷新对话
// const refreshChat = () => {
//   if (chatIframe.value) {
//     chatIframe.value.src = chatIframe.value.src
//   }
// }

onMounted(() => {
  // 页面加载完成后的初始化逻辑
  console.log('AI对话页面已加载')
})
</script>

<style scoped>
.chat-container {
  min-height: 700px;
  background-color: #f5f7fa;
  border-radius: 4px;
  overflow: hidden;
}
</style> 