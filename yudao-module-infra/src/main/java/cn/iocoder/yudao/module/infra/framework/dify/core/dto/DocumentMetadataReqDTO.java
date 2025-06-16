package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 文档元数据请求 DTO
 */
@Data
@NoArgsConstructor
public class DocumentMetadataReqDTO {

    /**
     * 文档ID
     */
    private String documentId;
    
    /**
     * 操作类型，可选值：set（设置）、delete（删除）
     */
    private String operation;
    
    /**
     * 元数据对象，key为元数据字段名，value为值
     */
    private Map<String, Object> metadata;
    
    /**
     * 构造函数
     *
     * @param documentId 文档ID
     * @param operation 操作类型
     * @param metadata 元数据
     */
    public DocumentMetadataReqDTO(String documentId, String operation, Map<String, Object> metadata) {
        this.documentId = documentId;
        this.operation = operation;
        this.metadata = metadata;
    }
} 