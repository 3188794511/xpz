package com.lj.interceptor;

import com.alibaba.fastjson.JSON;
import com.lj.base.Result;
import com.lj.util.JwtTokenUtil;
import com.lj.util.UserInfoContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 用户拦截器
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)){
            log.info("请求头中的token为:{}", token);
            Long userId = JwtTokenUtil.getUserId(token);
            UserInfoContext.set(userId);
            //刷新token并返回
            if (JwtTokenUtil.isExpiringSoon(token)){
                token = JwtTokenUtil.refreshToken(token);
                response.addHeader("token", token);
                log.info("刷新了token");
            }
            return true;
        }
        //提示用户登录
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String str = JSON.toJSONString(Result.fail().message("请先登录"));
        writer.print(str);
        writer.flush();
        return false;
    }
}
