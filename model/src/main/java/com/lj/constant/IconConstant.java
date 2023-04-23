package com.lj.constant;

import com.lj.vo.UserCoreDataVo;

import java.util.*;

public class IconConstant {
    public static final List<UserCoreDataVo> ICONS =
            new ArrayList<>();

    static {
        UserCoreDataVo userCoreDataVo1 = new UserCoreDataVo();
        userCoreDataVo1.setIcon("el-icon-view");
        userCoreDataVo1.setName("浏览量");
        ICONS.add(userCoreDataVo1);

        UserCoreDataVo userCoreDataVo2 = new UserCoreDataVo();
        userCoreDataVo2.setIcon("icon-thirdicon");
        userCoreDataVo2.setName("点赞量");
        ICONS.add(userCoreDataVo2);

        UserCoreDataVo userCoreDataVo3 = new UserCoreDataVo();
        userCoreDataVo3.setIcon("el-icon-s-comment");
        userCoreDataVo3.setName("评论量");
        ICONS.add(userCoreDataVo3);

        UserCoreDataVo userCoreDataVo4 = new UserCoreDataVo();
        userCoreDataVo4.setIcon("el-icon-user");
        userCoreDataVo4.setName("粉丝数");
        ICONS.add(userCoreDataVo4);
    }
}
