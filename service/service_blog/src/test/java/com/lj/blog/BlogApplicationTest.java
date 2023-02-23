package com.lj.blog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lj.blog.mapper.BlogMapper;
import com.lj.blog.mapper.CommentMapper;
import com.lj.blog.mapper.es.BlogDocumentMapper;
import com.lj.blog.service.*;
import com.lj.model.blog.Blog;
import com.lj.blog.es.BlogDocument;
import com.lj.util.SendMessageUtil;
import com.lj.vo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class BlogApplicationTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogService blogService;
    @Autowired
    private LeaveMessageService leaveMessageService;
    @Autowired
    private BlogDocumentMapper blogDocumentMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TypeService typeService;

    @Test
    void name() {
        List<TypeVo> typeVoTree = typeService.listTypeVoTree();
        System.out.println(typeVoTree);
    }

    @Test
    void test1() throws ParseException {
        CommentQueryDto commentQueryDto = new CommentQueryDto();
        commentQueryDto.setKeyword("测");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2022-11-11 17:27:53");
        commentQueryDto.setCreateTime(date);
        List<CommentVo> commentVoList = commentMapper.selectCommentPage(0L, 2L, commentQueryDto);
        System.out.println(commentVoList);
        long total = commentMapper.selectCountParam(commentQueryDto);
        System.out.println("总条数:" + total);
    }

    @Test
    void test2() {
        Page<BlogVo> blogVoPage = blogService.pageQueryBlog(1L, 5L, new BlogQueryDto1());
        System.out.println(blogVoPage.getRecords());
    }

    @Test
    void test3() {
        Map<String, Object> map = blogService.reportsBlogTags();
        System.out.println(map);
    }

    @Test
    void test4() {
        blogService.pageQueryPublishBlog(1L,10L,new BlogQueryDto2());
    }

    @Test
    void test5() {
        System.out.println(String.class.getSimpleName());
        System.out.println(String.class.getName());
    }

    @Test
    void test6() {
        LeaveMessageQueryDto leaveMessageQueryDto = new LeaveMessageQueryDto();
        leaveMessageQueryDto.setSendTime(new Date());
        leaveMessageService.PageQueryLeaveMessage(1L,5L,leaveMessageQueryDto);
    }

    @Autowired
    private BlogDetailService blogDetailService;
    @Test
    void test7() {
        blogDetailService.getByBlogId(22L);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void test8() {
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        operations.add("test","1");
        System.out.println(operations.isMember("test", "1"));
    }

    @Test
    void test9() {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Blog::getId)
                .select(Blog::getId,Blog::getTitle);
        System.out.println(blogService.getBaseMapper().selectList(wrapper));
    }

    @Test
    void test10() {
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        System.out.println(operations.isMember("a", "1"));
    }

    @Test
    void test11() {
        BlogSearchDto blogSearchDto = new BlogSearchDto();
        blogSearchDto.setKeyword("我的美图");
        blogSearchDto.setPage(1L);
        blogSearchDto.setSize(5L);
        blogService.searchByKeyWord(blogSearchDto);
    }

    @Test
    void testCreateIndex() {
        blogDocumentMapper.deleteIndex("blog");
        Boolean isSuccess = blogDocumentMapper.createIndex("blog");
        System.out.println(isSuccess);
    }

    @Test
    void testDeleteIndex() {
        Boolean isSuccess = blogDocumentMapper.deleteIndex("blog");
        System.out.println(isSuccess);
    }

    @Test
    void testInsertOne() {
        BlogDocument blogDocument = new BlogDocument();
        BlogViewVo blogViewVo = blogMapper.searchById(4L);
        BeanUtils.copyProperties(blogViewVo,blogDocument);
        boolean isSuccess = blogService.insertBlogDocument(blogDocument);
        System.out.println(isSuccess);
    }

    @Test
    void testBatchInsert() {
        List<BlogDocument> blogDocuments = blogService.selectAllBlogDocument();
        boolean isSuccess = blogService.batchInsertBlogDocument(blogDocuments);
        System.out.println(isSuccess);
    }

    @Test
    void testQuery() {
        System.out.println(blogService.selectOneBlogDocument(40L));
    }

    @Test
    void testDelete() {
        blogService.removeBlogDocument(32L);
    }

    @Test
    void testQueryByKeyword() {
        BlogSearchDto blogSearchDto = new BlogSearchDto();
        blogSearchDto.setPage(1L);
        blogSearchDto.setSize(10L);
        blogSearchDto.setKeyword("我的mysql学习");
        blogSearchDto.setSortWord(0);
        List<BlogDocument> blogDocuments = blogService.selectBlogDocumentByKeyword(blogSearchDto).getList();
        System.out.println(blogDocuments);
    }

    @Test
    void testCompleteKeyword() {
        String keyword = "我的mysql学习";
        List<String> list = blogService.completeKeyword(keyword);
        System.out.println(list);
    }

    @Test
    void testUserFollowBlog() {
        System.out.println(SendMessageUtil.webSocketMap.hashCode());
    }
}
