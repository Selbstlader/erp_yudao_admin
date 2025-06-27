import request from '@/config/axios'

export interface WarehouseInternationalVO {
  id: number
  warehouseId: number
  warehouseName: string
  countryCode: string
  countryName: string
  address: string
  contactName: string
  contactPhone: string
  contactEmail: string
  isBonded: boolean
  status: number
  remark: string
  createTime: Date
}

export interface WarehouseInternationalPageReqVO extends PageParam {
  warehouseId?: number
  countryCode?: string
  isBonded?: boolean
  status?: number
}

// 查询国际仓库列表
export const getWarehouseInternationalPage = async (params: WarehouseInternationalPageReqVO) => {
  return await request.get({ url: '/erp/warehouse-international/page', params })
}

// 查询国际仓库详情
export const getWarehouseInternational = async (id: number) => {
  return await request.get({ url: `/erp/warehouse-international/get?id=${id}` })
}

// 新增国际仓库
export const createWarehouseInternational = async (data: WarehouseInternationalVO) => {
  return await request.post({ url: '/erp/warehouse-international/create', data })
}

// 修改国际仓库
export const updateWarehouseInternational = async (data: WarehouseInternationalVO) => {
  return await request.put({ url: '/erp/warehouse-international/update', data })
}

// 删除国际仓库
export const deleteWarehouseInternational = async (id: number) => {
  return await request.delete({ url: `/erp/warehouse-international/delete?id=${id}` })
}

// 导出国际仓库 Excel
export const exportWarehouseInternational = async (params: WarehouseInternationalPageReqVO) => {
  return await request.download({ url: '/erp/warehouse-international/export-excel', params })
}

// 获取国际仓库精简列表
export const getWarehouseInternationalSimpleList = async () => {
  return await request.get({ url: '/erp/warehouse-international/simple-list' })
}

// 获取指定国家的国际仓库列表
export const getWarehouseInternationalListByCountryCode = async (countryCode: string) => {
  return await request.get({ url: `/erp/warehouse-international/list-by-country-code?countryCode=${countryCode}` })
}

// 国际仓库 API 对象
export const WarehouseInternationalApi = {
  getWarehouseInternationalPage,
  getWarehouseInternational,
  createWarehouseInternational,
  updateWarehouseInternational,
  deleteWarehouseInternational,
  exportWarehouseInternational,
  getWarehouseInternationalSimpleList,
  getWarehouseInternationalListByCountryCode
} 