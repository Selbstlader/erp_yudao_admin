package cn.iocoder.yudao.module.infra.framework.dify.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * Dify 聊天消息请求 DTO
 */
@Data
@Accessors(chain = true)
public class ChatMessageReqDTO {

    /**
     * 用户提问的问题
     */
    private String query;

    /**
     * 会话ID，为空时创建新会话
     */
    private String conversationId;

    /**
     * 用户ID，用于标识用户，保证会话隔离
     */
    private String user;

    /**
     * 输入变量，用于填充提示词模板中的变量
     */
    private Map<String, String> inputs;

    /**
     * 响应模式，streaming-流式响应，blocking-阻塞响应
     */
    private String responseMode = "blocking";

    /**
     * 是否启用搜索
     */
    private Boolean enableSearch;

    /**
     * 搜索关键词
     */
    private String searchKeywords;

    /**
     * 是否返回引用
     */
    private Boolean returnReferences;
} 