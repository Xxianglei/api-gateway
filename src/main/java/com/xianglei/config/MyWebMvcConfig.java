package com.xianglei.config;

import com.xianglei.intercepter.CrossInterceptor;
import com.xianglei.intercepter.OptionsIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    OptionsIntercepter intercepter;
    @Autowired
    CrossInterceptor crossInterceptor;
    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 解决跨域问题
        registry.addInterceptor(crossInterceptor).addPathPatterns("/**");
        // 解决options请求问题
        registry.addInterceptor(intercepter).addPathPatterns("/**");
    }
}