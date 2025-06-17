package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Dify 知识库列表响应DTO
 */
@Data
public class DatasetListRespDTO {

    /**
     * 知识库列表
     */
    private List<DatasetRespDTO> data;
    
    /**
     * 是否有更多数据
     */
    private Boolean has_more;
    
    /**
     * 每页限制
     */
    private Integer limit;
    
    /**
     * 总条数
     */
    private Integer total;
    
    /**
     * 当前页码
     */
    private Integer page;
} 