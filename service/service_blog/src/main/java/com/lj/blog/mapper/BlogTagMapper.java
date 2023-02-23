package com.lj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.blog.BlogTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 博客-标签表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

}
