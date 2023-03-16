package com.lj;

import com.lj.log.MyLogApplication;
import com.lj.log.service.LogService;
import com.lj.dto.LogQueryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(classes = MyLogApplication.class)
public class MyLogApplicationTest {
    @Autowired
    private LogService logService;

    @Test
    void test1() {
        LogQueryDto logQueryDto = new LogQueryDto();
        logQueryDto.setKeyword("依山");
        logQueryDto.setDate(new Date());
        System.out.println(logService.pageQueryLog(1L, 5L, logQueryDto));
    }
}
