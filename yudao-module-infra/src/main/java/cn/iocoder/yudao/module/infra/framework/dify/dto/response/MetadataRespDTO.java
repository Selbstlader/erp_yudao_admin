package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

/**
 * Dify 元数据响应DTO
 */
@Data
public class MetadataRespDTO {

    /**
     * 元数据ID
     */
    private String id;
    
    /**
     * 知识库ID
     */
    private String dataset_id;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 类型: text, number, boolean
     */
    private String type;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 是否内置
     */
    private Boolean is_builtin;
    
    /**
     * 是否启用
     */
    private Boolean is_enabled;
    
    /**
     * 创建时间
     */
    private String created_at;
    
    /**
     * 更新时间
     */
    private String updated_at;
} 