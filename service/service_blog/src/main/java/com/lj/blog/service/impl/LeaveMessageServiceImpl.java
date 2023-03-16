package com.lj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.base.Result;
import com.lj.blog.mapper.LeaveMessageMapper;
import com.lj.blog.service.LeaveMessageService;
import com.lj.client.UserClientService;
import com.lj.model.blog.LeaveMessage;
import com.lj.model.user.User;
import com.lj.util.UserInfoContext;
import com.lj.dto.LeaveMessageQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 留言表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-12-01
 */
@Service
public class LeaveMessageServiceImpl extends ServiceImpl<LeaveMessageMapper, LeaveMessage> implements LeaveMessageService {
    @Autowired
    private UserClientService userClientService;

    /**
     * 发布一条留言
     * @param leaveMessage
     * @return
     */
    public Result saveLeaveMessage(LeaveMessage leaveMessage) {
        leaveMessage.setUserId(UserInfoContext.get());
        boolean isSuccess = baseMapper.insert(leaveMessage) > 0;
        return isSuccess ? Result.ok().message("发送成功") : Result.fail().message("发送失败");
    }

    /**
     * 获取留言列表
     * @return
     */
    public List<LeaveMessage> listAll() {
        LambdaQueryWrapper<LeaveMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(LeaveMessage::getCreateTime);
        List<LeaveMessage> leaveMessages = baseMapper.selectList(wrapper);
        leaveMessages.forEach(i -> setUserInfo(i));
        return leaveMessages;
    }

    /**
     * 留言  添加用户信息
     * @param leaveMessage
     */
    private void setUserInfo(LeaveMessage leaveMessage) {
        User user = userClientService.getById(leaveMessage.getUserId());
        leaveMessage.setUsername(user.getNickName());
        leaveMessage.setPic(user.getPic());
    }

    /**
     * 分页条件查询留言
     * @param page
     * @param size
     * @param leaveMessageQueryDto
     * @return
     */
    public Page<LeaveMessage> PageQueryLeaveMessage(Long page, Long size, LeaveMessageQueryDto leaveMessageQueryDto) {
        Page<LeaveMessage> res = new Page<>(page,size);
        //计算起始偏移量
        page = (page - 1) * size;
        List<LeaveMessage> records =  baseMapper.selectPageQuery(page,size,leaveMessageQueryDto);
        Long total = baseMapper.selectCountQuery(leaveMessageQueryDto);
        res.setRecords(records);
        res.setTotal(total);
        return res;
    }

    /**
     * 条件查询留言
     * @param userId
     * @param page
     * @param size
     * @param leaveMessageQueryDto
     * @return
     */
    public Page<LeaveMessage> searchLeaveMessageByParams(Long userId, Long page, Long size, LeaveMessageQueryDto leaveMessageQueryDto) {
        Page<LeaveMessage> res = new Page<>(page,size);
        //计算起始偏移量
        page = (page - 1) * size;
        List<LeaveMessage> records =  baseMapper.selectByParams(userId,page,size,leaveMessageQueryDto);
        Long total = baseMapper.selectCountByParams(userId,leaveMessageQueryDto);
        res.setRecords(records);
        res.setTotal(total);
        return res;
    }
}
