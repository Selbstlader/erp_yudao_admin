<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="所属仓库" prop="warehouseId">
        <el-select v-model="formData.warehouseId" placeholder="请选择所属仓库" :disabled="viewMode" class="w-full">
          <el-option v-for="warehouse in warehouseOptions" :key="warehouse.id" :label="warehouse.name" :value="warehouse.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="国家代码" prop="countryCode">
        <el-input v-model="formData.countryCode" placeholder="请输入国家代码，如：US" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="国家名称" prop="countryName">
        <el-input v-model="formData.countryName" placeholder="请输入国家名称，如：美国" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="详细地址" prop="address">
        <el-input v-model="formData.address" placeholder="请输入详细地址" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="联系人" prop="contactName">
        <el-input v-model="formData.contactName" placeholder="请输入联系人" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="电子邮箱" prop="contactEmail">
        <el-input v-model="formData.contactEmail" placeholder="请输入电子邮箱" :disabled="viewMode" />
      </el-form-item>
      <el-form-item label="是否保税仓" prop="isBonded">
        <el-radio-group v-model="formData.isBonded" :disabled="viewMode">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
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
import { WarehouseInternationalApi, WarehouseInternationalVO } from '@/api/erp/logistics/warehouse'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import request from '@/config/axios'

/** ERP 国际仓库表单 */
defineOptions({ name: 'WarehouseForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

// 从API获取仓库数据
const warehouseOptions = ref<any[]>([])
const loadWarehouses = async () => {
  try {
    const res = await request.get({ 
      url: '/erp/warehouse/simple-list'
    })
    warehouseOptions.value = res || []
  } catch (error) {
    console.error('获取仓库列表失败', error)
  }
}

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改；view - 查看
const viewMode = ref(false) // 是否为查看模式
const formData = ref({
  id: undefined,
  warehouseId: undefined,
  countryCode: '',
  countryName: '',
  address: '',
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  isBonded: false,
  status: 1,
  remark: ''
})
const formRules = reactive({
  warehouseId: [{ required: true, message: '所属仓库不能为空', trigger: 'change' }],
  countryCode: [{ required: true, message: '国家代码不能为空', trigger: 'blur' }],
  countryName: [{ required: true, message: '国家名称不能为空', trigger: 'blur' }],
  address: [{ required: true, message: '详细地址不能为空', trigger: 'blur' }],
  contactName: [{ required: true, message: '联系人不能为空', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '联系电话不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  formType.value = type
  resetForm()
  
  // 加载仓库选项
  await loadWarehouses()
  
  // 设置标题和模式
  if (type === 'create') {
    dialogTitle.value = '新增国际仓库'
    viewMode.value = false
  } else if (type === 'update') {
    dialogTitle.value = '修改国际仓库'
    viewMode.value = false
    await loadFormData(id)
  } else if (type === 'view') {
    dialogTitle.value = '查看国际仓库详情'
    viewMode.value = true
    await loadFormData(id)
  }
}

/** 加载表单数据 */
const loadFormData = async (id?: number) => {
  if (!id) return
  formLoading.value = true
  try {
    formData.value = await WarehouseInternationalApi.getWarehouseInternational(id)
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
    const data = formData.value as unknown as WarehouseInternationalVO
    if (formType.value === 'create') {
      await WarehouseInternationalApi.createWarehouseInternational(data)
      message.success(t('common.createSuccess'))
    } else {
      await WarehouseInternationalApi.updateWarehouseInternational(data)
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
    warehouseId: undefined,
    countryCode: '',
    countryName: '',
    address: '',
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    isBonded: false,
    status: 1,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script> 