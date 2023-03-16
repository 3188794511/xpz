package com.lj.blog.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.LeaveMessageService;
import com.lj.dto.LeaveMessageQueryDto;
import com.lj.model.blog.LeaveMessage;
import com.lj.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/xpz/api/blog/leave-message")
public class LeaveMessageApiController {
    @Autowired
    private LeaveMessageService leaveMessageService;

    /**
     * 发布一条留言
     * @param leaveMessage
     * @return
     */
    @MyLog("发送了留言")
    @PostMapping("/send")
    public Result sendLeaveMessage(@RequestBody @Validated LeaveMessage leaveMessage){
        Result res = leaveMessageService.saveLeaveMessage(leaveMessage);
        return res;
    }

    /**
     * 删除留言
     * @param ids
     * @return
     */
    @MyLog("删除了留言")
    @DeleteMapping("/remove")
    public Result removeLeaveMessage(@RequestParam("ids") List<Long> ids){
        boolean isSuccess = leaveMessageService.removeByIds(ids);
        return isSuccess ? Result.ok().message("删除留言成功") : Result.fail().message("删除留言失败");
    }

    /**
     * 条件查询自己的留言
     * @param page
     * @param size
     * @param leaveMessageQueryDto
     * @param request
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result searchMyLeaveMessageByParams(@PathVariable Long page, @PathVariable Long size,
                                               LeaveMessageQueryDto leaveMessageQueryDto, HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        Page<LeaveMessage> leaveMessagePage = leaveMessageService.searchLeaveMessageByParams(userId,page,size,leaveMessageQueryDto);
        return Result.ok(leaveMessagePage);
    }

    /**
     * 获取留言列表
     * @return
     */
    @GetMapping("/list")
    public Result listLeaveMessages(){
        List<LeaveMessage> list = leaveMessageService.listAll();
        return Result.ok(list);
    }

    /**
     * 留言数量
     * @return
     */
    @GetMapping("/count")
    public Result leaveMessagesCount(){
        int count = leaveMessageService.count();
        return Result.ok(count);
    }

}
