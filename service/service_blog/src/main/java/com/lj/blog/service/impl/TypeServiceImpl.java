package com.lj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.base.Result;
import com.lj.blog.mapper.TypeMapper;
import com.lj.blog.service.BlogService;
import com.lj.blog.service.TypeService;
import com.lj.model.blog.Blog;
import com.lj.model.blog.Type;
import com.lj.vo.TypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-11-05
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Autowired
    private BlogService blogService;

    /**
     * 查询所有分类以及它们的子分类
     * @return
     */
    public List<Type> listByTree() {
        //查询出所有一级分类
        List<Type> firstLevelTypeList = this.queryByParentId(-1L);
        //查询出一级分类对应的二级分类
        this.searchSecondLevelType(firstLevelTypeList);
        //封装数据返回
        return firstLevelTypeList;
    }

    /**
     * 查询所有二级分类
     * @return
     */
    public List<Type> listSecondLevel() {
        LambdaQueryWrapper<Type> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Type::getParentId,-1);
        List<Type> typeList = baseMapper.selectList(wrapper);
        return typeList;
    }

    /**
     * 根据id获取分类
     * @param id
     * @return
     */
    public Type getTypeById(Long id) {
        Type type = baseMapper.selectById(id);
        if (!StringUtils.isEmpty(type) && type.getParentId() != -1L){
            Type parentType = baseMapper.selectById(type.getParentId());
            type.getParams().put("parentName", parentType.getTypeName());
        }
        return type;
    }

    private void searchSecondLevelType(List<Type> firstLevelTypeList) {
        firstLevelTypeList.stream().forEach(f -> {
            List<Type> secondLevelTypeList = this.queryByParentId(f.getId());
            secondLevelTypeList.forEach(s -> s.getParams().put("parentName", f.getTypeName()));
            f.setSonTypeList(secondLevelTypeList);
        });
    }

    //根据父id查询分类
    public List<Type> queryByParentId(Long parentId){
        return this.baseMapper.selectList(new LambdaQueryWrapper<Type>().eq(Type::getParentId, parentId));
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    public Result removeTypeByIds(List<Long> ids) {
        for (Long typeId : ids) {
            LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Blog::getTypeId,typeId);
            int count = blogService.count(wrapper);
            if(count > 0){
                return Result.fail().message("所删除分类关联了文章,删除失败");
            }
        }
        boolean isSuccess = baseMapper.deleteBatchIds(ids) > 0;
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 查询 typeVoTree
     * @return
     */
    public List<TypeVo> listTypeVoTree() {
        List<Type> types = this.listByTree();
        List<TypeVo> typeVoTree = types.stream().map(t -> {
            TypeVo typeVo = new TypeVo();
            typeVo.setValue(t.getId());
            typeVo.setLabel(t.getTypeName());
            typeVo.setChildren(t.getSonTypeList().stream().map(i -> {
                TypeVo typeVo1 = new TypeVo();
                typeVo1.setValue(i.getId());
                typeVo1.setLabel(i.getTypeName());
                return typeVo1;
            }).collect(Collectors.toList()));
            return typeVo;
        }).collect(Collectors.toList());
        return typeVoTree;
    }

    /**
     * 查询完整的二级分类id
     * @param typeId
     * @return
     */
    public List<Long> getParentTypeAndChild(Long typeId) {
        Type type = baseMapper.selectById(typeId);
        Long parentId = type.getParentId();
        List<Long> secondType = new ArrayList<>();
        if(!type.equals(parentId)){
            secondType.add(parentId);
        }
        secondType.add(typeId);
        return secondType;
    }


}
