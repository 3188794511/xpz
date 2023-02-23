package com.lj.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.model.blog.BlogDetail;

/**
 * <p>
 * 博客详情表 服务类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
public interface BlogDetailService extends IService<BlogDetail> {

    BlogDetail getByBlogId(Long id);
}
