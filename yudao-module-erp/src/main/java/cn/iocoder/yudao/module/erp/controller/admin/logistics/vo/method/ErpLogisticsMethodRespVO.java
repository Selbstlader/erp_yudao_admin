package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 物流方式 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpLogisticsMethodRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "物流服务商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @ExcelProperty("物流服务商编号")
    private Long providerId;
    
    @Schema(description = "物流服务商名称", example = "DHL Express")
    @ExcelProperty("物流服务商名称")
    private String providerName;

    @Schema(description = "方式代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "EXPRESS")
    @ExcelProperty("方式代码")
    private String code;

    @Schema(description = "方式名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "快递")
    @ExcelProperty("方式名称")
    private String name;

    @Schema(description = "预计送达天数", example = "3")
    @ExcelProperty("预计送达天数")
    private Integer estimatedDays;

    @Schema(description = "支持的国家代码", example = "US,UK,DE,FR")
    @ExcelProperty("支持的国家代码")
    private String supportedCountries;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "备注", example = "国际快递服务")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

} 