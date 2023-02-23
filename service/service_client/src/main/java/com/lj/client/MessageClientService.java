package com.lj.client;

import com.lj.base.Result;
import com.lj.model.message.Message;
import com.lj.model.message.SendMessage2AllDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("service-message")
public interface MessageClientService {
    @PostMapping("/xpz/admin/message/send")
    Result sendMessage(@RequestBody Message message);

    @PostMapping("/xpz/admin/message/save")
    Result saveMessage(@RequestBody List<Message> messages);

    @PostMapping("/xpz/admin/message/send-all")
    Result sendMessage2All(@RequestBody SendMessage2AllDto sendMessage2AllDto);

    @GetMapping("/xpz/admin/message/online")
    List<Long> getOnlineUserIds();
}
