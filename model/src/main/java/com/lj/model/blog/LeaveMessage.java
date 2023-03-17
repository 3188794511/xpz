package com.lj.model.blog;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lj.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 留言表
 * </p>
 *
 * @author lj
 * @since 2022-12-01
 */
@Data
@TableName("leave_message")
public class LeaveMessage extends BaseModel implements Serializable{

    private static final long serialVersionUID=1L;

    /**
     * 留言用户id
     */
    @TableField("user_id")
    @NotNull(message = "留言用户ID不能为空")
    private Long userId;

    /**
     * 留言用户昵称
     */
    @TableField(exist = false)
    private String username;

    /**
     * 留言用户头像
     */
    @TableField(exist = false)
    private String pic;

    /**
     * 留言内容
     */
    @TableField("content")
    @NotBlank(message = "留言内容不能为空")
    private String content;
}
