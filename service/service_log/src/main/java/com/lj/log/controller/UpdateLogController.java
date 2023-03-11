package com.lj.log.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.base.Result;
import com.lj.log.service.UpdateLogService;
import com.lj.model.log.UpdateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 更新日志 前端控制器
 * </p>
 *
 * @author lj
 * @since 2022-12-05
 */
@RestController
@RequestMapping("xpz/log/update-log")
public class UpdateLogController {
    @Autowired
    private UpdateLogService updateLogService;

    @PostMapping("/save")
    public Result saveUpdateLog(@RequestBody @Validated UpdateLog updateLog){
        boolean isSuccess = updateLogService.save(updateLog);
        return isSuccess ? Result.ok() : Result.fail();
    }

    @GetMapping("/list/{page}/{size}")
    public Result pageQueryUpdateLog(@PathVariable Long page,@PathVariable Long size,UpdateLog updateLog){
        Page<UpdateLog> res = updateLogService.pageQuery(page,size,updateLog);
        return Result.ok(res);
    }

}

