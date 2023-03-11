package com.lj.blog.controller;


import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.TypeService;
import com.lj.model.blog.Type;
import com.lj.vo.TypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/xpz/admin/blog/type")
public class TypeController {
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
     * 添加分类
     * @param type
     * @return
     */
    @MyLog(type = "admin",value = "新增分类")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated Type type){
        boolean isSuccess = typeService.save(type);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 修改分类
     * @param type
     * @return
     */
    @MyLog(type = "admin",value = "修改分类")
    @PutMapping("/update")
    public Result updateById(@RequestBody @Validated Type type){
        boolean isSuccess = typeService.updateById(type);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @MyLog(type = "admin",value = "删除分类")
    @DeleteMapping("/remove/{ids}")
    public Result removeById(@PathVariable List<Long> ids){
        return typeService.removeTypeByIds(ids);
    }

    /**
     * 根据id获取分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){
        Type type = typeService.getTypeById(id);
        return Result.ok(type);
    }

    /**
     * 获取所有一级分类
     * @return
     */
    @GetMapping("/parent")
    public Result getParentType(){
        List<Type> typeList = typeService.queryByParentId(-1L);
        return Result.ok(typeList);
    }

    /**
     * 获取所有二级分类
     * @return
     */
    @GetMapping("/second")
    public Result getSecondLevel(){
        List<Type> types = typeService.listSecondLevel();
        return Result.ok(types);
    }



}

