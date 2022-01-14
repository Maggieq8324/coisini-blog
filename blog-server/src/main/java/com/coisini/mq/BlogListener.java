package com.coisini.mq;


import com.coisini.config.RabbitMqConfig;
import com.coisini.mapper.BlogMapper;
import com.coisini.entity.Blog;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新博客的队列消费者
 *
 * @blame mqpearh
 */
//@Component
//@RabbitListener(queues = RabbitMqConfig.BLOG_QUEUE)
public class BlogListener {

    @Autowired
    private BlogMapper blogDao;

    @RabbitHandler
    public void updateBlog(Blog blog) {
        blogDao.updateBlog(blog);
    }
}
