package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - Dify知识库分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DifyDatasetPageReqVO extends PageParam {

    @Schema(description = "页码，从1开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    @Override
    public Integer getPageNo() {
        return super.getPageNo();
    }

    @Schema(description = "每页条数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "每页条数不能为空")
    @Override
    public Integer getPageSize() {
        return super.getPageSize();
    }
} 