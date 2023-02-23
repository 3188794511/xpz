package com.lj.model.message;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageVo {
    private Long sendUserId;
    private Long receiveUserId;
    private String content;
    private Date createTime;
    private Integer type;  //消息类型  0 系统消息  1 私信消息  2 系统 私信消息未读数量 3 动态消息 4 动态消息未读数量
}
