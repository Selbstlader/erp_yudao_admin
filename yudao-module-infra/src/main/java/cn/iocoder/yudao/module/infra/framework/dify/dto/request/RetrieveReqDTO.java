package cn.iocoder.yudao.module.infra.framework.dify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Dify 检索请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveReqDTO {

    /**
     * 查询内容
     */
    private String query;
    
    /**
     * 返回结果数量
     */
    private Integer top_k;
    
    /**
     * 重新排序
     */
    private Boolean reranking;
    
    /**
     * 包含元数据
     */
    private Boolean include_metadata;
    
    /**
     * 过滤条件
     */
    private Map<String, Object> filter;
} 