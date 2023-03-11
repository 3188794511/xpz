package com.lj.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lj.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_user")
public class User extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    @TableField("account")
    @NotBlank(message = "用户账号不能为空")
    private String account;

    /**
     * 用户密码
     */
    @TableField("password")
    @NotBlank(message = "用户密码不能为空")
    private String password;

    /**
     * 用户头像
     */
    @TableField("pic")
    private String pic;

    /**
     * 用户昵称
     */
    @TableField("nick_name")
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    /**
     * 用户角色(admin,user)
     */
    @TableField("role")
    @NotBlank(message = "用户角色不能为空")
    private String role;

    /**
     * 用户生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("birthday")
    private Date birthday;

    /**
     * 用户性别(0女,1男,2保密)
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 用户自我介绍
     */
    @TableField("self_describe")
    private String selfDescribe;
}
