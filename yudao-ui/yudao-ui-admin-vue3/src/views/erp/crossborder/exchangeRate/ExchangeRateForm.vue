<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="源币种" prop="fromCurrencyId">
        <el-select v-model="formData.fromCurrencyId" placeholder="请选择源币种" :disabled="viewMode" filterable>
          <el-option v-for="item in currencyOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="目标币种" prop="toCurrencyId">
        <el-select v-model="formData.toCurrencyId" placeholder="请选择目标币种" :disabled="viewMode" filterable>
          <el-option v-for="item in currencyOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="汇率" prop="rate">
        <el-input-number v-model="formData.rate" :precision="6" :step="0.01" :min="0.000001" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="生效日期" prop="effectiveDate">
        <el-date-picker v-model="formData.effectiveDate" type="date" placeholder="请选择生效日期" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="失效日期" prop="expiryDate">
        <el-date-picker v-model="formData.expiryDate" type="date" placeholder="请选择失效日期" :disabled="viewMode" />
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
      <el-form-item label="汇率来源" prop="source">
        <el-radio-group v-model="formData.source" :disabled="viewMode">
          <el-radio
            v-for="dict in getIntDictOptions('erp_exchange_rate_source')"
            :key="dict.value"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
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
import { ExchangeRateApi, ExchangeRateVO } from '@/api/erp/crossborder/exchangeRate'
import { CurrencyApi } from '@/api/erp/crossborder/currency'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

// 定义币种选项类型
interface CurrencyOption {
  id: number;
  name: string;
  code: string;
}

// 定义表单数据类型
interface FormData {
  id?: number;
  fromCurrencyId?: number;
  fromCurrencyName: string;
  toCurrencyId?: number;
  toCurrencyName: string;
  rate: number;
  effectiveDate?: Date;
  expiryDate?: Date;
  status: number;
  source: number;
  remark: string;
}

/** ERP 汇率表单 */
defineOptions({ name: 'ExchangeRateForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改；view - 查看
const viewMode = ref(false) // 是否为查看模式
const currencyOptions = ref<CurrencyOption[]>([]) // 币种选项
const formData = ref<FormData>({
  id: undefined,
  fromCurrencyId: undefined,
  fromCurrencyName: '',
  toCurrencyId: undefined,
  toCurrencyName: '',
  rate: 1.0,
  effectiveDate: undefined,
  expiryDate: undefined,
  status: 1,
  source: 1, // 1-手动录入，2-自动获取
  remark: ''
})
const formRules = reactive({
  fromCurrencyId: [{ required: true, message: '源币种不能为空', trigger: 'change' }],
  toCurrencyId: [{ required: true, message: '目标币种不能为空', trigger: 'change' }],
  rate: [{ required: true, message: '汇率不能为空', trigger: 'blur' }],
  effectiveDate: [{ required: true, message: '生效日期不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
  source: [{ required: true, message: '汇率来源不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

/** 获取币种选项 */
const getCurrencyOptions = async () => {
  try {
    currencyOptions.value = await CurrencyApi.getCurrencySimpleList()
  } catch (error) {
    console.error('获取币种选项失败', error)
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  formType.value = type
  resetForm()
  
  // 获取币种选项
  await getCurrencyOptions()
  
  // 设置标题和模式
  if (type === 'create') {
    dialogTitle.value = '新增汇率'
    viewMode.value = false
    // 设置默认日期
    formData.value.effectiveDate = new Date()
    const expiryDate = new Date()
    expiryDate.setFullYear(expiryDate.getFullYear() + 1)
    formData.value.expiryDate = expiryDate
  } else if (type === 'update') {
    dialogTitle.value = '修改汇率'
    viewMode.value = false
    await loadFormData(id)
  } else if (type === 'view') {
    dialogTitle.value = '查看汇率详情'
    viewMode.value = true
    await loadFormData(id)
  }
}

/** 加载表单数据 */
const loadFormData = async (id?: number) => {
  if (!id) return
  formLoading.value = true
  try {
    formData.value = await ExchangeRateApi.getExchangeRate(id)
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
    const data = formData.value as unknown as ExchangeRateVO
    if (formType.value === 'create') {
      await ExchangeRateApi.createExchangeRate(data)
      message.success(t('common.createSuccess'))
    } else {
      await ExchangeRateApi.updateExchangeRate(data)
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
    fromCurrencyId: undefined,
    fromCurrencyName: '',
    toCurrencyId: undefined,
    toCurrencyName: '',
    rate: 1.0,
    effectiveDate: undefined,
    expiryDate: undefined,
    status: 1,
    source: 1,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script> 