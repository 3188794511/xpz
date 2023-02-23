package com.lj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.base.Result;
import com.lj.client.MessageClientService;
import com.lj.model.user.User;
import com.lj.user.mapper.UserMapper;
import com.lj.user.service.UserService;
import com.lj.util.JwtTokenUtil;
import com.lj.util.UserInfoContext;
import com.lj.util.generateUtil;
import com.lj.vo.LoginUserDto;
import com.lj.vo.UserInfoDto;
import com.lj.vo.UserInfoVo;
import com.lj.vo.UserQueryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lj.constant.RedisConstant.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MessageClientService messageClientService;

    /**
     * 管理员登录
     * @param loginUserDto
     * @return
     */
    public Result adminLogin(LoginUserDto loginUserDto) {
        User user = login(loginUserDto);
        //查询账号密码
        if(StringUtils.isEmpty(user)){
            return Result.fail().message("账号信息有误");
        }
        //查看权限
        boolean isAdmin = user.getRole().equals("admin");
        if(!isAdmin){
            return Result.fail().message("请用管理员账号登录");
        }
        //生成token返回
        String token = JwtTokenUtil.createToken(user.getId(),user.getRole());
        log.info("生成的token为:{}",token);
        return Result.ok(token);
    }

    /**
     * 后台根据token查询用户信息
     * @param token
     * @return
     */
    public Result info(String token) {
        Long userId = JwtTokenUtil.getUserId(token);
        if(StringUtils.isEmpty(userId)){
           return Result.fail();
        }
        User user = baseMapper.selectById(userId);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", user.getNickName());
        userInfo.put("avatar", user.getPic());
        return Result.ok(userInfo);
    }

    /**
     * 用户登录
     * @param loginUserDto
     * @return
     */
    public Result userLogin(LoginUserDto loginUserDto) {
        User user = login(loginUserDto);
        //查询账号密码
        if(StringUtils.isEmpty(user)){
            return Result.fail().message("账号信息有误");
        }
        //生成token返回
        String token = JwtTokenUtil.createToken(user.getId(),user.getRole());
        return Result.ok(token).message("登录成功");
    }

    /**
     * 用户注册
     * @param loginUserDto
     * @return
     */
    public Result register(LoginUserDto loginUserDto) {
        User userExist = login(loginUserDto);
        //账号已存在
        if(!StringUtils.isEmpty(userExist)){
            return Result.fail().message("账号已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(loginUserDto,user);
        user.setNickName(generateUtil.generateUserName());
        boolean isSuccess = baseMapper.insert(user) > 0;
        return isSuccess ? Result.ok().message("注册成功") : Result.fail().message("注册失败");
    }

    /**
     * 修改用户信息
     * @param userInfoDto
     * @return
     */
    public Result updateUserInfo(UserInfoDto userInfoDto) {
        Long userId = UserInfoContext.get();
        int row = 0;
        if(!StringUtils.isEmpty(userInfoDto)){
            User user = new User();
            BeanUtils.copyProperties(userInfoDto,user);
            user.setId(userId);
            row = baseMapper.updateById(user);
        }
        return row > 0 ? Result.ok().message("个人信息修改成功") : Result.fail().message("个人信息修改失败");
    }

    /**
     * 分页条件查询用户
     * @param page
     * @param size
     * @param userQueryDto
     * @return
     */
    public Page<User> pageQueryUser(Long page, Long size, UserQueryDto userQueryDto) {
        Page<User> userPage = new Page<>(page,size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(userQueryDto)){
            String keyWord = userQueryDto.getKeyword();
            Date createTime = userQueryDto.getCreateTime();
            wrapper.like(!StringUtils.isEmpty(keyWord),User::getNickName,keyWord)
                    .or().like(!StringUtils.isEmpty(keyWord),User::getAccount,keyWord);
            wrapper.apply(Objects.nonNull(createTime),"DATE(create_time) = DATE({0})",createTime);
        }
        wrapper.orderByDesc(User::getCreateTime).orderByDesc(User::getUpdateTime);
        baseMapper.selectPage(userPage,wrapper);
        return userPage;
    }

    /**
     *  用户根据token查询信息
     * @param token
     * @return
     */
    public Result userInfo(String token) {
        Long userId = JwtTokenUtil.getUserId(token);
        if(StringUtils.isEmpty(userId)){
            return Result.fail();
        }
        User user = baseMapper.selectById(userId);
        return Result.ok(user);
    }

    /**
     * 根据账号密码查询用户
     * @param loginUserDto
     * @return
     */
    private User login(LoginUserDto loginUserDto){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount, loginUserDto.getAccount())
                .eq(User::getPassword, loginUserDto.getPassword());
        User user = baseMapper.selectOne(wrapper);
        return user;
    }

    /**
     * 关注 取关一位用户
     * @param userId
     * @param followUserId
     * @return
     */
    public Result followUser(Long userId, Long followUserId) {
        //从redis中查询当前用户是否已经关注了该用户
        String key1 = FOLLOW_ME_USER + followUserId;//谁关注了我
        String key2 = MY_FOLLOW_USER + userId;//我关注了谁
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        Boolean isFollowed = operations.isMember(key2, followUserId.toString());
        //未关注
        if(!isFollowed){
            //redis需要更新用户关注表和被关注表
            boolean res1 = operations.add(key1, userId.toString()) > 0;
            boolean res2 = operations.add(key2, followUserId.toString()) > 0;
            return res1 && res2 ? Result.ok().message("关注成功") : Result.fail().message("关注失败");

        }
        //已关注,取消关注
        else{
            boolean res1 = operations.remove(key1, userId.toString()) > 0;
            boolean res2 = operations.remove(key2, followUserId.toString()) > 0;
            return res1 && res2 ? Result.ok().message("取消关注成功") : Result.fail().message("取消关注失败");
        }
    }

    /**
     * 我关注的用户数量
     * @param userId
     * @return
     */
    public Long myFollowUserCount(Long userId){
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        String key = MY_FOLLOW_USER + userId;//我关注了谁
        long count = operations.members(key).size();
        return count;
    }


    /**
     * 关注我的用户数量
     * @param userId
     * @return
     */
    public Long followMeUserCount(Long userId){
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        String key = FOLLOW_ME_USER + userId;//谁关注了我
        long count = operations.members(key).size();
        return count;
    }

    /**
     * 我关注的用户
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public Page<User> pageQueryMyFollowUser(Long userId,Long page, Long size) {
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        String key = MY_FOLLOW_USER + userId;//我关注了谁
        Page<User> userPage = new Page<>(page,size);
        if(operations.members(key).size() > 0){
            Set<Long> userIds = operations.members(key).stream()
                    .map(i -> Long.parseLong(i))
                    .collect(Collectors.toSet());
            if (!userIds.isEmpty()) {
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.select(User::getId,User::getNickName,User::getPic,User::getSelfDescribe)
                        .in(User::getId,userIds);
                baseMapper.selectPage(userPage,wrapper);
            }
            userPage.setTotal(userIds.size());
        }
        return userPage;
    }

    /**
     * 关注我的用户
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public Page<User> pageQueryFollowMeUser(Long userId, Long page, Long size) {
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        String key = FOLLOW_ME_USER + userId;//谁关注了我
        Page<User> userPage = new Page<>(page,size);
        if(operations.members(key).size() > 0){
            Set<Long> userIds = operations.members(key).stream()
                    .map(i -> Long.parseLong(i))
                    .collect(Collectors.toSet());
            if (!userIds.isEmpty()) {
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.select(User::getId,User::getNickName,User::getPic,User::getSelfDescribe)
                        .in(User::getId,userIds);
                baseMapper.selectPage(userPage,wrapper);
            }
            userPage.setTotal(userIds.size());
        }
        return userPage;
    }

    /**
     * 判断是否关注该用户
     * @param userId
     * @return
     */
    public Boolean isFollowed(Long userId,Long followUserId) {
        String key = MY_FOLLOW_USER + userId;//我关注了谁
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        Boolean isFollowed = operations.isMember(key, followUserId.toString());
        return isFollowed;
    }

    /**
     * 根据id查找用户信息
     * @param id
     * @return
     */
    public UserInfoVo getUserInfoById(Long id) {
        UserInfoVo userInfoVo = new UserInfoVo();
        User user = baseMapper.selectById(id);
        BeanUtils.copyProperties(user, userInfoVo);
        List<Long> onlineUserIds  = messageClientService.getOnlineUserIds();
        if(!onlineUserIds.contains(id)){
            userInfoVo.setIsOnline(0);
        }
        else{
            userInfoVo.setIsOnline(1);
        }
        return userInfoVo;
    }

    /**
     * 根据ids查找用户信息
     * @param ids
     * @return
     */
    public List<UserInfoVo> getUserInfoByIds(Set<Long> ids) {
        List<Long> onlineUserIds = messageClientService.getOnlineUserIds();
        List<UserInfoVo> userInfoVos = baseMapper.selectBatchIds(ids).stream().map(i -> {
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(i, userInfoVo);
            if(!onlineUserIds.contains(i.getId())){
                userInfoVo.setIsOnline(0);
            }
            else{
                userInfoVo.setIsOnline(1);
            }
            return userInfoVo;
        }).collect(Collectors.toList());
        return userInfoVos;
    }

    /**
     * 添加一个聊天用户
     * @param userId
     * @param id
     * @return
     */
    public boolean addChatUser(Long userId, Long id) {
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        String key = CHAT_WITH_ME_USER + userId;
        boolean isSuccess = true;
        if(!ops.isMember(key,id.toString())){
            isSuccess = ops.add(key,id.toString()) > 0;
        }
        return isSuccess;
    }

    /**
     * 删除一个聊天用户
     * @param userId
     * @param id
     * @return
     */
    public boolean deleteChatUser(Long userId, Long id) {
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        String key = CHAT_WITH_ME_USER + userId;
        boolean isSuccess = true;
        if(ops.isMember(key,id.toString())){
            isSuccess = ops.remove(key, id.toString()) > 0;
        }
        return isSuccess;
    }
}
