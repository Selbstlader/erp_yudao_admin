package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 文件文档请求 DTO
 */
@Data
@NoArgsConstructor
public class DocumentFileReqDTO {

    /**
     * 索引技术，可选值：high_quality, economy
     */
    private String indexingTechnique = "high_quality";
    
    /**
     * 处理规则
     */
    private DocumentTextReqDTO.ProcessRules processRules;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 构造函数
     * 
     * @param indexingTechnique 索引技术
     */
    public DocumentFileReqDTO(String indexingTechnique) {
        this.indexingTechnique = indexingTechnique;
    }
    
    /**
     * 构造函数
     * 
     * @param indexingTechnique 索引技术
     * @param metadata 元数据
     */
    public DocumentFileReqDTO(String indexingTechnique, Map<String, Object> metadata) {
        this.indexingTechnique = indexingTechnique;
        this.metadata = metadata;
    }
} 