package com.lj.message.task;

import com.lj.message.service.MessageService;
import com.lj.model.message.ChatMessageVo;
import com.lj.util.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
public class MyScheduleTask {
    @Autowired
    private MessageService messageService;

    /**
     * 监听用户新消息数量
     */
    @Scheduled(cron = "0/10 * * * * ? ")
    public void newMessageNotice(){
        SendMessageUtil.webSocketMap.entrySet().forEach(i -> {
            //消息类型为0 1的未读数量
            Long userId = i.getKey();
            ChatMessageVo unreadMsg1 = new ChatMessageVo();
            unreadMsg1.setReceiveUserId(userId);
            unreadMsg1.setType(2);
            unreadMsg1.setCreateTime(new Date());
            unreadMsg1.setContent(messageService.unReadMessageCount01(userId).toString());
            SendMessageUtil.sendMessage2One(unreadMsg1);
            //消息类型为3的未读数量
            ChatMessageVo unreadMsg2 = new ChatMessageVo();
            unreadMsg2.setReceiveUserId(userId);
            unreadMsg2.setType(4);
            unreadMsg2.setCreateTime(new Date());
            unreadMsg2.setContent(messageService.unReadMessageCount3(userId).toString());
            SendMessageUtil.sendMessage2One(unreadMsg2);
        });
    }
}
