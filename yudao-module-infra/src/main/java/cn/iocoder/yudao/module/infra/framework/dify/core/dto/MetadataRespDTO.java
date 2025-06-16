package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

/**
 * 元数据字段响应 DTO
 */
@Data
public class MetadataRespDTO {

    /**
     * 字段ID
     */
    private String id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型，可选值：string, number, datetime
     */
    private String type;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 搜索模式，可选值：exact, contains
     */
    private String searchMode;

    /**
     * 是否为内置字段
     */
    private Boolean isBuiltIn;

    /**
     * 所属知识库ID
     */
    private String datasetId;

    /**
     * 创建时间
     */
    private String createdAt;
} 