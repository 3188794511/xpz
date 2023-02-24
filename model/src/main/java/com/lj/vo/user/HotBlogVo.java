package com.lj.vo.user;

import com.lj.model.blog.Blog;
import lombok.Data;

@Data
public class HotBlogVo extends Blog {
    private String pic;//用户头像
    private String username;//用户昵称
}
