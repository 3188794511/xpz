package com.lj.vo.user;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class BlogSearchDto{
    //关键字 title article_summary typeName authorName搜索
    private String keyword;
    //根据 0 title article_summary 或 1 typeName 或 2 authorName搜索
    private Integer by;
    //排序字段  0 发布时间  1 浏览量 2 点赞量
    private Integer sortWord;
    @Min(value = 1,message = "起始页必须为正整数")
    private Long page;
    @Min(value = 1,message = "分页大小必须为正整数")
    private Long size;
}
