package com.lj.model.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lj.base.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 博客表
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Data
@TableName("blog")
public class Blog extends BaseModel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 博客标题
     */
    @TableField("title")
    private String title;

    /**
     * 博客作者id
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 博客摘要
     */
    @TableField("article_summary")
    private String articleSummary;

    /**
     * 博客浏览量

     */
    @TableField("views")
    private Long views;

    /**
     * 博客封面
     */
    @TableField("cover")
    private String cover;

    /**
     * 博客点赞数
     */
    @TableField("likes")
    private Long likes;

    /**
     * 排序
     */
    @TableField("sorted")
    private Integer sorted;

    /**
     * 博客分类id
     */
    @TableField("type_id")
    private Long typeId;

    /**
     * 博客状态(0 草稿,1 已发布)
     */
    @TableField("status")
    private Integer status;

    /**
     * 博客发布时间
     */
    @TableField("publish_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

}
