package cn.iocoder.yudao.module.erp.controller.admin.logistics;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsMethodDO;
import cn.iocoder.yudao.module.erp.service.logistics.ErpLogisticsMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 物流方式")
@RestController
@RequestMapping("/erp/logistics-method")
@Validated
public class ErpLogisticsMethodController {

    @Resource
    private ErpLogisticsMethodService logisticsMethodService;

    @PostMapping("/create")
    @Operation(summary = "创建物流方式")
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:create')")
    public CommonResult<Long> createLogisticsMethod(@Valid @RequestBody ErpLogisticsMethodSaveReqVO createReqVO) {
        return success(logisticsMethodService.createLogisticsMethod(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物流方式")
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:update')")
    public CommonResult<Boolean> updateLogisticsMethod(@Valid @RequestBody ErpLogisticsMethodSaveReqVO updateReqVO) {
        logisticsMethodService.updateLogisticsMethod(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物流方式")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:delete')")
    public CommonResult<Boolean> deleteLogisticsMethod(@RequestParam("id") Long id) {
        logisticsMethodService.deleteLogisticsMethod(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物流方式")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:query')")
    public CommonResult<ErpLogisticsMethodRespVO> getLogisticsMethod(@RequestParam("id") Long id) {
        ErpLogisticsMethodDO logisticsMethod = logisticsMethodService.getLogisticsMethod(id);
        return success(BeanUtils.toBean(logisticsMethod, ErpLogisticsMethodRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物流方式分页")
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:query')")
    public CommonResult<PageResult<ErpLogisticsMethodRespVO>> getLogisticsMethodPage(@Valid ErpLogisticsMethodPageReqVO pageReqVO) {
        PageResult<ErpLogisticsMethodRespVO> pageResult = logisticsMethodService.getLogisticsMethodPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得物流方式精简列表")
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:query')")
    public CommonResult<List<ErpLogisticsMethodRespVO>> getLogisticsMethodSimpleList() {
        List<ErpLogisticsMethodRespVO> list = logisticsMethodService.getLogisticsMethodListByStatus(1); // 1 表示启用状态
        return success(list);
    }
    
    @GetMapping("/list-by-provider")
    @Operation(summary = "根据服务商编号获得物流方式列表")
    @Parameter(name = "providerId", description = "服务商编号", required = true, example = "1001")
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:query')")
    public CommonResult<List<ErpLogisticsMethodRespVO>> getLogisticsMethodListByProviderId(@RequestParam("providerId") Long providerId) {
        List<ErpLogisticsMethodRespVO> list = logisticsMethodService.getLogisticsMethodListByProviderId(providerId);
        return success(list);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物流方式 Excel")
    @PreAuthorize("@ss.hasPermission('erp:logistics-method:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLogisticsMethodExcel(@Valid ErpLogisticsMethodPageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpLogisticsMethodRespVO> list = logisticsMethodService.getLogisticsMethodPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物流方式.xls", "数据", ErpLogisticsMethodRespVO.class, list);
    }

} 