package com.lj.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户浏览历史表
 * </p>
 *
 * @author lj
 * @since 2023-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ViewHistory implements Serializable {
    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id

     */
    @TableField("user_id")
    private Long userId;

    /**
     * 帖子id
     */
    @TableField("blog_id")
    private Long blogId;

    /**
     * 浏览时间
     */
    @TableField("view_time")
    private Date viewTime;

    /**
     * 访问类型(0 帖子    1 用户)
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}
