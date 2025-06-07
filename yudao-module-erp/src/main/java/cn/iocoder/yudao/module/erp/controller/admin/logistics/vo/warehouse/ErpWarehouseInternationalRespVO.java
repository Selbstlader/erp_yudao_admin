package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 国际仓库信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpWarehouseInternationalRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "关联基础仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @ExcelProperty("关联基础仓库ID")
    private Long warehouseId;

    @Schema(description = "国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @ExcelProperty("国家代码")
    private String countryCode;

    @Schema(description = "地区/州/省", example = "California")
    @ExcelProperty("地区/州/省")
    private String region;

    @Schema(description = "城市", example = "Los Angeles")
    @ExcelProperty("城市")
    private String city;

    @Schema(description = "邮政编码", example = "90001")
    @ExcelProperty("邮政编码")
    private String postalCode;

    @Schema(description = "详细地址", example = "123 Main St")
    @ExcelProperty("详细地址")
    private String address;

    @Schema(description = "联系人", example = "John Doe")
    @ExcelProperty("联系人")
    private String contactName;

    @Schema(description = "联系电话", example = "1234567890")
    @ExcelProperty("联系电话")
    private String contactPhone;

    @Schema(description = "联系邮箱", example = "john@example.com")
    @ExcelProperty("联系邮箱")
    private String contactEmail;

    @Schema(description = "海关编码", example = "US12345")
    @ExcelProperty("海关编码")
    private String customsCode;

    @Schema(description = "是否保税仓", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @ExcelProperty("是否保税仓")
    private Boolean isBonded;

    @Schema(description = "增值税号", example = "VAT123456")
    @ExcelProperty("增值税号")
    private String vatNumber;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "备注", example = "北美主要仓库")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

} 