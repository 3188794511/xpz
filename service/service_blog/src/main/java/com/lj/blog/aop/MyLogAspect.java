package com.lj.blog.aop;

import com.alibaba.fastjson.JSON;
import com.lj.base.Result;
import com.lj.client.LogClientService;
import com.lj.client.UserClientService;
import com.lj.model.log.MyLog;
import com.lj.util.AdminInfoContext;
import com.lj.util.UserInfoContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
public class MyLogAspect {
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private LogClientService logClientService;

    @Pointcut("@annotation(com.lj.annotation.MyLog)")
    public void myPointcut(){}

    @Around("myPointcut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
        Object res = recordLog(proceedingJoinPoint);
        return res;
    }

    private Object recordLog(ProceedingJoinPoint proceedingJoinPoint){
        Object res = null;
        try {
            //方法开始时间
            Date startTime = new Date();
            //用户信息获取
            //判断当前的用户是管理员还是普通用户
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            String userType = signature.getMethod().getDeclaredAnnotation(com.lj.annotation.MyLog.class).type();
            Long userId = userType.equals("admin") ? AdminInfoContext.get() : UserInfoContext.get();
            String userName = null;
            if(Objects.nonNull(userId)){
                userName = userClientService.getById(userId).getNickName();
            }
            String className = proceedingJoinPoint.getTarget().getClass().getName();
            //操作
            String operation = signature.getMethod().getDeclaredAnnotation(com.lj.annotation.MyLog.class).value();
            //方法名
            String methodName = className + "." + signature.getMethod().getName();
            //方法参数
            List<Object> params = Arrays.stream(proceedingJoinPoint.getArgs()).filter(i -> !(i instanceof MultipartFile)).collect(Collectors.toList());
            String paramsStr = JSON.toJSONString(params);
            //方法返回值
            res = proceedingJoinPoint.proceed();
            //方法状态码
            Result result = (Result) res;
            Integer code = result.getCode();
            String resStr = JSON.toJSONString(res);
            //方法结束时间
            Date endTime = new Date();
            //记录日志到数据库
            MyLog myLog = new MyLog();
            myLog.setUserId(userId);
            myLog.setUserName(userName);
            myLog.setOperation(operation);
            myLog.setMethodName(methodName);
            myLog.setParams(paramsStr);
            myLog.setResult(resStr);
            myLog.setCode(code);
            myLog.setStartTime(startTime);
            myLog.setEndTime(endTime);
            logClientService.saveLog(myLog);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return res;
    }
}
