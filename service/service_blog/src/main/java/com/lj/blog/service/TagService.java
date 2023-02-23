package com.lj.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.base.Result;
import com.lj.model.blog.Tag;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lj
 * @since 2022-11-05
 */
public interface TagService extends IService<Tag> {

    Page<Tag> pageParamQuery(Long page, Long size, String keyword);

    Result removeTagByIds(List<Long> ids);


    List<Tag> queryIsExists(List<String> tagNames);
}
