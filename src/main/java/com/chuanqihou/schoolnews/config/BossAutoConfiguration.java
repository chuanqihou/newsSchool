package com.chuanqihou.schoolnews.config;

import com.chuanqihou.schoolnews.utils.SnowflakeManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @auther 传奇后
 * @date 2022/11/12 0:46
 * @veersion 1.0
 */
@Configuration
@EnableConfigurationProperties(TestProperties.class)
public class BossAutoConfiguration {
    @Resource
    private TestProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeManager snowflakeManager() {
        return new SnowflakeManager(this.properties.getSnowflake().getMachineId(), this.properties.getSnowflake().getDataCenterId());
    }
}
