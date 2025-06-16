package cn.iocoder.yudao.module.infra.framework.dify.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文本文档请求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTextReqDTO {

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文本内容
     */
    private String text;

    /**
     * 文档ID（仅在更新时需要）
     */
    private String documentId;

    /**
     * 索引技术，可选值：high_quality, economy
     */
    private String indexingTechnique = "high_quality";

    /**
     * 处理规则
     */
    private ProcessRules processRules;

    /**
     * 处理规则
     */
    @Data
    public static class ProcessRules {
        
        /**
         * 规则模式，可选值：automatic, custom
         */
        private String mode = "automatic";
        
        /**
         * 规则详情，仅在custom模式下使用
         */
        private Rules rules;
        
        /**
         * 规则详情
         */
        @Data
        public static class Rules {
            
            /**
             * 预处理
             */
            private PreProcessing preProcessing;
            
            /**
             * 分段
             */
            private Segmentation segmentation;
            
            /**
             * 预处理
             */
            @Data
            public static class PreProcessing {
                
                /**
                 * 移除额外空格
                 */
                private Boolean removeExtraSpaces;
                
                /**
                 * 移除URL和邮件
                 */
                private Boolean removeUrlsEmails;
            }
            
            /**
             * 分段
             */
            @Data
            public static class Segmentation {
                
                /**
                 * 分隔符
                 */
                private String separator;
                
                /**
                 * 最大Token数
                 */
                private Integer maxTokens;
            }
        }
    }
} 