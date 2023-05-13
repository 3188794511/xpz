package com.lj.blog.api;

import cn.easyes.core.biz.EsPageInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.BlogService;
import com.lj.blog.es.BlogDocument;
import com.lj.dto.BlogDto;
import com.lj.dto.BlogQueryDto2;
import com.lj.dto.BlogQueryDto3;
import com.lj.dto.BlogSearchDto;
import com.lj.util.JwtTokenUtil;
import com.lj.util.UserInfoContext;
import com.lj.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("/xpz/api/blog/blog")
public class BlogApiController {
    @Autowired
    private BlogService blogService;

    /**
     * 发布一篇帖子
     * @param blogDto
     * @return
     */
    @MyLog(value = "发布帖子")
    @PostMapping("/my-blog/save")
    public Result saveBlog(@RequestBody @Validated BlogDto blogDto){
        boolean isSuccess = blogService.userSaveBlog(blogDto);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 根据id查询帖子
     * @param id
     * @return
     */
    @GetMapping("/my-blog/{id}")
    public Result getBlogById(@PathVariable Long id){
        BlogDto blogDto = blogService.getBlogById(id);
        return Result.ok(blogDto);
    }


    /**
     * 搜索补全关键字根据帖子标题)
     * @param keyword
     * @return
     */
    @GetMapping("/complete-keyword")
    public Result completeKeyword(String keyword){
        List<String> keywordList = blogService.completeKeyword(keyword);
        return Result.ok(keywordList);
    }

    /**
     *
     * 根据关键字全文检索帖子
     * @param blogSearchDto
     * @return
     */
    @GetMapping("/search")
    public Result searchByKeyword(@Validated BlogSearchDto blogSearchDto){
        EsPageInfo<BlogDocument> data = blogService.selectBlogDocumentByKeyword(blogSearchDto);
        return Result.ok(data);
    }

    /**
     * 更新一条帖子
     * @param blogDto
     * @return
     */
    @MyLog(value = "更新帖子")
    @PutMapping("/my-blog/update")
    public Result updateBlog(@RequestBody @Validated BlogDto blogDto){
        boolean isSuccess = blogService.updateBlog(blogDto);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 根据id批量删除帖子
     * @param ids
     * @return
     */
    @MyLog(value = "删除帖子")
    @DeleteMapping("/my-blog/remove/{ids}")
    public Result deleteBlog(@PathVariable List<Long> ids){
        boolean isSuccess = blogService.deleteBlogByIds(ids);
        return isSuccess ? Result.ok() : Result.fail();
    }

    /**
     * 分页条件查询自己的帖子
     * @param page
     * @param size
     * @param blogQueryDto
     * @return
     */
    @PostMapping("/my-blog/{page}/{size}")
    public Result pageQueryMyBlog(@PathVariable Long page, @PathVariable Long size,@RequestBody BlogQueryDto3 blogQueryDto){
        blogQueryDto.setUserId(UserInfoContext.get());
        Page<BlogVo> res = blogService.pageQueryMyBlog(page, size, blogQueryDto);
        return Result.ok(res);
    }

    /**
     * 查询用户动态
     * @param page
     * @param size
     * @param request
     * @return
     */
    @GetMapping("/follow-user-blog/{page}/{size}")
    public Result pageQueryFollowUserBlog(@PathVariable Long page,@PathVariable Long size,@RequestParam(required = false,value = "blogAuthorId") Long blogAuthorId,HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request.getHeader("token"));
        Page<BlogVo> res = blogService.pageQueryFollowUserBlog(page,size,userId,blogAuthorId);
        return Result.ok(res);
    }

    /**
     * 主页已发布帖子
     * @param page
     * @param size
     * @param blogQueryDto2
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public Result pageQueryBlog(@PathVariable Long page, @PathVariable Long size, BlogQueryDto2 blogQueryDto2){
        Page<BlogVo> blogVoPage = blogService.pageQueryPublishBlog(page, size, blogQueryDto2);
        return Result.ok(blogVoPage);
    }

    /**
     * 查询最热门的的7篇帖子(已发布)
     * @return
     */
    @GetMapping("/hot")
    public Result hotBlogs(){
        List<HotBlogVo> blogs = blogService.hotBlogs();
        return Result.ok(blogs);
    }

    /**
     * 最新六篇帖子(已发布)
     * @return
     */
    @GetMapping("/new")
    public Result newBlogs(){
        List<HotBlogVo> blogs = blogService.newBlogs();
        return Result.ok(blogs);
    }

    /**
     * 根据id浏览帖子(已发布)
     * @param id
     * @return Result
     */
    @GetMapping("/{id}")
    public Result viewBlogById(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader("token");
        BlogViewVo blogViewVo = blogService.searchById(id,token);
        return Result.ok(blogViewVo);
    }

    /**
     * 推荐帖子  3篇
     * @param id
     * @return
     */
    @GetMapping("/other")
    public Result otherBlogs(@RequestParam(required = false) Long id){
        List<HotBlogVo> blogs =  blogService.getRecommendBlogs(id);
        return Result.ok(blogs);
    }


    /**
     * 主页的帖子 6篇
     * @return
     */
    @GetMapping("/home-blog")
    public Result homePageBlogs(){
        List<HotBlogVo> blogs = blogService.homePageBlogs(6);
        return Result.ok(blogs);
    }



    /**
     * 根据用户id查询自己的帖子(已发布)
     * @param userId
     * @return
     */
    @GetMapping("/user-blog/{userId}")
    public Result getBlogByUserid(@PathVariable Long userId){
        UserBlogVo userBlogVo = blogService.getBlogByUserId(userId);
        return Result.ok(userBlogVo);
    }

    /**
     * 查询当前登录用户是否已经点过攒
     * @return
     */
    @GetMapping("/likes/is-liked/{id}")
    public Result isLiked(@PathVariable Long id){
        Boolean isLike =  blogService.isLiked(id);
        return isLike ? Result.ok(1) : Result.ok(0);
    }

    /**
     * 点赞帖子
     * @param id
     * @return
     */
    @MyLog(value = "点赞或取消点赞帖子")
    @PutMapping("/likes/{id}")
    public Result likes(@PathVariable Long id){
        Result res =  blogService.likes(id);
        return res;
    }

    /**
     * 刷新帖子
     * @param id
     * @return
     */
    @GetMapping("/refresh/{id}")
    public Result refreshBlog(@PathVariable Long id){
        BlogViewVo blogViewVo = blogService.refreshBlog(id);
        return Result.ok(blogViewVo);
    }

    /**
     * 获取用户博客的例如浏览量、点赞量等数据
     * @param userId
     * @return
     */
    @GetMapping("/user-blog-data")
    public List<UserCoreDataVo> getUserBlogData(Long userId){
        List<UserCoreDataVo> data = blogService.getUserBlogData(userId);
        return data;
    }

    /**
     * 获取用户所有发布博客的id
     * @param userId
     * @return
     */
    @GetMapping("/user-blog-id/{userId}")
    public List<Long> getUserBlogIds(@PathVariable Long userId){
        List<Long> blogIds = blogService.listUserBlogIds(userId);
        return blogIds;
    }
}
