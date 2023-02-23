package com.lj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.blog.mapper.BlogDetailMapper;
import com.lj.blog.service.BlogDetailService;
import com.lj.model.blog.BlogDetail;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客详情表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Service
public class BlogDetailServiceImpl extends ServiceImpl<BlogDetailMapper, BlogDetail> implements BlogDetailService {

    /**
     * 根据博客id查询博客详情
     * @param id
     * @return
     */
    public BlogDetail getByBlogId(Long id) {
        BlogDetail blogDetail = baseMapper.selectOne(new LambdaQueryWrapper<BlogDetail>().eq(BlogDetail::getBlogId, id));
        return blogDetail;
    }
}
