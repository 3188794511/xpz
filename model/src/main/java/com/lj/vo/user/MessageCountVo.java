package com.lj.vo.user;

import lombok.Data;

@Data
public class MessageCountVo {
    private Integer systemMsgCount;
    private Integer chatMsgCount;
    private Integer totalMsgCount;
}
