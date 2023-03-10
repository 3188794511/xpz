package com.lj.model.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@TableName("update_log")
@Data
public class UpdateLog implements Serializable {
    private static final long serialVersionUID=1L;

    @TableField("id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    @NotBlank(message = "发布更新日志的用户昵称不能为空")
    private String username;

    @TableField("content")
    @NotBlank(message = "更新日志内容不能为空")
    private String content;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
