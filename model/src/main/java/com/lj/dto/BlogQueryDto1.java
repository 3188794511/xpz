package com.lj.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 博客查询条件dto
 */
@Data
public class BlogQueryDto1 {
    //关键字  title auth_name article_summary
    private String keyword;
    //分类
    private Long typeId;
    private List<Long> typeIds;
    //状态
    private Integer status;
    //创建时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date createTime;
}
