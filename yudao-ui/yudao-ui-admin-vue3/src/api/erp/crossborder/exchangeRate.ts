import request from '@/config/axios'

// ERP 汇率 VO
export interface ExchangeRateVO {
  id: number // 汇率编号
  fromCurrencyId: number // 源币种编号
  fromCurrencyName: string // 源币种名称
  toCurrencyId: number // 目标币种编号
  toCurrencyName: string // 目标币种名称
  rate: number // 汇率
  effectiveDate: string // 生效日期
  expiryDate: string // 失效日期
  status: number // 汇率状态
  source: number // 汇率来源
  remark: string // 备注
  createTime: Date // 创建时间
}

// ERP 汇率 API
export const ExchangeRateApi = {
  // 查询汇率分页
  getExchangeRatePage: async (params: any) => {
    return await request.get({ url: `/erp/exchange-rate/page`, params })
  },

  // 查询汇率精简列表
  getExchangeRateSimpleList: async () => {
    return await request.get({ url: `/erp/exchange-rate/simple-list` })
  },

  // 查询汇率详情
  getExchangeRate: async (id: number) => {
    return await request.get({ url: `/erp/exchange-rate/get?id=` + id })
  },

  // 新增汇率
  createExchangeRate: async (data: ExchangeRateVO) => {
    return await request.post({ url: `/erp/exchange-rate/create`, data })
  },

  // 修改汇率
  updateExchangeRate: async (data: ExchangeRateVO) => {
    return await request.put({ url: `/erp/exchange-rate/update`, data })
  },

  // 删除汇率
  deleteExchangeRate: async (id: number) => {
    return await request.delete({ url: `/erp/exchange-rate/delete?id=` + id })
  },

  // 获得最新汇率
  getLatestExchangeRate: async (fromCurrencyId: number, toCurrencyId: number, date: string) => {
    return await request.get({ 
      url: `/erp/exchange-rate/latest`, 
      params: { fromCurrencyId, toCurrencyId, date } 
    })
  },

  // 币种转换
  convertCurrency: async (amount: number, fromCurrencyId: number, toCurrencyId: number, date: string) => {
    return await request.get({ 
      url: `/erp/exchange-rate/convert`, 
      params: { amount, fromCurrencyId, toCurrencyId, date } 
    })
  },

  // 导出汇率 Excel
  exportExchangeRate: async (params) => {
    return await request.download({ url: `/erp/exchange-rate/export-excel`, params })
  }
}
