import request from '@/config/axios'
// import { PageParam, PageResult } from '@/api/model/common'

export interface FileDifySyncRespVO {
  id: number
  fileId: number
  fileName: string
  difyDocumentId: string
  syncStatus: number
  syncStatusName: string
  errorMessage: string
  errorCode: string
  datasetId: string
  batchId: string
  retryCount: number
  nextRetryTime: string
  syncTime: string
  createTime: string
}

export interface DifyConfigRespVO {
  enabled: boolean
  autoSync: boolean
  defaultDatasetId: string
  baseUrl: string
  chatApiKey: string
}

export interface DifyConfigUpdateReqVO {
  enabled: boolean
  autoSync: boolean
  defaultDatasetId?: string
  chatApiKey?: string
}

// 聊天消息请求VO
export interface ChatMessageReqVO {
  inputs?: Record<string, any>
  query: string
  responseMode?: string
  user?: string
  conversationId?: string
}

// 聊天消息响应VO
export interface ChatMessageRespVO {
  id: string
  answer: string
  createdAt: number
  conversationId: string
  metadata: {
    retriever_resources?: Array<{
      document_id: string
      document_name: string
      segment_id: string
      segment_position: number
      segment_content: string
      segment_score: number
    }>
  }
}

// 会话列表响应DTO
export interface ConversationListRespDTO {
  data: Array<{
    id: string
    name: string
    inputs: Record<string, any>
    status: string
    createdAt: number
  }>
  has_more: boolean
  limit: number
  total: number
  page: number
}

// 消息历史响应DTO
export interface MessageHistoryRespDTO {
  data: Array<{
    id: string
    conversationId: string
    inputs: Record<string, any>
    query: string
    answer: string
    feedback: string | null
    createdAt: number
  }>
  has_more: boolean
  limit: number
}

// 同步文件到Dify知识库
export const syncFileToDify = (fileId: number, datasetId?: string) => {
  return request.post<FileDifySyncRespVO>({
    url: '/infra/file-dify/sync',
    params: { fileId, datasetId }
  })
}

// 批量同步文件到Dify知识库
export const batchSyncFiles = (fileIds: number[], datasetId?: string) => {
  return request.post<FileDifySyncRespVO[]>({
    url: '/infra/file-dify/batch-sync',
    params: { fileIds, datasetId }
  })
}

// 更新文件同步
export const updateFileSync = (fileId: number, datasetId?: string) => {
  return request.put<FileDifySyncRespVO>({
    url: '/infra/file-dify/update-sync',
    params: { fileId, datasetId }
  })
}

// 删除文件同步
export const deleteFileSync = (fileId: number) => {
  return request.delete<boolean>({
    url: '/infra/file-dify/delete-sync',
    params: { fileId }
  })
}

// 获取文件同步状态
export const getFileSyncStatus = (fileId: number) => {
  return request.get<FileDifySyncRespVO>({
    url: '/infra/file-dify/get-status',
    params: { fileId }
  })
}

// 获取文件同步分页
export const getFileSyncPage = (params: PageParam) => {
  return request.get<PageResult<FileDifySyncRespVO>>({
    url: '/infra/file-dify/page',
    params
  })
}

// 触发文件同步
export const triggerSync = (fileId: number) => {
  return request.post<FileDifySyncRespVO>({
    url: '/infra/file-dify/trigger-sync',
    params: { fileId }
  })
}

// 重试文件同步
export const retrySync = (fileId: number) => {
  return request.post<FileDifySyncRespVO>({
    url: '/infra/file-dify/retry-sync',
    params: { fileId }
  })
}

// 获取Dify配置
export const getDifyConfig = () => {
  return request.get<DifyConfigRespVO>({
    url: '/infra/file-dify/config'
  })
}

// 更新Dify配置
export const updateDifyConfig = (data: DifyConfigUpdateReqVO) => {
  return request.put<boolean>({
    url: '/infra/file-dify/config',
    data
  })
}

// 发送聊天消息（阻塞模式）
export const sendChatMessage = (data: ChatMessageReqVO) => {
  return request.post<ChatMessageRespVO>({
    url: '/infra/dify-chat/message',
    data
  })
}

// 获取会话列表
export const getConversations = (user: string, page: number = 1, limit: number = 10) => {
  return request.get<ConversationListRespDTO>({
    url: '/infra/dify-chat/conversations',
    params: { user, page, limit }
  })
}

// 重命名会话
export const renameConversation = (conversationId: string, name: string, user: string) => {
  return request.put<boolean>({
    url: `/infra/dify-chat/conversations/${conversationId}/rename`,
    params: { name, user }
  })
}

// 删除会话
export const deleteConversation = (conversationId: string, user: string) => {
  return request.delete<boolean>({
    url: `/infra/dify-chat/conversations/${conversationId}`,
    params: { user }
  })
}

// 获取会话消息历史
export const getMessageHistory = (conversationId: string, user: string, firstId?: string, limit: number = 20) => {
  return request.get<MessageHistoryRespDTO>({
    url: '/infra/dify-chat/messages',
    params: { conversationId, user, firstId, limit }
  })
} 