package com.lj.vo;

import lombok.Data;

@Data
public class WebSocketMessageVo {
    private Integer type;    // 0 新消息提醒   1 系统消息
    private Object data;
    private Long sendUserId;
    private Long receiveUserId;
    private String content;
}
