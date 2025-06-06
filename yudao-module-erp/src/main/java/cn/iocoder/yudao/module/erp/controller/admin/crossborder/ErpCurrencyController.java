package cn.iocoder.yudao.module.erp.controller.admin.crossborder;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencyPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencyRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencySaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpCurrencyDO;
import cn.iocoder.yudao.module.erp.service.crossborder.ErpCurrencyService;
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
import java.math.BigDecimal;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 多币种")
@RestController
@RequestMapping("/erp/currency")
@Validated
public class ErpCurrencyController {

    @Resource
    private ErpCurrencyService currencyService;

    @PostMapping("/create")
    @Operation(summary = "创建多币种")
    @PreAuthorize("@ss.hasPermission('erp:currency:create')")
    public CommonResult<Long> createCurrency(@Valid @RequestBody ErpCurrencySaveReqVO createReqVO) {
        return success(currencyService.createCurrency(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新多币种")
    @PreAuthorize("@ss.hasPermission('erp:currency:update')")
    public CommonResult<Boolean> updateCurrency(@Valid @RequestBody ErpCurrencySaveReqVO updateReqVO) {
        currencyService.updateCurrency(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除多币种")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:currency:delete')")
    public CommonResult<Boolean> deleteCurrency(@RequestParam("id") Long id) {
        currencyService.deleteCurrency(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得多币种")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:currency:query')")
    public CommonResult<ErpCurrencyRespVO> getCurrency(@RequestParam("id") Long id) {
        ErpCurrencyDO currency = currencyService.getCurrency(id);
        return success(BeanUtils.toBean(currency, ErpCurrencyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得多币种分页")
    @PreAuthorize("@ss.hasPermission('erp:currency:query')")
    public CommonResult<PageResult<ErpCurrencyRespVO>> getCurrencyPage(@Valid ErpCurrencyPageReqVO pageReqVO) {
        PageResult<ErpCurrencyRespVO> pageResult = currencyService.getCurrencyPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得多币种精简列表")
    @PreAuthorize("@ss.hasPermission('erp:currency:query')")
    public CommonResult<List<ErpCurrencyRespVO>> getCurrencySimpleList() {
        List<ErpCurrencyRespVO> list = currencyService.getCurrencyListByStatus(1); // 1 表示启用状态
        return success(list);
    }

    @GetMapping("/convert")
    @Operation(summary = "币种转换")
    @Parameter(name = "amount", description = "金额", required = true)
    @Parameter(name = "fromCurrency", description = "源币种代码", required = true)
    @Parameter(name = "toCurrency", description = "目标币种代码", required = true)
    @PreAuthorize("@ss.hasPermission('erp:currency:query')")
    public CommonResult<BigDecimal> convertCurrency(@RequestParam("amount") BigDecimal amount,
                                                   @RequestParam("fromCurrency") String fromCurrency,
                                                   @RequestParam("toCurrency") String toCurrency) {
        BigDecimal result = currencyService.convertCurrency(amount, fromCurrency, toCurrency);
        return success(result);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出多币种 Excel")
    @PreAuthorize("@ss.hasPermission('erp:currency:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCurrencyExcel(@Valid ErpCurrencyPageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpCurrencyRespVO> list = currencyService.getCurrencyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "多币种.xls", "数据", ErpCurrencyRespVO.class, list);
    }

}
