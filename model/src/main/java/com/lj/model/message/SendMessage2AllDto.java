package com.lj.model.message;

import lombok.Data;

import java.util.List;

@Data
public class SendMessage2AllDto {
    private List<Long> ids;
    private Integer type;
    private String content;
    private Long sendUserId;
}
