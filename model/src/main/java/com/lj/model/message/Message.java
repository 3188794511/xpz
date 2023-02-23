package com.lj.model.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户消息表
 * </p>
 *
 * @author lj
 * @since 2023-01-05
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收消息用户id
     */
    @TableField("receive_user_id")
    private Long receiveUserId;

    @TableField("send_user_id")
    private Long sendUserId;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 消息类型  0 系统消息  1 私信消息  2 系统 私信消息未读数量
     *         3 动态消息 4 动态消息未读数量
     */
    @TableField("type")
    private Integer type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 消息状态 0 未读 1 已读
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

}
