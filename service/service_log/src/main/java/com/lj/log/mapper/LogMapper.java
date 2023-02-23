package com.lj.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.log.MyLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-11-25
 */
@Mapper
public interface LogMapper extends BaseMapper<MyLog> {

}
