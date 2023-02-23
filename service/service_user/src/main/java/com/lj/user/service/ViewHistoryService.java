package com.lj.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.model.user.ViewHistory;
import com.lj.vo.ViewHistoryVo;

import java.util.List;

/**
 * <p>
 * 用户浏览历史表 服务类
 * </p>
 *
 * @author lj
 * @since 2023-01-31
 */
public interface ViewHistoryService extends IService<ViewHistory> {

    boolean saveOrUpdateViewHistory(ViewHistory viewHistory);

    boolean removeByUserId(Long userId);

    List<ViewHistoryVo> getViewHistoryByUserId(Long userId);
}
