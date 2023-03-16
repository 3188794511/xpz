package com.lj.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class BlogQueryDto3 {
    private Long userId; //作者id
    private Long typeId; //分类id
    private List<Long> typeIds;
    private Integer status;
    private String keyword; //title article_summary
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date createTime; //发布时间
}
