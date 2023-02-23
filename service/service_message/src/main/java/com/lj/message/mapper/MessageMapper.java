package com.lj.message.mapper;

import com.lj.model.message.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户消息表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2023-01-05
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
