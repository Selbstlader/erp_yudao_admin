package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Dify 会话列表响应 DTO
 */
@Data
public class ConversationListRespDTO {

    /**
     * 会话列表
     */
    private List<Conversation> data;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 会话信息
     */
    @Data
    public static class Conversation {
        /**
         * 会话ID
         */
        private String id;

        /**
         * 会话名称
         */
        private String name;

        /**
         * 会话状态
         */
        private String status;

        /**
         * 创建时间
         */
        private Long createdAt;

        /**
         * 更新时间
         */
        private Long updatedAt;

        /**
         * 最新消息内容
         */
        private String lastMessageContent;

        /**
         * 最新消息创建时间
         */
        private Long lastMessageCreatedAt;

        /**
         * 未读消息数
         */
        private Integer unreadCount;
    }
} 