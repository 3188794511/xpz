package com.lj.vo.user;

import com.lj.model.blog.Blog;
import lombok.Data;

import java.util.List;

@Data
/**
 * 用户发布的博客Vo
 */
public class UserBlogVo {
    private Long userId;
    private Long total;
    private List<Blog> blogs;
}
