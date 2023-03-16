package com.lj.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CommentQueryDto {
    //博客标题
    private String blogTitle;
    //评论用户姓名
    private String commentUsername;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date createTime;
}
