package com.lj.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.log.UpdateLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 更新日志 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-12-05
 */
@Mapper
public interface UpdateLogMapper extends BaseMapper<UpdateLog> {

}
