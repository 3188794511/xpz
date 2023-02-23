package com.lj.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserQueryDto {
    private String keyword;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
