package com.lj.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDto {
    private String pic;
    private Integer sex;
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String selfDescribe;
    private Boolean isFollowed;
}
