package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

/**
 * 知识库信息 Response DTO
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
     * 描述
     */
    private String description;

    /**
     * 权限，可选值：only_me, team
     */
    private String permission;

    /**
     * 文档数量
     */
    private Integer documentCount;

    /**
     * 创建时间
     */
    private String createdAt;
} 