package com.lj.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.log.mapper.LogMapper;
import com.lj.log.service.LogService;
import com.lj.model.log.MyLog;
import com.lj.dto.LogQueryDto;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-11-25
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, MyLog> implements LogService {


    /**
     * 分页条件查询日志
     * @param page
     * @param size
     * @param logQueryDto
     * @return
     */

    public Page<MyLog> pageQueryLog(Long page, Long size, LogQueryDto logQueryDto) {
        Page<MyLog> myLogPage = new Page<>(page,size);
        LambdaQueryWrapper<MyLog> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(logQueryDto)){
            String keyWord = logQueryDto.getKeyword();
            if (!StringUtils.isEmpty(keyWord)){
                wrapper.like(MyLog::getUserName,keyWord)
                        .or().like(MyLog::getOperation,keyWord);
            }
            Date date = logQueryDto.getDate();
            if(!StringUtils.isEmpty(date)){
                wrapper.apply(date != null,"DATE(start_time) = DATE({0})",date);
            }
        }
        wrapper.orderByDesc(MyLog::getStartTime);
        baseMapper.selectPage(myLogPage,wrapper);
        return myLogPage;
    }
}
