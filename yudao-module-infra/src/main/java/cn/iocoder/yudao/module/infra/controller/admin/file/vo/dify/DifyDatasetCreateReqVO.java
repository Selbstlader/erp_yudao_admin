package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Schema(description = "管理后台 - 创建Dify知识库 Request VO")
@Data
public class DifyDatasetCreateReqVO {

    @Schema(description = "知识库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例知识库")
    @NotEmpty(message = "知识库名称不能为空")
    @Length(max = 50, message = "知识库名称长度不能超过50")
    private String name;

    @Schema(description = "知识库描述", example = "这是一个示例知识库")
    @Length(max = 200, message = "知识库描述长度不能超过200")
    private String description;

    @Schema(description = "权限", requiredMode = Schema.RequiredMode.REQUIRED, example = "only_me")
    @NotEmpty(message = "权限不能为空")
    @Pattern(regexp = "^(only_me|team)$", message = "权限只能是only_me或team")
    private String permission;
    
    @Schema(description = "索引技术，可选值：high_quality(高质量), economy(经济)", example = "high_quality")
    @Pattern(regexp = "^(high_quality|economy)$", message = "索引技术只能是high_quality或economy")
    private String indexingTechnique;
} 