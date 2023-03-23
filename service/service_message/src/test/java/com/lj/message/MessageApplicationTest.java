package com.lj.message;

import com.lj.model.message.MyWebSocket;
import com.lj.model.user.User;
import com.lj.util.SendMessageUtil;
import com.lj.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootTest(classes = MessageApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageApplicationTest {

    @Test
    void test1() {
        ConcurrentHashMap<Long, MyWebSocket> webSocketMap = SendMessageUtil.webSocketMap;
        System.out.println(webSocketMap.hashCode());
    }

    @Test
    void test2() {
        User user = new User();
        user.setId(1l);
        Long id = user.getId();
        System.out.println("id为:"+id);
        user.setId(2l);
        System.out.println("id为:"+id);
    }

    @Test
    void test3() throws ExecutionException, InterruptedException {
        ThreadPoolUtil.submit(() -> {
            log.info("任务1正在执行...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("任务1执行完成...");
            return true;
        });

        ThreadPoolUtil.submit(() -> {
            log.info("任务2正在执行...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("任务2执行完成...");
            return true;
        });

        ThreadPoolUtil.submit(() -> {
            log.info("任务3正在执行...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("任务3执行完成...");
            return true;
        });

    }
}
