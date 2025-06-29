<template>
  <!-- <doc-alert title="Dify文件同步" url="https://doc.iocoder.cn/file/dify/" /> -->
  <!-- 搜索 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="同步状态" prop="syncStatus">
        <el-select
          v-model="queryParams.syncStatus"
          placeholder="请选择同步状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in syncStatusOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="同步时间" prop="syncTime">
        <el-date-picker
          v-model="queryParams.syncTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button type="primary" @click="handleBatchSync" >
          <Icon icon="ep:upload" class="mr-5px" /> 批量同步
        </el-button>
        <el-button type="warning" @click="handleOpenConfig" >
          <Icon icon="ep:setting" class="mr-5px" /> 设置
        </el-button>
        <el-button type="success" @click="openFileUploadForm">
          <Icon icon="ep:upload-filled" class="mr-5px" /> 上传文件
        </el-button>
        <el-button type="info" @click="handleDatasetManage">
          <Icon icon="ep:files" class="mr-5px" /> 知识库管理
        </el-button>
        <el-button type="info" @click="openDifyWebsite">
          <Icon icon="ep:files" class="mr-5px" /> 知识库地址
        </el-button>
        <el-button type="primary" @click="handleOpenChat">
          <Icon icon="ep:chat-dot-round" class="mr-5px" /> AI对话
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" @row-click="handleRowClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文件ID" align="center" prop="fileId" />
      <el-table-column label="文件名称" align="center" prop="fileName" :show-overflow-tooltip="true" />
      <el-table-column label="同步状态" align="center" prop="syncStatusName">
        <template #default="{ row }">
          <el-tag :type="getSyncStatusType(row.syncStatus)">{{ row.syncStatusName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="知识库ID" align="center" prop="datasetId" :show-overflow-tooltip="true" />
      <el-table-column label="文档ID" align="center" prop="difyDocumentId" :show-overflow-tooltip="true" />
      <el-table-column
        label="上次同步时间"
        align="center"
        prop="syncTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click.stop="handleViewDetail(scope.row)"
          >
            详情
          </el-button>
          <el-button
            v-if="scope.row.syncStatus === 3" 
            link
            type="primary"
            @click.stop="handleRetry(scope.row)"
          >
            重试
          </el-button>
          <el-button
            v-if="scope.row.syncStatus !== 1"
            link
            type="primary"
            @click.stop="handleSync(scope.row)"
          >
            同步
          </el-button>
          <el-button
            link
            type="danger"
            @click.stop="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 批量同步对话框 -->
  <el-dialog v-model="batchSyncVisible" title="批量同步文件" width="500px">
    <el-form :model="batchSyncForm" label-width="80px">
      <el-form-item label="知识库ID">
        <el-input v-model="batchSyncForm.datasetId" placeholder="请输入知识库ID，留空则使用默认知识库" />
      </el-form-item>
      <el-form-item label="文件列表">
        <div v-if="selectedFiles.length > 0">
          已选择 {{ selectedFiles.length }} 个文件
        </div>
        <div v-else class="text-red-500">请先在列表中选择需要同步的文件</div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="batchSyncVisible = false">取消</el-button>
      <el-button
        type="primary"
        :disabled="selectedFiles.length === 0"
        @click="confirmBatchSync"
      >
        确认同步
      </el-button>
    </template>
  </el-dialog>

  <!-- 配置对话框 -->
  <el-dialog v-model="configVisible" title="Dify配置" width="500px">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="API基础URL">{{ difyConfig.baseUrl }}</el-descriptions-item>
      <el-descriptions-item label="默认知识库">{{ difyConfig.defaultDatasetId || '未设置' }}</el-descriptions-item>
      <el-descriptions-item label="聊天API密钥">{{ difyConfig.chatApiKey || '未设置' }}</el-descriptions-item>
    </el-descriptions>
    <el-divider>功能设置</el-divider>
    <el-form :model="configForm" label-width="120px">
      <el-form-item label="启用Dify集成">
        <el-switch v-model="configForm.enabled" />
      </el-form-item>
      <el-form-item label="开启自动同步">
        <el-switch v-model="configForm.autoSync" :disabled="!configForm.enabled" />
        <div class="mt-2 text-gray-500 text-xs">
          开启后，文件新增和编辑时会自动同步到知识库
        </div>
      </el-form-item>
      <el-form-item label="默认知识库ID">
        <el-input v-model="configForm.defaultDatasetId" placeholder="请输入默认知识库ID" :disabled="!configForm.enabled" />
      </el-form-item>
      <el-form-item label="聊天API密钥">
        <el-input v-model="configForm.chatApiKey" placeholder="请输入聊天API密钥(app-开头)" :disabled="!configForm.enabled" />
        <div class="mt-2 text-gray-500 text-xs">
          用于聊天功能的API密钥，通常以app-开头
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="configVisible = false">取消</el-button>
      <el-button
        type="primary"
        @click="saveConfig"
      >
        保存配置
      </el-button>
    </template>
  </el-dialog>

  <!-- 同步详情抽屉 -->
  <el-drawer
    v-model="syncDetailVisible"
    title="同步详情"
    size="500px"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item label="文件ID">{{ currentDetail.fileId }}</el-descriptions-item>
      <el-descriptions-item label="文件名称">{{ currentDetail.fileName }}</el-descriptions-item>
      <el-descriptions-item label="同步状态">
        <el-tag :type="getSyncStatusType(currentDetail.syncStatus)">{{ currentDetail.syncStatusName }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="知识库ID">{{ currentDetail.datasetId }}</el-descriptions-item>
      <el-descriptions-item label="文档ID">{{ currentDetail.difyDocumentId }}</el-descriptions-item>
      <el-descriptions-item label="批处理ID">{{ currentDetail.batchId || '-' }}</el-descriptions-item>
      <el-descriptions-item label="重试次数">{{ currentDetail.retryCount }}</el-descriptions-item>
      <el-descriptions-item label="上次同步时间">{{ currentDetail.syncTime }}</el-descriptions-item>
      <el-descriptions-item label="下次重试时间">{{ currentDetail.nextRetryTime || '-' }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ currentDetail.createTime }}</el-descriptions-item>
      <el-descriptions-item label="错误信息" v-if="currentDetail.errorMessage">
        <div class="text-red-500 whitespace-pre-wrap">{{ currentDetail.errorMessage }}</div>
      </el-descriptions-item>
    </el-descriptions>
    
    <template #footer>
      <div class="flex justify-end">
        <el-button @click="syncDetailVisible = false">关闭</el-button>
        <el-button 
          v-if="currentDetail.syncStatus === 3" 
          type="primary" 
          @click="handleRetryFromDetail"
        >
          重试
        </el-button>
        <el-button 
          v-if="currentDetail.syncStatus !== 1" 
          type="primary" 
          @click="handleSyncFromDetail"
        >
          同步
        </el-button>
      </div>
    </template>
  </el-drawer>

  <!-- 文件上传表单 -->
  <FileForm ref="formRef" @success="handleFileUploadSuccess" />
</template>

<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as DifyApi from '@/api/infra/file/dify'
import { FileDifySyncRespVO, DifyConfigRespVO } from '@/api/infra/file/dify'
import { PageParam, PageResult } from '@/api/model/common'
import FileForm from '../FileForm.vue'
import { useRouter } from 'vue-router'

defineOptions({ name: 'InfraFileDify' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化
const router = useRouter()

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref<FileDifySyncRespVO[]>([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  syncStatus: undefined,
  syncTime: []
})
const queryFormRef = ref() // 搜索的表单

// 同步状态选项
const syncStatusOptions = [
  { value: 0, label: '未同步' },
  { value: 1, label: '同步中' },
  { value: 2, label: '已同步' },
  { value: 3, label: '同步失败' }
]

// 获取同步状态类型
const getSyncStatusType = (status: number): 'success' | 'warning' | 'info' | 'danger' | 'primary' => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'warning'
    case 2: return 'success'
    case 3: return 'danger'
    default: return 'info'
  }
}

// 批量同步相关
const batchSyncVisible = ref(false)
const batchSyncForm = reactive({
  datasetId: ''
})
const selectedFiles = ref<number[]>([])

// 同步详情相关
const syncDetailVisible = ref(false)
const currentDetail = ref<any>({
  id: 0,
  fileId: 0,
  fileName: '',
  difyDocumentId: '',
  syncStatus: 0,
  syncStatusName: '',
  errorMessage: '',
  errorCode: '',
  datasetId: '',
  batchId: '',
  retryCount: 0,
  nextRetryTime: '',
  syncTime: '',
  createTime: ''
})

// 文件上传表单
const formRef = ref()

// Dify配置相关
const configVisible = ref(false)
const difyConfig = ref<DifyConfigRespVO>({
  enabled: true,
  autoSync: true,
  defaultDatasetId: '',
  baseUrl: '',
  chatApiKey: ''
})
const configForm = reactive({
  enabled: true,
  autoSync: true,
  defaultDatasetId: '',
  chatApiKey: ''
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DifyApi.getFileSyncPage(queryParams)
    list.value = data.list || []
    total.value = data.total || 0
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

/** 表格多选框选择 */
const handleSelectionChange = (selection: FileDifySyncRespVO[]) => {
  selectedFiles.value = selection.map(item => item.fileId)
}

/** 处理行点击 */
const handleRowClick = (row: FileDifySyncRespVO) => {
  handleViewDetail(row)
}

/** 批量同步按钮操作 */
const handleBatchSync = () => {
  batchSyncForm.datasetId = '' // 重置字段
  batchSyncVisible.value = true
}

/** 确认批量同步 */
const confirmBatchSync = async () => {
  if (selectedFiles.value.length === 0) {
    message.warning('请选择需要同步的文件')
    return
  }
  
  try {
    await DifyApi.batchSyncFiles(selectedFiles.value, batchSyncForm.datasetId || undefined)
    message.success('批量同步任务已提交')
    batchSyncVisible.value = false
    await getList() // 刷新列表
  } catch (error) {
    console.error('批量同步失败', error)
  }
}

/** 同步按钮操作 */
const handleSync = async (row: FileDifySyncRespVO) => {
  try {
    await DifyApi.triggerSync(row.fileId)
    message.success('同步任务已提交')
    await getList() // 刷新列表
  } catch (error) {
    console.error('同步失败', error)
  }
}

/** 从详情抽屉同步按钮操作 */
const handleSyncFromDetail = async () => {
  if (!currentDetail.value.fileId) return
  try {
    await DifyApi.triggerSync(currentDetail.value.fileId)
    message.success('同步任务已提交')
    syncDetailVisible.value = false
    await getList() // 刷新列表
  } catch (error) {
    console.error('同步失败', error)
  }
}

/** 重试按钮操作 */
const handleRetry = async (row: FileDifySyncRespVO) => {
  try {
    await DifyApi.retrySync(row.fileId)
    message.success('重试任务已提交')
    await getList() // 刷新列表
  } catch (error) {
    console.error('重试失败', error)
  }
}

/** 从详情抽屉重试按钮操作 */
const handleRetryFromDetail = async () => {
  if (!currentDetail.value.fileId) return
  try {
    await DifyApi.retrySync(currentDetail.value.fileId)
    message.success('重试任务已提交')
    syncDetailVisible.value = false
    await getList() // 刷新列表
  } catch (error) {
    console.error('重试失败', error)
  }
}

/** 删除按钮操作 */
const handleDelete = async (row: FileDifySyncRespVO) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await DifyApi.deleteFileSync(row.fileId)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 查看详情 */
const handleViewDetail = (row: FileDifySyncRespVO) => {
  currentDetail.value = row
  syncDetailVisible.value = true
}

/** 打开配置对话框 */
const handleOpenConfig = async () => {
  try {
    const config = await DifyApi.getDifyConfig()
    difyConfig.value = config
    // 初始化表单
    configForm.enabled = config.enabled
    configForm.autoSync = config.autoSync
    configForm.defaultDatasetId = config.defaultDatasetId
    configForm.chatApiKey = config.chatApiKey
    configVisible.value = true
    
    // 检查是否配置了默认知识库
    if (configForm.autoSync && !configForm.defaultDatasetId) {
      message.warning('请配置默认知识库ID，否则自动同步功能将无法正常工作')
    }
  } catch (error) {
    console.error('获取Dify配置失败', error)
  }
}

/** 保存配置 */
const saveConfig = async () => {
  // 如果开启了自动同步但未设置默认知识库，提示用户
  if (configForm.autoSync && configForm.enabled && !configForm.defaultDatasetId) {
    message.warning('您已启用自动同步但未设置默认知识库ID，这可能导致文件无法自动同步')
  }

  try {
    await DifyApi.updateDifyConfig({
      enabled: configForm.enabled,
      autoSync: configForm.autoSync,
      defaultDatasetId: configForm.defaultDatasetId || undefined,
      chatApiKey: configForm.chatApiKey || undefined
    })
    message.success('配置已保存')
    configVisible.value = false
  } catch (error) {
    console.error('保存配置失败', error)
  }
}

/** 打开上传文件表单 */
const openFileUploadForm = () => {
  formRef.value.open()
}

/** 处理文件上传成功 */
const handleFileUploadSuccess = async (fileData) => {
  message.success('文件上传成功')
  
  // 刷新列表
  await getList()
  
  // 文件上传成功后，不立即触发同步
  // 因为FileForm组件返回的可能是URL而不是ID，所以不能直接传给triggerSync
  // 让用户查看列表后手动触发同步，或等待列表自动刷新显示新上传的文件
}

/** 跳转到知识库管理页面 */
const handleDatasetManage = () => {
  router.push('/infra/file/dataset')
}

/** 打开Dify官网 */
const openDifyWebsite = () => {
  window.open('https://cloud.dify.ai/datasets')
}

/** 跳转到AI对话页面 */
const handleOpenChat = () => {
  router.push('/infra/file/chat')
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script> 