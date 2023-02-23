package com.lj.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.CommentService;
import com.lj.vo.CommentQueryDto;
import com.lj.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/xpz/admin/blog/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 分页查询评论
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public Result list(@PathVariable Long page, @PathVariable Long size, CommentQueryDto commentQueryDto){
        Page<CommentVo> commentPage = commentService.pageParamQuery(page,size, commentQueryDto);
        return Result.ok(commentPage);
    }
    /**
     * 根据id删除评论
     * @param id
     * @return
     */
    @MyLog(type = "admin",value = "删除评论")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean isSuccess = commentService.removeCommentById(id);
        return isSuccess ? Result.ok() : Result.fail();
    }
}

