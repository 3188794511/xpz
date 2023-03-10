package com.lj.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class CommentViewVo{
    private Long id;//评论id
    private Long parentId;//父评论id
    @NotNull(message = "评论的博客ID不能为空")
    private Long blogId;//博客id
    private List<CommentViewVo> childrenComments;//子评论
    private CommentUser replayUser;//回复用户信息
    private Long isLiked;//评论点赞数
    private Boolean liked;//当前用户是否已点赞
    @NotBlank(message = "评论内容不能为空")
    private String content;//评论内容
    private CommentUser commentUser;//评论用户信息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//评论时间
}
