import request from '@/config/axios'

export interface LogisticsProviderVO {
  id: number
  code: string
  name: string
  // apiType: string
  apiConfig: string
  status: number
  sort: number
  remark: string
  createTime: Date
}

export interface LogisticsProviderPageReqVO extends PageParam {
  name?: string
  code?: string
  status?: number
}

// 查询物流服务商列表
export const getLogisticsProviderPage = async (params: LogisticsProviderPageReqVO) => {
  return await request.get({ url: '/erp/logistics-provider/page', params })
}

// 查询物流服务商详情
export const getLogisticsProvider = async (id: number) => {
  return await request.get({ url: `/erp/logistics-provider/get?id=${id}` })
}

// 新增物流服务商
export const createLogisticsProvider = async (data: LogisticsProviderVO) => {
  return await request.post({ url: '/erp/logistics-provider/create', data })
}

// 修改物流服务商
export const updateLogisticsProvider = async (data: LogisticsProviderVO) => {
  return await request.put({ url: '/erp/logistics-provider/update', data })
}

// 删除物流服务商
export const deleteLogisticsProvider = async (id: number) => {
  return await request.delete({ url: `/erp/logistics-provider/delete?id=${id}` })
}

// 导出物流服务商 Excel
export const exportLogisticsProvider = async (params: LogisticsProviderPageReqVO) => {
  return await request.download({ url: '/erp/logistics-provider/export-excel', params })
}

// 获取物流服务商精简列表
export const getLogisticsProviderSimpleList = async () => {
  return await request.get({ url: '/erp/logistics-provider/simple-list' })
}

// 测试物流服务商API连接
export const testLogisticsProviderConnection = async (id: number) => {
  return await request.post({ url: `/erp/logistics-provider/test-connection?id=${id}` })
}

// 通过服务商编号查询物流方式列表
export const getLogisticsMethodListByProviderId = async (providerId: number) => {
  return await request.get({ url: `/erp/logistics-method/list-by-provider?providerId=${providerId}` })
}

// 物流服务商 API 对象
export const LogisticsProviderApi = {
  getLogisticsProviderPage,
  getLogisticsProvider,
  createLogisticsProvider,
  updateLogisticsProvider,
  deleteLogisticsProvider,
  exportLogisticsProvider,
  getLogisticsProviderSimpleList,
  testLogisticsProviderConnection,
  getLogisticsMethodListByProviderId
} 