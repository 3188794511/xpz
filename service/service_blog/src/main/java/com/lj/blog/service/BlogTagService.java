package com.lj.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.model.blog.BlogTag;

import java.util.List;

/**
 * <p>
 * 博客-标签表 服务类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
public interface BlogTagService extends IService<BlogTag> {

    boolean removeByBlogId(Long id);

    List<BlogTag> getByBlogId(Long id);
}
