package com.lj.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论用户信息
 */
@Data
public class CommentUser {
    @NotNull(message = "评论用户ID不能为空")
    private Long id;
    @NotBlank(message = "评论用户昵称不能为空")
    private String name;
    private String avatar;
    private Boolean author;//是否为作者
}
