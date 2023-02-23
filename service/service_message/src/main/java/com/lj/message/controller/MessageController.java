package com.lj.message.controller;

import com.lj.base.Result;
import com.lj.message.service.MessageService;
import com.lj.model.message.Message;
import com.lj.model.message.SendMessage2AllDto;
import com.lj.util.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/xpz/admin/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 保存一条消息
     * @param message
     * @return
     */
    @PostMapping("/send")
    public Result sendMessage(@RequestBody Message message){
        boolean isSuccess = messageService.save(message);
        return isSuccess ? Result.ok().message("消息发送成功") : Result.fail().message("消息发送失败");
    }

    /**
     * 批量保存消息
     * @param messages
     * @return
     */
    @PostMapping("/save")
    public Result saveMessage(@RequestBody List<Message> messages){
        boolean isSuccess = messageService.saveBatch(messages);
        return isSuccess ? Result.ok().message("消息保存成功") : Result.fail().message("消息保存失败");
    }

    /**
     * 批量发送消息
     * @param sendMessage2AllDto
     * @return
     */
    @PostMapping("/send-all")
    public Result sendMessage2All(@RequestBody SendMessage2AllDto sendMessage2AllDto) {
        List<Long> ids = sendMessage2AllDto.getIds();
        Integer type = sendMessage2AllDto.getType();
        Long sendUserId = sendMessage2AllDto.getSendUserId();
        String content = sendMessage2AllDto.getContent();
        boolean isSuccess = SendMessageUtil.sendMessage2All(ids, type, content, sendUserId);
        return isSuccess ? Result.ok().message("消息发送成功") : Result.fail().message("消息发送失败");
    }

    /**
     * 得到在线用户ids
     * @return
     */
    @GetMapping("/online")
    public List<Long> getOnlineUserIds(){
        List<Long> onlineUserIds = SendMessageUtil.getOnlineUserIds();
        return onlineUserIds;
    }


}
