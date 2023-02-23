package com.lj.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.TagService;
import com.lj.model.blog.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lj
 * @since 2022-11-05
 */
@RestController
@RequestMapping("/xpz/admin/blog/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 分页查询标签
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public Result listByPage(@PathVariable Long page,@PathVariable Long size,String keyword){
        Page<Tag> resPage = tagService.pageParamQuery(page,size,keyword);
        return Result.ok(resPage);
    }

    @GetMapping("/list")
    public Result list(){
        List<Tag> tagList = tagService.list(new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getSort));
        return Result.ok(tagList);
    }

    @MyLog(type = "admin",value = "新增标签")
    @PostMapping("/add")
    public Result add(@RequestBody Tag tag){
        boolean isSuccess = tagService.save(tag);
        return isSuccess ? Result.ok() : Result.fail();
    }

    @MyLog(type = "admin",value = "修改标签")
    @PutMapping("/update")
    public Result updateById(@RequestBody Tag tag){
        boolean isSuccess = tagService.updateById(tag);
        return isSuccess ? Result.ok() : Result.fail();
    }

    @MyLog(type = "admin",value = "删除标签")
    @DeleteMapping("/remove/{ids}")
    public Result removeById(@PathVariable List<Long> ids){
        return tagService.removeTagByIds(ids);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){
        Tag tag = tagService.getById(id);
        return Result.ok(tag);
    }

}

