package com.lj.user.service;

import com.lj.model.user.UserLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户行为日志表(记录用户点赞、评论、浏览博客等行为) 服务类
 * </p>
 *
 * @author lj
 * @since 2023-05-12
 */
public interface UserLogService extends IService<UserLog> {

    List<Map<String, String>> fetchUserSevenDaysData(Long userId);
}
