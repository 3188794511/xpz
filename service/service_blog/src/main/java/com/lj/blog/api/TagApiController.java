package com.lj.blog.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lj.base.Result;
import com.lj.blog.service.TagService;
import com.lj.model.blog.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/xpz/api/blog/tag")
public class TagApiController {
    @Autowired
    private TagService tagService;

    /**
     * 所有标签
     * @return
     */
    @GetMapping("/list")
    public Result list(){
        List<Tag> tagList = tagService.list(new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getSort));
        return Result.ok(tagList);
    }

}
