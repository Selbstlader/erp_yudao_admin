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
    private String indexing_technique;
    
    /**
     * 处理规则
     */
    private ProcessRule process_rule;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 处理规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessRule {
        /**
         * 规则
         */
        private Rules rules;
        
        /**
         * 模式: auto, custom
         */
        private String mode = "custom";
    }
    
    /**
     * 规则详情
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rules {
        /**
         * 预处理规则
         */
        private java.util.List<PreProcessingRule> pre_processing_rules;
        
        /**
         * 分段设置
         */
        private Segmentation segmentation;
    }
    
    /**
     * 预处理规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreProcessingRule {
        /**
         * 规则ID
         */
        private String id;
        
        /**
         * 是否启用
         */
        private boolean enabled;
    }
    
    /**
     * 分段设置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Segmentation {
        /**
         * 分隔符
         */
        private String separator;
        
        /**
         * 最大token数
         */
        private Integer max_tokens;
    }
    
    /**
     * 创建一个简单的默认配置实例
     *
     * @param indexingTechnique 索引技术
     * @param metadata 元数据
     * @return 文档文件请求DTO
     */
    public static DocumentFileReqDTO createDefault(String indexingTechnique, Map<String, Object> metadata) {
        DocumentFileReqDTO dto = new DocumentFileReqDTO();
        dto.setIndexing_technique(indexingTechnique);
        dto.setMetadata(metadata);
        
        // 创建默认处理规则
        ProcessRule processRule = new ProcessRule();
        Rules rules = new Rules();
        
        // 添加默认预处理规则
        java.util.List<PreProcessingRule> preProcessingRules = new java.util.ArrayList<>();
        preProcessingRules.add(new PreProcessingRule("remove_extra_spaces", true));
        preProcessingRules.add(new PreProcessingRule("remove_urls_emails", true));
        rules.setPre_processing_rules(preProcessingRules);
        
        // 添加默认分段设置
        Segmentation segmentation = new Segmentation("###", 500);
        rules.setSegmentation(segmentation);
        
        processRule.setRules(rules);
        processRule.setMode("custom");
        dto.setProcess_rule(processRule);
        
        return dto;
    }
} 