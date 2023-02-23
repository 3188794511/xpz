package com.lj.vo;

import lombok.Data;

/**
 * 评论用户信息
 */
@Data
public class CommentUser {
    private Long id;
    private String name;
    private String avatar;
    private Boolean author;//是否为作者
}
