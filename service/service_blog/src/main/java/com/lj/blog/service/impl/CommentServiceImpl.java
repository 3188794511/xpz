package com.lj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lj.base.Result;
import com.lj.blog.mapper.CommentMapper;
import com.lj.blog.service.BlogService;
import com.lj.blog.service.CommentService;
import com.lj.client.UserClientService;
import com.lj.constant.UserBehaviorConstant;
import com.lj.model.blog.Blog;
import com.lj.model.blog.Comment;
import com.lj.model.user.User;
import com.lj.model.user.UserLog;
import com.lj.util.UserInfoContext;
import com.lj.dto.CommentQueryDto;
import com.lj.vo.CommentUser;
import com.lj.vo.CommentViewVo;
import com.lj.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lj.constant.RedisConstant.LIKE_COMMENT;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author lj
 * @since 2022-11-07
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserClientService userClientService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 发表评论
     * @param commentViewVo
     * @return
     */
    public Result saveComment(CommentViewVo commentViewVo) {
        Comment comment = new Comment();
        //获取当前用户id
        Long commentUserId = commentViewVo.getCommentUser().getId();
        //获取回复用户id
        if(Objects.nonNull(commentViewVo.getReplayUser())){
            Long replayUserId = commentViewVo.getReplayUser().getId();
            comment.setReplayUserId(replayUserId);
        }
        if(Objects.nonNull(commentViewVo.getParentId())){
            //获取父评论id
            Long parentId = commentViewVo.getParentId();
            comment.setParentId(parentId);
        }
        //获取博客id
        Long blogId = commentViewVo.getBlogId();
        //获取评论内容
        String content = commentViewVo.getContent();
        //新增评论
        comment.setUserId(commentUserId);
        comment.setBlogId(blogId);
        comment.setContent(content);
        boolean isSuccess = baseMapper.insert(comment) > 0;
        //记录用户行为日志
        UserLog userLog = new UserLog();
        userLog.setUserId(commentUserId);
        userLog.setBlogId(blogId);
        userLog.setBehavior(UserBehaviorConstant.COMMENT_BLOG);
        userClientService.addUserLog(userLog);
        return isSuccess ? Result.ok(comment.getId()).message("发表评论成功") : Result.fail().message("发表评论失败");
    }

    /**
     * 根据博客id查询评论
     * @param blogId
     * @param orderBy 排序(0按照点赞数,1按照时间)
     * @return
     */
    public  List<CommentViewVo> listTreeByBlogId(Long blogId, int orderBy,Long userId) {
        //根据博客id查询一级评论
        List<Comment> comments =  this.selectByBlogId(blogId).stream().filter(i -> i.getParentId() == -1).collect(Collectors.toList());
        Long authorId = blogService.getById(blogId).getAuthorId();//作者id
        List<CommentViewVo> commentViewVos = comments.stream().map(i -> {
            //属性封装
            CommentViewVo commentViewVo = getCommentViewVo(blogId, authorId, userId, i);
            //子评论查询
            List<CommentViewVo>  childrenComments = this.searchChildrenComments(authorId,commentViewVo,blogId,userId);
            commentViewVo.setChildrenComments(childrenComments);
            return commentViewVo;
            }).collect(Collectors.toList());
        return commentViewVos;
    }

    /**
     * 根据comment封装commentViewVo
     * @param blogId
     * @param authorId
     * @param userId
     * @param i
     * @return
     */
    private CommentViewVo getCommentViewVo(Long blogId, Long authorId, Long userId, Comment i) {
        CommentViewVo commentViewVo = new CommentViewVo();
        //评论用户信息
        CommentUser commentUser = new CommentUser();
        User user = userClientService.getById(i.getUserId());
        commentUser.setId(i.getUserId());
        commentUser.setName(user.getNickName());
        commentUser.setAvatar(user.getPic());
        commentUser.setAuthor(i.getUserId().equals(authorId));
        commentViewVo.setCommentUser(commentUser);
        //回复用户信息
        if(i.getReplayUserId() != -1){
            Long replayUserId = i.getReplayUserId();
            User replayUser = userClientService.getById(replayUserId);
            CommentUser replayUserInfo = new CommentUser();
            replayUserInfo.setId(replayUserId);
            replayUserInfo.setAuthor(blogService.getById(blogId).getAuthorId().equals(replayUserId));
            replayUserInfo.setName(replayUser.getNickName());
            replayUserInfo.setAvatar(replayUser.getPic());
            commentViewVo.setReplayUser(replayUserInfo);
        }
        //评论基本信息
        commentViewVo.setBlogId(blogId);
        commentViewVo.setId(i.getId());
        commentViewVo.setContent(i.getContent());
        commentViewVo.setCreateTime(i.getCreateTime());
        Boolean isLiked = this.isLikedComment(i.getId(),userId);
        commentViewVo.setLiked(isLiked);
        commentViewVo.setIsLiked(i.getIsLiked());
        return commentViewVo;
    }


    /**
     * 根据父评论id查找子评论 ,将子评论封装到对应的父评论中
     * @return
     */
    private List<CommentViewVo> searchChildrenComments(Long authorId,CommentViewVo parentComment,Long blogId,Long userId) {
        if(this.getByParentId(parentComment.getId()).size() == 0){
            //当前评论没有子评论时,结束递归
            return null;
        }
        List<CommentViewVo> childrenCommentViewVos = this.getByParentId(parentComment.getId()).stream().map(i -> {
            //属性填充
            CommentViewVo commentViewVo = this.getCommentViewVo(blogId, authorId, userId, i);
            //子评论开始递归
            commentViewVo.setChildrenComments(searchChildrenComments(authorId, commentViewVo, blogId, userId));
            return commentViewVo;
        }).collect(Collectors.toList());
        return childrenCommentViewVos;
    }

    private List<Comment> selectByBlogId(Long blogId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getBlogId,blogId));
    }

    /**
     * 根据id删除评论
     * 若是父评论,则将它的子评论一并删除
     * @param id
     * @return
     */
    @Transactional
    public boolean removeCommentById(Long id) {
        Comment comment = baseMapper.selectById(id);
        //查询此评论是否含有子评论
        List<Comment> childrenComments = getByParentId(id);
        if(childrenComments.isEmpty()){
            //不含子评论
            return baseMapper.deleteById(id) > 0;
        }
        //含有子评论,将所有子评论的parent_id修改为-1
        childrenComments.forEach(c -> {
            Comment updateComment = new Comment();
            updateComment.setId(c.getId());
            updateComment.setParentId(-1L);
            updateComment.setUpdateTime(new Date());
            baseMapper.updateById(updateComment);
        });
        //删除父评论
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 删除评论
     * @param ids
     * @return
     */
    public Result removeCommentByIds(List<Long> ids) {
        if(ids.isEmpty()){
            return Result.fail().message("评论id不能为空");
        }
        //获取所有删除评论的子评论
        List<Comment> childrenComments = new ArrayList<>();
        ids.forEach(i -> {
            List<Comment> childrenComment = getByParentId(i);
            if(!childrenComment.isEmpty()){
                childrenComments.addAll(childrenComment);
            }
        });
        //将所有子评论的parent_id修改为-1
        if(!childrenComments.isEmpty()){
            childrenComments.forEach(i -> {
                Comment updateComment = new Comment();
                updateComment.setId(i.getId());
                updateComment.setParentId(-1L);
                updateComment.setUpdateTime(new Date());
                baseMapper.updateById(updateComment);
            });
        }
        return baseMapper.deleteBatchIds(ids) > 0 ? Result.ok().message("删除评论成功")
                : Result.fail().message("删除评论失败");
    }

    /**
     * 给评论点赞或取消点赞
     * @param commentId
     * @return
     */
    public Result likeComment(Long commentId) {
        Boolean isLiked = this.isLikedComment(commentId, UserInfoContext.get());
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        Long userId = UserInfoContext.get();
        String key = LIKE_COMMENT + commentId;
        Comment comment = new Comment();
        comment.setId(commentId);
        if(isLiked){
            //点过赞
            operations.remove(key, userId.toString());
            comment.setIsLiked(operations.size(key));
            Boolean likedSuccess = baseMapper.updateById(comment) > 0;
            return likedSuccess ? Result.ok().message("取消点赞成功") : Result.fail().message("取消点赞失败");
        }
        else {
            //未点赞
            operations.add(key, userId.toString());
            comment.setIsLiked(operations.size(key));
            Boolean likedSuccess = baseMapper.updateById(comment) > 0;
            return likedSuccess ? Result.ok().message("点赞成功") : Result.fail().message("点赞失败");
        }
    }

    /**
     * 查询当前用户是否已经点赞过该评论
     * @param commentId
     * @return
     */
    public Boolean isLikedComment(Long commentId, Long userId ) {
        if(Objects.nonNull(userId)){
            SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
            Boolean isLiked = operations.isMember(LIKE_COMMENT + commentId, userId.toString());
            return isLiked;
        }
        return false;
    }

    /**
     * 分页条件查询评论
     * @param page
     * @param size
     * @param commentQueryDto
     * @return
     */
    public Page<CommentVo> pageParamQuery(Long page, Long size, CommentQueryDto commentQueryDto) {
        Page<CommentVo> commentVoPage = new Page<>();
        //当前页
        commentVoPage.setCurrent(page);
        //分页大小
        commentVoPage.setSize(size);
        //计算索引偏移量
        List<CommentVo> commentVoList = baseMapper.selectCommentPage((page - 1) * size,size, commentQueryDto);
        //记录
        commentVoPage.setRecords(commentVoList);
        //总条数
        long total = baseMapper.selectCountParam(commentQueryDto);
        commentVoPage.setTotal(total);
        return commentVoPage;
    }


    /**
     * 根据父评论id查询子评论
     * @param id
     * @return
     */
    public List<Comment> getByParentId(Long id) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId,id);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 递归查找父评论下的子评论id
     * @param comment
     * @param ids
     */
    private void searchChildrenIds(Comment comment,List<Long> ids){
        //将父评论添加到ids中
        ids.add(comment.getId());
        //判断父评论下是否含有子评论
        List<Comment> childrenComments = getByParentId(comment.getId());
        if(!childrenComments.isEmpty()){
            //含有子评论.继续递归查找
            childrenComments.stream().forEach(c -> searchChildrenIds(c,ids));
        }
    }

    /**
     * 查询用户通过审核博客的评论数量
     * @param userId
     * @return
     */
    public Long getUserBlogsCommentNums(Long userId) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getAuthorId, userId)
                .eq(Blog::getStatus, 1)
                .select(Blog::getId);
        List<Blog> blogList = blogService.list(wrapper);
        if (blogList.isEmpty()) {
            return 0L;
        }
        List<Long> blogIds = blogList.stream().map(i -> i.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<Comment> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(Comment::getBlogId, blogIds);
        Integer count = baseMapper.selectCount(wrapper1);
        return Long.valueOf(count);
    }
}
