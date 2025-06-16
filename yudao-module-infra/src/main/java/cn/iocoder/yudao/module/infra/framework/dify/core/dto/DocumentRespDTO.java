package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

/**
 * 文档响应 DTO
 */
@Data
public class DocumentRespDTO {

    /**
     * 文档ID
     */
    private String id;

    /**
     * 批处理ID
     */
    private String batch;

    /**
     * 知识库ID
     */
    private String datasetId;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档类型
     */
    private String displayType;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 是否已归档
     */
    private Boolean archived;

    /**
     * 是否已禁用
     */
    private Boolean disabled;

    /**
     * 元数据
     */
    private Object metadata;

    /**
     * 文档大小（字节）
     */
    private Long size;

    /**
     * 创建者ID
     */
    private String createdBy;

    /**
     * 分块数量
     */
    private Integer segmentCount;

    /**
     * 创建者邮箱
     */
    private String creatorEmail;

    /**
     * 处理状态
     */
    private String processingStatus;
} 