<template>
  <Dialog v-model="dialogVisible" title="上传文件">
    <el-form
      ref="formRef"
      :model="form"
      :rules="formRules"
      label-width="120px"
    >
      <el-form-item label="目标知识库" prop="difyDatasetId">
        <el-select
          v-model="form.difyDatasetId"
          placeholder="请选择目标知识库"
          clearable
          style="width: 100%"
        >
          <el-option
            v-for="dataset in datasetOptions"
            :key="dataset.id"
            :label="dataset.name"
            :value="dataset.id"
          />
        </el-select>
        <div class="el-form-item-msg">不选择则使用默认知识库</div>
      </el-form-item>
      <el-form-item label="同步到知识库" prop="syncDify">
        <el-switch v-model="form.syncDify" />
        <div class="el-form-item-msg">关闭则不会同步文件内容到知识库</div>
      </el-form-item>
      <el-form-item label="上传文件" prop="file">
        <el-upload
          ref="uploadRef"
          v-model:file-list="fileList"
          :action="uploadUrl"
          :auto-upload="false"
          :data="getUploadData"
          :disabled="formLoading"
          :limit="1"
          :on-change="handleFileChange"
          :on-error="submitFormError"
          :on-exceed="handleExceed"
          :on-success="submitFormSuccess"
          :http-request="httpRequest"
          accept="*"
          drag
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text"> 将文件拖到此处，或 <em>点击上传</em></div>
          <template #tip>
            <div class="el-upload__tip" style="color: red">
              提示：支持上传任意格式文件！
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitFileForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { ref, reactive, onMounted, unref } from 'vue'
import { useI18n } from '@/hooks/web/useI18n'
import { useMessage } from '@/hooks/web/useMessage'
import { useUpload } from '@/components/UploadFile/src/useUpload'
import { getDifyDatasetPage } from '@/api/infra/file/difyDataset'
import type { FormInstance, FormRules } from 'element-plus'
import type { UploadInstance, UploadProps, UploadUserFile } from 'element-plus'

defineOptions({ name: 'InfraFileDifyUploadForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const fileList = ref<UploadUserFile[]>([]) // 文件列表
const uploadRef = ref<UploadInstance>()
const formRef = ref<FormInstance>()
const datasetOptions = ref<any[]>([])

const form = reactive({
  syncDify: true,
  difyDatasetId: '',
  file: undefined as File | undefined,
  path: ''
})

// 表单校验规则
const formRules = reactive<FormRules>({
  file: [{ required: true, message: '请上传文件', trigger: 'change' }]
})

const { uploadUrl, httpRequest } = useUpload()

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  resetForm()
  await loadDatasets()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 加载知识库列表 */
const loadDatasets = async () => {
  try {
    const res = await getDifyDatasetPage({ pageNo: 1, pageSize: 100 })
    datasetOptions.value = res.list
  } catch (error) {
    console.error('获取知识库列表失败', error)
  }
}

/** 获取上传参数 */
const getUploadData = () => {
  return {
    path: form.path,
    syncDify: form.syncDify,
    difyDatasetId: form.difyDatasetId
  }
}

/** 处理上传的文件发生变化 */
const handleFileChange = (file: any) => {
  form.path = file.name
  form.file = file.raw
}

/** 提交表单 */
const submitFileForm = () => {
  if (!formRef.value) return
  
  formRef.value.validate((valid) => {
    if (!valid) return
    
    if (fileList.value.length == 0) {
      message.error('请上传文件')
      return
    }
    formLoading.value = true
    unref(uploadRef)?.submit()
  })
}

/** 文件上传成功处理 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitFormSuccess = (response: any) => {
  console.log('上传成功响应:', response)
  
  // 清理
  dialogVisible.value = false
  formLoading.value = false
  unref(uploadRef)?.clearFiles()
  
  // 提示成功，并刷新，传递上传成功的响应信息
  message.success(t('common.createSuccess'))
  emit('success', response)
}

/** 上传错误提示 */
const submitFormError = (): void => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 重置表单 */
const resetForm = () => {
  // 重置上传状态和文件
  formLoading.value = false
  uploadRef.value?.clearFiles()
  form.syncDify = true
  form.difyDatasetId = ''
  form.path = ''
  form.file = undefined
}

/** 文件数超出提示 */
const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}
</script>

<style>
.el-form-item-msg {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style> 