package com.lj.client;

import com.lj.base.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-blog")
public interface BlogClientService {
    @RequestMapping("/xpz/api/blog/blog/data/views/count")
    Result<Long> blogViews(@RequestParam("userId") Long userId);

    @RequestMapping("/xpz/api/blog/comment/data/count")
    Result<Long> userCommentCount(@RequestParam("userId") Long userId);

    @RequestMapping("/xpz/api/blog/blog/data/likes/count")
    Result<Long> blogLikes(@RequestParam("userId") Long userId);
}
