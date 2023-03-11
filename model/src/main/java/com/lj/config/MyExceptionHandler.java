package com.lj.config;

import com.lj.base.Result;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MyExceptionHandler  {
    @ExceptionHandler(ExpiredJwtException.class)
    public Result JwtExpireExceptionHandler(ExpiredJwtException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.fail().code(50000).message("登录信息过期,请重新登录");
    }

    @ExceptionHandler({BindException.class})
    public Result BindExceptionHandler(BindException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.fail().message(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.fail().message(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result CommonExceptionHandler(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.fail().message("服务器异常,请重试");
    }
}
