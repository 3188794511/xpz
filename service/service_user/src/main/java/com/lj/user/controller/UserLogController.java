package com.lj.user.controller;


import com.lj.base.Result;
import com.lj.model.user.UserLog;
import com.lj.user.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户行为日志表(记录用户点赞、评论、浏览博客等行为) 前端控制器
 * </p>
 *
 * @author lj
 * @since 2023-05-12
 */
@RestController
@RequestMapping("/xpz/admin/user/user-log")
public class UserLogController {
    @Autowired
    private UserLogService userLogService;

    /**
     * 新增用户操作行为
     * @param userLog
     * @return
     */
    @PostMapping("/save")
    public Result addUserLog(@RequestBody UserLog userLog){
        boolean isSuccess = userLogService.save(userLog);
        return isSuccess ? Result.ok() : Result.fail();
    }

}

