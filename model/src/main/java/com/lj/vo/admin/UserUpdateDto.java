package com.lj.vo.admin;

import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateDto {
    private Long id;
    private String nickName;
    private String pic;
    private Date birthday;
    private Integer sex;
    private String selfDescribe;
    private String role;
}
