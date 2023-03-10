package com.lj.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.base.Result;
import com.lj.model.user.User;
import com.lj.vo.LoginUserDto;
import com.lj.vo.UserInfoDto;
import com.lj.vo.UserInfoVo;
import com.lj.vo.UserQueryDto;

import java.util.List;
import java.util.Set;

public interface UserService extends IService<User> {

    Result adminLogin(LoginUserDto loginUserDto);

    Result info(String token);

    Result userLogin(LoginUserDto loginUserDto);

    Result register(LoginUserDto loginUserDto);

    Result updateUserInfo(UserInfoDto userInfoDto);

    Page<User> pageQueryUser(Long page, Long size, UserQueryDto userQueryDto);

    Result userInfo(String token);

    Result followUser(Long userId, Long followUserId);

    Long myFollowUserCount(Long userId);

    Long followMeUserCount(Long userId);

    Page<User> pageQueryMyFollowUser(Long userId,Long page, Long size);

    Page<User> pageQueryFollowMeUser(Long userId, Long page, Long size);

    Boolean isFollowed(Long userId,Long followUserId);

    List<UserInfoVo> getUserInfoByIds(Set<Long> ids);

    UserInfoVo getUserInfoById(Long id);

    boolean addChatUser(Long userId, Long id);

    boolean deleteChatUser(Long userId, Long id);
}
