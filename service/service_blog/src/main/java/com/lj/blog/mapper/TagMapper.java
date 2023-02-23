package com.lj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.blog.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-11-05
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
