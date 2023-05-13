package com.lj.user.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.model.user.User;
import com.lj.model.user.ViewHistory;
import com.lj.user.service.UserLogService;
import com.lj.user.service.UserService;
import com.lj.user.service.ViewHistoryService;
import com.lj.util.JwtTokenUtil;
import com.lj.dto.LoginUserDto;
import com.lj.vo.UserCoreDataVo;
import com.lj.vo.ViewHistoryVo;
import com.lj.dto.UserInfoDto;
import com.lj.vo.UserInfoVo;
import com.lj.vo.UserSecretInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lj.constant.RedisConstant.CHAT_WITH_ME_USER;

@RestController
@RequestMapping("/xpz/api/user")
public class UserApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLogService userLogService;
    @Autowired
    private ViewHistoryService viewHistoryService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录
     * @param loginUserDto
     * @return
     */
    @MyLog("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody @Validated LoginUserDto loginUserDto){
        return userService.userLogin(loginUserDto);
    }

    /**
     * 根据token查询用户信息
     * @return
     */
    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        return userService.userInfo(request.getHeader("token"));
    }

    /**
     * 远程调用接口
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public User getById(@PathVariable Long id){
        User user = userService.getById(id);
        return user;
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        UserInfoVo user = userService.getUserInfoById(id);
        return Result.ok(user);
    }

    /**
     * 注册用户
     * @param loginUserDto
     * @return
     */
    @PostMapping("/register")
    @MyLog("注册用户")
    public Result register(@RequestBody @Validated LoginUserDto loginUserDto){
        return userService.register(loginUserDto);
    }

    /**
     * 修改用户信息
     * @return
     */
    @PutMapping("/update/info")
    @MyLog("修改用户信息")
    public Result updateUserInfo(@RequestBody @Validated UserInfoDto userInfoDto){
        return userService.updateUserInfo(userInfoDto);
    }

    /**
     * 修改用户密码
     * @return
     */
    @PutMapping("/update/pwd")
    @MyLog("修改用户密码")
    public Result updateUserInfo(@RequestBody @Validated UserSecretInfo userSecretInfo, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        return userService.updateUserPwd(userSecretInfo,userId);
    }

    /**
     * 关注 取关一位用户
     * @param request
     * @param followUserId
     * @return
     */
    @PostMapping("/follow/{followUserId}")
    public Result followUser(HttpServletRequest request,@PathVariable Long followUserId){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        if(userId.equals(followUserId)){
            return Result.fail().message("不能对自己进行该操作");
        }
        return userService.followUser(userId,followUserId);
    }

    /**
     * 是否已经关注该用户
     * @param request
     * @param followUserId
     * @return
     */
    @GetMapping("/follow/is-followed/{followUserId}")
    public Result isFollowed(HttpServletRequest request,@PathVariable Long followUserId){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        Boolean isFollowed = userService.isFollowed(userId,followUserId);
        return Result.ok(isFollowed);
    }

    /**
     * 我关注的用户
     * @param request
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/follow/my-follow/{page}/{size}")
    public Result myFollowedUser(HttpServletRequest request,@PathVariable Long page,@PathVariable Long size){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        Page<User> users = userService.pageQueryMyFollowUser(userId,page,size);
        return Result.ok(users);
    }

    /**
     * id为userId关注的用户数量
     * @param userId
     * @return
     */
    @GetMapping("/follow-info/{userId}/count")
    public Result myFollowedUserCount(@PathVariable Long userId){
        return Result.ok(userService.myFollowUserCount(userId));
    }

    /**
     * 关注id为userId的用户数量
     * @param userId
     * @return
     */
    @GetMapping("/follow-me-info/{userId}/count")
    public Result followedMeUserCount(@PathVariable Long userId){
        return Result.ok(userService.followMeUserCount(userId));
    }

    /**
     * 我关注的用户数量
     * @param request
     * @return
     */
    @GetMapping("/follow/my-follow/count")
    public Result myFollowedUserCount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        return Result.ok(userService.myFollowUserCount(userId));
    }

    /**
     * 关注我的用户数量
     * @param request
     * @return
     */
    @GetMapping("/follow/follow-me/count")
    public Result followedMeUserCount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        return Result.ok(userService.followMeUserCount(userId));
    }

    /**
     * 关注我的用户
     * @param request
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/follow/follow-me/{page}/{size}")
    public Result followMeUser(HttpServletRequest request,@PathVariable Long page,@PathVariable Long size){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        Page<User> users = userService.pageQueryFollowMeUser(userId,page,size);
        return Result.ok(users);
    }

    /**
     * 新增或修改访问历史
     * @param viewHistory
     * @return
     */
    @PostMapping("/history/save")
    public Result addViewHistory(@RequestBody ViewHistory viewHistory){
        boolean isSuccess = viewHistoryService.saveOrUpdateViewHistory(viewHistory);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 查询用户访问历史
     * @param request
     * @return
     */
    @GetMapping("/history/{page}/{size}")
    public Result getViewHistory(HttpServletRequest request,@PathVariable Long page,@PathVariable Long size){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        Page<ViewHistoryVo> viewHistoryVoPage =  viewHistoryService.getViewHistoryByUserId(userId,page,size);
        return Result.ok(viewHistoryVoPage);
    }

    /**
     * 删除一条访问历史
     * @param id
     * @return
     */
    @DeleteMapping("/history/remove/{id}")
    public Result removeViewHistoryById(@PathVariable Long id){
        boolean isSuccess = viewHistoryService.removeById(id);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 删除用户所有访问历史
     * @param userId
     * @return
     */
    @DeleteMapping("/history/remove-all/{userId}")
    public Result removeViewHistoryByUserId(@PathVariable Long userId){
        boolean isSuccess = viewHistoryService.removeByUserId(userId);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 与登录用户聊过天的用户
     * @return
     */
    @GetMapping("/chat-user/{id}")
    public Result chatUserList(HttpServletRequest request,@PathVariable Long id){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        String key = CHAT_WITH_ME_USER + userId;
        UserInfoVo userInfoVo = new UserInfoVo();
        if(ops.isMember(key,id.toString())){
            userInfoVo = userService.getUserInfoById(id);
        }
        return Result.ok(userInfoVo);
    }

    /**
     * 与登录用户聊过天的所有用户
     * @return
     */
    @GetMapping("/chat-user/all")
    public Result chatUserList(HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        String key = CHAT_WITH_ME_USER + userId;
        Set<String> members = ops.members(key);
        List<UserInfoVo> userInfos = new ArrayList<>();
        if(!members.isEmpty()){
            userInfos = userService.getUserInfoByIds(members.stream().map(i -> Long.valueOf(i)).collect(Collectors.toSet()));
        }
        return Result.ok(userInfos);
    }

    /**
     * 删除一个聊天用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/chat-user/{id}")
    public Result deleteChatUser(@PathVariable Long id,HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        if(userId.equals(id)){
            return Result.fail().message("不能对自己进行该操作");
        }
        boolean isSuccess = userService.deleteChatUser(userId,id);
        return isSuccess ? Result.ok() : Result.fail();
    }


    /**
     * 新增一个聊天用户
     * @param id
     * @return
     */
    @PostMapping("/add/chat-user/{id}")
    public Result addChatUser(@PathVariable Long id,HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        if(userId.equals(id)){
            return Result.fail().message("不能对自己进行该操作");
        }
        boolean isSuccess = userService.addChatUser(userId,id);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 查询用户中心数据
     * @param request
     * @return
     */
    @GetMapping("/data/core")
    public Result getUserCoreData(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        List<UserCoreDataVo> data = userService.fetchUserCoreData(userId);
        return Result.ok(data);
    }

    /**
     * 查询用户近七天的博客访问量
     * @param request
     * @return
     */
    @GetMapping("/data/seven-days-data")
    public Result getUserSevenDaysData(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtTokenUtil.getUserId(token);
        List<Map<String,String>> data = userLogService.fetchUserSevenDaysData(userId);
        return Result.ok(data);
    }


}
