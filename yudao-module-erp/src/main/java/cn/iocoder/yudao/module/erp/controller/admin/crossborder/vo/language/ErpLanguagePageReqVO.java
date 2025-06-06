package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - ERP 多语言分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErpLanguagePageReqVO extends PageParam {

    @Schema(description = "语言名称", example = "简体中文")
    private String name;

    @Schema(description = "语言代码", example = "zh-CN")
    private String code;

    @Schema(description = "语言状态", example = "1")
    private Integer status;

}
