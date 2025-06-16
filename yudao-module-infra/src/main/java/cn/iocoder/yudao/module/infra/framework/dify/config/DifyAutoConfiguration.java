package cn.iocoder.yudao.module.infra.framework.dify.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Dify 自动配置类
 */
@Configuration
@EnableConfigurationProperties(DifyProperties.class)
public class DifyAutoConfiguration {
} 