package com.lj.message.mapper;

import com.lj.dto.MessageQueryDto;
import com.lj.model.message.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.vo.MessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    Long selectAllMessageCount(@Param("userId") Long userId,@Param("messageQueryDto") MessageQueryDto messageQueryDto);

    List<MessageVo> selectAllMessage(@Param("userId") Long userId,@Param("messageQueryDto") MessageQueryDto messageQueryDto);
}
