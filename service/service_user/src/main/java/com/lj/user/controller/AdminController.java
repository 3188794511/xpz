package com.lj.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.model.user.User;
import com.lj.user.service.UserService;
import com.lj.vo.LoginUserDto;
import com.lj.vo.UserQueryDto;
import com.lj.vo.admin.UserBaseInfo;
import com.lj.vo.admin.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/xpz/admin/user")
public class AdminController {
    @Autowired
    private UserService userService;

    /**
     * 分页查询所有用户
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public Result findUserPage(@PathVariable Long page, @PathVariable Long size,@Validated UserQueryDto userQueryDto){
        Page<User> pageRes = userService.pageQueryUser(page,size, userQueryDto);
        return Result.ok(pageRes);
    }

    /**
     * 管理员登录
     * @param loginUserDto
     * @return
     */
    @PostMapping("/login")
    @MyLog(type = "admin",value = "管理员登录")
    public Result login(@RequestBody @Validated LoginUserDto loginUserDto){
        return userService.adminLogin(loginUserDto);
    }

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    @GetMapping("/info")
    public Result info(@RequestParam(value = "token") String token){
        return userService.info(token);
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        UserBaseInfo user = userService.getUserBaseInfo(id);
        return Result.ok(user);
    }

    @MyLog(type = "admin",value = "创建用户")
    @PostMapping("/save")
    public Result saveUser(@RequestBody @Validated User user){
        return userService.createUser(user);
    }

    @MyLog(type = "admin",value = "修改用户信息")
    @PutMapping("/update")
    public Result updateUser(@RequestBody @Validated UserUpdateDto user){
        return userService.adminUpdateUser(user);
    }
}
