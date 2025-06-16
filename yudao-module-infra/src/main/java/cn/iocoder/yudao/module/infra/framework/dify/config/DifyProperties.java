package cn.iocoder.yudao.module.infra.framework.dify.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Dify 配置类
 */
@ConfigurationProperties(prefix = "yudao.dify")
@Data
@Validated
public class DifyProperties {

    /**
     * API基础URL
     */
    private String baseUrl;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 默认知识库ID
     */
    private String defaultDatasetId;

    /**
     * 连接超时（毫秒）
     */
    private Integer connectTimeout = 5000;

    /**
     * 读取超时（毫秒）
     */
    private Integer readTimeout = 30000;

    /**
     * 是否启用同步
     */
    private Boolean enabled = true;

    /**
     * 索引技术（high_quality/economy）
     */
    private String indexingTechnique = "high_quality";

    /**
     * 重试配置
     */
    private Retry retry = new Retry();

    /**
     * 处理规则
     */
    private ProcessRule processRule = new ProcessRule();

    /**
     * 是否启用自动同步
     * 当为true时，文件创建、更新和删除操作会自动触发Dify知识库同步
     */
    private Boolean autoSync = true;

    /**
     * 重试配置
     */
    @Data
    public static class Retry {
        /**
         * 最大重试次数
         */
        private Integer maxRetries = 3;

        /**
         * 重试初始间隔（毫秒）
         */
        private Long initialRetryInterval = 1000L;

        /**
         * 重试间隔倍数
         */
        private Double retryMultiplier = 2.0;

        /**
         * 最大重试间隔（毫秒）
         */
        private Long maxInterval = 60000L;
    }

    /**
     * 处理规则
     */
    @Data
    public static class ProcessRule {
        /**
         * 处理模式：automatic-自动，custom-自定义
         */
        private String mode = "automatic";

        /**
         * 处理规则配置，仅在custom模式下使用
         */
        private Rules rules = new Rules();

        /**
         * 处理规则详细配置
         */
        @Data
        public static class Rules {
            /**
             * 预处理规则
             */
            private PreProcessing preProcessing = new PreProcessing();

            /**
             * 分段规则
             */
            private Segmentation segmentation = new Segmentation();

            /**
             * 预处理规则
             */
            @Data
            public static class PreProcessing {
                /**
                 * 移除额外空格
                 */
                private Boolean removeExtraSpaces = true;

                /**
                 * 移除URL和邮件
                 */
                private Boolean removeUrlsEmails = true;
            }

            /**
             * 分段规则
             */
            @Data
            public static class Segmentation {
                /**
                 * 分隔符
                 */
                private String separator = "###";

                /**
                 * 最大token数
                 */
                private Integer maxTokens = 500;
            }
        }
    }
} 