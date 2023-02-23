package com.lj.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.base.Result;
import com.lj.model.blog.LeaveMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.vo.LeaveMessageQueryDto;

import java.util.List;

/**
 * <p>
 * 留言表 服务类
 * </p>
 *
 * @author lj
 * @since 2022-12-01
 */
public interface LeaveMessageService extends IService<LeaveMessage> {

    Result saveLeaveMessage(LeaveMessage leaveMessage);

    Page<LeaveMessage> PageQueryLeaveMessage(Long page, Long size, LeaveMessageQueryDto leaveMessageQueryDto);

    List<LeaveMessage> listAll();
}
