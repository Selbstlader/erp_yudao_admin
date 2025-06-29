package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - Dify聊天消息响应 VO")
@Data
public class ChatMessageRespVO {

    @Schema(description = "消息ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "msg_123456")
    private String id;

    @Schema(description = "会话ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "conv_123456")
    private String conversationId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1625123456789")
    private Long createdAt;

    @Schema(description = "AI回答内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "今天北京天气晴朗，气温25℃，适合户外活动。")
    private String answer;

    @Schema(description = "引用信息")
    private List<Reference> references;

    @Schema(description = "思考过程", example = "task_123456")
    private String taskId;

    @Schema(description = "消息类型", example = "text")
    private String messageType;

    @Schema(description = "元数据")
    private Map<String, Object> metadata;

    @Schema(description = "引用信息")
    @Data
    public static class Reference {
        @Schema(description = "引用ID", example = "ref_123")
        private String id;
        
        @Schema(description = "文档ID", example = "doc_123")
        private String documentId;
        
        @Schema(description = "数据集ID", example = "dataset_123")
        private String datasetId;
        
        @Schema(description = "数据集名称", example = "天气数据集")
        private String datasetName;
        
        @Schema(description = "文件名称", example = "weather_report.pdf")
        private String fileName;
        
        @Schema(description = "文件类型", example = "pdf")
        private String fileType;
        
        @Schema(description = "引用内容", example = "北京今日天气晴朗...")
        private String content;
        
        @Schema(description = "引用相关度", example = "0.95")
        private Double score;
        
        @Schema(description = "引用位置信息")
        private Map<String, Object> position;
        
        @Schema(description = "元数据")
        private Map<String, Object> metadata;
    }
} 