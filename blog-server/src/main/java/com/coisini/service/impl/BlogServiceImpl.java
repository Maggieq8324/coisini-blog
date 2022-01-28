package com.coisini.service.impl;

import com.coisini.config.ImgUploadConfig;
import com.coisini.config.RabbitMqConfig;
import com.coisini.config.RedisConfig;
import com.coisini.entity.Blog;
import com.coisini.entity.User;
import com.coisini.mapper.BlogMapper;
import com.coisini.mapper.TagMapper;
import com.coisini.mapper.UserMapper;
import com.coisini.schedule.BlogTask;
import com.coisini.service.BlogService;
import com.coisini.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description 博客实现类
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogDao;

    private final UserMapper userDao;

    private final TagMapper tagDao;

    private final FormatUtil formatUtil;

    private final FileUtil fileUtil;

    private final UUIDUtil uuidUtil;

    private final JwtTokenUtil jwtTokenUtil;

    private final DateUtil dateUtil;

    private final BlogTask blogTask;

    private final ObjectMapper objectMapper;

    private final ImgUploadConfig imgUploadConfig;

    private final HttpServletRequest request;

    private final RedisTemplate<String, String> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 返回的首页博客列表内容的最大字符数
     */
    private static final int MAX_BODY_CHAR_COUNT = 150;

    /**
     * 保存图片,返回url
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public synchronized String saveImg(MultipartFile file) throws IOException {
        // TODO 获取图片格式/后缀
        String format = formatUtil.getFileFormat(file.getOriginalFilename());
        // TODO 获取图片保存路径
        String savePath = fileUtil.getSavePath();
        // TODO 存储已满
        if (!formatUtil.checkStringNull(savePath)) {
            throw new IOException("存储已满 请联系管理员");
        }
        // TODO 保存图片
        String fileName = uuidUtil.generateUUID() + format;
        File diskFile = new File(savePath + "/" + fileName);
        file.transferTo(diskFile);
        // TODO 将硬盘路径转换为url，返回
        return imgUploadConfig.getStaticAccessPath().replaceAll("\\*", "") + fileName;
    }

    /**
     * 保存博客
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBlog(String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));

        for (Integer tagId : tagIds) {
            // TODO 通过标签id检查标签是否属于该用户
            if (!tagDao.findTagById(tagId).getUser().getId().equals(user.getId())) {
                throw new RuntimeException();
            }
        }

        Blog blog = new Blog();
        // TODO 博客用户
        blog.setUser(user);
        // TODO 浏览量
        blog.setBlogViews(0);
        // TODO 评论数
        blog.setDiscussCount(0);
        // TODO 标题
        blog.setTitle(blogTitle);
        // TODO 内容
        blog.setBody(blogBody);
        // TODO 1 正常状态
        blog.setState(1);
        // TODO 发布时间
        blog.setTime(dateUtil.getCurrentDate());
        blogDao.saveBlog(blog);

        for (Integer tagId : tagIds) {
            // TODO 保存该博客的标签
            blogDao.saveBlogTag(blog.getId(), tagId);
        }

        // TODO 删除博客归档的缓存
        redisTemplate.delete(RedisConfig.REDIS_STATISTICAL);
        // TODO 移除 最后一位
        redisTemplate.opsForList().rightPop(RedisConfig.REDIS_NEW_BLOG);
        // TODO 获取标签名
        blog.setTags(tagDao.findTagByBlogId(blog.getId()));
        // TODO 存入newblog 的左边第一位
        redisTemplate.opsForList().leftPush(RedisConfig.REDIS_NEW_BLOG, blog.getId().toString());
        // TODO user隐藏相关字段
        blog.getUser().setPassword(null);
        blog.getUser().setMail(null);
        blog.getUser().setState(null);
        redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blog.getId(), objectMapper.writeValueAsString(blog));
    }

    /**
     * 根据id查询博客以及博客标签
     * 正常状态
     * @param blogId
     * @param isHistory
     * @return
     * @throws IOException
     */
    public Blog findBlogById(Integer blogId, boolean isHistory) throws IOException {
        // TODO 查询缓存
        String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
        Blog blog = null;
        if (null != blogJson) {
            // TODO 有缓存
            blog = objectMapper.readValue(blogJson, Blog.class);
        } else {
            blog = blogDao.findBlogById(blogId);
            if (blog == null) {
                throw new RuntimeException("博客不存在");
            }
            blog.setTags(tagDao.findTagByBlogId(blogId));
        }

        // TODO 历史查看过
        if (isHistory) {
            // TODO 直接返回 浏览量不增加
            return blog;
        } else {
            // TODO 浏览量 + 1
            blog.setBlogViews(blog.getBlogViews() + 1);

            if (null != blogJson) {
                // TODO 有缓存 同步操作redis
                redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));
                // TODO 异步操作mysql 增加浏览量
                rabbitTemplate.convertAndSend(RabbitMqConfig.BLOG_QUEUE, blog);
            } else {
                // TODO 没有缓存 同步操作mysql
                blogDao.updateBlog(blog);
            }
        }

        return blog;
    }

    /**
     * 根据用户查询博客以及标签
     * 正常状态
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    public List<Blog> findBlogByUser(Integer page, Integer showCount) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        List<Blog> blogs = blogDao.findBlogByUserId(user.getId(), (page - 1) * showCount, showCount);

        for (Blog blog : blogs) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 查询该用户的博客数量
     * 正常状态
     * @return
     */
    public Long getBlogCountByUser() {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return blogDao.getBlogCountByUserId(user.getId());
    }

    /**
     * 查询主页所有博客数量
     * 正常状态
     * @return
     */
    public Long getHomeBlogCount() {
        return blogDao.getHomeBlogCount();
    }

    /**
     * 查询主页博客
     * 正常状态
     * @param page      页码
     * @param showCount 显示条数
     * @return
     * @throws IOException
     */
    public List<Blog> findHomeBlog(Integer page, Integer showCount) throws IOException {
        // TODO mysql 分页中的开始位置
        int start = (page - 1) * showCount;

        // TODO 没有缓存 需查询mysql 设置缓存
        System.out.println(redisTemplate.hasKey(RedisConfig.REDIS_NEW_BLOG));
        if (!redisTemplate.hasKey(RedisConfig.REDIS_NEW_BLOG)) {

            List<Blog> blogsFromMysql = blogDao.findHomeBlog(0, RedisConfig.REDIS_NEW_BLOG_COUNT);
            for (Blog blog : blogsFromMysql) {
                blog.setTags(tagDao.findTagByBlogId(blog.getId()));
                String blogId = Integer.toString(blog.getId());

                redisTemplate.opsForList().rightPush(RedisConfig.REDIS_NEW_BLOG, blogId);
                redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));
            }
        }

        // TODO 返回的blog列表
        List<Blog> blogs = new LinkedList<>();

        if (start >= RedisConfig.REDIS_NEW_BLOG_COUNT) {
            // TODO 开始位置大于缓存数量 即查询范围不在缓存内 查询mysql 且不设置缓存
            blogs.addAll(blogDao.findHomeBlog(start, showCount));
            for (Blog blog : blogs) {

                blog.setTags(tagDao.findTagByBlogId(blog.getId()));

            }
        } else if (start + showCount > RedisConfig.REDIS_NEW_BLOG_COUNT) {
            // TODO 查询范围部分在缓存内
            List<String> redisBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, start, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);
            for (String blogId : redisBlogIds) {
                String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                Blog blog = objectMapper.readValue(blogJson, Blog.class);
                blogs.add(blog);
            }

            blogs.addAll(blogDao.findHomeBlog(RedisConfig.REDIS_NEW_BLOG_COUNT, showCount - (RedisConfig.REDIS_NEW_BLOG_COUNT - start)));
        } else {
            // TODO 查询范围全在缓存
            List<String> redisBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, start, start - 1 + showCount);
            for (String blogId : redisBlogIds) {
                String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                Blog blog = objectMapper.readValue(blogJson, Blog.class);
                blogs.add(blog);
            }
        }

        // TODO 截取前150个字符 减少网络io
        for (Blog blog : blogs) {
            String body = blog.getBody();
            if (body.length() > BlogServiceImpl.MAX_BODY_CHAR_COUNT) {

                blog.setBody(body.substring(0, BlogServiceImpl.MAX_BODY_CHAR_COUNT));
            }
        }

        return blogs;
    }

    /**
     * 查询热门博客
     * 正常状态
     * @return
     * @throws IOException
     */
    public List<Blog> findHotBlog() throws IOException {
        // TODO 查询redis 热门博客id set
        if (redisTemplate.hasKey(RedisConfig.REDIS_HOT_BLOG)) {

            // TODO 有缓存
            List<Blog> blogList = new ArrayList<>(6);

            List<String> blogIdList = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT);

            for (String blogId : blogIdList) {
                // TODO 根据缓存获取 blog
                String blogJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                // TODO 返回 缓存
                Blog blog = objectMapper.readValue(blogJson, Blog.class);
                blogList.add(blog);
            }

            return blogList;
        } else {
            // TODO redis中没有缓存 查询 mysql
            // TODO 通过定时任务进行热门博客列表更新
            return blogDao.findHotBlog(6);
        }
    }

    /**
     * 搜索博客
     * 正常状态
     * @param searchText
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> searchBlog(String searchText, Integer page, Integer showCount) {
        List<Blog> blogs = blogDao.searchBlog(searchText, (page - 1) * showCount, showCount);
        for (Blog blog : blogs) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 符合关键词的博客数量
     * 正常状态
     * @param searchText
     * @return
     */
    public Long getSearchBlogCount(String searchText) {
        return blogDao.getSearchBlogCount(searchText);
    }

    /**
     * 查询所有博客
     * 正常状态
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> findAllBlog(Integer page, Integer showCount) {
        return blogDao.findAllBlog((page - 1) * showCount, showCount);
    }

    /**
     * 修改博客
     * @param blogId
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBlog(Integer blogId, String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogDao.findBlogById(blogId);
        if (!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("无权限修改");
        }

        blog.setTitle(blogTitle);
        blog.setBody(blogBody);
        blogDao.updateBlog(blog);
        // TODO 删除原有标签
        tagDao.deleteTagByBlogId(blog.getId());
        // TODO 保存新标签
        for (Integer tagId : tagIds) {
            // TODO 保存该博客的标签
            blogDao.saveBlogTag(blog.getId(), tagId);
        }
        // TODO 数据 存在于缓存中
        if (redisTemplate.hasKey(RedisConfig.REDIS_BLOG_PREFIX + blogId)) {
            blog.setTags(tagDao.findTagByBlogId(blogId));
            redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));
        }
    }

    /**
     * 用户删除博客
     * @param blogId
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlog(Integer blogId) throws JsonProcessingException {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogDao.findBlogById(blogId);

        if (!user.getId().equals(blog.getUser().getId())) {
            throw new RuntimeException("无权限删除");
        }

        // TODO 更改博客状态
        blog.setState(0);
        blogDao.updateBlog(blog);

        // TODO 级联删除blog_tag
        tagDao.deleteTagByBlogId(blogId);
        // TODO 缓存中有数据
        if (redisTemplate.hasKey(RedisConfig.REDIS_BLOG_PREFIX + blogId)) {
            // TODO 更新最新博客列表
            blogTask.updateRedisNewBlogList();

        }
        // TODO 删除博客归档信息
        redisTemplate.delete(RedisConfig.REDIS_STATISTICAL);
    }

    /**
     * 管理员删除博客
     * @param blogId
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteBlog(Integer blogId) throws JsonProcessingException {
        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setState(0);
        // TODO 更改博客状态
        blogDao.updateBlog(blog);
        // TODO 级联删除blog_tag
        tagDao.deleteTagByBlogId(blogId);

        // TODO 缓存中有数据
        if (redisTemplate.hasKey(RedisConfig.REDIS_BLOG_PREFIX + blogId)) {
            List<String> newBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, 0, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);
            List<String> hotBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT - 1);

            if (newBlogIds.contains(blogId + "")) {
                // TODO 更新最新博客列表
                blogTask.updateRedisNewBlogList();
            }

            if (hotBlogIds.contains(blogId + "")) {
                // TODO 更新热门博客列表
                blogTask.updateRedisHotBlogList();
            }
        }

        // TODO 删除博客归档信息
        redisTemplate.delete(RedisConfig.REDIS_STATISTICAL);
    }

    /**
     * 符合关键字的博客数量
     * 所有状态
     * @param searchText
     * @return
     */
    public Long getSearchAllBlogCount(String searchText) {
        return blogDao.getSearchAllBlogCount(searchText);
    }

    /**
     * 搜索博客
     * 所有状态
     * @param searchText
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> searchAllBlog(String searchText, Integer page, Integer showCount) {
        List<Blog> blogs = blogDao.searchAllBlog(searchText, (page - 1) * showCount, showCount);
        return blogs;
    }

    /**
     * 按月份归档博客
     * 正常状态
     * @return
     */
    public List<Map<String, Object>> statisticalBlogByMonth() throws IOException {

        if (redisTemplate.hasKey(RedisConfig.REDIS_STATISTICAL)) {
            String mapJson = redisTemplate.opsForValue().get(RedisConfig.REDIS_STATISTICAL);
			List<Map<String, Object>> list = objectMapper.readValue(mapJson, List.class);
            return list;
        } else {
            // TODO 设置缓存
            List<Map<String, Object>> maps = blogDao.statisticalBlogByMonth(6);
            redisTemplate.opsForValue().set(RedisConfig.REDIS_STATISTICAL, objectMapper.writeValueAsString(maps));
            return maps;
        }
    }

    /**
     * 查询博客记录数
     * 所有状态
     * @return
     */
    public Long getAllBlogCount() {
        return blogDao.getAllBlogCount();
    }
}
