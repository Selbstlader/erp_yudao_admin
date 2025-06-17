package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

/**
 * Dify 知识库响应DTO
 */
@Data
public class DatasetRespDTO {

    /**
     * 知识库ID
     */
    private String id;
    
    /**
     * 知识库名称
     */
    private String name;
    
    /**
     * 知识库描述
     */
    private String description;
    
    /**
     * 权限
     */
    private String permission;
    
    /**
     * 数据源类型
     */
    private String data_source_type;
    
    /**
     * 索引技术
     */
    private String indexing_technique;
    
    /**
     * 创建时间
     */
    private String created_at;
    
    /**
     * 更新时间
     */
    private String updated_at;
    
    /**
     * 创建者
     */
    private String created_by;
    
    /**
     * 更新者
     */
    private String updated_by;
    
    /**
     * 应用数量
     */
    private Integer app_count;
    
    /**
     * 文档数量
     */
    private Integer document_count;
    
    /**
     * 词数
     */
    private Integer word_count;
} 