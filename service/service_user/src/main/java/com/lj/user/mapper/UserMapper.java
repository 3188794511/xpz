package com.lj.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.user.User;
import com.lj.vo.ViewHistoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {


}
