package com.lj.blog.mapper;

import com.lj.model.blog.LeaveMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.dto.LeaveMessageQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 留言表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-12-01
 */
@Mapper
public interface LeaveMessageMapper extends BaseMapper<LeaveMessage> {

    List<LeaveMessage> selectPageQuery(@Param("page") Long page,@Param("size") Long size,@Param("leaveMessageQueryDto") LeaveMessageQueryDto leaveMessageQueryDto);

    Long selectCountQuery(@Param("leaveMessageQueryDto")LeaveMessageQueryDto leaveMessageQueryDto);

    List<LeaveMessage> selectByParams(@Param("userId")Long userId,@Param("page") Long page,@Param("size") Long size,@Param("leaveMessageQueryDto") LeaveMessageQueryDto leaveMessageQueryDto);

    Long selectCountByParams(@Param("userId")Long userId,@Param("leaveMessageQueryDto") LeaveMessageQueryDto leaveMessageQueryDto);
}
