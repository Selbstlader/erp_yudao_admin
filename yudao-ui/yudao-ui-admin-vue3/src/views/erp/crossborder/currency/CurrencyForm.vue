<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="币种代码" prop="code">
        <el-input v-model="formData.code" placeholder="请输入币种代码，如：CNY" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="币种名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入币种名称，如：人民币" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="英文名称" prop="englishName">
        <el-input v-model="formData.englishName" placeholder="请输入英文名称，如：Chinese Yuan" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="币种符号" prop="symbol">
        <el-input v-model="formData.symbol" placeholder="请输入币种符号，如：¥" :disabled="viewMode" />
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
      <el-form-item label="基础币种" prop="isBase">
        <el-radio-group v-model="formData.isBase" :disabled="viewMode">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="汇率" prop="exchangeRate">
        <el-input-number v-model="formData.exchangeRate" :precision="6" :step="0.1" :disabled="viewMode || formData.isBase" />
        <span class="text-gray-400 ml-2" v-if="formData.isBase">基础币种汇率固定为1.0</span>
      </el-form-item>
      <el-form-item label="小数位数" prop="decimalPlaces">
        <el-input-number v-model="formData.decimalPlaces" :min="0" :max="6" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="formData.sort" :min="0" :max="9999" controls-position="right" :disabled="viewMode" />
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
import { CurrencyApi, CurrencyVO } from '@/api/erp/crossborder/currency'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** ERP 币种表单 */
defineOptions({ name: 'CurrencyForm' })

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
  symbol: '',
  status: 1,
  isBase: false,
  exchangeRate: 1.0,
  decimalPlaces: 2,
  sort: 0,
  remark: ''
})
const formRules = reactive({
  code: [{ required: true, message: '币种代码不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '币种名称不能为空', trigger: 'blur' }],
  englishName: [{ required: true, message: '英文名称不能为空', trigger: 'blur' }],
  symbol: [{ required: true, message: '币种符号不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
  isBase: [{ required: true, message: '是否基础币种不能为空', trigger: 'change' }],
  exchangeRate: [{ required: true, message: '汇率不能为空', trigger: 'blur' }],
  decimalPlaces: [{ required: true, message: '小数位数不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

// 监听基础币种变化
watch(() => formData.value.isBase, (newVal) => {
  if (newVal === true) {
    formData.value.exchangeRate = 1.0;
  }
});

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  formType.value = type
  resetForm()
  
  // 设置标题和模式
  if (type === 'create') {
    dialogTitle.value = '新增币种'
    viewMode.value = false
  } else if (type === 'update') {
    dialogTitle.value = '修改币种'
    viewMode.value = false
    await loadFormData(id)
  } else if (type === 'view') {
    dialogTitle.value = '查看币种详情'
    viewMode.value = true
    await loadFormData(id)
  }
}

/** 加载表单数据 */
const loadFormData = async (id?: number) => {
  if (!id) return
  formLoading.value = true
  try {
    formData.value = await CurrencyApi.getCurrency(id)
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
    const data = formData.value as unknown as CurrencyVO
    if (formType.value === 'create') {
      await CurrencyApi.createCurrency(data)
      message.success(t('common.createSuccess'))
    } else {
      await CurrencyApi.updateCurrency(data)
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
    symbol: '',
    status: 1,
    isBase: false,
    exchangeRate: 1.0,
    decimalPlaces: 2,
    sort: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script> 