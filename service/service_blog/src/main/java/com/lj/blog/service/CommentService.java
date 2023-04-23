package com.lj.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.base.Result;
import com.lj.model.blog.Comment;
import com.lj.dto.CommentQueryDto;
import com.lj.vo.CommentViewVo;
import com.lj.vo.CommentVo;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
public interface CommentService extends IService<Comment> {

    Result saveComment(CommentViewVo commentViewVo);

    List<CommentViewVo> listTreeByBlogId(Long blogId, int orderBy,Long userId);

    boolean removeCommentById(Long id);

    Page<CommentVo> pageParamQuery(Long page, Long size, CommentQueryDto commentQueryDto);

    Result likeComment(Long commentId);

    Boolean isLikedComment(Long commentId,Long userId);

    Result removeCommentByIds(List<Long> ids);

    Long getUserBlogsCommentNums(Long userId);

}
