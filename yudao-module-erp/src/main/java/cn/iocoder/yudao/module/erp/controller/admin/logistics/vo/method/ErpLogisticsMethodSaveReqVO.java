package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - ERP 物流方式新增/修改 Request VO")
@Data
public class ErpLogisticsMethodSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "物流服务商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "物流服务商编号不能为空")
    private Long providerId;

    @Schema(description = "方式代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "EXPRESS")
    @NotEmpty(message = "方式代码不能为空")
    private String code;

    @Schema(description = "方式名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "快递")
    @NotEmpty(message = "方式名称不能为空")
    private String name;

    @Schema(description = "预计送达天数", example = "3")
    private Integer estimatedDays;

    @Schema(description = "支持的国家代码", example = "US,UK,DE,FR")
    private String supportedCountries;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注", example = "国际快递服务")
    private String remark;

} 