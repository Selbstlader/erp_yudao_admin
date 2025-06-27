import request from '@/config/axios'
import { PageParam, PageResult } from '@/types/global'

export interface DifyDatasetVO {
  id: string
  name: string
  description?: string
  permission: string
  dataSourceType?: string
  indexingTechnique?: string
  appCount?: number
  documentCount?: number
  wordCount?: number
  createdBy?: string
  createTime?: string
  updatedBy?: string
  updateTime?: string
}

export interface DifyDatasetPageReqVO extends PageParam {
  // 可以添加其他查询条件
}

export interface DifyDatasetCreateReqVO {
  name: string
  description?: string
  permission: string
  indexingTechnique?: string
}

// 获取知识库分页
export const getDifyDatasetPage = (params: DifyDatasetPageReqVO) => {
  return request.get<PageResult<DifyDatasetVO>>({
    url: '/infra/dify-dataset/page',
    params
  })
}

// 创建知识库
export const createDifyDataset = (data: DifyDatasetCreateReqVO) => {
  return request.post<string>({
    url: '/infra/dify-dataset/create',
    data
  })
}

// 删除知识库
export const deleteDifyDataset = (id: string) => {
  return request.delete<boolean>({
    url: '/infra/dify-dataset/delete',
    params: { id }
  })
} 