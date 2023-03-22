package com.lj.vo;

import com.lj.model.message.Message;
import lombok.Data;

import java.util.Date;

@Data
public class MessageVo extends Message {
    private String sendUserName;
    private String sendUserPic;
}
