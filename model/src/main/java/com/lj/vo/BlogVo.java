package com.lj.vo;

import com.lj.model.blog.Blog;
import lombok.Data;

import java.util.List;

@Data
public class BlogVo extends Blog {
    //作者姓名
    private String authorName;
    //作者头像
    private String pic;
    //分类名称
    private String typeName;
    //标签名称
    private String tagNamesAsStr;
    private List<String> tagNames;
}
