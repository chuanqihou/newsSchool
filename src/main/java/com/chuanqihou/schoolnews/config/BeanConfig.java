package com.chuanqihou.schoolnews.config;

import com.chuanqihou.schoolnews.utils.SnowflakeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther 传奇后
 * @date 2022/11/12 0:41
 * @veersion 1.0
 */
@Configuration
public class BeanConfig {

    @Bean
    public SnowflakeManager getSnowflakeManager() {
        return new SnowflakeManager(0L, 0L);
    }

}
