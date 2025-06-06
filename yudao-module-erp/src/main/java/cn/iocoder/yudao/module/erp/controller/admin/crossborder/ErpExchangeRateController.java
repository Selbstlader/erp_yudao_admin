package cn.iocoder.yudao.module.erp.controller.admin.crossborder;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRatePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRateRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRateSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpExchangeRateDO;
import cn.iocoder.yudao.module.erp.service.crossborder.ErpExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Tag(name = "管理后台 - ERP 汇率")
@RestController
@RequestMapping("/erp/exchange-rate")
@Validated
public class ErpExchangeRateController {

    @Resource
    private ErpExchangeRateService exchangeRateService;

    @PostMapping("/create")
    @Operation(summary = "创建汇率")
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:create')")
    public CommonResult<Long> createExchangeRate(@Valid @RequestBody ErpExchangeRateSaveReqVO createReqVO) {
        return success(exchangeRateService.createExchangeRate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新汇率")
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:update')")
    public CommonResult<Boolean> updateExchangeRate(@Valid @RequestBody ErpExchangeRateSaveReqVO updateReqVO) {
        exchangeRateService.updateExchangeRate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除汇率")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:delete')")
    public CommonResult<Boolean> deleteExchangeRate(@RequestParam("id") Long id) {
        exchangeRateService.deleteExchangeRate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得汇率")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:query')")
    public CommonResult<ErpExchangeRateRespVO> getExchangeRate(@RequestParam("id") Long id) {
        ErpExchangeRateDO exchangeRate = exchangeRateService.getExchangeRate(id);
        return success(BeanUtils.toBean(exchangeRate, ErpExchangeRateRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得汇率分页")
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:query')")
    public CommonResult<PageResult<ErpExchangeRateRespVO>> getExchangeRatePage(@Valid ErpExchangeRatePageReqVO pageReqVO) {
        PageResult<ErpExchangeRateRespVO> pageResult = exchangeRateService.getExchangeRatePage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得汇率精简列表")
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:query')")
    public CommonResult<List<ErpExchangeRateRespVO>> getExchangeRateSimpleList() {
        List<ErpExchangeRateRespVO> list = exchangeRateService.getExchangeRateListByStatus(1); // 1 表示启用状态
        return success(list);
    }

    @GetMapping("/latest")
    @Operation(summary = "获得最新汇率")
    @Parameter(name = "fromCurrencyId", description = "源币种编号", required = true)
    @Parameter(name = "toCurrencyId", description = "目标币种编号", required = true)
    @Parameter(name = "date", description = "日期", required = true)
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:query')")
    public CommonResult<ErpExchangeRateRespVO> getLatestExchangeRate(@RequestParam("fromCurrencyId") Long fromCurrencyId,
                                                                    @RequestParam("toCurrencyId") Long toCurrencyId,
                                                                    @RequestParam("date") @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate date) {
        ErpExchangeRateDO exchangeRate = exchangeRateService.getLatestExchangeRate(fromCurrencyId, toCurrencyId, date);
        return success(BeanUtils.toBean(exchangeRate, ErpExchangeRateRespVO.class));
    }

    @GetMapping("/convert")
    @Operation(summary = "币种转换")
    @Parameter(name = "amount", description = "金额", required = true)
    @Parameter(name = "fromCurrencyId", description = "源币种编号", required = true)
    @Parameter(name = "toCurrencyId", description = "目标币种编号", required = true)
    @Parameter(name = "date", description = "日期", required = true)
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:query')")
    public CommonResult<BigDecimal> convertCurrency(@RequestParam("amount") BigDecimal amount,
                                                   @RequestParam("fromCurrencyId") Long fromCurrencyId,
                                                   @RequestParam("toCurrencyId") Long toCurrencyId,
                                                   @RequestParam("date") @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate date) {
        BigDecimal result = exchangeRateService.convertCurrencyByRate(amount, fromCurrencyId, toCurrencyId, date);
        return success(result);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出汇率 Excel")
    @PreAuthorize("@ss.hasPermission('erp:exchange-rate:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExchangeRateExcel(@Valid ErpExchangeRatePageReqVO pageReqVO,
                                       HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpExchangeRateRespVO> list = exchangeRateService.getExchangeRatePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "汇率.xls", "数据", ErpExchangeRateRespVO.class, list);
    }

}
