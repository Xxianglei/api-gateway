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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.tools.Tool;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class TokenFilter extends ZuulFilter {
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
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        logger.info("token校验开启");
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpSession session = request.getSession();
        Object token = session.getAttribute("user_flowId");
        if (!Tools.isNull(token)) {
            String rightToken = (String) token;
            if (JwtUtils.verify(rightToken)) {
                context.setSendZuulResponse(true); // 对该请求进行路由
                context.setResponseStatusCode(200);// 设置响应状态码
                logger.info("通过token校验，网关转发进入api校验");
                // 传递给APIFilter token值为FlowID
                context.set("token",rightToken);
            } else {
                sendNoPass(context, "token校验未通过，token失效请重新登录");
            }
        } else {
            sendNoPass(context, "token校验未通过，token为空");
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
