package com.lj.config;

import com.lj.interceptor.AdminInterceptor;
import com.lj.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        //添加管理员拦截器以及响应路径
        /**管理员
         * 除了登录,获取用户token 通过feign调用消息接口 都需要登录
         */
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/xpz/admin/**")
                .excludePathPatterns("/xpz/admin/user/login"
                        ,"/xpz/admin/user/info"
                        ,"/xpz/admin/message/send"
                        ,"/xpz/admin/message/save"
                        ,"/xpz/admin/message/send-all"
                        ,"/xpz/admin/message/online");
        //添加用户拦截器以及响应路径
        /**用户
         * 查看、修改个人信息(包括头像),关注,取关用户需要登录
         * 发表 点赞评论,  发送留言,  发布 更新 删除 点赞博客, 查看自己发布的博客, 消息相关
         * ---需要登录
         */
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/xpz/api/blog/comment/save",
                        "/xpz/api/blog/comment/likes/**"
                        , "/xpz/api/user/update/info"
                        ,"/xpz/api/user/info"
                        ,"/xpz/api/user/follow/**"
                        ,"/xpz/api/blog/file/user-upload"
                        ,"/xpz/api/blog/leave-message/send"
                        ,"/xpz/api/blog/blog/likes/**"
                        ,"/xpz/api/blog/blog/my-blog/**"
                        ,"/xpz/api/message/**"
                        ,"/xpz/api/user/history/**")
                .excludePathPatterns("/xpz/api/user/history/save");
    }
}
