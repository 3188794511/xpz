package com.lj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.blog.BlogDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 博客详情表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Mapper
public interface BlogDetailMapper extends BaseMapper<BlogDetail> {

}
