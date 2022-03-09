package com.coisini.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coisini.config.RedisConfig;
import com.coisini.mapper.BlogMapper;
import com.coisini.mapper.TagMapper;
import com.coisini.entity.Blog;
import com.coisini.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @Description 博客定时任务
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Component
@Configuration
@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogTask {

    private final BlogMapper blogDao;

    private final TagMapper tagDao;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private Logger logger = LoggerUtil.loggerFactory(this.getClass());

    /**
     * 1000ms 1s
     * 60 min 执行一次
     * 博客任务
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    private void blogTask() {
        try {
            updateRedisHotBlogList();
            logger.info("热门博客列表更新成功");
            updateRedisNewBlogList();
            logger.info("最新博客列表更新成功");
        } catch (JsonProcessingException e) {
            logger.error("热门博客列表更新失败");
        }
    }

    /**
     * 更新热门博客列表
     * @throws JsonProcessingException
     */
    public void updateRedisHotBlogList() throws JsonProcessingException {
        // TODO 若缓存没有newBlog 或 hotBlog 就不进行删除过期博客的操作
        if (hasNewBlogOrHotBlog()) {
            List<String> hotBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT - 1);
            List<String> newBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, 0, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);

            // TODO 判断热门博客的id是否存在于newBlog中，如果不在newBlog中 就删除此key
            // TODO 避免删除同时在newBlog 和 hotBlog的id
            for (String blogId : hotBlogIds) {
                if (!newBlogIds.contains(blogId)) {
                    redisTemplate.delete(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                }
            }
        }

        // TODO 删除键值
        redisTemplate.delete(RedisConfig.REDIS_HOT_BLOG);
        // TODO 查询数据库
        List<Blog> hotBlog = blogDao.findHotBlog(6);

        // TODO 更新redis
        updateBlogToRedis(hotBlog, RedisConfig.REDIS_HOT_BLOG);
    }

    /**
     * 更新最新博客列表
     * @throws JsonProcessingException
     */
    public void updateRedisNewBlogList() throws JsonProcessingException {
        // TODO 若缓存没有newBlog 或 hotBlog 就不进行删除过期博客的操作
        if (hasNewBlogOrHotBlog()) {
            List<String> hotBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT - 1);
            List<String> newBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, 0, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);

            for (String blogId : newBlogIds) {
                if (!hotBlogIds.contains(blogId)) {
                    redisTemplate.delete(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                }
            }
        }

        // TODO 删除键值
        redisTemplate.delete(RedisConfig.REDIS_NEW_BLOG);
        // TODO 查询数据库
        List<Blog> newBlog = blogDao.findHomeBlog(0, 10);

        // TODO 更新redis
        updateBlogToRedis(newBlog, RedisConfig.REDIS_NEW_BLOG);
    }

    /**
     * 是否缓存有 newBlog 或 hotBlog
     * @return
     */
    private boolean hasNewBlogOrHotBlog() {
        return Boolean.TRUE.equals(redisTemplate.hasKey(RedisConfig.REDIS_NEW_BLOG)) && Boolean.TRUE.equals(redisTemplate.hasKey(RedisConfig.REDIS_HOT_BLOG));
    }

    /**
     * 更新Redis中的blog
     * @param blogs
     * @param blogTitle
     * @throws JsonProcessingException
     */
    private void updateBlogToRedis(List<Blog> blogs, String blogTitle) throws JsonProcessingException {
        for (Blog blog : blogs) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
            // TODO 向 hot 集合中存 id
            String blogId = Integer.toString(blog.getId());

            redisTemplate.opsForList().rightPush(blogTitle, blogId);
            // TODO 存具体的blog对象
            redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));
        }
    }


}
