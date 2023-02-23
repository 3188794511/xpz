package com.lj.blog.es;

import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@IndexName("blog")
public class BlogDocument implements Serializable {
    private static final long serialVersionUID=1L;

    /**
     * 博客id
     */
    @IndexId(type = IdType.CUSTOMIZE)
    private Long id;

    /**
     * 博客标题
     */
    @HighLight(fragmentSize = 30,preTag = "<span style='color: #F25D8E'>",postTag = "</span>")
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_SMART)
    private String title;

    /**
     * 博客摘要
     */
    @HighLight(fragmentSize = 30,preTag = "<span style='color: #F25D8E'>",postTag = "</span>")
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_SMART)
    private String articleSummary;

    /**
     * 博客浏览量
     */
    @IndexField(fieldType = FieldType.LONG)
    private Long views;

    /**
     * 博客点赞数
     */
    @IndexField(fieldType = FieldType.LONG)
    private Long likes;

    /**
     * 博客封面
     */
    @IndexField(fieldType = FieldType.KEYWORD)
    private String cover;

    /**
     * 博客发布时间
     */
    @IndexField(fieldType = FieldType.KEYWORD)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    /**
     * 作者id
     */
    @IndexField(fieldType = FieldType.LONG)
    private Long authorId;

    /**
     * 作者姓名 不指定类型默认被创建为keyword类型,可进行精确查询
     */
    @HighLight(preTag = "<span style='color: #F25D8E'>",postTag = "</span>")
    @IndexField(fieldType = FieldType.KEYWORD)
    private String authorName;

    /**
     * 作者头像
     */
    @IndexField(fieldType = FieldType.KEYWORD)
    private String pic;

    /**
     * /分类名称
     */
    @IndexField(fieldType = FieldType.KEYWORD)
    @HighLight(preTag = "<span style='color: #F25D8E'>",postTag = "</span>")
    private String typeName;
}
