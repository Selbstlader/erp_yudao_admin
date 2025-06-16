package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 文件Dify同步 Response VO")
@Data
public class FileDifySyncRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "文件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long fileId;

    @Schema(description = "Dify文档ID", example = "dify_doc_123")
    private String difyDocumentId;

    @Schema(description = "同步状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer syncStatus;
    
    @Schema(description = "同步状态名", requiredMode = Schema.RequiredMode.REQUIRED, example = "已同步")
    private String syncStatusName;

    @Schema(description = "错误信息", example = "网络连接失败")
    private String errorMessage;

    @Schema(description = "错误码", example = "API_ERROR")
    private String errorCode;

    @Schema(description = "知识库ID", example = "dataset_123")
    private String datasetId;

    @Schema(description = "批处理ID", example = "batch_123")
    private String batchId;

    @Schema(description = "重试次数", example = "3")
    private Integer retryCount;

    @Schema(description = "下次重试时间")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime nextRetryTime;

    @Schema(description = "上次同步时间")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime syncTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;
} 