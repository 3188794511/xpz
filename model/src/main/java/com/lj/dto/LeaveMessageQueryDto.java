package com.lj.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class LeaveMessageQueryDto {
    //关键字  留言用户昵称,留言内容
    private String keyword;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendTime;
}
