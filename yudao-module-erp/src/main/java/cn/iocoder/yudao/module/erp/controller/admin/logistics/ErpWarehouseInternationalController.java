package cn.iocoder.yudao.module.erp.controller.admin.logistics;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpWarehouseInternationalDO;
import cn.iocoder.yudao.module.erp.service.logistics.ErpWarehouseInternationalService;
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

@Tag(name = "管理后台 - ERP 国际仓库")
@RestController
@RequestMapping("/erp/warehouse-international")
@Validated
public class ErpWarehouseInternationalController {

    @Resource
    private ErpWarehouseInternationalService warehouseInternationalService;

    @PostMapping("/create")
    @Operation(summary = "创建国际仓库")
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:create')")
    public CommonResult<Long> createWarehouseInternational(@Valid @RequestBody ErpWarehouseInternationalSaveReqVO createReqVO) {
        return success(warehouseInternationalService.createWarehouseInternational(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新国际仓库")
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:update')")
    public CommonResult<Boolean> updateWarehouseInternational(@Valid @RequestBody ErpWarehouseInternationalSaveReqVO updateReqVO) {
        warehouseInternationalService.updateWarehouseInternational(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除国际仓库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:delete')")
    public CommonResult<Boolean> deleteWarehouseInternational(@RequestParam("id") Long id) {
        warehouseInternationalService.deleteWarehouseInternational(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得国际仓库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:query')")
    public CommonResult<ErpWarehouseInternationalRespVO> getWarehouseInternational(@RequestParam("id") Long id) {
        ErpWarehouseInternationalDO warehouseInternational = warehouseInternationalService.getWarehouseInternational(id);
        return success(BeanUtils.toBean(warehouseInternational, ErpWarehouseInternationalRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得国际仓库分页")
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:query')")
    public CommonResult<PageResult<ErpWarehouseInternationalRespVO>> getWarehouseInternationalPage(@Valid ErpWarehouseInternationalPageReqVO pageReqVO) {
        PageResult<ErpWarehouseInternationalRespVO> pageResult = warehouseInternationalService.getWarehouseInternationalPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得国际仓库精简列表")
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:query')")
    public CommonResult<List<ErpWarehouseInternationalRespVO>> getWarehouseInternationalSimpleList() {
        List<ErpWarehouseInternationalRespVO> list = warehouseInternationalService.getWarehouseInternationalListByStatus(1); // 1 表示启用状态
        return success(list);
    }

    @GetMapping("/list-by-country")
    @Operation(summary = "根据国家代码获得国际仓库列表")
    @Parameter(name = "countryCode", description = "国家代码", required = true, example = "US")
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:query')")
    public CommonResult<List<ErpWarehouseInternationalRespVO>> getWarehouseInternationalListByCountryCode(@RequestParam("countryCode") String countryCode) {
        List<ErpWarehouseInternationalRespVO> list = warehouseInternationalService.getWarehouseInternationalListByCountryCode(countryCode);
        return success(list);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出国际仓库 Excel")
    @PreAuthorize("@ss.hasPermission('erp:warehouse-international:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWarehouseInternationalExcel(@Valid ErpWarehouseInternationalPageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpWarehouseInternationalRespVO> list = warehouseInternationalService.getWarehouseInternationalPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "国际仓库.xls", "数据", ErpWarehouseInternationalRespVO.class, list);
    }

} 