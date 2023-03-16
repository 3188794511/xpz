package com.lj.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.base.Result;
import com.lj.blog.service.LeaveMessageService;
import com.lj.model.blog.LeaveMessage;
import com.lj.dto.LeaveMessageQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 留言表 前端控制器
 * </p>
 *
 * @author lj
 * @since 2022-12-01
 */
@RestController
@RequestMapping("/xpz/admin/blog/leave-message")
public class LeaveMessageController {
    @Autowired
    private LeaveMessageService leaveMessageService;

    /**
     * 分页条件查询留言
     * @param page
     * @param size
     * @param leaveMessageQueryDto
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public Result PageQueryLeaveMessage(@PathVariable Long page, @PathVariable Long size, LeaveMessageQueryDto leaveMessageQueryDto){
        Page<LeaveMessage> res = leaveMessageService.PageQueryLeaveMessage(page,size,leaveMessageQueryDto);
        return Result.ok(res);
    }
}

