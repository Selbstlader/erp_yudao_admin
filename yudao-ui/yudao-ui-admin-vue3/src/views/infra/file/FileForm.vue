<template>
  <Dialog v-model="dialogVisible" title="上传文件">
    <el-upload
      ref="uploadRef"
      v-model:file-list="fileList"
      :action="uploadUrl"
      :auto-upload="false"
      :data="data"
      :disabled="formLoading"
      :limit="1"
      :on-change="handleFileChange"
      :on-error="submitFormError"
      :on-exceed="handleExceed"
      :on-success="submitFormSuccess"
      :before-upload="beforeUpload"
      :http-request="httpRequest"
      accept=".docx,.xlsx,.pptx"
      drag
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text"> 将文件拖到此处，或 <em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip" style="color: red">
          提示：仅支持上传 docx、xlsx、pptx 格式文件！上传后将自动同步到Dify知识库（如已启用自动同步）
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitFileForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { useUpload } from '@/components/UploadFile/src/useUpload'

defineOptions({ name: 'InfraFileForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const fileList = ref([]) // 文件列表
const data = ref({ path: '' })
const uploadRef = ref()

const { uploadUrl, httpRequest } = useUpload()

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  resetForm()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 处理上传的文件发生变化 */
const handleFileChange = (file) => {
  data.value.path = file.name
}

/** 上传前检查文件格式 */
const beforeUpload = (file) => {
  // const allowedTypes = [
  //   'application/vnd.openxmlformats-officedocument.wordprocessingml.document', // .docx
  //   'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',       // .xlsx
  //   'application/vnd.openxmlformats-officedocument.presentationml.presentation' // .pptx
  // ]
  //
  // // 检查文件扩展名
  // const extension = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
  // const validExtension = ['docx', 'xlsx', 'pptx'].includes(extension)
  //
  // // 检查MIME类型
  // const validType = allowedTypes.includes(file.type)
  //
  // if (!validExtension || !validType) {
  //   message.error('只能上传 docx、xlsx、pptx 格式的文件！')
  //   return false
  // }
  //
  return true
}

/** 提交表单 */
const submitFileForm = () => {
  if (fileList.value.length == 0) {
    message.error('请上传文件')
    return
  }
  unref(uploadRef)?.submit()
}

/** 文件上传成功处理 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitFormSuccess = (response) => {
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
}

/** 文件数超出提示 */
const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}
</script>
