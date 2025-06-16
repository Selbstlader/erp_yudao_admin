package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

import java.util.Map;

/**
 * 检索请求 DTO
 */
@Data
public class RetrieveReqDTO {

    /**
     * 查询内容
     */
    private String query;

    /**
     * 检索类型，可选值：keyword（关键词检索）, semantic（语义检索）
     */
    private String searchType;

    /**
     * 分页参数 - 页码
     */
    private Integer page;

    /**
     * 分页参数 - 每页条数
     */
    private Integer limit;

    /**
     * 元数据过滤器
     */
    private Map<String, Object> filters;

    /**
     * 是否重排序
     */
    private Boolean rerank;

    /**
     * 返回结果数量
     */
    private Integer topK;

    /**
     * 相似度阈值
     */
    private Double scoreThreshold;
} 