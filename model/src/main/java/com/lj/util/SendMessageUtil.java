package com.lj.util;

import com.alibaba.fastjson.JSON;
import com.lj.model.message.ChatMessageVo;
import com.lj.model.message.MyWebSocket;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class SendMessageUtil {
    public static final AtomicInteger onlineCount = new AtomicInteger();
    public static final ConcurrentHashMap<Long,MyWebSocket> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 根据userId获取socket
     * @param userId
     * @return
     */
    public static MyWebSocket getMyWebSocketByUserId(Long userId){
        return webSocketMap.get(userId);
    }

    /**
     * 获取所有在线用户id
     * @return
     */
    public static List<Long> getOnlineUserIds(){
        List<Long> ids = webSocketMap.keySet().stream().map(i -> i.longValue()).collect(Collectors.toList());
        return ids;
    }

    /**
     * 根据token获取socket
     * @param token
     * @return
     */
    public static MyWebSocket getMyWebSocketByToken(String token){
        Long userId = JwtTokenUtil.getUserId(token);
        return webSocketMap.get(userId);
    }

    /**
     * 创建一个socket
     * @param session
     * @param token
     * @return
     */
    public static MyWebSocket prepareOpen(Session session, String token){
        MyWebSocket myWebSocket = new MyWebSocket();
        myWebSocket.setSession(session);
        return myWebSocket;
    }

    /**
     * 给指定用户发消息
     * @param chatMessageVo
     */
    public static boolean sendMessage2One(ChatMessageVo chatMessageVo){
        try {
            getMyWebSocketByUserId(chatMessageVo.getReceiveUserId()).getSession().getBasicRemote().sendText(JSON.toJSONString(chatMessageVo));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("服务端发送消息时发错误:",e.getMessage());
            return false;
        }
    }

    /**
     *
     * 给所有满足条件的用户发消息
     * @param ids
     */
    public static boolean sendMessage2All(List<Long>ids,Integer type,String content,Long sendUserId){
        AtomicBoolean isSuccess = new AtomicBoolean(true);
        ids.forEach(id -> {
            try {
                if(Objects.nonNull(webSocketMap.get(id))){
                    ChatMessageVo chatMessageVo = new ChatMessageVo();
                    chatMessageVo.setType(type);
                    chatMessageVo.setContent(content);
                    chatMessageVo.setReceiveUserId(id);
                    chatMessageVo.setSendUserId(sendUserId);
                    chatMessageVo.setCreateTime(new Date());
                    String msgStr = JSON.toJSONString(chatMessageVo);
                    getMyWebSocketByUserId(id).getSession().getBasicRemote().sendText(msgStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("服务端发送消息时发错误:",e.getMessage());
                isSuccess.set(false);
            }
        });
        return isSuccess.get();
    }

    /**
     * 获取在线人数
     * @return
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 在线人数+1
     */
    public static void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    /**
     * 在线人数-1
     */
    public static void subOnlineCount() {
        synchronized (SendMessageUtil.class){
            if(getOnlineCount() > 0){
                onlineCount.decrementAndGet();
            }
        }
    }

}
