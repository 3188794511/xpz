package com.lj.dto;

import com.lj.model.blog.Blog;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BlogDto extends Blog {
    //博客详情md
    @NotBlank(message = "投稿详情不能为空")
    private String contentMd;
    //博客详情html
    private String contentHtml;
    //博客分类
    @NotEmpty(message = "分类不能为空")
    private List<Long> typeIds;
    private String typeName;
    //博客标签
    @NotEmpty(message = "标签不能为空")
    private List<String> tagNames;
}
