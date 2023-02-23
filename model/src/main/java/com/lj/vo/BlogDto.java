package com.lj.vo;

import lombok.Data;
import com.lj.model.blog.Blog;

import java.util.List;

@Data
public class BlogDto extends Blog {
    //博客详情md
    private String contentMd;
    //博客详情html
    private String contentHtml;
    //博客分类
    private List<Long> typeIds;
    private String typeName;
    //博客标签
    private List<String> tagNames;
}
