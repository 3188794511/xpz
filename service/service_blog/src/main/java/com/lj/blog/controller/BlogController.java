package com.lj.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.BlogService;
import com.lj.dto.BlogDto;
import com.lj.dto.BlogQueryDto1;
import com.lj.vo.BlogVo;
import com.lj.vo.ReasonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/xpz/admin/blog/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    /**
     * 博客审核通过
     * @param id
     * @return
     */
    @MyLog(type = "admin",value = "博客审核通过")
    @PutMapping("/approved/{id}")
    public Result approvedBlog(@PathVariable Long id){
        boolean isSuccess = blogService.approvedBlog(id);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 博客审核不通过
     * @param id
     * @return
     */
    @MyLog(type = "admin",value = "博客审核不通过")
    @PutMapping("/no-approved/{id}")
    public Result noApprovedBlog(@PathVariable Long id,@RequestBody ReasonVo reasonVo){
        boolean isSuccess = blogService.noApprovedBlog(id,reasonVo.getReason());
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 根据id查询博客
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getBlogById(@PathVariable Long id){
        BlogDto blogDto = blogService.getBlogById(id);
        return Result.ok(blogDto);
    }

    /**
     * 分页条件查询博客
     * @param page
     * @param size
     * @param blogQueryDto1
     * @return
     */
    @PostMapping("/{page}/{size}")
    public Result pageQueryBlog(@PathVariable Long page, @PathVariable Long size,@RequestBody BlogQueryDto1 blogQueryDto1){
        Page<BlogVo> res = blogService.pageQueryBlog(page, size, blogQueryDto1);
        return Result.ok(res);
    }



    /**
     * 根据id批量删除博客
     * @param ids
     * @return
     */
    @MyLog(type = "admin",value = "删除博客")
    @DeleteMapping("/remove/{ids}")
    public Result deleteBlog(@PathVariable List<Long> ids){
        boolean isSuccess = blogService.deleteBlogByIds(ids);
        return isSuccess ? Result.ok() : Result.fail();
    }


    /**
     * 统计每个标签所占博客数量
     * @return
     */
    @GetMapping("/reports/tag")
    public Result reportsBlogTags(){
        Map<String,Object> data = blogService.reportsBlogTags();
        return Result.ok(data);
    }


    /**
     * 统计每个分类下的博客数量
     * @return
     */
    @GetMapping("/reports/type")
    public Result reportsBlogTypes(){
        Map<String,Object> data = blogService.reportsBlogTypes();
        return Result.ok(data);
    }


}

