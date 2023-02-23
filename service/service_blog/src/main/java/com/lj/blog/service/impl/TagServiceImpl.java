package com.lj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.base.Result;
import com.lj.blog.mapper.TagMapper;
import com.lj.blog.service.BlogTagService;
import com.lj.blog.service.TagService;
import com.lj.model.blog.BlogTag;
import com.lj.model.blog.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-11-05
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private BlogTagService blogTagService;
    /**
     * 条件分页查询
     * @param page
     * @param size
     * @param keyword
     * @return
     */
    public Page<Tag> pageParamQuery(Long page, Long size, String keyword) {
        Page<Tag> tagPage = new Page<>(page,size);
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(keyword),Tag::getTagName,keyword)
                .orderByDesc(Tag::getCreateTime).orderByDesc(Tag::getUpdateTime);
        baseMapper.selectPage(tagPage,wrapper);
        return tagPage;
    }

    /**
     * 批量删除标签
     * @param ids
     * @return
     */
    public Result removeTagByIds(List<Long> ids) {
        //查询标签是否关联了文章
        for (Long tagId : ids) {
            LambdaQueryWrapper<BlogTag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BlogTag::getTagId,tagId);
            int count = blogTagService.count(wrapper);
            if(count > 0){
                return Result.fail().message("所删除标签关联了文章,删除失败");
            }
        }
        boolean isSuccess = baseMapper.deleteBatchIds(ids) > 0;
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 查询标签是否存在,若不存在,创建该标签
     * @param tagNames
     * @return
     */
    @Transactional
    public List<Tag> queryIsExists(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        tagNames.forEach(i -> {
            LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Tag::getTagName,i);
            Tag tag = baseMapper.selectOne(wrapper);
            if(Objects.isNull(tag)){
                //若不存在该标签,创建
                tag = new Tag();
                tag.setTagName(i);
                baseMapper.insert(tag);
            }
            tags.add(tag);
        });
        return tags;
    }
}
