package com.lj.user.service.impl;

import com.lj.model.user.UserLog;
import com.lj.user.mapper.UserLogMapper;
import com.lj.user.service.UserLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户行为日志表(记录用户点赞、评论、浏览博客等行为) 服务实现类
 * </p>
 *
 * @author lj
 * @since 2023-05-12
 */
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {

}
