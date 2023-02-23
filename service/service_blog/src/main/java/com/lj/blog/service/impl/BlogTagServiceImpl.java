package com.lj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.blog.mapper.BlogTagMapper;
import com.lj.blog.service.BlogTagService;
import com.lj.model.blog.BlogTag;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 博客-标签表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {

    /**
     * 根据博客id删除博客标签
     * @param id
     */
    public boolean removeByBlogId(Long id) {
        int rows = baseMapper.delete(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getBlogId, id));
        return rows > 0;
    }

    /**
     * 根据博客id获取博客标签
     * @param id
     */
    public List<BlogTag> getByBlogId(Long id) {
        List<BlogTag> blogTags = baseMapper.selectList(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getBlogId, id));
        return blogTags;
    }
}
