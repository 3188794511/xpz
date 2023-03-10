package com.lj.model.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class LeaveMessage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    @NotBlank(message = "留言用户昵称不能为空")
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

    /**
     * 留言时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
