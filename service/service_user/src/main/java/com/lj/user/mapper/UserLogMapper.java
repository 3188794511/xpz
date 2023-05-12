package com.lj.user.mapper;

import com.lj.model.user.UserLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户行为日志表(记录用户点赞、评论、浏览博客等行为) Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2023-05-12
 */
@Mapper
public interface UserLogMapper extends BaseMapper<UserLog> {

}
