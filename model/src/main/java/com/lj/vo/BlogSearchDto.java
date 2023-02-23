package com.lj.vo;

import lombok.Data;

@Data
public class BlogSearchDto{
    //关键字 title article_summary typeName authorName搜索
    private String keyword;
    //根据 0 title article_summary 或 1 typeName 或 2 authorName搜索
    private Integer by;
    //排序字段  0 发布时间  1 浏览量 2 点赞量
    private Integer sortWord;
    private Long page;
    private Long size;
}
