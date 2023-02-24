package com.lj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.model.blog.Blog;
import com.lj.blog.es.BlogDocument;
import com.lj.vo.*;
import com.lj.vo.admin.EchartsVo;
import com.lj.vo.user.BlogQueryDto2;
import com.lj.vo.user.BlogQueryDto3;
import com.lj.vo.user.BlogViewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 博客表 Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    List<BlogVo> selectPageQuery(@Param("page") Long page,@Param("size") Long size,@Param("blogQueryDto1") BlogQueryDto1 blogQueryDto1);

    Long selectCountQuery(@Param("blogQueryDto1") BlogQueryDto1 blogQueryDto1);

    List<EchartsVo> reportsBlogTags();

    List<EchartsVo> reportsBlogTypes();

    List<BlogVo> selectPageQueryPublish(@Param("page") Long page,@Param("size") Long size,@Param("blogQueryDto2") BlogQueryDto2 blogQueryDto2);

    Long selectCountQueryPublish(@Param("blogQueryDto2") BlogQueryDto2 blogQueryDto2);

    BlogViewVo searchById(Long id);

    List<BlogVo> selectPageQueryMyBlog(@Param("page")Long page,@Param("size") Long size,@Param("blogQueryDto") BlogQueryDto3 blogQueryDto);

    Long selectPageQueryMyBlogCount(@Param("blogQueryDto")BlogQueryDto3 blogQueryDto);

    Long searchByKeyWordCount(@Param("keyword") String keyword);

    List<BlogVo> searchByKeyWord(@Param("keyword")String keyword,@Param("sortWord") Integer sortWord,@Param("page") Long page,@Param("size") Long size);

    List<BlogDocument> selectAllBlogDocument();

    BlogDocument selectOneBlogDocument(Long id);

    List<BlogVo> selectFollowUserBlog(@Param("page") Long page,@Param("size")  Long size,@Param("myFollowUsers") Set<Long> myFollowUsers);

    Long selectFollowUserBlogCount(@Param("myFollowUsers") Set<String> myFollowUsers);
}
