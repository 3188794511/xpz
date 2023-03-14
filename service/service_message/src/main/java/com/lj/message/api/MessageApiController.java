package com.lj.message.api;

import com.lj.base.Result;
import com.lj.message.service.MessageService;
import com.lj.model.message.Message;
import com.lj.util.JwtTokenUtil;
import com.lj.util.UserInfoContext;
import com.lj.vo.user.MessageCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/xpz/api/message")
public class MessageApiController {
    @Autowired
    private MessageService messageService;

    /**
     * 发送私信消息
     * @param message
     * @return
     */
    @PostMapping("/send")
    public Result sendMessage(@RequestBody Message message){
        boolean isSuccess = messageService.save(message);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 查询当前登录用户消息
     * @param
     * @return
     */
    @GetMapping("/list/{type}")
    public Result listMyMessages(@PathVariable Integer type){
        Long userId = UserInfoContext.get();
        List<Message> messages = messageService.listMessageById(userId,type);
        return Result.ok(messages);
    }

    /**
     * 查找登录用户与某个用户的聊天历史
     * @param id
     * @return
     */
    @GetMapping("/chat-history/{id}")
    public Result chatHistory(@PathVariable Long id){
        Long userId = UserInfoContext.get();
        List<Message> messages = messageService.chatHistory(userId,id);
        return Result.ok(messages);
    }

    /**
     * 用户消息全部已读 类型:0 1
     * @return
     */
    @PutMapping("/read-all-01")
    public Result readAllMessage01(HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        messageService.readAllType01(userId);
        return Result.ok();
    }

    /**
     * 用户未读消息数量 类型:0 1
     * @return
     */
    @GetMapping("/unread-01")
    public Result unreadMegCount01(HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        MessageCountVo messageCountVo = messageService.unReadMessageCount01(userId);
        return Result.ok(messageCountVo);
    }

    /**
     * 用户消息全部已读 类型:3
     * @return
     */
    @PutMapping("/read-all-3")
    public Result readAllMessage3(HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        messageService.readAllType3(userId);
        return Result.ok();
    }

    /**
     * 用户未读消息数量 类型:0 1
     * @return
     */
    @GetMapping("/unread-3")
    public Result unreadMegCount3(HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        Long count = messageService.unReadMessageCount3(userId);
        return Result.ok(count);
    }

    /**
     * 已读一条消息
     * @param id
     * @return
     */
    @PutMapping("/read/{id}")
    public Result readMessage(@PathVariable Long id){
        messageService.readMsg(id);
        return Result.ok();
    }

    /**
     * 删除一条消息
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public Result deleteMessage(@PathVariable Long id){
        boolean isSuccess = messageService.removeById(id);
        return isSuccess ? Result.ok().message("消息删除成功") : Result.fail().message("消息删除失败");
    }


}
