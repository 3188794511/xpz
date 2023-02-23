package com.lj.model.blog;

import com.lj.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 博客详情表
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Data
@TableName("blog_detail")
public class BlogDetail extends BaseModel implements Serializable {

    private static final long serialVersionUID=1L;


    /**
     * 对应博客id
     */
    @TableField("blog_id")
    private Long blogId;

    /**
     * 博客markdown内容
     */
    @TableField("content_md")
    private String contentMd;

    /**
     * 博客html内容
     */
    @TableField("content_html")
    private String contentHtml;

}
