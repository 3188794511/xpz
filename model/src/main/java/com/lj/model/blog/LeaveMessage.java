package com.lj.model.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
    private String content;

    /**
     * 留言时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
