package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

import java.util.List;

/**
 * 知识库列表 Response DTO
 */
@Data
public class DatasetListRespDTO {

    /**
     * 知识库列表
     */
    private List<DatasetRespDTO> data;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer limit;
} 