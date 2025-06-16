package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - Dify配置更新 Request VO")
@Data
public class DifyConfigUpdateReqVO {

    @Schema(description = "是否启用Dify", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled;

    @Schema(description = "是否开启自动同步", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否开启自动同步不能为空")
    private Boolean autoSync;

    @Schema(description = "默认知识库ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1024")
    private String defaultDatasetId;
} 