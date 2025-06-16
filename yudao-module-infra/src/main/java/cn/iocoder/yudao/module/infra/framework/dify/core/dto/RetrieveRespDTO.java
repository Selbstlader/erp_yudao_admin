package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 检索响应 DTO
 */
@Data
public class RetrieveRespDTO {

    /**
     * 检索分段列表
     */
    private List<Segment> data;

    /**
     * 分段信息
     */
    @Data
    public static class Segment {
        
        /**
         * 分段ID
         */
        private String id;
        
        /**
         * 分段内容
         */
        private String content;
        
        /**
         * 相似度分数
         */
        private Double score;

        /**
         * 文档信息
         */
        private Document document;
        
        /**
         * 文档信息
         */
        @Data
        public static class Document {
            
            /**
             * 文档ID
             */
            private String id;
            
            /**
             * 文档名称
             */
            private String name;
            
            /**
             * 文档显示类型
             */
            private String displayType;
            
            /**
             * 元数据
             */
            private Map<String, Object> metadata;
        }
    }
} 