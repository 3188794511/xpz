package com.lj.vo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserSecretInfo {
    @NotBlank(message = "密码不能为空")
    private String password;
}
