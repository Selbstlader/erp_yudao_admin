package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

import java.util.List;

/**
 * 元数据列表响应 DTO
 */
@Data
public class MetadataListRespDTO {

    /**
     * 元数据字段列表
     */
    private List<MetadataRespDTO> data;
    
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