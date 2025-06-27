<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" :inline="true" label-width="70px">
      <el-form-item label="仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable class="!w-240px">
          <el-option v-for="warehouse in warehouseOptions" :key="warehouse.id" :label="warehouse.name" :value="warehouse.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="国家" prop="countryCode">
        <el-input v-model="queryParams.countryCode" placeholder="请输入国家代码" clearable @keyup.enter="handleQuery"
          class="!w-240px" />
      </el-form-item>
      <el-form-item label="保税仓" prop="isBonded">
        <el-select v-model="queryParams.isBonded" placeholder="请选择是否保税仓" clearable class="!w-240px">
          <el-option label="是" :value="true" />
          <el-option label="否" :value="false" />
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
      <el-table-column label="仓库编号" align="center" prop="id" />
      <el-table-column label="仓库名称" align="center" prop="warehouseName" />
      <el-table-column label="国家" align="center" prop="countryName" />
      <el-table-column label="国家代码" align="center" prop="countryCode" />
      <el-table-column label="保税仓" align="center" prop="isBonded">
        <template #default="scope">
          <el-tag :type="scope.row.isBonded ? 'success' : 'info'">
            {{ scope.row.isBonded ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="联系人" align="center" prop="contactName" />
      <el-table-column label="联系电话" align="center" prop="contactPhone" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
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
  <WarehouseForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import { WarehouseInternationalApi, WarehouseInternationalVO } from '@/api/erp/logistics/warehouse'
import WarehouseForm from './WarehouseForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import download from '@/utils/download'

// 模拟仓库数据，实际应该从API获取
const warehouseOptions = ref([
  { id: 1, name: '北京仓库' },
  { id: 2, name: '上海仓库' },
  { id: 3, name: '广州仓库' },
  { id: 4, name: '深圳仓库' }
])

/** ERP 国际仓库列表 */
defineOptions({ name: 'ErpWarehouseInternational' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<WarehouseInternationalVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  warehouseId: undefined,
  countryCode: undefined,
  isBonded: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await WarehouseInternationalApi.getWarehouseInternationalPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
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
    await WarehouseInternationalApi.deleteWarehouseInternational(id)
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
    const data = await WarehouseInternationalApi.exportWarehouseInternational(queryParams)
    download.excel(data, '国际仓库.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script> 