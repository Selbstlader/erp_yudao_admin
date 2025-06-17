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
     * 位置
     */
    private Integer position;
    
    /**
     * 数据源类型
     */
    private String data_source_type;
    
    /**
     * 数据源信息
     */
    private Map<String, Object> data_source_info;
    
    /**
     * 知识库处理规则ID
     */
    private String dataset_process_rule_id;
    
    /**
     * 知识库ID
     */
    private String dataset_id;
    
    /**
     * 文档名称
     */
    private String name;
    
    /**
     * 创建来源
     */
    private String created_from;
    
    /**
     * 创建者
     */
    private String created_by;
    
    /**
     * 创建时间
     */
    private Long created_at;
    
    /**
     * Token数量
     */
    private Integer tokens;
    
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
     * 错误信息
     */
    private String error;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 禁用时间
     */
    private Long disabled_at;
    
    /**
     * 禁用者
     */
    private String disabled_by;
    
    /**
     * 是否归档
     */
    private Boolean archived;
    
    /**
     * 显示状态
     */
    private String display_status;
    
    /**
     * 字数
     */
    private Integer word_count;
    
    /**
     * 命中次数
     */
    private Integer hit_count;
    
    /**
     * 文档格式
     */
    private String doc_form;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 创建时间
     */
    private String updated_at;
    
    /**
     * 分段数
     */
    private Integer segments_count;
    
    /**
     * 向量数
     */
    private Integer tokens_count;
} 