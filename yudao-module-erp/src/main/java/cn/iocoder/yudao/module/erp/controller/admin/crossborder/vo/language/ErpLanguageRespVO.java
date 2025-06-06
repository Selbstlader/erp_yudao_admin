package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 多语言 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpLanguageRespVO {

    @Schema(description = "语言编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("语言编号")
    private Long id;

    @Schema(description = "语言代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "zh-CN")
    @ExcelProperty("语言代码")
    private String code;

    @Schema(description = "语言名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "简体中文")
    @ExcelProperty("语言名称")
    private String name;

    @Schema(description = "语言英文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Chinese Simplified")
    @ExcelProperty("语言英文名称")
    private String englishName;

    @Schema(description = "语言状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("语言状态")
    private Integer status;

    @Schema(description = "是否默认语言", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @ExcelProperty("是否默认语言")
    private Boolean isDefault;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "备注", example = "系统默认语言")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
