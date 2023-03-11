package com.lj.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.user.ViewHistory;
import com.lj.vo.ViewHistoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户浏览历史表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2023-01-31
 */
@Mapper
public interface ViewHistoryMapper extends BaseMapper<ViewHistory> {
    List<ViewHistoryVo> selectViewHistoryByUserId(@Param("userId") Long userId,@Param("page") Long page,@Param("size") Long size);

    Long selectViewHistoryCount(@Param("userId")Long userId);
}
