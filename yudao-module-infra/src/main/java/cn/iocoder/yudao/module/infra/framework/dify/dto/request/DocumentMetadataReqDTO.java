package cn.iocoder.yudao.module.infra.framework.dify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Dify 文档元数据请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadataReqDTO {

    /**
     * 操作类型: set, delete
     */
    private String operation;
    
    /**
     * 文档ID
     */
    private String document_id;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
} 