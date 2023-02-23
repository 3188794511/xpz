package com.lj.vo;

import com.lj.model.blog.Comment;
import lombok.Data;

@Data
public class CommentVo extends Comment {
    //博客标题
    private String blogTile;
    //评论用户姓名
    private String userName;
    //父评论用户姓名
    private String parentCommentUser;
}
