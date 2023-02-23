package com.lj.model.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lj.base.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 博客-标签表
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Data
@TableName("blog_tag")
public class BlogTag extends BaseModel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 博客id
     */
    @TableField("blog_id")
    private Long blogId;

    /**
     * 标签id
     */
    @TableField("tag_id")
    private Long tagId;

}
