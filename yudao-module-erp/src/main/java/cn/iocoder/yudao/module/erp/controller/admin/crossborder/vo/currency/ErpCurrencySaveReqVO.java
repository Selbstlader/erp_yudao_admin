package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 多币种新增/修改 Request VO")
@Data
public class ErpCurrencySaveReqVO {

    @Schema(description = "币种编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "币种代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "CNY")
    @NotEmpty(message = "币种代码不能为空")
    private String code;

    @Schema(description = "币种名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "人民币")
    @NotEmpty(message = "币种名称不能为空")
    private String name;

    @Schema(description = "币种英文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Chinese Yuan")
    @NotEmpty(message = "币种英文名称不能为空")
    private String englishName;

    @Schema(description = "币种符号", requiredMode = Schema.RequiredMode.REQUIRED, example = "¥")
    @NotEmpty(message = "币种符号不能为空")
    private String symbol;

    @Schema(description = "币种状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "币种状态不能为空")
    private Integer status;

    @Schema(description = "是否基础币种", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @NotNull(message = "是否基础币种不能为空")
    private Boolean isBase;

    @Schema(description = "汇率", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.0")
    @NotNull(message = "汇率不能为空")
    @DecimalMin(value = "0", message = "汇率必须大于0")
    private BigDecimal exchangeRate;

    @Schema(description = "小数位数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "小数位数不能为空")
    private Integer decimalPlaces;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注", example = "系统基础币种")
    private String remark;

}
