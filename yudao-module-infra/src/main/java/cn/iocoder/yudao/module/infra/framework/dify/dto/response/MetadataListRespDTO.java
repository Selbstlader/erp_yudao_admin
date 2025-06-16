package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Dify 元数据列表响应DTO
 */
@Data
public class MetadataListRespDTO {

    /**
     * 元数据列表
     */
    private List<MetadataRespDTO> data;
} 