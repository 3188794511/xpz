package com.lj.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserInfoDto {
    @NotBlank(message = "用户头像不能为空")
    private String pic;
    @Range(min = 0,max = 2,message = "性别(0:女 1:男 2:保密)")
    private Integer sex;
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "用户生日不能为空")
    private Date birthday;
    @NotBlank(message = "用户自我介绍不能为空")
    private String selfDescribe;
    private Boolean isFollowed;
}
