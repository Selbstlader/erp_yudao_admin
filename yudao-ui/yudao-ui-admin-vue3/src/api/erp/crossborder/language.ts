import request from '@/config/axios'

// ERP 多语言 VO
export interface LanguageVO {
  id: number // 语言编号
  code: string // 语言代码
  name: string // 语言名称
  englishName: string // 语言英文名称
  status: number // 语言状态
  isDefault: boolean // 是否默认语言
  sort: number // 排序
  remark: string // 备注
  createTime: Date // 创建时间
}

// ERP 多语言 API
export const LanguageApi = {
  // 查询多语言分页
  getLanguagePage: async (params: any) => {
    return await request.get({ url: `/erp/language/page`, params })
  },

  // 查询多语言精简列表
  getLanguageSimpleList: async () => {
    return await request.get({ url: `/erp/language/simple-list` })
  },

  // 查询多语言详情
  getLanguage: async (id: number) => {
    return await request.get({ url: `/erp/language/get?id=` + id })
  },

  // 新增多语言
  createLanguage: async (data: LanguageVO) => {
    return await request.post({ url: `/erp/language/create`, data })
  },

  // 修改多语言
  updateLanguage: async (data: LanguageVO) => {
    return await request.put({ url: `/erp/language/update`, data })
  },

  // 删除多语言
  deleteLanguage: async (id: number) => {
    return await request.delete({ url: `/erp/language/delete?id=` + id })
  },

  // 导出多语言 Excel
  exportLanguage: async (params) => {
    return await request.download({ url: `/erp/language/export-excel`, params })
  }
}
