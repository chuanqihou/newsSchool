package com.chuanqihou.schoolnews.config;

import com.chuanqihou.schoolnews.utils.SnowflakeProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @auther 传奇后
 * @date 2022/11/12 0:45
 * @veersion 1.0
 */
@Data
@ConfigurationProperties(prefix = "com.chuanqihou.schoolnews")
public class TestProperties {
    private SnowflakeProperties snowflake;
}
