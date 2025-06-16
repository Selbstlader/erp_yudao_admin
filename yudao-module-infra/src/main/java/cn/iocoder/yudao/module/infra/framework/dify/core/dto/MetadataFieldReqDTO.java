package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

/**
 * 元数据字段请求 DTO
 */
@Data
public class MetadataFieldReqDTO {

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
} 