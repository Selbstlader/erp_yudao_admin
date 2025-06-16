package cn.iocoder.yudao.module.infra.framework.dify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dify 创建知识库请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DifyCreateDatasetRequest {

    /**
     * 知识库名称
     */
    private String name;
    
    /**
     * 知识库描述
     */
    private String description;
    
    /**
     * 权限: only_me, all_team_members
     */
    private String permission;
    
    /**
     * 索引技术: high_quality, economy
     */
    private String indexingTechnique;
} 