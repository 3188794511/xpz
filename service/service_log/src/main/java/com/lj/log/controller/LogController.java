package com.lj.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.base.Result;
import com.lj.log.service.LogService;
import com.lj.model.log.MyLog;
import com.lj.vo.LogQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author lj
 * @since 2022-11-25
 */
@RestController
@RequestMapping("/xpz/log/my-log")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 新增一条日志
     * @param myLog
     * @return
     */
    @PostMapping("/save")
    public Result saveLog(@RequestBody MyLog myLog){
        boolean isSuccess = logService.save(myLog);
        return isSuccess ? Result.ok( ) : Result.fail();
    }

    /**
     * 分页条件查询日志
     * @param page
     * @param size
     * @param logQueryDto
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public Result pageQueryLog(@PathVariable Long page, @PathVariable Long size, LogQueryDto logQueryDto){
        Page<MyLog> res = logService.pageQueryLog(page,size,logQueryDto);
        return Result.ok(res);
    }

}

