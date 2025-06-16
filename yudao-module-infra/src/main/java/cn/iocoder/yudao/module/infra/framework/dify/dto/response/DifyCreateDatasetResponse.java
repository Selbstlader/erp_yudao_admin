package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

/**
 * Dify 创建知识库响应DTO
 */
@Data
public class DifyCreateDatasetResponse {

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
     * 创建时间
     */
    private String created_at;
    
    /**
     * 更新时间
     */
    private String updated_at;
} 