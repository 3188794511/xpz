package com.lj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.model.user.ViewHistory;
import com.lj.user.mapper.ViewHistoryMapper;
import com.lj.user.service.ViewHistoryService;
import com.lj.vo.ViewHistoryVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户浏览历史表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2023-01-31
 */
@Service
public class ViewHistoryServiceImpl extends ServiceImpl<ViewHistoryMapper, ViewHistory> implements ViewHistoryService {

    /**
     * 查询用户访问历史
     * @param userId
     * @return
     */
    public Page<ViewHistoryVo> getViewHistoryByUserId(Long userId,Long page,Long size) {
        Page<ViewHistoryVo> data = new Page<>(page,size);
        Long total = baseMapper.selectViewHistoryCount(userId);
        List<ViewHistoryVo> records =  baseMapper.selectViewHistoryByUserId(userId,(page - 1) * size,size);
        data.setTotal(total);
        data.setRecords(records);
        return data;
    }

    /**
     * 新增或修改访问历史
     * @param viewHistory
     * @return
     */
    public boolean saveOrUpdateViewHistory(ViewHistory viewHistory) {
        LambdaQueryWrapper<ViewHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ViewHistory::getBlogId, viewHistory.getBlogId())
                .eq(ViewHistory::getUserId,viewHistory.getUserId());
        ViewHistory data = baseMapper.selectOne(wrapper);
        boolean res = false;
        if(Objects.nonNull(data)){
            //修改访问时间
            data.setViewTime(new Date());
            res = baseMapper.updateById(data) > 0;
        }
        else{
            //新增访问历史
            res = baseMapper.insert(viewHistory) > 0;
        }
        return res;
    }

    /**
     * 删除用户所有访问历史
     * @param userId
     * @return
     */
    public boolean removeByUserId(Long userId) {
        LambdaQueryWrapper<ViewHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ViewHistory::getUserId,userId);
        return baseMapper.delete(wrapper) > 0;
    }
}
