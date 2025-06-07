package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - ERP 物流服务商新增/修改 Request VO")
@Data
public class ErpLogisticsProviderSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "服务商代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "DHL")
    @NotEmpty(message = "服务商代码不能为空")
    private String code;

    @Schema(description = "服务商名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "DHL Express")
    @NotEmpty(message = "服务商名称不能为空")
    private String name;

    @Schema(description = "官网地址", example = "https://www.dhl.com")
    private String website;

    @Schema(description = "API配置", example = "{\"apiKey\":\"dhl_api_key\",\"apiSecret\":\"dhl_api_secret\",\"apiUrl\":\"https://api.dhl.com\"}")
    private String apiConfig;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注", example = "全球知名物流公司")
    private String remark;

} 