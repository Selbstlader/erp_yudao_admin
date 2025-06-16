package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Dify 检索响应DTO
 */
@Data
public class RetrieveRespDTO {

    /**
     * 检索项列表
     */
    private List<RetrieveItem> data;
    
    /**
     * 检索项
     */
    @Data
    public static class RetrieveItem {
        /**
         * 文档ID
         */
        private String document_id;
        
        /**
         * 分段ID
         */
        private String segment_id;
        
        /**
         * 分段索引
         */
        private Integer segment_index;
        
        /**
         * 相似度得分
         */
        private Double score;
        
        /**
         * 内容
         */
        private String content;
        
        /**
         * 键值对
         */
        private Map<String, Object> keyValues;
        
        /**
         * 元数据
         */
        private Map<String, Object> metadata;
    }
} 