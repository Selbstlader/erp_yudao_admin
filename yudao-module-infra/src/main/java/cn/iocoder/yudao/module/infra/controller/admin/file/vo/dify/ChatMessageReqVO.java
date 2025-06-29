package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "管理后台 - Dify聊天消息请求 VO")
@Data
public class ChatMessageReqVO {

    @Schema(description = "用户提问的问题", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好，请问今天天气如何？")
    @NotEmpty(message = "问题不能为空")
    private String query;

    @Schema(description = "会话ID，为空时创建新会话", example = "conv_123456")
    private String conversationId;

    @Schema(description = "用户ID，用于标识用户，保证会话隔离", requiredMode = Schema.RequiredMode.REQUIRED, example = "user_123")
    @NotEmpty(message = "用户ID不能为空")
    private String user;

    @Schema(description = "输入变量，用于填充提示词模板中的变量", example = "{\"city\": \"北京\"}")
    private Map<String, String> inputs;

    @Schema(description = "响应模式，streaming-流式响应，blocking-阻塞响应", requiredMode = Schema.RequiredMode.REQUIRED, example = "blocking")
    @NotEmpty(message = "响应模式不能为空")
    private String responseMode = "blocking";

    @Schema(description = "是否启用搜索", example = "true")
    private Boolean enableSearch;

    @Schema(description = "搜索关键词", example = "天气")
    private String searchKeywords;

    @Schema(description = "是否返回引用", example = "true")
    private Boolean returnReferences;
} 