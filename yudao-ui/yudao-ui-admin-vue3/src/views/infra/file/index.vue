<template>
  <doc-alert title="上传下载" url="https://doc.iocoder.cn/file/" />
  <!-- 搜索 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="文件路径" prop="path">
        <el-input
          v-model="queryParams.path"
          placeholder="请输入文件路径"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="文件类型" prop="type" width="80">
        <el-input
          v-model="queryParams.type"
          placeholder="请输入文件类型"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
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
        <el-button type="primary" plain @click="openDifyUploadForm">
          <Icon icon="ep:upload" class="mr-5px" /> 上传文件
        </el-button>
        <el-button type="success" plain @click="handleBatchSync" v-hasPermi="['infra:file:sync']">
          <Icon icon="ep:connection" class="mr-5px" /> 批量同步到Dify
        </el-button>
        <el-button type="info" plain @click="handleDifyManage" v-hasPermi="['infra:file:sync']">
          <Icon icon="ep:management" class="mr-5px" /> Dify同步管理
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文件名" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="文件路径" align="center" prop="path" :show-overflow-tooltip="true" />
      <el-table-column label="URL" align="center" prop="url" :show-overflow-tooltip="true" />
      <el-table-column
        label="文件大小"
        align="center"
        prop="size"
        width="120"
        :formatter="fileSizeFormatter"
      />
      <el-table-column label="文件类型" align="center" prop="type" width="180px" />
      <el-table-column label="文件内容" align="center" prop="url" width="110px">
        <template #default="{ row }">
          <el-image
            v-if="row.type.includes('image')"
            class="h-80px w-80px"
            lazy
            :src="row.url"
            :preview-src-list="[row.url]"
            preview-teleported
            fit="cover"
          />
          <el-link
            v-else-if="row.type.includes('pdf')"
            type="primary"
            :href="row.url"
            :underline="false"
            target="_blank"
            >预览</el-link
          >
          <el-link v-else type="primary" download :href="row.url" :underline="false" target="_blank"
            >下载</el-link
          >
        </template>
      </el-table-column>
      <el-table-column
        label="上传时间"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="copyToClipboard(scope.row.url)">
            复制链接
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleSyncToDify(scope.row)"
            v-hasPermi="['infra:file:sync']"
          >
            同步到Dify
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['infra:file:delete']"
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

  <!-- 表单弹窗：添加/修改 -->
  <FileForm ref="formRef" @success="getList" />
  
  <!-- 支持选择知识库的文件上传表单 -->
  <DifyFileUploadForm ref="difyUploadFormRef" @success="getList" />

  <!-- 同步到Dify对话框 -->
  <el-dialog v-model="syncDialogVisible" title="同步到Dify" width="500px">
    <el-form :model="syncForm" label-width="80px">
      <el-form-item label="知识库ID">
        <el-input v-model="syncForm.datasetId" placeholder="请输入知识库ID，留空则使用默认知识库" />
      </el-form-item>
      <el-form-item label="文件名">
        <div>{{ syncForm.fileName }}</div>
      </el-form-item>
      <el-form-item label="文件类型">
        <div>{{ syncForm.fileType }}</div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="syncDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmSync">确认同步</el-button>
    </template>
  </el-dialog>

  <!-- 批量同步对话框 -->
  <el-dialog v-model="batchSyncVisible" title="批量同步文件到Dify" width="500px">
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
</template>
<script lang="ts" setup>
import { fileSizeFormatter } from '@/utils'
import { dateFormatter } from '@/utils/formatTime'
import * as FileApi from '@/api/infra/file'
import * as DifyApi from '@/api/infra/file/dify'
import FileForm from './FileForm.vue'
import DifyFileUploadForm from './dify/FileUploadForm.vue'
import { useRouter } from 'vue-router'

defineOptions({ name: 'InfraFile' })

const router = useRouter()
const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  type: undefined,
  path: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

// Dify 同步相关
const syncDialogVisible = ref(false)
const syncForm = reactive({
  fileId: undefined as number | undefined,
  datasetId: '',
  fileName: '',
  fileType: ''
})

// 批量同步相关
const batchSyncVisible = ref(false)
const batchSyncForm = reactive({
  datasetId: ''
})
const selectedFiles = ref<number[]>([])

const formRef = ref() // 表单的Ref
const difyUploadFormRef = ref() // Dify文件上传表单的Ref

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await FileApi.getFilePage(queryParams)
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

/** 打开表单 */
const openForm = () => {
  formRef.value.open()
}

/** 打开支持选择知识库的文件上传表单 */
const openDifyUploadForm = () => {
  difyUploadFormRef.value.open()
}

/** 复制到剪贴板方法 */
const copyToClipboard = (text: string) => {
  navigator.clipboard.writeText(text).then(() => {
    message.success('复制成功')
  })
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await FileApi.deleteFile(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 同步到Dify按钮操作 */
const handleSyncToDify = (row: any) => {
  syncForm.fileId = row.id
  syncForm.fileName = row.name
  syncForm.fileType = row.type
  syncForm.datasetId = '' // 重置字段
  syncDialogVisible.value = true
}

/** 确认同步到Dify */
const confirmSync = async () => {
  if (!syncForm.fileId) {
    message.error('同步失败，文件ID不存在')
    return
  }
  try {
    await DifyApi.syncFileToDify(syncForm.fileId, syncForm.datasetId || undefined)
    message.success('同步任务已提交')
    syncDialogVisible.value = false
  } catch (error) {
    console.error('同步失败', error)
  }
}

/** 表格多选框选择 */
const handleSelectionChange = (selection: any[]) => {
  selectedFiles.value = selection.map(item => item.id)
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
  } catch (error) {
    console.error('批量同步失败', error)
  }
}

/** 跳转到Dify管理页面 */
const handleDifyManage = () => {
  router.push('/infra/file/dify')
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
