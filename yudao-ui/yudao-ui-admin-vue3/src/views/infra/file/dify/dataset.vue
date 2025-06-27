<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="mb-10px">
      <div class="mb-10px">
        <el-button
          type="primary"
          plain
          @click="openForm()"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 创建知识库
        </el-button>
        <el-button
          type="success"
          plain
          @click="refreshTable()"
        >
          <Icon icon="ep:refresh" class="mr-5px" /> 刷新
        </el-button>
      </div>

      <!-- 列表 -->
      <el-table v-loading="loading" :data="datasetList">
        <el-table-column label="ID" prop="id" width="300" :show-overflow-tooltip="true" />
        <el-table-column label="名称" prop="name" min-width="150" />
        <el-table-column label="描述" prop="description" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="权限" prop="permission" min-width="100">
          <template #default="{ row }">
            <dict-tag :type="DICT_TYPE.INFRA_DIFY_PERMISSION" :value="row.permission" />
          </template>
        </el-table-column>
        <el-table-column label="索引技术" prop="indexingTechnique" min-width="100">
          <template #default="{ row }">
            <dict-tag :type="DICT_TYPE.INFRA_DIFY_INDEXING_TECHNIQUE" :value="row.indexingTechnique" />
          </template>
        </el-table-column>
        <el-table-column label="文档数量" prop="documentCount" min-width="100" />
        <el-table-column label="创建时间" prop="createTime" min-width="170">
          <template #default="scope">
            <span>{{ formatDate(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="scope">
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              link
              type="primary"
              @click="handleSetDefault(scope.row)"
            >设为默认</el-button>
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
    </el-card>
    
    <!-- 表单弹窗：新增/修改 -->
    <el-dialog
      v-model="open"
      :title="formTitle"
      width="600px"
      append-to-body
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
        v-loading="formLoading"
      >
        <el-form-item label="知识库名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入知识库名称" />
        </el-form-item>
        <el-form-item label="知识库描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入知识库描述" />
        </el-form-item>
        <el-form-item label="权限" prop="permission">
          <el-select v-model="form.permission" placeholder="请选择权限" clearable>
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_DIFY_PERMISSION)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="索引技术" prop="indexingTechnique">
          <el-select v-model="form.indexingTechnique" placeholder="请选择索引技术" clearable>
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_DIFY_INDEXING_TECHNIQUE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="open = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDate } from '@/utils/formatTime'
import * as DifyDatasetApi from '@/api/infra/file/difyDataset'
import { getDifyConfig, updateDifyConfig } from '@/api/infra/file/dify'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import type { FormInstance } from 'element-plus'

defineOptions({ name: 'InfraFileDifyDataset' })

const loading = ref(true)
const total = ref(0)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10
})
const datasetList = ref([])

const open = ref(false)
const formTitle = ref('')
const formLoading = ref(false)
const formRef = ref<FormInstance>()
const form = reactive({
  name: '',
  description: '',
  permission: 'only_me',
  indexingTechnique: 'high_quality'
})
const formRules = reactive({
  name: [{ required: true, message: '知识库名称不能为空', trigger: 'blur' }],
  permission: [{ required: true, message: '权限不能为空', trigger: 'change' }]
})

/** 查询知识库列表 */
const getList = async () => {
  loading.value = true
  try {
    const res = await DifyDatasetApi.getDifyDatasetPage(queryParams)
    datasetList.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取知识库列表失败', error)
    ElMessage.error('获取知识库列表失败')
  } finally {
    loading.value = false
  }
}

/** 刷新表格 */
const refreshTable = () => {
  getList()
}

/** 打开表单弹窗 */
const openForm = () => {
  formTitle.value = '创建知识库'
  open.value = true
  resetForm()
}

/** 重置表单 */
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.name = ''
  form.description = ''
  form.permission = 'only_me'
  form.indexingTechnique = 'high_quality'
}

/** 提交表单 */
const submitForm = () => {
  if (!formRef.value) return
  
  formRef.value.validate(async (valid) => {
    if (!valid) return
    
    formLoading.value = true
    try {
      // 创建知识库
      await DifyDatasetApi.createDifyDataset(form)
      ElMessage.success('创建知识库成功')
      open.value = false
      getList()
    } catch (error) {
      console.error('创建知识库失败', error)
      ElMessage.error('创建知识库失败')
    } finally {
      formLoading.value = false
    }
  })
}

/** 删除知识库 */
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除知识库"${row.name}"吗？删除后不可恢复！`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    loading.value = true
    try {
      await DifyDatasetApi.deleteDifyDataset(row.id)
      ElMessage.success('删除知识库成功')
      getList()
    } catch (error) {
      console.error('删除知识库失败', error)
      ElMessage.error('删除知识库失败')
    } finally {
      loading.value = false
    }
  }).catch(() => {})
}

/** 设置为默认知识库 */
const handleSetDefault = (row) => {
  ElMessageBox.confirm(`确认将"${row.name}"设置为默认知识库吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(async () => {
    loading.value = true
    try {
      // 获取当前配置
      const config = await getDifyConfig()
      // 更新默认知识库ID
      await updateDifyConfig({
        enabled: config.enabled,
        autoSync: config.autoSync,
        defaultDatasetId: row.id
      })
      ElMessage.success('设置默认知识库成功')
    } catch (error) {
      console.error('设置默认知识库失败', error)
      ElMessage.error('设置默认知识库失败')
    } finally {
      loading.value = false
    }
  }).catch(() => {})
}

/** 初始化 */
onMounted(() => {
  getList()
})
</script> 