package com.lj.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVo {
    private Long id;
    private String nickName;
    private String pic;
    private Date birthday;
    private Integer sex;
    private String selfDescribe;
    private Integer isOnline;  //是否在线
}
