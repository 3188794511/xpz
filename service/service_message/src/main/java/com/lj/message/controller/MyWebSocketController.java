package com.lj.message.controller;

import com.alibaba.fastjson.JSON;
import com.lj.message.service.MessageService;
import com.lj.model.message.ChatMessageVo;
import com.lj.model.message.Message;
import com.lj.model.message.MyWebSocket;
import com.lj.util.JwtTokenUtil;
import com.lj.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.lj.util.SendMessageUtil.*;

@ServerEndpoint(value = "/message/{token}")
@Component
@Slf4j
public class MyWebSocketController {
    private static MessageService messageService;
    @Autowired
    public void MyWebSocketController(MessageService messageService){
        MyWebSocketController.messageService = messageService;
    }

    /**
     * 客户端新建ws连接时,触发的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {
            Long key = JwtTokenUtil.getUserId(token);
            MyWebSocket newSocket = prepareOpen(session, token);
            if(!webSocketMap.containsKey(key)){
                webSocketMap.put(key,newSocket);
                addOnlineCount();
                log.info("有新的客户端连接...当前在线人数为:{}",getOnlineCount());
            }
        } catch (Exception e) {
            log.error("连接时发生异常:",e.getMessage());
        }
    }

    @OnClose
    public void onClose(@PathParam("token") String token) {
        try {
            Long key = JwtTokenUtil.getUserId(token);
            webSocketMap.remove(key);
            subOnlineCount();
            log.info("有一连接关闭！当前在线人数为:{}" ,getOnlineCount());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("断开连接时发生异常:",e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session,@PathParam("token") String token) throws ExecutionException, InterruptedException {
        ChatMessageVo chatMessageVo = JSON.parseObject(message, ChatMessageVo.class);
        if(Objects.nonNull(chatMessageVo.getReceiveUserId()) && Objects.nonNull(chatMessageVo.getContent())){
            chatMessageVo.setSendUserId(JwtTokenUtil.getUserId(token));
            //将消息转发给指定用户(判断用户是否在线)
            MyWebSocket myWebSocket = webSocketMap.get(chatMessageVo.getReceiveUserId());
            if(Objects.nonNull(myWebSocket)){
                sendMessage2One(chatMessageVo);
            }
            //TODO 线程池优化
            ThreadPoolUtil.submit(() -> {
                //将消息保存到数据库
                Message msg = new Message();
                BeanUtils.copyProperties(chatMessageVo,msg);
                messageService.save(msg);
                //若接收用户未与登录用户发过消息,添加该登录用户
                boolean isSuccess = messageService.addChatUser(chatMessageVo.getSendUserId(),chatMessageVo.getReceiveUserId());
                return isSuccess;
            });
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        log.error("发生错误",error.getMessage());
    }
}
