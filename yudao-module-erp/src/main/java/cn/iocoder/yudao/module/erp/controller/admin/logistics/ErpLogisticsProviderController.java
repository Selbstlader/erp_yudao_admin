package cn.iocoder.yudao.module.erp.controller.admin.logistics;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsProviderDO;
import cn.iocoder.yudao.module.erp.service.logistics.ErpLogisticsProviderService;
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
import java.util.Map;
import java.util.HashMap;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 物流服务商")
@RestController
@RequestMapping("/erp/logistics-provider")
@Validated
public class ErpLogisticsProviderController {

    @Resource
    private ErpLogisticsProviderService logisticsProviderService;

    @PostMapping("/create")
    @Operation(summary = "创建物流服务商")
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:create')")
    public CommonResult<Long> createLogisticsProvider(@Valid @RequestBody ErpLogisticsProviderSaveReqVO createReqVO) {
        return success(logisticsProviderService.createLogisticsProvider(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物流服务商")
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:update')")
    public CommonResult<Boolean> updateLogisticsProvider(@Valid @RequestBody ErpLogisticsProviderSaveReqVO updateReqVO) {
        logisticsProviderService.updateLogisticsProvider(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物流服务商")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:delete')")
    public CommonResult<Boolean> deleteLogisticsProvider(@RequestParam("id") Long id) {
        logisticsProviderService.deleteLogisticsProvider(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物流服务商")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:query')")
    public CommonResult<ErpLogisticsProviderRespVO> getLogisticsProvider(@RequestParam("id") Long id) {
        ErpLogisticsProviderDO logisticsProvider = logisticsProviderService.getLogisticsProvider(id);
        return success(BeanUtils.toBean(logisticsProvider, ErpLogisticsProviderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物流服务商分页")
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:query')")
    public CommonResult<PageResult<ErpLogisticsProviderRespVO>> getLogisticsProviderPage(@Valid ErpLogisticsProviderPageReqVO pageReqVO) {
        PageResult<ErpLogisticsProviderRespVO> pageResult = logisticsProviderService.getLogisticsProviderPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得物流服务商精简列表")
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:query')")
    public CommonResult<List<ErpLogisticsProviderRespVO>> getLogisticsProviderSimpleList() {
        List<ErpLogisticsProviderRespVO> list = logisticsProviderService.getLogisticsProviderListByStatus(1); // 1 表示启用状态
        return success(list);
    }

    @PostMapping("/test-connection")
    @Operation(summary = "测试物流服务商API连接")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:update')")
    public CommonResult<Map<String, Object>> testLogisticsProviderConnection(@RequestParam("id") Long id) {
        boolean success = logisticsProviderService.testLogisticsProviderConnection(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "连接成功" : "连接失败");
        return success(result);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物流服务商 Excel")
    @PreAuthorize("@ss.hasPermission('erp:logistics-provider:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLogisticsProviderExcel(@Valid ErpLogisticsProviderPageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpLogisticsProviderRespVO> list = logisticsProviderService.getLogisticsProviderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物流服务商.xls", "数据", ErpLogisticsProviderRespVO.class, list);
    }

} 