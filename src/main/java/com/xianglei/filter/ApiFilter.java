package com.xianglei.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xianglei.common.BaseJson;
import com.xianglei.common.JwtUtils;
import com.xianglei.common.Tools;
import com.xianglei.domain.User;
import com.xianglei.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.tools.Tool;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class ApiFilter extends ZuulFilter {
    @Autowired
    private UserService userService;
    private static Logger logger = LoggerFactory.getLogger(ApiFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (request.getMethod().equals("OPTIONS")) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        Boolean aBoolean = checkUserIsRightful();
        if (aBoolean) {
            context.setSendZuulResponse(true); // 对该请求进行路由
            context.setResponseStatusCode(200);// 设置响应状态码
            logger.info("通过校验，网关转发");
            return null;
        } else {
            context = RequestContext.getCurrentContext();
            context.setSendZuulResponse(false); // 不对其进行路由
            context.setResponseStatusCode(401);// 设置响应状态码
            logger.warn("此操作需要先登录系统...");
            //response401(context.getResponse());
            return null;
        }
    }

    // 用户未登陆
    private void response401(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        logger.warn("此操作需要先登录系统...");
        PrintWriter writer = null;
        try {
            //响应结果
            BaseJson baseJson = new BaseJson(false);
            baseJson.setCode(-1);
            baseJson.setMessage("您未登录，请先登录");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(baseJson);
            writer.write(result);
        } catch (IOException e) {
            logger.error("鉴权出错:{}", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    // 登录user_flowId是否存在
    public Boolean checkUserIsRightful() {
        RequestContext context = RequestContext.getCurrentContext();
        Object token = context.get("token");
        if (Tools.isNull(token)) {
            return false;
        } else {
            logger.info("获取到了传递过来的token:" + token.toString());
            String user_flowId = JwtUtils.getFlowId(token.toString()) ;
            logger.info("获取到了传递过来的token对应的flowId:" + user_flowId);
            User user = new User();
            user.setFlowId(user_flowId);
            User findUser = userService.getUser(user);
            if (!StringUtils.isEmpty(user_flowId) && findUser != null) {
                return true;
            } else {
                return false;
            }
        }
    }
}
