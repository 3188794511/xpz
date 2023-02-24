package com.lj.vo.admin;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 日志条件Dto
 */
@Data
public class LogQueryDto {
    //日志关键字
    private String keyword;
    //日志发布时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
