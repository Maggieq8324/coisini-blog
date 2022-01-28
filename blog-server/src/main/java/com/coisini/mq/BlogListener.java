package com.coisini.mq;

import com.coisini.config.RabbitMqConfig;
import com.coisini.mapper.BlogMapper;
import com.coisini.entity.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 更新博客的队列消费者
 * @author coisini
 * @date Jan 19, 2021
 * @version 2.0
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RabbitListener(queues = RabbitMqConfig.BLOG_QUEUE)
public class BlogListener {

    private final BlogMapper blogDao;

    @RabbitHandler
    public void updateBlog(Blog blog) {
        blogDao.updateBlog(blog);
    }
}
