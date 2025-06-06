package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - ERP 多语言新增/修改 Request VO")
@Data
public class ErpLanguageSaveReqVO {

    @Schema(description = "语言编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "语言代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "zh-CN")
    @NotEmpty(message = "语言代码不能为空")
    private String code;

    @Schema(description = "语言名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "简体中文")
    @NotEmpty(message = "语言名称不能为空")
    private String name;

    @Schema(description = "语言英文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Chinese Simplified")
    @NotEmpty(message = "语言英文名称不能为空")
    private String englishName;

    @Schema(description = "语言状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "语言状态不能为空")
    private Integer status;

    @Schema(description = "是否默认语言", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @NotNull(message = "是否默认语言不能为空")
    private Boolean isDefault;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注", example = "系统默认语言")
    private String remark;

}
