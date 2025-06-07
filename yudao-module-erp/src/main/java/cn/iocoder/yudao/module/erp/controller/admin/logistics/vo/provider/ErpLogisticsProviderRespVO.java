package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 物流服务商 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpLogisticsProviderRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "服务商代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "DHL")
    @ExcelProperty("服务商代码")
    private String code;

    @Schema(description = "服务商名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "DHL Express")
    @ExcelProperty("服务商名称")
    private String name;

    @Schema(description = "官网地址", example = "https://www.dhl.com")
    @ExcelProperty("官网地址")
    private String website;

    @Schema(description = "API配置", example = "{\"apiKey\":\"dhl_api_key\",\"apiSecret\":\"******\",\"apiUrl\":\"https://api.dhl.com\"}")
    private String apiConfig;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "备注", example = "全球知名物流公司")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

} 