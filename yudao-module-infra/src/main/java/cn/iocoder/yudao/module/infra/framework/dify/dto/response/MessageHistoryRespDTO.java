package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Dify 消息历史响应 DTO
 */
@Data
public class MessageHistoryRespDTO {

    /**
     * 消息列表
     */
    private List<Message> data;

    /**
     * 是否有更多消息
     */
    private Boolean hasMore;

    /**
     * 消息信息
     */
    @Data
    public static class Message {
        /**
         * 消息ID
         */
        private String id;

        /**
         * 会话ID
         */
        private String conversationId;

        /**
         * 创建时间
         */
        private Long createdAt;

        /**
         * 用户提问内容
         */
        private String query;
        
        /**
         * AI回答内容
         */
        private String answer;
        
        /**
         * 消息角色（user/assistant）
         */
        private String role;

        /**
         * 引用信息
         */
        private List<ChatMessageRespDTO.Reference> references;

        /**
         * 元数据
         */
        private Map<String, Object> metadata;
    }
} 