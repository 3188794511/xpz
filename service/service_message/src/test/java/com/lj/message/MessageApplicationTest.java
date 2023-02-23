package com.lj.message;

import com.lj.model.message.MyWebSocket;
import com.lj.util.SendMessageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest(classes = MessageApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageApplicationTest {

    @Test
    void test1() {
        ConcurrentHashMap<Long, MyWebSocket> webSocketMap = SendMessageUtil.webSocketMap;
        System.out.println(webSocketMap.hashCode());
    }
}
