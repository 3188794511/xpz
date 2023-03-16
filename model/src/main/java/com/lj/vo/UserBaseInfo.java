package com.lj.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserBaseInfo {
    private Long id;
    private String account;
    private String nickName;
    private String pic;
    private Date birthday;
    private Integer sex;
    private String selfDescribe;
    private String role;
}
