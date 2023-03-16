package com.lj.blog.service.impl;

import cn.easyes.core.biz.EsPageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.base.Result;
import com.lj.blog.service.*;
import com.lj.constant.EsConstant;
import com.lj.blog.mapper.BlogMapper;
import com.lj.blog.mapper.es.BlogDocumentMapper;
import com.lj.client.MessageClientService;
import com.lj.client.UserClientService;
import com.lj.constant.RedisConstant;
import com.lj.dto.*;
import com.lj.model.blog.Blog;
import com.lj.model.blog.BlogDetail;
import com.lj.model.blog.BlogTag;
import com.lj.blog.es.BlogDocument;
import com.lj.model.blog.Tag;
import com.lj.model.message.Message;
import com.lj.model.message.SendMessage2AllDto;
import com.lj.model.user.User;
import com.lj.model.user.ViewHistory;
import com.lj.util.*;
import com.lj.vo.*;
import com.lj.vo.EchartsVo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lj.constant.RedisConstant.LIKE_BLOG;

/**
 * <p>
 * 帖子表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private BlogDetailService blogDetailService;
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private BlogTagService blogTagService;
    @Autowired
    private MessageClientService messageClientService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BlogDocumentMapper blogDocumentMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;

    /**
     *
     * 根据关键字全文检索帖子
     * @param blogSearchDto
     * @return
     */
    public Page<BlogVo> searchByKeyWord(BlogSearchDto blogSearchDto) {
        Long size = blogSearchDto.getSize();
        Long page = (blogSearchDto.getPage() - 1) * size;
        String keyword = blogSearchDto.getKeyword();
        Integer sortWord = blogSearchDto.getSortWord();
        Page<BlogVo> data = new Page<>(page,size);
        Long total = baseMapper.searchByKeyWordCount(keyword);
        List<BlogVo> blogVos = baseMapper.searchByKeyWord(keyword, sortWord,page, size);
        blogVos.stream().forEach(b -> {
            if(!StringUtils.isEmpty(b.getTagNamesAsStr())){
                String[] tagNames = b.getTagNamesAsStr().split(",");
                b.setTagNames(List.of(tagNames));
            }
        });
        data.setTotal(total);
        data.setRecords(blogVos);
        return data;
    }

    /**
     * 用户发布一篇帖子
     * @param blogDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean userSaveBlog(BlogDto blogDto) {
        //保存帖子基本信息
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDto,blog);
        List<Long> typeIds = blogDto.getTypeIds();
        blog.setTypeId(typeIds.get(typeIds.size() - 1));
        Long userId = UserInfoContext.get();
        blog.setAuthorId(userId);
        boolean row1 = baseMapper.insert(blog) > 0;
        //保存帖子详情信息
        BlogDetail blogDetail = new BlogDetail();
        BeanUtils.copyProperties(blogDto,blogDetail);
        blogDetail.setBlogId(blog.getId());
        boolean row2 = blogDetailService.save(blogDetail);
        //保存帖子标签信息
        List<String> tagNames = blogDto.getTagNames();
        List<Tag> tags = tagService.queryIsExists(tagNames);
        List<BlogTag> blogTagList = tags.stream().map(i -> {
            BlogTag blogTag = new BlogTag();
            blogTag.setTagId(i.getId());
            blogTag.setBlogId(blog.getId());
            return blogTag;
        }).collect(Collectors.toList());
        boolean row3 = blogTagService.saveBatch(blogTagList);
        return row1 && row2 && row3;
    }

    /**
     * 更新帖子
     * @param blogDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBlog(BlogDto blogDto) {
        //更新帖子基本信息
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDto,blog,"updateTime","publishDate");
        List<Long> typeIds = blogDto.getTypeIds();
        blog.setTypeId(typeIds.get(typeIds.size() - 1));
        //重置发布时间
        blog.setPublishDate(null);
        //将帖子状态重置
        blog.setStatus(0);
        boolean r1 = baseMapper.updateById(blog) > 0;
        //更新帖子详情信息
        BlogDetail blogDetail = blogDetailService.getByBlogId(blog.getId());
        blogDetail.setContentHtml(blogDto.getContentHtml());
        blogDetail.setContentMd(blogDto.getContentMd());
        boolean r2 = blogDetailService.updateById(blogDetail);
        //更新帖子标签信息
        blogTagService.removeByBlogId(blogDto.getId());
        List<String> tagNames = blogDto.getTagNames();
        List<Tag> tags = tagService.queryIsExists(tagNames);
        List<BlogTag> blogTagList = tags.stream().map(i -> {
            BlogTag blogTag = new BlogTag();
            blogTag.setTagId(i.getId());
            blogTag.setBlogId(blog.getId());
            return blogTag;
        }).collect(Collectors.toList());
        boolean r3 = blogTagService.saveBatch(blogTagList);
        //删除es中的数据
        boolean r4 = removeBlogDocument(blogDto.getId());
        return r1 && r2 && r3 && r4;
    }

    /**
     * 管理员分页条件查询帖子
     * @param blogQueryDto1
     * @return
     */
    public Page<BlogVo> pageQueryBlog(Long page, Long size, BlogQueryDto1 blogQueryDto1) {
        Page<BlogVo> res = new Page<>(page,size);
        page = (page - 1) * size;//起始行偏移量
        List<Long> typeIds = blogQueryDto1.getTypeIds();
        if(Objects.nonNull(typeIds) && !typeIds.isEmpty()){
            blogQueryDto1.setTypeId(typeIds.get(typeIds.size() - 1));
        }
        List<BlogVo> blogVos = baseMapper.selectPageQuery(page,size, blogQueryDto1);
        blogVos.stream().forEach(b -> {
            if(!StringUtils.isEmpty(b.getTagNamesAsStr())){
                String[] tagNames = b.getTagNamesAsStr().split(",");
                b.setTagNames(List.of(tagNames));
            }
        });
        Long total = baseMapper.selectCountQuery(blogQueryDto1);
        //记录
        res.setRecords(blogVos);
        //总数
        res.setTotal(total);
        return res;
    }

    /**
     * 根据id批量删除帖子
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBlogByIds(List<Long> ids) {
        //删除帖子详情
        List<Long> detailIds = ids.stream().map(i -> {
            BlogDetail blogDetail = blogDetailService.getByBlogId(i);
            if(Objects.isNull(blogDetail)){
                return null;
            }
            return blogDetail.getId();
        }).collect(Collectors.toList());
        boolean r1 = blogDetailService.removeByIds(detailIds);
        //删除帖子标签
        List<Long> blogTagIds = new ArrayList<>();
        ids.stream().forEach(i -> {
            blogTagIds.addAll(blogTagService.getByBlogId(i).stream()
                    .map(bg -> bg.getId()).collect(Collectors.toList()));
        });
        boolean r2 = blogTagService.removeByIds(blogTagIds);
        //删除帖子基本内容
        boolean r3 = baseMapper.deleteBatchIds(ids) > 0;
        //删除es中数据
        boolean r4 = batchRemoveBlogDocument(ids);
        return r1 & r2 & r3 & r4;
    }

    /**
     * 根据id查询帖子
     * @param id
     * @return
     */
    public BlogDto getBlogById(Long id) {
        BlogDto blogDto = new BlogDto();
        //帖子基本信息
        Blog blog = baseMapper.selectById(id);
        BeanUtils.copyProperties(blog,blogDto);
        List<Long> typeIds = typeService.getParentTypeAndChild(blog.getTypeId());
        blogDto.setTypeIds(typeIds);
        blogDto.setTypeName(typeService.getById(blog.getTypeId()).getTypeName());
        //帖子详细信息
        BlogDetail blogDetail = blogDetailService.getByBlogId(id);
        BeanUtils.copyProperties(blogDetail,blogDto,"id");
        //帖子标签信息
        List<BlogTag> tags = blogTagService.getByBlogId(id);
        if (!tags.isEmpty()) {
            List<String> tagNames = tags.stream().map(i -> {
                Long tagId = i.getTagId();
                return tagService.getById(tagId).getTagName();
            }).collect(Collectors.toList());
            blogDto.setTagNames(tagNames);
        }
        return blogDto;
    }

    /**
     * 帖子审核通过
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean approvedBlog(Long id) {
        Blog blog = new Blog();
        blog.setId(id);
        blog.setStatus(1);
        blog.setPublishDate(new Date());
        boolean r1 = baseMapper.updateById(blog) > 0;
        //TODO 线程池优化
        //通知该用户,帖子审核通过
        Blog blogInfo = baseMapper.selectById(id);
        Long userId = blogInfo.getAuthorId();
        Message message = new Message();
        message.setReceiveUserId(userId);
        message.setSendUserId(AdminInfoContext.get());
        message.setType(0);
        message.setContent("你发布的" + "《" + blogInfo.getTitle() + "》已通过审核");
        messageClientService.sendMessage(message);
        //通知粉丝,更新了一条动态
        List<Long> followMeUserIds = followMeUserIds(userId);
        if(!followMeUserIds.isEmpty()){
            SendMessage2AllDto sendMessage2AllDto = new SendMessage2AllDto();
            sendMessage2AllDto.setIds(followMeUserIds);
            sendMessage2AllDto.setType(3);
            sendMessage2AllDto.setContent("有一条新动态,点击查看");
            sendMessage2AllDto.setSendUserId(userId);
            messageClientService.sendMessage2All(sendMessage2AllDto);
            List<Message> messages = followMeUserIds.stream().map(i -> {
                Message msg = new Message();
                msg.setType(3);
                msg.setSendUserId(userId);
                msg.setReceiveUserId(i);
                msg.setContent(userClientService.getById(userId).getNickName() + "发布了一条动态");
                return msg;
            }).collect(Collectors.toList());
            messageClientService.saveMessage(messages);
        }
        //es中添加对应数据
        BlogDocument blogDocument = selectOneBlogDocument(id);
        boolean r2 = insertBlogDocument(blogDocument);
        return r1 & r2;
    }

    private List<Long> followMeUserIds(Long id){
        String key = RedisConstant.FOLLOW_ME_USER + id;
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        Set<String> members = ops.members(key);
        List<Long> ids = new ArrayList<>();
        if(!members.isEmpty()){
            ids = members.stream().map(i -> Long.valueOf(i)).collect(Collectors.toList());
        }
        return ids;
    }

    /**
     * 推荐帖子(通过审核的)
     * @param id
     * @return
     */
    public List<HotBlogVo> getRecommendBlogs(Long id){
        //生成随机的帖子id
        List<Long> roundIds = getAllBlogIds();
        List<Long> generateIds = RandomUtil.generateIds(id, 3, roundIds);
        //封装数据返回
        List<HotBlogVo> res = baseMapper.selectBatchIds(generateIds).stream().map(i -> {
            HotBlogVo hotBlogVo = new HotBlogVo();
            BeanUtils.copyProperties(i,hotBlogVo);
            User user = userClientService.getById(i.getAuthorId());
            hotBlogVo.setUsername(user.getNickName());
            hotBlogVo.setPic(user.getPic());
            return hotBlogVo;
        }).collect(Collectors.toList());
        //TODO 可以利用相关性算法推荐帖子
        return res;
    }

    /**
     * 获得所有帖子的id (通过审核的)
     * @return
     */
    private List<Long> getAllBlogIds() {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getStatus,1)
                .select(Blog::getId);
        List<Long> roundIds = baseMapper.selectList(wrapper).stream()
                .map(i -> i.getId())
                .collect(Collectors.toList());
        return roundIds;
    }

    /**
     * 帖子审核不通过
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean noApprovedBlog(Long id,String reason) {
        Blog blog = new Blog();
        blog.setId(id);
        blog.setStatus(2);
        boolean isSuccess = baseMapper.updateById(blog) > 0;
        //TODO 线程池优化
        //通知该用户,帖子审核未通过
        Blog blogInfo = baseMapper.selectById(id);
        Long userId = blogInfo.getAuthorId();
        Message message = new Message();
        message.setReceiveUserId(userId);
        message.setSendUserId(AdminInfoContext.get());
        message.setType(0);
        message.setContent("你发布的" + "《" + blogInfo.getTitle() + "》未通过审核,原因:" + reason);
        messageClientService.sendMessage(message);
        return isSuccess;
    }

    /**
     * 统计每个标签所占帖子数量
     * @return
     */
    public Map<String, Object> reportsBlogTags() {
        List<EchartsVo> list = baseMapper.reportsBlogTags();
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", list);
        EchartsVo maxVal = list.stream().max((a,b) -> (int)(a.getValue() - b.getValue())).get();
        map.put("max",maxVal.getValue());
        return map;
    }

    /**
     * 统计每个分类下的帖子数量
     * @return
     */
    public Map<String, Object> reportsBlogTypes() {
        List<EchartsVo> list = baseMapper.reportsBlogTypes();
        List<String> nameList = list.stream().map(i -> i.getName()).collect(Collectors.toList());
        List<Long> valueList = list.stream().map(i -> i.getValue()).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", nameList);
        map.put("value", valueList);
        return map;
    }

    /**
     * 查询最热门的的5篇帖子(已发布)
     * @return
     */
    public List<HotBlogVo> hotFiveBlogs() {
        IPage<Blog> page = new Page<>(1,5);
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Blog::getViews)
                .eq(Blog::getStatus,1)
                .orderByDesc(Blog::getLikes);
        baseMapper.selectPage(page, wrapper);
        List<HotBlogVo> blogs = null;
        if(Objects.nonNull(page.getRecords())){
            blogs = page.getRecords().stream().map(i -> {
                HotBlogVo hotBlogVo = new HotBlogVo();
                BeanUtils.copyProperties(i,hotBlogVo);
                hotBlogVo.setPic(userClientService.getById(i.getAuthorId()).getPic());
                return hotBlogVo;
            }).collect(Collectors.toList());
        }
        return blogs;
    }

    /**
     * 最新五篇帖子(已发布)
     * @return
     */
    public List<HotBlogVo> newBlogs() {
        IPage<Blog> page = new Page<>(1,6);
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Blog::getPublishDate)
                .orderByDesc(Blog::getUpdateTime)
                .eq(Blog::getStatus,1);
        baseMapper.selectPage(page, wrapper);
        List<HotBlogVo> blogs = null;
        if(Objects.nonNull(page.getRecords())){
            blogs = page.getRecords().stream().map(i -> {
                HotBlogVo hotBlogVo = new HotBlogVo();
                User user = userClientService.getById(i.getAuthorId());
                BeanUtils.copyProperties(i,hotBlogVo);
                hotBlogVo.setPic(user.getPic());
                hotBlogVo.setUsername(user.getNickName());
                return hotBlogVo;
            }).collect(Collectors.toList());
        }
        return blogs;
    }

    /**
     * 分页条件查询已发布帖子
     * @param page
     * @param size
     * @param blogQueryDto2
     * @return
     */
    public Page<BlogVo> pageQueryPublishBlog(Long page, Long size, BlogQueryDto2 blogQueryDto2) {
        Page<BlogVo> res = new Page<>(page,size);
        page = (page - 1) * size;//起始行偏移量
        List<BlogVo> blogVos = baseMapper.selectPageQueryPublish(page,size, blogQueryDto2);
        blogVos.stream().forEach(b -> {
            if(!StringUtils.isEmpty(b.getTagNamesAsStr())){
                String[] tagNames = b.getTagNamesAsStr().split(",");
                b.setTagNames(List.of(tagNames));
            }
        });
        Long total = baseMapper.selectCountQueryPublish(blogQueryDto2);
        //记录
        //将查询到的结果打乱
        Collections.shuffle(blogVos);
        res.setRecords(blogVos);
        //总数
        res.setTotal(total);
        return res;
    }

    /**
     * 分页条件查询自己的帖子
     * @param page
     * @param size
     * @param blogQueryDto
     * @return
     */
    public Page<BlogVo> pageQueryMyBlog(Long page, Long size, BlogQueryDto3 blogQueryDto) {
        Page<BlogVo> res = new Page<>(page,size);
        page = (page - 1) * size;//起始行偏移量
        List<Long> typeIds = blogQueryDto.getTypeIds();
        if(Objects.nonNull(typeIds) && !typeIds.isEmpty()){
            blogQueryDto.setTypeId(typeIds.get(typeIds.size() - 1));
        }
        List<BlogVo> blogVos = baseMapper.selectPageQueryMyBlog(page,size, blogQueryDto);
        blogVos.stream().forEach(b -> {
            if(!StringUtils.isEmpty(b.getTagNamesAsStr())){
                String[] tagNames = b.getTagNamesAsStr().split(",");
                b.setTagNames(List.of(tagNames));
            }
        });
        Long total = baseMapper.selectPageQueryMyBlogCount(blogQueryDto);
        //记录
        res.setRecords(blogVos);
        //总数
        res.setTotal(total);
        return res;
    }

    /**
     * 查询用户发布的帖子(已发布)
     * @param userId
     * @return
     */
    public UserBlogVo getBlogByUserId(Long userId) {
        UserBlogVo userBlogVo = new UserBlogVo();
        userBlogVo.setUserId(userId);
        List<Blog> blogs = this.getByUserId(userId);
        userBlogVo.setTotal((long) blogs.size());
        userBlogVo.setBlogs(blogs);
        return userBlogVo;
    }

    /**
     * 根据用户id查询用户已发布的帖子
     * @param userId
     * @return
     */
    private List<Blog> getByUserId(Long userId) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getAuthorId,userId)
                .eq(Blog::getStatus,1)
                .orderByDesc(Blog::getPublishDate)
                .orderByDesc(Blog::getUpdateTime);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据id查找帖子
     * @param id
     * @return BlogViewVo
     */
    @Transactional(rollbackFor = Exception.class)
    public BlogViewVo searchById(Long id,String token) {
        //浏览量 +1
        Blog blog = new Blog();
        blog.setId(id);
        blog.setViews(baseMapper.selectById(id).getViews() + 1);
        baseMapper.updateById(blog);
        //查询帖子数据
        BlogViewVo blogViewVo = baseMapper.searchById(id);
        String[] tagNameList = blogViewVo.getTagNamesAsStr().split(",");
        blogViewVo.setTagNames(Arrays.asList(tagNameList));
        //添加访问历史(用户登录后)
        if(Objects.nonNull(token)){
            Long userId = JwtTokenUtil.getUserId(token);
            ViewHistory viewHistory = new ViewHistory();
            viewHistory.setUserId(userId);
            viewHistory.setType(0);
            viewHistory.setBlogId(id);
            userClientService.addViewHistory(viewHistory);
        }
        return blogViewVo;
    }


    /**
     * 刷新帖子
     * @param id
     * @return
     */
    public BlogViewVo refreshBlog(Long id){
        //浏览量 +1
        Blog blog = new Blog();
        blog.setId(id);
        blog.setViews(baseMapper.selectById(id).getViews() + 1);
        baseMapper.updateById(blog);
        //查询帖子数据
        BlogViewVo blogViewVo = baseMapper.searchById(id);
        String[] tagNameList = blogViewVo.getTagNamesAsStr().split(",");
        blogViewVo.setTagNames(Arrays.asList(tagNameList));
        return blogViewVo;
    }

    /**
     * 添加作者name
     * @param records
     */
    private void setAuthName(List<Blog> records) {
        records.stream().forEach(r -> {
            Long authorId = r.getAuthorId();
            User user = userClientService.getById(authorId);
            r.getParams().put("authName", user.getNickName());
        });
    }

    /**
     * 查询当前登录用户是否已经点过赞
     * @return
     */
    public Boolean isLiked(Long id) {
        Long userId = UserInfoContext.get();
        String key = LIKE_BLOG + id;
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        return operations.isMember(key,userId.toString());
    }

    /**
     * 点赞帖子
     * @param id
     * @return
     */
    public Result likes(Long id) {
        Blog blog = new Blog();
        blog.setId(id);
        Long userId = UserInfoContext.get();
        //查询该用户是否已经点赞过该帖子
        Boolean isLiked = isLiked(id);
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        String key = LIKE_BLOG + id;
        if(isLiked){
            //已经点过赞,取消点赞
            operations.remove(key,userId.toString());
            blog.setLikes(operations.size(key));
            boolean isSuccess = baseMapper.updateById(blog) > 0;
            return isSuccess ? Result.ok().message("取消点赞成功") : Result.fail().message("取消点赞失败");
        }
        else{
            //为点过赞,点赞
            operations.add(key,userId.toString());
            blog.setLikes(operations.size(key));
            boolean isSuccess = baseMapper.updateById(blog) > 0;
            return isSuccess ? Result.ok().message("点赞成功") : Result.fail().message("点赞失败");
        }
    }

    /**
     * 根据id查询BlogDocument
     * @param id
     * @return
     */
    public BlogDocument selectOneBlogDocument(Long id){
        BlogDocument blogDocument = baseMapper.selectOneBlogDocument(id);
        return blogDocument;
    }

    /**
     * 查询所有的BlogDocuments
     * @return
     */
    public List<BlogDocument> selectAllBlogDocument() {
        List<BlogDocument> blogDocuments= baseMapper.selectAllBlogDocument();
        return blogDocuments;
    }

    /**
     * 往es中插入(更新)一条数据
     * @param blogDocument
     * @return
     */
    public boolean insertBlogDocument(BlogDocument blogDocument) {
        boolean isSuccess = blogDocumentMapper.insert(blogDocument, EsConstant.BLOG_INDEX) > 0;
        return isSuccess;
    }

    /**
     * 往es中批量插入数据
     * @param blogDocumentList
     * @return
     */
    public boolean batchInsertBlogDocument(List<BlogDocument> blogDocumentList) {
        boolean isSuccess = blogDocumentMapper.insertBatch(blogDocumentList,EsConstant.BLOG_INDEX) > 0;
        return isSuccess;
    }

    /**
     * 删除es中一条数据
     * @param id
     * @return
     */
    public boolean removeBlogDocument(Long id) {
        boolean isSuccess = blogDocumentMapper.deleteById(id, EsConstant.BLOG_INDEX) > 0;
        return isSuccess;
    }

    /**
     * 批量删除es中数据
     * @param ids
     * @return
     */
    public boolean batchRemoveBlogDocument(List<Long> ids) {
        boolean isSuccess = blogDocumentMapper.deleteBatchIds(ids, EsConstant.BLOG_INDEX) > 0;
        return isSuccess;
    }

    /**
     * 搜索补全关键字根据帖子标题补全)
     * @param keyword
     * @return
     */
    public List<String> completeKeyword(String keyword) {
        LambdaEsQueryWrapper<BlogDocument> wrapper = new LambdaEsQueryWrapper<>();
        if (Strings.isNotBlank(keyword)) {
            wrapper.index(EsConstant.BLOG_INDEX)
                    .match(BlogDocument::getTitle,keyword)
                    .select(BlogDocument::getTitle)
                    .sortByScore();
        }
        List<BlogDocument> blogDocuments = blogDocumentMapper.selectList(wrapper);
        List<String> keywords = new ArrayList<>();
        if(!blogDocuments.isEmpty()){
            blogDocuments.stream().forEach(i -> {
                String title = i.getTitle();
                if(Strings.isNotBlank(title)){
                    keywords.add(title);
                }
            });
        }
        return keywords;
    }

    /**
     * 当前登录用户关注博主的所有帖子
     * @param page
     * @param size
     * @param userId
     * @return
     */
    public Page<BlogVo> pageQueryFollowUserBlog(Long page, Long size, Long userId) {
        Page<BlogVo> res = new Page<>(page,size);
        //起始偏移量
        page = (page - 1) * size;
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        Set<String> myFollowUsers = ops.members(RedisConstant.MY_FOLLOW_USER + userId);
        List<BlogVo> records = new ArrayList<>();
        Long total = 0L;
        if(!myFollowUsers.isEmpty()){
            Set<Long> followUsers = myFollowUsers.stream().map(i -> Long.valueOf(i)).collect(Collectors.toSet());
            records = baseMapper.selectFollowUserBlog(page,size,followUsers);
            records.forEach(i -> {
               i.setTagNames(List.of( i.getTagNamesAsStr().split(",")));
            });
            total = baseMapper.selectFollowUserBlogCount(myFollowUsers);
        }
        res.setTotal(total);
        res.setRecords(records);
        return res;
    }

    /**
     * 主页帖子内容
     * @param i
     * @return
     */
    public List<HotBlogVo> homePageBlogs(int i) {
        List<Long> allBlogIds = getAllBlogIds();
        List<Long> ids = RandomUtil.generateIds(6, allBlogIds);
        List<Blog> blogs = baseMapper.selectBatchIds(ids);
        List<HotBlogVo> res = new ArrayList<>();
        if(!blogs.isEmpty()){
            blogs.forEach(item -> {
                HotBlogVo hotBlogVo = new HotBlogVo();
                BeanUtils.copyProperties(item,hotBlogVo);
                User user = userClientService.getById(item.getAuthorId());
                hotBlogVo.setUsername(user.getNickName());
                hotBlogVo.setPic(user.getPic());
                res.add(hotBlogVo);
            });
        }
        return res;
    }

    /**
     * 根据关键字检索
     * @param blogSearchDto
     * @return
     */
    public EsPageInfo<BlogDocument> selectBlogDocumentByKeyword(BlogSearchDto blogSearchDto) {
        LambdaEsQueryWrapper<BlogDocument> wrapper = new LambdaEsQueryWrapper<>();
        //索引名
        wrapper.index(EsConstant.BLOG_INDEX);
        String keyword = blogSearchDto.getKeyword();
        Integer by = blogSearchDto.getBy();
        Integer page = Math.toIntExact(blogSearchDto.getPage());
        Integer size = Math.toIntExact(blogSearchDto.getSize());
        Integer sortWord = blogSearchDto.getSortWord();
        //关键字
        if(!StringUtils.isEmpty(by)){
            if(by == 0 && !StringUtils.isEmpty((keyword))){
                //分词搜索
                wrapper.multiMatchQuery(keyword,2,BlogDocument::getTitle,BlogDocument::getArticleSummary);
            }
            //精确搜索 按分类
            else if(by == 1 && !StringUtils.isEmpty(keyword)){
                wrapper.eq(BlogDocument::getTypeName,keyword);
            }
            //精确搜索 按作者
            else if(by == 2 && !StringUtils.isEmpty(keyword)){
                wrapper.eq(BlogDocument::getAuthorName,keyword);
            }
        }
        //排序字段
        if(Objects.nonNull(sortWord)){
            if(sortWord == 0){
                wrapper.orderByDesc(BlogDocument::getPublishDate);
            }
            if(sortWord == 1){
                wrapper.orderByDesc(BlogDocument::getViews);
            }
            else if (sortWord == 2){
                wrapper.orderByDesc(BlogDocument::getLikes);
            }
            else{
                wrapper.sortByScore();
            }
        }
        EsPageInfo<BlogDocument> blogDocumentEsPageInfo = blogDocumentMapper.pageQuery(wrapper, page, size);
        return blogDocumentEsPageInfo;
    }
}
