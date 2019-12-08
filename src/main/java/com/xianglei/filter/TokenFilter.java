package com.xianglei.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xianglei.common.BaseJson;
import com.xianglei.common.JwtUtils;
import com.xianglei.common.Tools;
import com.xianglei.domain.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.tools.Tool;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * 描述：token过滤器
 * 时间：[2019/12/4:12:32]
 * 作者：xianglei
 * params: * @param null
 */
@Component
public class TokenFilter extends ZuulFilter {
    @Autowired
    RedisTemplate redisTemplate;

    private static Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        logger.info("token校验开启");
        String tokens = null;
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        // 其他请求方式则拿到请求tokens
        tokens = request.getHeader("tokens") == null ? "" : request.getHeader("tokens");
        // 这个token其实是redis中的可以转成flowID
        String flowId = JwtUtils.getFlowId(tokens);
        if (!Tools.isNull(flowId) && redisTemplate.hasKey(tokens)) {
            if (JwtUtils.verify(tokens)) {
                context.setSendZuulResponse(true); // 对该请求进行路由
                context.setResponseStatusCode(200);// 设置响应状态码
                logger.info("通过token校验，网关转发进入api校验");
                // 传递给APIFilter token值为FlowID
                context.set("token", tokens);
            } else {
                sendNoPass(context, "token校验未通过，token失效请重新登录");
            }
        } else {
            sendNoPass(context, "请重新登录");
            return null;
        }
        return null;
    }

    private void sendNoPass(RequestContext context, String msg) {
        BaseJson baseJson = new BaseJson();
        baseJson.setStatus(false);
        baseJson.setCode(HttpStatus.OK.value());
        baseJson.setMessage(msg);
        HttpServletResponse response = context.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        context = RequestContext.getCurrentContext();
        context.setSendZuulResponse(false); // 不对其进行路由
        context.setResponseStatusCode(401);// 设置响应状态码
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(baseJson);
            writer.write(result);
            logger.info(msg);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
