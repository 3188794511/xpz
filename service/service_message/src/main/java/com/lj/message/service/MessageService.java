package com.lj.message.service;

import com.lj.model.message.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.vo.user.MessageCountVo;

import java.util.List;

/**
 * <p>
 * 用户消息表 服务类
 * </p>
 *
 * @author lj
 * @since 2023-01-05
 */
public interface MessageService extends IService<Message> {
    List<Message> listMessageById(Long userId,Integer type);
    void readMsg(Long id);
    void readAllType01(Long userId);
    MessageCountVo unReadMessageCount01(Long userId);
    void readAllType3(Long userId);
    Long unReadMessageCount3(Long userId);
    List<Message> chatHistory(Long userId, Long chatUserId);
    boolean addChatUser(Long sendUserId, Long receiveUserId);
}
