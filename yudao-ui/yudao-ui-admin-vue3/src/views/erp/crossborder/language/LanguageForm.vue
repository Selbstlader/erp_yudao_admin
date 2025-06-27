<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="语言代码" prop="code">
        <el-input v-model="formData.code" placeholder="请输入语言代码，如：zh-CN" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="语言名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入语言名称，如：简体中文" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="英文名称" prop="englishName">
        <el-input v-model="formData.englishName" placeholder="请输入英文名称，如：Chinese Simplified" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status" :disabled="viewMode">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="默认语言" prop="isDefault">
        <el-radio-group v-model="formData.isDefault" :disabled="viewMode">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="formData.sort" controls-position="right" :min="0" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          placeholder="请输入备注"
          :rows="3"
          :disabled="viewMode"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <template v-if="viewMode">
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </template>
      <template v-else>
        <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </template>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { LanguageApi, LanguageVO } from '@/api/erp/crossborder/language'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** ERP 多语言 表单 */
defineOptions({ name: 'LanguageForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改；view - 查看
const viewMode = ref(false) // 是否为查看模式
const formData = ref({
  id: undefined,
  code: '',
  name: '',
  englishName: '',
  status: 1,
  isDefault: false,
  sort: 0,
  remark: ''
})
const formRules = reactive({
  code: [{ required: true, message: '语言代码不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '语言名称不能为空', trigger: 'blur' }],
  englishName: [{ required: true, message: '英文名称不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
  isDefault: [{ required: true, message: '是否默认语言不能为空', trigger: 'change' }],
  sort: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  formType.value = type
  resetForm()
  
  // 设置标题和模式
  if (type === 'create') {
    dialogTitle.value = '新增语言'
    viewMode.value = false
  } else if (type === 'update') {
    dialogTitle.value = '修改语言'
    viewMode.value = false
    await loadFormData(id)
  } else if (type === 'view') {
    dialogTitle.value = '查看语言详情'
    viewMode.value = true
    await loadFormData(id)
  }
}

/** 加载表单数据 */
const loadFormData = async (id?: number) => {
  if (!id) return
  formLoading.value = true
  try {
    formData.value = await LanguageApi.getLanguage(id)
  } finally {
    formLoading.value = false
  }
}

defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as LanguageVO
    if (formType.value === 'create') {
      await LanguageApi.createLanguage(data)
      message.success(t('common.createSuccess'))
    } else {
      await LanguageApi.updateLanguage(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    code: '',
    name: '',
    englishName: '',
    status: 1,
    isDefault: false,
    sort: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
