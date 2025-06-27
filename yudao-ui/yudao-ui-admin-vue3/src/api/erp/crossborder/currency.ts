import request from '@/config/axios'

// ERP 多币种 VO
export interface CurrencyVO {
  id: number // 币种编号
  code: string // 币种代码
  name: string // 币种名称
  englishName: string // 币种英文名称
  symbol: string // 币种符号
  status: number // 币种状态
  isBase: boolean // 是否基础币种
  exchangeRate: number // 汇率
  decimalPlaces: number // 小数位数
  sort: number // 排序
  remark: string // 备注
  createTime: Date // 创建时间
}

// ERP 多币种 API
export const CurrencyApi = {
  // 查询多币种分页
  getCurrencyPage: async (params: any) => {
    return await request.get({ url: `/erp/currency/page`, params })
  },

  // 查询多币种精简列表
  getCurrencySimpleList: async () => {
    return await request.get({ url: `/erp/currency/simple-list` })
  },

  // 查询多币种详情
  getCurrency: async (id: number) => {
    return await request.get({ url: `/erp/currency/get?id=` + id })
  },

  // 新增多币种
  createCurrency: async (data: CurrencyVO) => {
    return await request.post({ url: `/erp/currency/create`, data })
  },

  // 修改多币种
  updateCurrency: async (data: CurrencyVO) => {
    return await request.put({ url: `/erp/currency/update`, data })
  },

  // 删除多币种
  deleteCurrency: async (id: number) => {
    return await request.delete({ url: `/erp/currency/delete?id=` + id })
  },

  // 币种转换
  convertCurrency: async (amount: number, fromCurrency: string, toCurrency: string) => {
    return await request.get({ 
      url: `/erp/currency/convert`, 
      params: { amount, fromCurrency, toCurrency } 
    })
  },

  // 导出多币种 Excel
  exportCurrency: async (params) => {
    return await request.download({ url: `/erp/currency/export-excel`, params })
  }
}
