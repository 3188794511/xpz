package com.lj.client;

import com.lj.vo.UserCoreDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-blog")
public interface BlogClientService {

    @GetMapping("/xpz/api/blog/blog/user-blog-data")
    List<UserCoreDataVo> getUserBlogData(@RequestParam("userId") Long userId);
}
