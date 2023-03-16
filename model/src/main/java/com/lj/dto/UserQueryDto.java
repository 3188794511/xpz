package com.lj.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserQueryDto {
    private String keyword;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date createTime;
}
