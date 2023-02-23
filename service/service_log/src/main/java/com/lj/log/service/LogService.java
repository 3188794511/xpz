package com.lj.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.model.log.MyLog;
import com.lj.vo.LogQueryDto;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author lj
 * @since 2022-11-25
 */
public interface LogService extends IService<MyLog> {

    Page<MyLog> pageQueryLog(Long page, Long size, LogQueryDto logQueryDto);
}
