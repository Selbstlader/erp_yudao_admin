package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Dify 聊天消息响应 DTO
 */
@Data
public class ChatMessageRespDTO {

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
     * AI回答内容
     */
    private String answer;

    /**
     * 引用信息
     */
    private List<Reference> references;

    /**
     * 思考过程
     */
    private String taskId;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 元数据
     */
    private Map<String, Object> metadata;

    /**
     * 引用信息
     */
    @Data
    public static class Reference {
        /**
         * 引用ID
         */
        private String id;
        
        /**
         * 文档ID
         */
        private String documentId;
        
        /**
         * 数据集ID
         */
        private String datasetId;
        
        /**
         * 数据集名称
         */
        private String datasetName;
        
        /**
         * 文件名称
         */
        private String fileName;
        
        /**
         * 文件类型
         */
        private String fileType;
        
        /**
         * 引用内容
         */
        private String content;
        
        /**
         * 引用相关度
         */
        private Double score;
        
        /**
         * 引用位置信息
         */
        private Map<String, Object> position;
        
        /**
         * 元数据
         */
        private Map<String, Object> metadata;
    }
} 