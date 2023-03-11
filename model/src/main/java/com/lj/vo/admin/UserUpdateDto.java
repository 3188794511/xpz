package com.lj.vo.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UserUpdateDto {
    @NotNull(message = "用户ID不能为空")
    private Long id;
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;
    @NotBlank(message = "用户头像不能为空")
    private String pic;
    private Date birthday;
    private Integer sex;
    @NotBlank(message = "用户自我介绍不能为空")
    private String selfDescribe;
    @NotBlank(message = "用户角色不能为空")
    private String role;
}
