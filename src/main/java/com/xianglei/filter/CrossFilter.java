package com.xianglei.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 描述：解决前端请求跨域问题
 * 时间：[2019/12/1:19:13]
 * 作者：xianglei
 * params: * @param null
 */
@Component
public class CrossFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(CrossFilter.class);
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String method = request.getMethod();
        if ("options".equals(method.toLowerCase())) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        logger.info("跨域请求过滤器");
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        response.setHeader("Access-Control-Allow-Origin", "www.caipark.com");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Credentials","true");
        return null;
    }
}
