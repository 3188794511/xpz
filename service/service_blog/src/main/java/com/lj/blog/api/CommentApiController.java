package com.lj.blog.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.CommentService;
import com.lj.dto.CommentQueryDto;
import com.lj.util.JwtTokenUtil;
import com.lj.vo.CommentViewVo;
import com.lj.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/xpz/api/blog/comment")
public class CommentApiController {
    @Autowired
    private CommentService commentService;

    /**
     * 新增一条评论
     * @param commentViewVo
     * @return
     */
    @MyLog(value = "发表评论")
    @PostMapping("/save")
    public Result saveComment(@RequestBody CommentViewVo commentViewVo){
        return commentService.saveComment(commentViewVo);
    }

    /**
     * 删除评论
     * @param ids
     * @return
     */
    @MyLog(value = "删除评论")
    @DeleteMapping("/remove")
    public Result removeComment(@RequestParam("ids") List<Long> ids) {
        return commentService.removeCommentByIds(ids);
    }

    /**
     * 查询评论(自己发布的博客下的评论)
     * @param page
     * @param size
     * @param commentQueryDto
     * @param request
     * @return
     */
    @GetMapping("search/{page}/{size}")
    public Result searchCommentByParams(@PathVariable Long page,@PathVariable Long size,CommentQueryDto commentQueryDto,HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        commentQueryDto.setUserId(userId);
        Page<CommentVo> commentVoList = commentService.pageParamQuery(page,size,commentQueryDto);
        return Result.ok(commentVoList);
    }

    /**
     * 根据博客id查询评论
     * @param blogId
     * @param orderBy 排序(0按照点赞数,1按照时间)
     * @return
     */
    @GetMapping("/{blogId}")
    public Result listTreeByBlogId(@PathVariable Long blogId, @RequestParam(required = false) Integer orderBy, HttpServletRequest request){
        //获取请求头token信息
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        List<CommentViewVo> commentViewVoList = commentService.listTreeByBlogId(blogId,orderBy,userId);
        return Result.ok(commentViewVoList);
    }

    /**
     * 在动态中查看评论(最多显示五条)
     * @param blogId
     * @param orderBy
     * @param request
     * @return
     */
    @GetMapping("/dynamic-comment/{blogId}")
    public Result getDynamicComment(@PathVariable Long blogId, @RequestParam(required = false) Integer orderBy, HttpServletRequest request){
        //获取请求头token信息
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        List<CommentViewVo> allComments = commentService.listTreeByBlogId(blogId,orderBy,userId);
        List<CommentViewVo> commentViewVoList = allComments.size() >= 5 ? allComments.subList(0,5) : allComments;
        return Result.ok(commentViewVoList);
    }

    /**
     * 查询当前用户是否已经点赞过该评论
     * @param commentId
     * @return
     */
    @GetMapping("/likes/is-liked/{commentId}")
    public Result isLiked(@PathVariable Long commentId,Long userId){
        Boolean isLiked = commentService.isLikedComment(commentId,userId);
        return isLiked ? Result.ok(1) : Result.ok(0);
    }

    /**
     * 给评论点赞或取消点赞
     * @param commentId
     * @return
     */
    @MyLog("点赞或取消点赞评论")
    @GetMapping("/likes/{commentId}")
    public Result likeComment(@PathVariable Long commentId){
        return commentService.likeComment(commentId);
    }

}
