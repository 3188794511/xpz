package com.lj.blog.api;

import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.LeaveMessageService;
import com.lj.model.blog.LeaveMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result sendLeaveMessage(@RequestBody LeaveMessage leaveMessage){
        Result res = leaveMessageService.saveLeaveMessage(leaveMessage);
        return res;
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
