package com.lj.client;

import com.lj.base.Result;
import com.lj.model.user.User;
import com.lj.model.user.ViewHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-user")
public interface UserClientService {
    @GetMapping("/xpz/api/user/get/{id}")
    User getById(@PathVariable("id") Long id);

    @PostMapping("/xpz/api/user/history/save")
    Result addViewHistory(@RequestBody ViewHistory viewHistory);
}
