package com.lj.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BlogQueryDto3 {
    private Long userId; //作者id
    private Long typeId; //分类id
    private String keyword; //title article_summary
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishTime; //发布时间
}
