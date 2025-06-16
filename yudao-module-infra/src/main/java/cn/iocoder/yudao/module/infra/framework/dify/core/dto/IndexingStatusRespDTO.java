package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

/**
 * 索引状态响应 DTO
 */
@Data
public class IndexingStatusRespDTO {

    /**
     * 批处理ID
     */
    private String batch;

    /**
     * 处理状态，可选值：waiting, parsing, cleaning, splitting, indexing, error, completed
     */
    private String status;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 完成的分块数量
     */
    private Integer completedSegments;

    /**
     * 总分块数量
     */
    private Integer totalSegments;

    /**
     * 解析时间
     */
    private String parseTime;

    /**
     * 清洗时间
     */
    private String cleanTime;

    /**
     * 分块时间
     */
    private String splitTime;

    /**
     * 索引时间
     */
    private String indexTime;

    /**
     * 完成时间
     */
    private String finishTime;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 进度（百分比）
     */
    private Double progress;
} 