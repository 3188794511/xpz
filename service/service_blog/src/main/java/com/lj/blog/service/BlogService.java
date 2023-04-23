package com.lj.blog.service;

import cn.easyes.core.biz.EsPageInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.base.Result;
import com.lj.dto.*;
import com.lj.model.blog.Blog;
import com.lj.blog.es.BlogDocument;
import com.lj.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
public interface BlogService extends IService<Blog> {


    boolean updateBlog(BlogDto blogDto);

    Page<BlogVo> pageQueryBlog(Long page, Long size, BlogQueryDto1 blogQueryDto1);

    boolean deleteBlogByIds(List<Long> ids);

    BlogDto getBlogById(Long id);

    boolean approvedBlog(Long id);

    Map<String, Object> reportsBlogTags();

    Map<String, Object> reportsBlogTypes();

    List<HotBlogVo> hotBlogs();

    Page<BlogVo> pageQueryPublishBlog(Long page, Long size, BlogQueryDto2 blogQueryDto2);

    BlogViewVo searchById(Long id,String token);

    Result likes(Long id);

    BlogViewVo refreshBlog(Long id);

    Boolean isLiked(Long id);

    List<HotBlogVo> newBlogs();

    UserBlogVo getBlogByUserId(Long userId);

    Page<BlogVo> pageQueryMyBlog(Long page, Long size, BlogQueryDto3 blogQueryDto);

    boolean userSaveBlog(BlogDto blogDto);

    boolean noApprovedBlog(Long id,String reason);

    List<HotBlogVo> getRecommendBlogs(Long id);

    Page<BlogVo> searchByKeyWord(BlogSearchDto blogSearchDto);

    BlogDocument selectOneBlogDocument(Long id);

    List<BlogDocument> selectAllBlogDocument();

    boolean insertBlogDocument(BlogDocument blogDocument);

    boolean batchInsertBlogDocument(List<BlogDocument> blogDocumentList);

    boolean removeBlogDocument(Long id);

    boolean batchRemoveBlogDocument(List<Long> ids);

    EsPageInfo<BlogDocument> selectBlogDocumentByKeyword(BlogSearchDto blogSearchDto);

    List<String> completeKeyword(String keyword);

    List<HotBlogVo> homePageBlogs(int i);

    Page<BlogVo> pageQueryFollowUserBlog(Long page, Long size, Long userId,Long blogAuthorId);

    List<UserCoreDataVo> getUserBlogData(Long userId);
}
