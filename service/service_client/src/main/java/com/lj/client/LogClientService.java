package com.lj.client;

import com.lj.base.Result;
import com.lj.model.log.MyLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-log")
public interface LogClientService {
    @PostMapping("/xpz/log/my-log/save")
    Result saveLog(@RequestBody MyLog myLog);

}
