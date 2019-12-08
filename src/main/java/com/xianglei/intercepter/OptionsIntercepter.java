package com.xianglei.intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * options请求拦截器
 */
@Configuration
public class OptionsIntercepter extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(OptionsIntercepter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("---------------------------进入options请求拦截器-----------------------------");
        if(request.getMethod().toLowerCase().equals("options")){
            logger.info("options请求拦截，直接返回200");
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        logger.info("---------------------------非options请求放行-----------------------------");
        return true;
    }
}
