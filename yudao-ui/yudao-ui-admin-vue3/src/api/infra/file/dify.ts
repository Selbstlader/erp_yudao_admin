import request from '@/config/axios'
import { PageParam, PageResult } from '@/types/global'

export interface FileDifySyncRespVO {
  id: number
  fileId: number
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
}

export interface DifyConfigUpdateReqVO {
  enabled: boolean
  autoSync: boolean
  defaultDatasetId?: string
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