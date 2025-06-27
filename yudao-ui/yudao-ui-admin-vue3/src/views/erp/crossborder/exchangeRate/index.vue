<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
      <el-form-item label="源币种" prop="fromCurrencyId">
        <el-select v-model="queryParams.fromCurrencyId" placeholder="请选择源币种" clearable class="!w-240px">
          <el-option v-for="item in currencyOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="目标币种" prop="toCurrencyId">
        <el-select v-model="queryParams.toCurrencyId" placeholder="请选择目标币种" clearable class="!w-240px">
          <el-option v-for="item in currencyOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="!w-240px">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" plain @click="openForm('create')">
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button type="success" plain @click="handleExport" :loading="exportLoading">
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" /> 搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" /> 重置
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="汇率编号" align="center" prop="id" />
      <el-table-column label="源币种" align="center" prop="fromCurrencyName" />
      <el-table-column label="目标币种" align="center" prop="toCurrencyName" />
      <el-table-column label="汇率" align="center" prop="rate" />
      <el-table-column label="生效日期" align="center" prop="effectiveDate" />
      <el-table-column label="失效日期" align="center" prop="expiryDate" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="汇率来源" align="center" prop="source">
        <template #default="scope">
          <dict-tag :type="'erp_exchange_rate_source'" :value="scope.row.source" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" :formatter="dateFormatter" width="180px" />
      <el-table-column label="操作" align="center" width="200px">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('view', scope.row.id)">
            详情
          </el-button>
          <el-button link type="primary" @click="openForm('update', scope.row.id)">
            编辑
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ExchangeRateForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import { ExchangeRateApi, ExchangeRateVO } from '@/api/erp/crossborder/exchangeRate'
import { CurrencyApi } from '@/api/erp/crossborder/currency'
import ExchangeRateForm from './ExchangeRateForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import download from '@/utils/download'

// 定义币种选项类型
interface CurrencyOption {
  id: number;
  name: string;
  code: string;
}

/** ERP 汇率列表 */
defineOptions({ name: 'ErpExchangeRate' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ExchangeRateVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  fromCurrencyId: undefined,
  toCurrencyId: undefined,
  status: undefined
})
const currencyOptions = ref<CurrencyOption[]>([]) // 币种选项

const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ExchangeRateApi.getExchangeRatePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 获取币种选项 */
const getCurrencyOptions = async () => {
  try {
    currencyOptions.value = await CurrencyApi.getCurrencySimpleList()
  } catch (error) {
    console.error('获取币种选项失败', error)
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ExchangeRateApi.deleteExchangeRate(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch { }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ExchangeRateApi.exportExchangeRate(queryParams)
    download.excel(data, '汇率.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
  getCurrencyOptions() // 获取币种选项
})
</script>