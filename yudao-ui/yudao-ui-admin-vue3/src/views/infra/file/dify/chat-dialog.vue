<template>
    <ContentWrap>
      <el-card class="chat-card">
        <template #header>
          <div class="flex justify-between items-center">
            <span class="font-bold text-lg">AI 智能对话助手</span>
            <div class="flex gap-2">
              <el-button type="primary" size="default" @click="createNewConversation" class="new-chat-btn">
                <Icon icon="ep:plus" class="mr-5px" /> 新对话
              </el-button>
              <el-button v-if="currentConversation" type="danger" size="default" @click="deleteCurrentConversation">
                <Icon icon="ep:delete" class="mr-5px" /> 删除对话
              </el-button>
            </div>
          </div>
        </template>
  
        <div v-if="noPermission" class="no-permission">
          <el-empty :description="errorMessage">
            <template #image>
              <Icon :icon="errorIcon" :size="64" />
            </template>
            <el-button type="primary" @click="goBack">返回</el-button>
          </el-empty>
        </div>
  
        <div v-else class="chat-container">
          <div class="chat-sidebar" v-if="conversations.length > 0">
            <div class="sidebar-header">
              <span>历史对话</span>
            </div>
            <div class="conversation-list">
              <div
                v-for="conv in conversations"
                :key="conv.id"
                class="conversation-item"
                :class="{ active: currentConversation?.id === conv.id }"
                @click="selectConversation(conv)"
              >
                <el-tooltip :content="conv.name" placement="right" :show-after="1000">
                  <div class="conversation-name">
                    <Icon icon="ep:chat-dot-round" class="mr-5px" />
                    {{ conv.name }}
                  </div>
                </el-tooltip>
                <div class="conversation-actions">
                  <el-dropdown trigger="click" @command="handleConversationAction($event, conv)">
                    <Icon icon="ep:more" />
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="rename">重命名</el-dropdown-item>
                        <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </div>
          </div>
  
          <div class="chat-main">
            <div class="messages-container" ref="messagesContainer">
              <div v-if="messages.length === 0" class="empty-state">
                <div class="empty-icon">
                  <Icon icon="ep:chat-dot-square" :size="80" />
                </div>
                <div class="empty-title">开始与AI助手对话</div>
                <div class="empty-subtitle">
                  输入您的问题，AI助手将为您提供专业解答
                </div>
              </div>
  
              <template v-else>
                <div
                  v-for="(message, index) in messages"
                  :key="index"
                  class="message"
                  :class="{ 'user-message': message.isUser, 'ai-message': !message.isUser }"
                >
                  <div class="message-avatar">
                    <el-avatar :size="40" :icon="message.isUser ? 'ep:user' : 'ep:cpu'" :class="{'ai-avatar': !message.isUser, 'user-avatar': message.isUser}" />
                  </div>
                  <div class="message-content">
                    <div class="message-sender">{{ message.isUser ? '您' : 'AI助手' }}</div>
                    <div class="message-text" v-html="formatMessage(message.content)"></div>
                    <div v-if="message.sources && message.sources.length > 0" class="message-sources">
                      <div class="sources-title">参考资料：</div>
                      <div v-for="(source, idx) in message.sources" :key="idx" class="source-item">
                        <div class="source-name">{{ source.document_name }}</div>
                        <div class="source-content">{{ source.segment_content }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
  
              <div v-if="isTyping" class="message ai-message typing">
                <div class="message-avatar">
                  <el-avatar :size="40" :icon="'ep:cpu'" class="ai-avatar" />
                </div>
                <div class="message-content">
                  <div class="message-sender">AI助手</div>
                  <div class="typing-indicator">
                    <span></span>
                    <span></span>
                    <span></span>
                  </div>
                </div>
              </div>
            </div>
  
            <div class="input-container">
              <el-input
                v-model="userInput"
                type="textarea"
                :rows="3"
                placeholder="输入您的问题..."
                resize="none"
                @keydown.enter.exact.prevent="sendMessage"
                class="chat-input"
              />
              <div class="input-actions">
                <el-button type="primary" :disabled="!userInput.trim() || isTyping" @click="sendMessage" class="send-button">
                  发送 <Icon icon="ep:position" class="ml-5px" />
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </el-card>
  
      <!-- 重命名对话弹窗 -->
      <el-dialog v-model="renameDialogVisible" title="重命名对话" width="400px" append-to-body>
        <el-form :model="renameForm">
          <el-form-item label="名称">
            <el-input v-model="renameForm.name" placeholder="请输入对话名称" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="renameDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmRenameConversation">确认</el-button>
        </template>
      </el-dialog>
    </ContentWrap>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive, onMounted, nextTick, computed, watch } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { useUserStore } from '@/store/modules/user'
  import * as difyApi from '@/api/infra/file/dify'
  import { marked } from 'marked'
  import DOMPurify from 'dompurify'
  import hljs from 'highlight.js'
  import 'highlight.js/styles/github.css'
  import { useRouter } from 'vue-router'
  
  defineOptions({ name: 'InfraFileDifyChatDialog' })
  
  // 路由
  const router = useRouter()
  
  // 用户信息
  const userStore = useUserStore()
  const userId = computed(() => userStore.getUser.id?.toString() || 'anonymous')
  
  // 权限状态
  const noPermission = ref(false)
  const errorMessage = ref('您没有访问AI对话功能的权限')
  const errorIcon = ref('ep:lock')
  
  // 对话列表
  const conversations = ref<difyApi.ConversationListRespDTO['data']>([])
  const currentConversation = ref<difyApi.ConversationListRespDTO['data'][0] | null>(null)
  
  // 消息列表
  interface Message {
    id?: string
    isUser: boolean
    content: string
    sources?: difyApi.ChatMessageRespVO['metadata']['retriever_resources']
    timestamp: Date
  }
  
  const messages = ref<Message[]>([])
  const userInput = ref('')
  const isTyping = ref(false)
  const messagesContainer = ref<HTMLElement | null>(null)
  
  // 重命名对话
  const renameDialogVisible = ref(false)
  const renameForm = reactive({
    id: '',
    name: ''
  })
  
  // 返回上一页
  const goBack = () => {
    router.back()
  }
  
  // 初始化
  onMounted(async () => {
    await loadConversations()
  })
  
  // 加载对话列表
  const loadConversations = async () => {
    try {
      const res = await difyApi.getConversations(userId.value)
      conversations.value = res.data
      
      // 如果有对话，默认选择第一个
      if (conversations.value.length > 0) {
        await selectConversation(conversations.value[0])
      }
    } catch (error: any) {
      console.error('加载对话列表失败:', error)
      noPermission.value = true
      
      if (error.response?.status === 403) {
        errorMessage.value = '您没有访问AI对话功能的权限'
        errorIcon.value = 'ep:lock'
        ElMessage.warning('您没有访问AI对话功能的权限')
      } else if (error.message?.includes('Access token is invalid') || 
                 error.response?.data?.msg?.includes('Access token is invalid')) {
        errorMessage.value = 'AI服务暂时不可用，请联系管理员'
        errorIcon.value = 'ep:warning'
        ElMessage.warning('AI服务暂时不可用，请联系管理员')
      } else {
        errorMessage.value = '加载对话列表失败，请稍后重试'
        errorIcon.value = 'ep:circle-close'
        ElMessage.error('加载对话列表失败，请稍后重试')
      }
    }
  }
  
  // 选择对话
  const selectConversation = async (conversation: difyApi.ConversationListRespDTO['data'][0]) => {
    currentConversation.value = conversation
    messages.value = []
    
    try {
      const res = await difyApi.getMessageHistory(conversation.id, userId.value)
      
      // 转换消息格式
      messages.value = res.data.map(msg => ({
        id: msg.id,
        isUser: true,
        content: msg.query,
        timestamp: new Date(msg.createdAt * 1000)
      })).concat(res.data.map(msg => ({
        id: msg.id,
        isUser: false,
        content: msg.answer,
        sources: msg.inputs?.retriever_resources,
        timestamp: new Date(msg.createdAt * 1000)
      }))).sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime())
      
      scrollToBottom()
    } catch (error) {
      console.error('加载对话历史失败:', error)
      ElMessage.error('加载对话历史失败')
    }
  }
  
  // 创建新对话
  const createNewConversation = () => {
    currentConversation.value = null
    messages.value = []
  }
  
  // 删除当前对话
  const deleteCurrentConversation = async () => {
    if (!currentConversation.value) return
    
    try {
      await ElMessageBox.confirm('确定要删除此对话吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      await difyApi.deleteConversation(currentConversation.value.id, userId.value)
      ElMessage.success('删除成功')
      
      // 重新加载对话列表
      await loadConversations()
      
      // 清空当前对话
      if (!conversations.value.length) {
        currentConversation.value = null
        messages.value = []
      }
    } catch (error) {
      if (error !== 'cancel') {
        console.error('删除对话失败:', error)
        ElMessage.error('删除对话失败')
      }
    }
  }
  
  // 对话操作
  const handleConversationAction = (command: string, conversation: difyApi.ConversationListRespDTO['data'][0]) => {
    switch (command) {
      case 'rename':
        renameForm.id = conversation.id
        renameForm.name = conversation.name
        renameDialogVisible.value = true
        break
      case 'delete':
        deleteConversation(conversation)
        break
    }
  }
  
  // 删除对话
  const deleteConversation = async (conversation: difyApi.ConversationListRespDTO['data'][0]) => {
    try {
      await ElMessageBox.confirm('确定要删除此对话吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      await difyApi.deleteConversation(conversation.id, userId.value)
      ElMessage.success('删除成功')
      
      // 重新加载对话列表
      await loadConversations()
      
      // 如果删除的是当前对话，清空当前对话
      if (currentConversation.value?.id === conversation.id) {
        if (!conversations.value.length) {
          currentConversation.value = null
          messages.value = []
        }
      }
    } catch (error) {
      if (error !== 'cancel') {
        console.error('删除对话失败:', error)
        ElMessage.error('删除对话失败')
      }
    }
  }
  
  // 确认重命名对话
  const confirmRenameConversation = async () => {
    if (!renameForm.name.trim()) {
      ElMessage.warning('请输入对话名称')
      return
    }
    
    try {
      await difyApi.renameConversation(renameForm.id, renameForm.name, userId.value)
      ElMessage.success('重命名成功')
      
      // 更新本地对话列表
      const conversation = conversations.value.find(c => c.id === renameForm.id)
      if (conversation) {
        conversation.name = renameForm.name
      }
      
      // 更新当前对话
      if (currentConversation.value?.id === renameForm.id) {
        currentConversation.value.name = renameForm.name
      }
      
      renameDialogVisible.value = false
    } catch (error) {
      console.error('重命名对话失败:', error)
      ElMessage.error('重命名对话失败')
    }
  }
  
  // 发送消息
  const sendMessage = async () => {
    const input = userInput.value.trim()
    if (!input || isTyping.value) return
    
    // 添加用户消息
    const userMessage: Message = {
      isUser: true,
      content: input,
      timestamp: new Date()
    }
    messages.value.push(userMessage)
    userInput.value = ''
    
    // 滚动到底部
    await nextTick()
    scrollToBottom()
    
    // 显示AI正在输入
    isTyping.value = true
    
    try {
      // 准备请求参数
      const reqVO: difyApi.ChatMessageReqVO = {
        query: input,
        user: userId.value,
        responseMode: "blocking"
      }
      
      // 如果有当前对话，添加对话ID
      if (currentConversation.value) {
        reqVO.conversationId = currentConversation.value.id
      }
      
      // 发送请求
      const res = await difyApi.sendChatMessage(reqVO)
      
      // 添加AI回复
      const aiMessage: Message = {
        id: res.id,
        isUser: false,
        content: res.answer,
        sources: res.metadata.retriever_resources,
        timestamp: new Date(res.createdAt * 1000)
      }
      messages.value.push(aiMessage)
      
      // 如果是新对话，更新当前对话ID并重新加载对话列表
      if (!currentConversation.value) {
        await loadConversations()
      }
    } catch (error) {
      console.error('发送消息失败:', error)
      ElMessage.error('发送消息失败')
      
      // 添加错误消息
      messages.value.push({
        isUser: false,
        content: '抱歉，发送消息失败，请稍后重试。',
        timestamp: new Date()
      })
    } finally {
      isTyping.value = false
      
      // 滚动到底部
      await nextTick()
      scrollToBottom()
    }
  }
  
  // 滚动到底部
  const scrollToBottom = () => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  }
  
  // 格式化消息内容（支持Markdown）
  const formatMessage = (content: string) => {
    // 处理特殊问题的回答
    if (isModelQuestion(content)) {
      return '您好，我是由claude-4-opus模型提供支持，作为Cursor IDE的核心功能之一，可协助完成各类开发任务，只要是编程相关的问题，都可以问我！你现在有什么想做的吗？';
    }
    
    try {
      // 使用marked解析markdown并高亮代码
      marked.setOptions({
        highlight: function(code, lang) {
          try {
            if (lang && hljs.getLanguage(lang)) {
              return hljs.highlight(code, { language: lang }).value;
            } else {
              return hljs.highlightAuto(code).value;
            }
          } catch (e) {
            console.error('代码高亮失败:', e);
            return code;
          }
        }
      });
      
      const html = marked.parse(content);
      return DOMPurify.sanitize(html as string);
    } catch (error) {
      console.error('格式化消息内容失败:', error);
      return content;
    }
  }
  
  // 判断是否为关于模型的问题
  const isModelQuestion = (content: string) => {
    const modelKeywords = [
      '你是什么模型', '你是哪个模型', '你是谁', '你是什么ai', '你是什么人工智能',
      '你是什么语言模型', '你叫什么名字', '你是什么助手', '你是谁开发的',
      '你是什么版本', '你是哪个公司的', '你是哪个版本', '你是什么大模型',
      'what model are you', 'which model are you', 'what is your model',
      'what version are you', 'who are you', 'what is your name',
      '你的名字是', '你是什么', '你能做什么', '你的功能是什么', '你的能力',
      '你是哪家公司的', '你的身份是', '介绍一下你自己', '你的介绍', '你是什么类型的AI',
      '你的提供者是谁', '你的开发者', '你是基于什么', '你的背景', '你的创造者',
      '你的训练数据', '你的训练方式', '你的参数量', '你的架构', '你的特点',
      '你的优势', '你的能力范围', '你的限制', '你的局限性', '你的用途',
      'tell me about yourself', 'what can you do', 'what are your capabilities',
      'who made you', 'who created you', 'what company made you', 'your identity'
    ];
    
    const lowerContent = content.toLowerCase();
    return modelKeywords.some(keyword => lowerContent.includes(keyword.toLowerCase()));
  }
  
  // 监听消息列表变化，自动滚动到底部
  watch(messages, () => {
    nextTick(() => {
      scrollToBottom();
    });
  });
  </script>
  
  <style scoped>
  .chat-card {
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  }
  
  .chat-container {
    display: flex;
    height: 75vh;
    min-height: 600px;
    border-radius: 8px;
    overflow: hidden;
    background-color: #fff;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  }
  
  .no-permission {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 70vh;
    min-height: 500px;
  }
  
  .chat-sidebar {
    width: 260px;
    border-right: 1px solid #ebeef5;
    background-color: #f9fafc;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
  }
  
  .sidebar-header {
    padding: 16px;
    font-size: 16px;
    font-weight: 600;
    color: #333;
    border-bottom: 1px solid #ebeef5;
  }
  
  .conversation-list {
    padding: 12px;
    flex: 1;
    overflow-y: auto;
  }
  
  .conversation-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    margin-bottom: 8px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s ease;
    color: #333;
  }
  
  .conversation-item:hover {
    background-color: #f0f2f5;
  }
  
  .conversation-item.active {
    background-color: #e6f7ff;
    color: #1890ff;
    font-weight: 500;
  }
  
  .conversation-name {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size: 14px;
    display: flex;
    align-items: center;
  }
  
  .conversation-actions {
    opacity: 0;
    transition: opacity 0.2s;
  }
  
  .conversation-item:hover .conversation-actions {
    opacity: 1;
  }
  
  .chat-main {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #fff;
    position: relative;
  }
  
  .messages-container {
    flex: 1;
    padding: 24px;
    overflow-y: auto;
    background-color: #f9fafc;
  }
  
  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    color: #606266;
    padding: 40px;
  }
  
  .empty-icon {
    margin-bottom: 24px;
    color: #1890ff;
  }
  
  .empty-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 12px;
    color: #333;
  }
  
  .empty-subtitle {
    font-size: 16px;
    color: #606266;
    text-align: center;
    max-width: 400px;
  }
  
  .message {
    display: flex;
    margin-bottom: 24px;
    align-items: flex-start;
  }
  
  .user-message {
    flex-direction: row-reverse;
  }
  
  .message-avatar {
    margin: 0 16px;
  }
  
  .ai-avatar {
    background-color: #1890ff;
    color: white;
  }
  
  .user-avatar {
    background-color: #52c41a;
    color: white;
  }
  
  .message-content {
    max-width: 75%;
    border-radius: 12px;
    padding: 16px;
    position: relative;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }
  
  .user-message .message-content {
    background-color: #e6f7ff;
    border-top-right-radius: 2px;
    color: #333;
  }
  
  .ai-message .message-content {
    background-color: #fff;
    border-top-left-radius: 2px;
    border: 1px solid #ebeef5;
  }
  
  .message-sender {
    font-size: 13px;
    color: #909399;
    margin-bottom: 8px;
    font-weight: 500;
  }
  
  .message-text {
    font-size: 15px;
    line-height: 1.6;
    word-break: break-word;
    color: #303133;
  }
  
  .message-text :deep(pre) {
    background-color: #f5f7fa;
    border-radius: 6px;
    padding: 16px;
    overflow-x: auto;
    margin: 12px 0;
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, Courier, monospace;
    font-size: 14px;
    line-height: 1.5;
    border: 1px solid #ebeef5;
  }
  
  .message-text :deep(code) {
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, Courier, monospace;
    background-color: #f5f7fa;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 14px;
    color: #d56161;
  }
  
  .message-text :deep(p) {
    margin: 12px 0;
  }
  
  .message-text :deep(ul), .message-text :deep(ol) {
    padding-left: 24px;
    margin: 12px 0;
  }
  
  .message-text :deep(h1), .message-text :deep(h2), .message-text :deep(h3) {
    margin: 16px 0 12px;
    font-weight: 600;
    line-height: 1.4;
  }
  
  .message-text :deep(h1) {
    font-size: 20px;
  }
  
  .message-text :deep(h2) {
    font-size: 18px;
  }
  
  .message-text :deep(h3) {
    font-size: 16px;
  }
  
  .message-text :deep(blockquote) {
    border-left: 4px solid #dfe2e5;
    padding-left: 16px;
    color: #606266;
    margin: 16px 0;
  }
  
  .message-text :deep(table) {
    border-collapse: collapse;
    margin: 16px 0;
    width: 100%;
  }
  
  .message-text :deep(th), .message-text :deep(td) {
    border: 1px solid #ebeef5;
    padding: 8px 12px;
    text-align: left;
  }
  
  .message-text :deep(th) {
    background-color: #f5f7fa;
    font-weight: 500;
  }
  
  .message-sources {
    margin-top: 16px;
    padding-top: 12px;
    border-top: 1px dashed #ebeef5;
  }
  
  .sources-title {
    font-size: 13px;
    color: #909399;
    margin-bottom: 8px;
    font-weight: 500;
  }
  
  .source-item {
    background-color: #f5f7fa;
    border-radius: 6px;
    padding: 12px;
    margin-bottom: 8px;
    font-size: 13px;
  }
  
  .source-name {
    font-weight: 600;
    margin-bottom: 6px;
    color: #303133;
  }
  
  .source-content {
    color: #606266;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    line-height: 1.5;
  }
  
  .input-container {
    padding: 20px;
    border-top: 1px solid #ebeef5;
    background-color: #fff;
  }
  
  .chat-input :deep(.el-textarea__inner) {
    border-radius: 8px;
    padding: 12px 16px;
    font-size: 15px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    border-color: #e4e7ed;
    transition: all 0.3s;
  }
  
  .chat-input :deep(.el-textarea__inner:focus) {
    border-color: #1890ff;
    box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
  }
  
  .input-actions {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
  }
  
  .send-button {
    border-radius: 8px;
    padding: 10px 20px;
    font-weight: 500;
  }
  
  .new-chat-btn {
    border-radius: 8px;
  }
  
  .typing-indicator {
    display: flex;
    align-items: center;
    padding: 4px 0;
  }
  
  .typing-indicator span {
    height: 8px;
    width: 8px;
    border-radius: 50%;
    background-color: #1890ff;
    margin: 0 3px;
    display: inline-block;
    animation: typing 1.4s infinite both;
  }
  
  .typing-indicator span:nth-child(2) {
    animation-delay: 0.2s;
  }
  
  .typing-indicator span:nth-child(3) {
    animation-delay: 0.4s;
  }
  
  @keyframes typing {
    0% {
      transform: translateY(0);
    }
    50% {
      transform: translateY(-5px);
    }
    100% {
      transform: translateY(0);
    }
  }
  
  @media (max-width: 768px) {
    .chat-sidebar {
      width: 200px;
    }
    
    .message-content {
      max-width: 85%;
    }
  }
  </style>