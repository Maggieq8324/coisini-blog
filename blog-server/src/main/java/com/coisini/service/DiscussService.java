package com.coisini.service;

import com.coisini.entity.Discuss;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 评论接口
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
public interface DiscussService {

    /**
     * 发布评论
     * 博客评论数加1
     * @param discussBody
     * @param blogId
     */
    void saveDiscuss(String discussBody, Integer blogId);

    /**
     * 删除评论
     * 级联删除评论下的所有回复
     * 博客评论数 - (评论数+回复数)
     * @param discussId
     */
    void deleteDiscuss(Integer discussId);

    /**
     * 管理员删除评论
     * 博客评论数-1
     * @param discussId
     */
    void adminDeleteDiscuss(Integer discussId);

    /**
     * 根据博客id查询 该博客下的评论及回复
     *
     * @param blogId
     * @return
     */
    List<Discuss> findDiscussByBlogId(Integer blogId, Integer page, Integer showCount);

    /**
     * 获取博客下评论数量
     *
     * @param blogId
     * @return
     */
    Long getDiscussCountByBlogId(Integer blogId);

    /**
     * 获取最新六条评论
     *
     * @return
     */
    List<Discuss> findNewDiscuss();

    /**
     * 获取用户发布的所有博客下的评论
     * @return
     */
    List<Discuss> findUserNewDiscuss();

}

