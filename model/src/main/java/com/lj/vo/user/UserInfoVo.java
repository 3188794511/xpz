package com.lj.vo.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVo {
    private String account;
    private Long id;
    private String nickName;
    private String pic;
    private Date birthday;
    private Integer sex;
    private String selfDescribe;
    private Integer isOnline;  //是否在线
}
