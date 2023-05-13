package com.lj.model.user;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 用户行为日志表(记录用户点赞、评论、浏览博客等行为)
 * </p>
 *
 * @author lj
 * @since 2023-05-12
 */
@Data
@TableName("user_log")
public class UserLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("behavior")
    private String behavior;

    @TableField("blog_id")
    private Long blogId;

    @TableField("create_time")
    private Date createTime;
}
