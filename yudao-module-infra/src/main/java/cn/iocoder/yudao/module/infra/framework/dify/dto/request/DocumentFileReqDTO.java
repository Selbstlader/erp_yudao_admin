package cn.iocoder.yudao.module.infra.framework.dify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Dify 文件文档请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFileReqDTO {

    /**
     * 索引技术: high_quality, economy
     */
    private String indexingTechnique;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
} 