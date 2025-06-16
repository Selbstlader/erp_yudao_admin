package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - Dify配置 Response VO")
@Data
public class DifyConfigRespVO {

    @Schema(description = "是否启用Dify", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean enabled;

    @Schema(description = "是否开启自动同步", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean autoSync;

    @Schema(description = "默认知识库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String defaultDatasetId;

    @Schema(description = "API基础路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://api.dify.ai")
    private String baseUrl;
} 