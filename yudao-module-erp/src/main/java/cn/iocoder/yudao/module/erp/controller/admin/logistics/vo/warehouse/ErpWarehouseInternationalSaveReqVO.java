package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - ERP 国际仓库信息新增/修改 Request VO")
@Data
public class ErpWarehouseInternationalSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "关联基础仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "关联基础仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotEmpty(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "地区/州/省", example = "California")
    private String region;

    @Schema(description = "城市", example = "Los Angeles")
    private String city;

    @Schema(description = "邮政编码", example = "90001")
    private String postalCode;

    @Schema(description = "详细地址", example = "123 Main St")
    private String address;

    @Schema(description = "联系人", example = "John Doe")
    private String contactName;

    @Schema(description = "联系电话", example = "1234567890")
    private String contactPhone;

    @Schema(description = "联系邮箱", example = "john@example.com")
    private String contactEmail;

    @Schema(description = "海关编码", example = "US12345")
    private String customsCode;

    @Schema(description = "是否保税仓", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否保税仓不能为空")
    private Boolean isBonded;

    @Schema(description = "增值税号", example = "VAT123456")
    private String vatNumber;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "北美主要仓库")
    private String remark;

} 