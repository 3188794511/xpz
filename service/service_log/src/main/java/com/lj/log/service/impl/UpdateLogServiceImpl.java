package com.lj.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.log.mapper.UpdateLogMapper;
import com.lj.log.service.UpdateLogService;
import com.lj.model.log.UpdateLog;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 更新日志 服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-12-05
 */
@Service
public class UpdateLogServiceImpl extends ServiceImpl<UpdateLogMapper, UpdateLog> implements UpdateLogService {

    /**
     * 分页条件查询
     * @param page
     * @param size
     * @param updateLog
     * @return
     */
    public Page<UpdateLog> pageQuery(Long page, Long size, UpdateLog updateLog) {
        Page<UpdateLog> res = new Page<>(page, size);
        LambdaQueryWrapper<UpdateLog> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(updateLog)){
            wrapper.eq(Strings.isNotBlank(updateLog.getUsername()),UpdateLog::getUsername,updateLog.getUsername());
            wrapper.eq(Objects.nonNull(updateLog.getUpdateTime()),UpdateLog::getUpdateTime,updateLog.getUpdateTime());
        }
        baseMapper.selectPage(res,wrapper);
        return res;
    }
}
