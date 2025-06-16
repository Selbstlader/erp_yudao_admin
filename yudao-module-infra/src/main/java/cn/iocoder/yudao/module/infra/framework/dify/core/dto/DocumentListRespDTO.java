package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

import java.util.List;

/**
 * 文档列表响应 DTO
 */
@Data
public class DocumentListRespDTO {

    /**
     * 文档列表
     */
    private List<DocumentRespDTO> data;

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