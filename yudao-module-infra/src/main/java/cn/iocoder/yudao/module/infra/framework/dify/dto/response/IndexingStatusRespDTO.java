package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

/**
 * Dify 索引状态响应DTO
 */
@Data
public class IndexingStatusRespDTO {

    /**
     * 知识库ID
     */
    private String dataset_id;
    
    /**
     * 批处理ID
     */
    private String batch;
    
    /**
     * 状态: waiting, parsing, indexing, completed, error
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String error;
    
    /**
     * 任务数
     */
    private Integer total;
    
    /**
     * 已成功数
     */
    private Integer successful;
    
    /**
     * 已失败数
     */
    private Integer failed;
} 