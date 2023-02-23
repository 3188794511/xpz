package com.lj.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.model.log.UpdateLog;

/**
 * <p>
 * 更新日志 服务类
 * </p>
 *
 * @author lj
 * @since 2022-12-05
 */
public interface UpdateLogService extends IService<UpdateLog> {

    Page<UpdateLog> pageQuery(Long page, Long size, UpdateLog updateLog);
}
