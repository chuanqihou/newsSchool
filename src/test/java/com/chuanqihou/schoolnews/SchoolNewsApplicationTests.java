package com.chuanqihou.schoolnews;

import com.chuanqihou.schoolnews.utils.SnowflakeManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SchoolNewsApplicationTests {

    @Resource
    SnowflakeManager snowflakeManager;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() throws Exception {

/*        redisTemplate.opsForValue().set("18774137163","568114",5, TimeUnit.MINUTES);

        String s = (String) redisTemplate.opsForValue().get("18774137163");
        System.out.println(s);*/
        for (int i = 0;i<5;i++) {
            Thread.sleep(1000);
            long l = snowflakeManager.nextValue();
            //System.out.println(l);
            System.out.println(String.valueOf(l).substring(0,16));
        }
    }



}
