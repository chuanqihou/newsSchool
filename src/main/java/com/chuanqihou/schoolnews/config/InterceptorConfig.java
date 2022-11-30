package com.chuanqihou.schoolnews.config;

import com.chuanqihou.schoolnews.interceptor.CheckTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


/**
 * @auther 传奇后
 * @date 2022/8/8 10:07
 * @Description 注册token登录拦截器
 * @veersion 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private CheckTokenInterceptor checkTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkTokenInterceptor)
                .addPathPatterns("/comment/add")
                .addPathPatterns("/comment/delete")
                .addPathPatterns("/comment/updateStatus")
                .addPathPatterns("/user/update")
                .addPathPatterns("/user/delete")
                .addPathPatterns("/user/add")
                .addPathPatterns("/user/adminUpdate")
                .addPathPatterns("/user/updateStatus")
                .addPathPatterns("/news/delete")
                .addPathPatterns("/news/add")
                .addPathPatterns("/news/update");
    }
}
