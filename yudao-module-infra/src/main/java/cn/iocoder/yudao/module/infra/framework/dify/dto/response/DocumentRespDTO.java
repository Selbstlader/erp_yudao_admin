package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.Map;

/**
 * Dify 文档响应DTO
 */
@Data
public class DocumentRespDTO {

    /**
     * 文档ID
     */
    private String id;
    
    /**
     * 知识库ID
     */
    private String dataset_id;
    
    /**
     * 文档名称
     */
    private String name;
    
    /**
     * 文档类型
     */
    private String document_type;
    
    /**
     * 文档内容
     */
    private String content;
    
    /**
     * 批处理ID
     */
    private String batch;
    
    /**
     * 索引状态
     */
    private String indexing_status;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 创建时间
     */
    private String created_at;
    
    /**
     * 更新时间
     */
    private String updated_at;
    
    /**
     * 错误信息
     */
    private String error;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 分段数
     */
    private Integer segments_count;
    
    /**
     * 字符数
     */
    private Integer word_count;
    
    /**
     * 向量数
     */
    private Integer tokens_count;
} 