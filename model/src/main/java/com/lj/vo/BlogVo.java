package com.lj.vo;

import com.lj.model.blog.Blog;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlogVo extends Blog {
    //作者姓名
    private String authorName;
    //作者头像
    private String pic;
    //分类名称
    private String typeName;
    //标签名称
    private String tagNamesAsStr;
    private List<String> tagNames;
    //评论数量
    private Integer commentsCount;
    //是否展开评论
    private Boolean showComment = false;
    //评论列表
    private List<CommentViewVo> comments = new ArrayList<>();
    //当前用户是否点赞
    private Integer isLiked;
    private String contentMd;
}
