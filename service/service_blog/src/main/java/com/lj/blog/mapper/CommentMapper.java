package com.lj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.blog.Comment;
import com.lj.vo.CommentQueryDto;
import com.lj.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVo> selectCommentPage(@Param("page") Long page,@Param("size") Long size,@Param("commentQueryDto") CommentQueryDto commentQueryDto);

    long selectCountParam(@Param("commentQueryDto") CommentQueryDto commentQueryDto);
}
