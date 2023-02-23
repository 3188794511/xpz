package com.lj.model.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lj.base.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Data
@TableName("comment")
public class Comment extends BaseModel implements Serializable {

    private static final long serialVersionUID=1L;


    /**
     * 评论用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 回复用户id
     */
    @TableField("replay_user_id")
    private Long replayUserId;

    /**
     * 博客id
     */
    @TableField("blog_id")
    private Long blogId;

    /**
     * 父评论id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 点赞数
     */
    @TableField("is_liked")
    private Long isLiked;


}
