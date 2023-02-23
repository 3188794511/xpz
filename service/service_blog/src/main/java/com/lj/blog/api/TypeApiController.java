package com.lj.blog.api;

import com.lj.base.Result;
import com.lj.blog.service.TypeService;
import com.lj.model.blog.Type;
import com.lj.vo.TypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/xpz/api/blog/type")
public class TypeApiController {
    @Autowired
    private TypeService typeService;

    /**
     * 查询所有分类以及它们的子分类
     * @return
     */
    @GetMapping("/list/tree")
    public Result listByTree(){
        List<Type> typeList = typeService.listByTree();
        return Result.ok(typeList);
    }

    /**
     * 查询所有分类以及它们的子分类
     * @return
     */
    @GetMapping("/list/type-tree")
    public Result listTypeByTree(){
        List<TypeVo> typeVoTree = typeService.listTypeVoTree();
        return Result.ok(typeVoTree);
    }

    /**
     * 查询所有二级分类
     * @return
     */
    @GetMapping("/list/secondLevel")
    public Result listSecondLevel(){
        List<Type> typeList = typeService.listSecondLevel();
        return Result.ok(typeList);
    }

}
