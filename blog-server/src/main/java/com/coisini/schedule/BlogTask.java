package com.coisini.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coisini.config.RedisConfig;
import com.coisini.mapper.BlogMapper;
import com.coisini.mapper.TagMapper;
import com.coisini.entity.Blog;
import com.coisini.utils.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Configuration
@EnableScheduling
public class BlogTask {

    @Autowired
    private BlogMapper blogDao;

    @Autowired
    private TagMapper tagDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private Logger logger = LoggerUtil.loggerFactory(this.getClass());

    /**
     * 1000ms 1s
     * 10 min 执行一次
     * 博客任务
     */
    @Scheduled(fixedRate = 1000 * 60 * 30)
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
     *
     * @throws JsonProcessingException
     */
    public void updateRedisHotBlogList() throws JsonProcessingException {
        // 若缓存没有newbog 或 hotblog 就不进行删除过期博客的操作
        if (redisTemplate.hasKey(RedisConfig.REDIS_NEW_BLOG) && redisTemplate.hasKey(RedisConfig.REDIS_HOT_BLOG)) {

            List<String> hotBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT - 1);
            List<String> newBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, 0, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);

            // 判断热门博客的id是否存在于newblog中，如果不在newblog中 就删除此key
            // 避免删除同时在newblog 和 hotblog的id
            for (String blogId : hotBlogIds) {
                if (!newBlogIds.contains(blogId)) {
                    redisTemplate.delete(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                }
            }
        }

        // 删除键值
        redisTemplate.delete(RedisConfig.REDIS_HOT_BLOG);
        // 先查询数据库
        List<Blog> hotBlog = blogDao.findHotBlog(6);

        for (Blog blog : hotBlog) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
            // 向hot 集合中存 id
            String blogId = Integer.toString(blog.getId());

            redisTemplate.opsForList().rightPush(RedisConfig.REDIS_HOT_BLOG, blogId);
            // 存具体的blog对象

            redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));

        }
    }


    /**
     * 更新最新博客列表
     *
     * @throws JsonProcessingException
     */
    public void updateRedisNewBlogList() throws JsonProcessingException {
        // 若缓存没有newbog 或 hotblog 就不进行删除过期博客的操作
        if (redisTemplate.hasKey(RedisConfig.REDIS_NEW_BLOG) && redisTemplate.hasKey(RedisConfig.REDIS_HOT_BLOG)) {

            List<String> hotBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_HOT_BLOG, 0, RedisConfig.REDIS_HOT_BLOG_COUNT - 1);
            List<String> newBlogIds = redisTemplate.opsForList().range(RedisConfig.REDIS_NEW_BLOG, 0, RedisConfig.REDIS_NEW_BLOG_COUNT - 1);


            for (String blogId : newBlogIds) {
                if (!hotBlogIds.contains(blogId)) {
                    redisTemplate.delete(RedisConfig.REDIS_BLOG_PREFIX + blogId);
                }
            }
        }

        // 删除键值
        redisTemplate.delete(RedisConfig.REDIS_NEW_BLOG);
        // 先查询数据库
        List<Blog> newBlog = blogDao.findHomeBlog(0, 10);

        for (Blog blog : newBlog) {
            blog.setTags(tagDao.findTagByBlogId(blog.getId()));
            // 向hot 集合中存 id
            String blogId = Integer.toString(blog.getId());

            redisTemplate.opsForList().rightPush(RedisConfig.REDIS_NEW_BLOG, blogId);
            // 存具体的blog对象
            redisTemplate.opsForValue().set(RedisConfig.REDIS_BLOG_PREFIX + blogId, objectMapper.writeValueAsString(blog));

        }
    }
}
