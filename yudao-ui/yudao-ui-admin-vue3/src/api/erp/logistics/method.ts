import request from '@/config/axios'

export interface LogisticsMethodVO {
  id: number
  providerId: number
  providerName: string
  code: string
  name: string
  estimatedDays: number
  supportedCountries: string
  status: number
  sort: number
  remark: string
  createTime: Date
}

export interface LogisticsMethodPageReqVO extends PageParam {
  name?: string
  code?: string
  providerId?: number
  status?: number
}

// 查询物流方式列表
export const getLogisticsMethodPage = async (params: LogisticsMethodPageReqVO) => {
  return await request.get({ url: '/erp/logistics-method/page', params })
}

// 查询物流方式详情
export const getLogisticsMethod = async (id: number) => {
  return await request.get({ url: `/erp/logistics-method/get?id=${id}` })
}

// 新增物流方式
export const createLogisticsMethod = async (data: LogisticsMethodVO) => {
  return await request.post({ url: '/erp/logistics-method/create', data })
}

// 修改物流方式
export const updateLogisticsMethod = async (data: LogisticsMethodVO) => {
  return await request.put({ url: '/erp/logistics-method/update', data })
}

// 删除物流方式
export const deleteLogisticsMethod = async (id: number) => {
  return await request.delete({ url: `/erp/logistics-method/delete?id=${id}` })
}

// 导出物流方式 Excel
export const exportLogisticsMethod = async (params: LogisticsMethodPageReqVO) => {
  return await request.download({ url: '/erp/logistics-method/export-excel', params })
}

// 获取物流方式精简列表
export const getLogisticsMethodSimpleList = async () => {
  return await request.get({ url: '/erp/logistics-method/simple-list' })
}

// 获取指定服务商的物流方式列表
export const getLogisticsMethodListByProviderId = async (providerId: number) => {
  return await request.get({ url: `/erp/logistics-method/list-by-provider?providerId=${providerId}` })
}

// 物流方式 API 对象
export const LogisticsMethodApi = {
  getLogisticsMethodPage,
  getLogisticsMethod,
  createLogisticsMethod,
  updateLogisticsMethod,
  deleteLogisticsMethod,
  exportLogisticsMethod,
  getLogisticsMethodSimpleList,
  getLogisticsMethodListByProviderId
} 