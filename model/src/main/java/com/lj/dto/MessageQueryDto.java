package com.lj.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.util.Date;

@Data
public class MessageQueryDto {
    private Integer type;//消息类型
    private Integer status;//消息状态
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date createTime;//消息发送时间
    @Min(1)
    private Long page;
    @Min(1)
    private Long size;
}
