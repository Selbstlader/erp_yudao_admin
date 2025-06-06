package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 汇率 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpExchangeRateRespVO {

    @Schema(description = "汇率编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("汇率编号")
    private Long id;

    @Schema(description = "源币种编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long fromCurrencyId;
    
    @Schema(description = "源币种名称", example = "人民币")
    @ExcelProperty("源币种名称")
    private String fromCurrencyName;

    @Schema(description = "目标币种编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long toCurrencyId;
    
    @Schema(description = "目标币种名称", example = "美元")
    @ExcelProperty("目标币种名称")
    private String toCurrencyName;

    @Schema(description = "汇率", requiredMode = Schema.RequiredMode.REQUIRED, example = "6.8")
    @ExcelProperty("汇率")
    private BigDecimal rate;

    @Schema(description = "生效日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01")
    @ExcelProperty("生效日期")
    private LocalDate effectiveDate;

    @Schema(description = "失效日期", example = "2023-12-31")
    @ExcelProperty("失效日期")
    private LocalDate expiryDate;

    @Schema(description = "汇率状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("汇率状态")
    private Integer status;

    @Schema(description = "汇率来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("汇率来源")
    private Integer source;

    @Schema(description = "备注", example = "手动录入的汇率")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
