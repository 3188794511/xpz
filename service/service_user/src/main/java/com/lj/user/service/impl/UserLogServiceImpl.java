package com.lj.user.service.impl;

import com.lj.client.BlogClientService;
import com.lj.model.user.UserLog;
import com.lj.user.mapper.UserLogMapper;
import com.lj.user.service.UserLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    @Autowired
    private BlogClientService blogClientService;

    /**
     * 查询用户近七天的博客访问量
     * @param userId
     * @return
     */
    public List<Map<String, String>> fetchUserSevenDaysData(Long userId) {
        List<Map<String, String>> data = new ArrayList<>();
        List<Long> blogIds = blogClientService.getUserBlogIds(userId);
        LocalDate curDate = LocalDate.now().plusDays(1);
        LocalDate startDate = curDate.minus(7, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate date = startDate;date.isBefore(curDate);date = date.plusDays(1)){
            Map<String,String> oneDayData = new HashMap<>();
            Long viewCount = baseMapper.selectUserSevenDaysData(date, blogIds);
            oneDayData.put("date", date.format(formatter));
            oneDayData.put("viewCount", viewCount.toString());
            data.add(oneDayData);
        }
        return data;
    }
}
