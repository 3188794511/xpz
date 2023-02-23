package com.lj.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CommentQueryDto {
    //博客标题或评论用户姓名
    private String keyword;
    //评论发表时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
