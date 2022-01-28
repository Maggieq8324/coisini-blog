package com.coisini.mapper;

import com.coisini.entity.Discuss;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Description 评论Mapper
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Repository
@Mapper
public interface DiscussMapper {

    /**
     * 保存回复
     * @param discuss
     */
    void saveDiscuss(Discuss discuss);

    /**
     * 根据id查询评论
     * @param discussId
     * @return
     */
    Discuss findDiscussById(Integer discussId);

    /**
     * 根据id删除评论
     * @param discussId
     */
    void deleteDiscussById(Integer discussId);

    /**
     * 查询博客下的评论
     * @param blogId
     * @return
     */
    List<Discuss> findDiscussByBlogId(@Param("blogId") Integer blogId, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 获取博客下评论数量
     * @param blogId
     * @return
     */
    Long getDiscussCountByBlogId(Integer blogId);

    /**
     * 获取最新count 条评论
     * @param count
     * @return
     */
    List<Discuss> findNewDiscuss(Integer count);

    /**
     * 查询
     * @param userId    用户id
     * @param count 显示数量
     * @return
     */
    List<Discuss> findUserNewDiscuss(@Param("userId") Integer userId,@Param("count") Integer count);

}
