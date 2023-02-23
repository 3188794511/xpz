package com.lj.interceptor;


import com.alibaba.fastjson.JSON;
import com.lj.base.Result;
import com.lj.util.AdminInfoContext;
import com.lj.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 管理员拦截器
 */
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)){
            log.info("请求头中的token为:{}", token);
            Long userId = JwtTokenUtil.getUserId(token);
            String role = JwtTokenUtil.getUserRole(token);
            if ("admin".equals(role)){
                AdminInfoContext.set(userId);
                //刷新token并返回
                if (JwtTokenUtil.isExpiringSoon(token)){
                    token = JwtTokenUtil.refreshToken(token);
                    response.addHeader("token", token);
                    log.info("刷新了token");
                }
                return true;
            }
            else{
                //非管理员账号
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                String str = JSON.toJSONString(Result.fail().message("请用管理员账号登录"));
                writer.print(str);
                writer.flush();
                return false;
            }
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
