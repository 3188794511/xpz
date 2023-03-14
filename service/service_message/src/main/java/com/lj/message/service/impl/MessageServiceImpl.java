package com.lj.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.message.mapper.MessageMapper;
import com.lj.message.service.MessageService;
import com.lj.model.message.Message;
import com.lj.vo.user.MessageCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lj.constant.RedisConstant.CHAT_WITH_ME_USER;

/**
 * <p>
 * 用户消息表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2023-01-05
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 根据用户id查询消息(并将所有未读消息状态置为已读)
     * @param userId
     * @param type 消息类型
     * @return
     */
    public List<Message> listMessageById(Long userId,Integer type) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiveUserId,userId)
                .eq(Message::getType,type)
                .orderByDesc(Message::getCreateTime);
        List<Message> messages = baseMapper.selectList(wrapper);
        return messages;
    }

    /**
     * 已读一条消息
     * @param id
     */
    public void readMsg(Long id){
        Message message = baseMapper.selectById(id);
        if(message.getStatus() == 0){
            message.setStatus(1);
            baseMapper.updateById(message);
        }
    }

    /**
     * 用户类型0 1消息全部已读
     * @param userId
     */
    public void readAllType01(Long userId){
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiveUserId,userId)
                .and(c -> {
                    c.eq(Message::getType,0)
                            .or().eq(Message::getType,1);
                })
                .eq(Message::getStatus,0);
        List<Message> messages = baseMapper.selectList(wrapper);
        messages.forEach(m -> {
            if (m.getStatus() == 0) {
                m.setStatus(1);
                baseMapper.updateById(m);
            }
        });
    }

    /**
     * id为userId的用户未读0 1类型消息的数量
     * @param userId
     * @return
     */
    public MessageCountVo unReadMessageCount01(Long userId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiveUserId,userId)
                .eq(Message::getStatus,0)
                .eq(Message::getType,0);
        LambdaQueryWrapper<Message> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Message::getReceiveUserId,userId)
                .eq(Message::getStatus,0)
                .eq(Message::getType,1);
        Integer systemMsgCount = baseMapper.selectCount(wrapper);
        Integer chatMsgCount = baseMapper.selectCount(wrapper1);
        MessageCountVo messageCountVo = new MessageCountVo();
        messageCountVo.setSystemMsgCount(systemMsgCount);
        messageCountVo.setChatMsgCount(chatMsgCount);
        messageCountVo.setTotalMsgCount(systemMsgCount + chatMsgCount);
        return messageCountVo;
    }


    /**
     * 用户类型2消息全部已读
     * @param userId
     */
    public void readAllType3(Long userId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiveUserId,userId)
                .eq(Message::getType,3)
                .eq(Message::getStatus,0);
        List<Message> messages = baseMapper.selectList(wrapper);
        messages.forEach(m -> {
            if (m.getStatus() == 0) {
                m.setStatus(1);
                baseMapper.updateById(m);
            }
        });
    }

    /**
     * id为userId的用户未读3类型消息的数量
     * @param userId
     * @return
     */
    public Long unReadMessageCount3(Long userId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiveUserId,userId)
                .eq(Message::getStatus,0)
                .eq(Message::getType,3);
        return Long.valueOf(baseMapper.selectCount(wrapper));
    }

    /**
     * 查找登录用户与某个用户的聊天历史  (并将所有消息已读)
     * @param chatUserId
     * @param userId
     * @return
     */
    public List<Message> chatHistory(Long userId, Long chatUserId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Message::getSendUserId,userId)
                .eq(Message::getReceiveUserId,chatUserId)
                .eq(Message::getType,1)
                .or(w -> w.eq(Message::getSendUserId,chatUserId)
                        .eq(Message::getReceiveUserId,userId)
                        .eq(Message::getType,1))
                .orderByAsc(Message::getCreateTime);
        List<Message> messages = baseMapper.selectList(wrapper);
        messages.forEach(i -> {
            Message message = new Message();
            message.setId(i.getId());
            message.setStatus(1);
            baseMapper.updateById(message);
        });
        return messages;
    }

    /**
     * 添加一个聊天用户
     * @param sendUserId
     * @param receiveUserId
     * @return
     */
    public boolean addChatUser(Long sendUserId, Long receiveUserId) {
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        String key = CHAT_WITH_ME_USER + receiveUserId;
        boolean isSuccess = true;
        if(!ops.isMember(key,sendUserId.toString())){
            isSuccess = ops.add(key,sendUserId.toString()) > 0;
        }
        return isSuccess;
    }
}
