package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 多币种 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpCurrencyRespVO {

    @Schema(description = "币种编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("币种编号")
    private Long id;

    @Schema(description = "币种代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "CNY")
    @ExcelProperty("币种代码")
    private String code;

    @Schema(description = "币种名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "人民币")
    @ExcelProperty("币种名称")
    private String name;

    @Schema(description = "币种英文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Chinese Yuan")
    @ExcelProperty("币种英文名称")
    private String englishName;

    @Schema(description = "币种符号", requiredMode = Schema.RequiredMode.REQUIRED, example = "¥")
    @ExcelProperty("币种符号")
    private String symbol;

    @Schema(description = "币种状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("币种状态")
    private Integer status;

    @Schema(description = "是否基础币种", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @ExcelProperty("是否基础币种")
    private Boolean isBase;

    @Schema(description = "汇率", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.0")
    @ExcelProperty("汇率")
    private BigDecimal exchangeRate;

    @Schema(description = "小数位数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("小数位数")
    private Integer decimalPlaces;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "备注", example = "系统基础币种")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
