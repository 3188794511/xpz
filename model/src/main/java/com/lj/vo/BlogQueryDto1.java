package com.lj.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 博客查询条件dto
 */
@Data
public class BlogQueryDto1 {
    //关键字  title auth_name article_summary
    private String keyword;
    //分类
    private Long typeId;
    //状态
    private Integer status;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
