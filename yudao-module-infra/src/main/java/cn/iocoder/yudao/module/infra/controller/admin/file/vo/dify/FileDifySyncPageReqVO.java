package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 文件Dify同步分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileDifySyncPageReqVO extends PageParam {

    @Schema(description = "文件ID", example = "1024")
    private Long fileId;

    @Schema(description = "Dify文档ID", example = "dify_doc_123")
    private String difyDocumentId;

    @Schema(description = "同步状态（0-未同步，1-同步中，2-已同步，3-同步失败）", example = "2")
    private Integer syncStatus;

    @Schema(description = "错误信息", example = "网络连接失败")
    private String errorMessage;

    @Schema(description = "知识库ID", example = "dataset_123")
    private String datasetId;
} 