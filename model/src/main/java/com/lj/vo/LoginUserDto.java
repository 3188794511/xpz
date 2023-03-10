package com.lj.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUserDto {
    @NotBlank(message = "账号不能为空")
    private String account;
    @NotBlank(message = "密码不能为空")
    private String password;
}
