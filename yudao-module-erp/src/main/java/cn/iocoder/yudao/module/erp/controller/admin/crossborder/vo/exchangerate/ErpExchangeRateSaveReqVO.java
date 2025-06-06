package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - ERP 汇率新增/修改 Request VO")
@Data
public class ErpExchangeRateSaveReqVO {

    @Schema(description = "汇率编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "源币种编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "源币种编号不能为空")
    private Long fromCurrencyId;

    @Schema(description = "目标币种编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "目标币种编号不能为空")
    private Long toCurrencyId;

    @Schema(description = "汇率", requiredMode = Schema.RequiredMode.REQUIRED, example = "6.8")
    @NotNull(message = "汇率不能为空")
    @DecimalMin(value = "0", message = "汇率必须大于0")
    private BigDecimal rate;

    @Schema(description = "生效日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01")
    @NotNull(message = "生效日期不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate effectiveDate;

    @Schema(description = "失效日期", example = "2023-12-31")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate expiryDate;

    @Schema(description = "汇率状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "汇率状态不能为空")
    private Integer status;

    @Schema(description = "汇率来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "汇率来源不能为空")
    private Integer source;

    @Schema(description = "备注", example = "手动录入的汇率")
    private String remark;

}
