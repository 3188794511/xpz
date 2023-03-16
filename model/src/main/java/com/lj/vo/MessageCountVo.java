package com.lj.vo;

import lombok.Data;

@Data
public class MessageCountVo {
    private Integer systemMsgCount;
    private Integer chatMsgCount;
    private Integer totalMsgCount;
}
