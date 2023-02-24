package com.lj.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lj.vo.user.HotBlogVo;
import lombok.Data;

import java.util.Date;

@Data
public class ViewHistoryVo {
    private Long id;
    private HotBlogVo hotBlogVo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date viewTime;
}
