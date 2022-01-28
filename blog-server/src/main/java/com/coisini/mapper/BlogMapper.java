package com.coisini.mapper;

import com.coisini.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * @Description 博客Mapper
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Repository
@Mapper
public interface BlogMapper {

    /**
     * 保存博客
     * @param blog
     */
    void saveBlog(Blog blog);

    /**
     * 保存博客标签
     * @param blogId
     * @param tagId
     */
    void saveBlogTag(@Param("blogId") Integer blogId, @Param("tagId") Integer tagId);

    /**
     * 根据id查询博客
     * @param blogId
     * @return
     */
    Blog findBlogById(Integer blogId);

    /**
     * 根据用户id查询博客
     * @param id
     * @return
     */
    List<Blog> findBlogByUserId(@Param("id") Integer id, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 查询该用户的博客数量
     * 正常状态
     * @return
     */
    Long getBlogCountByUserId(Integer id);

    /**
     * 查询主页博客数量
     * @return
     */
    Long getHomeBlogCount();

    /**
     * 查询主页博客
     * @param start
     * @param showCount
     * @return
     */
    List<Blog> findHomeBlog(@Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 查询热门博客
     * @param count 显示数量
     * @return
     */
    List<Blog> findHotBlog(Integer count);

    /**
     * 搜索博客标题，内容
     * @param searchText
     * @param start
     * @param showCount
     * @return
     */
    List<Blog> searchBlog(@Param("searchText") String searchText,
                          @Param("start") Integer start,
                          @Param("showCount") Integer showCount);

    /**
     * 查询所有博客
     * @param start
     * @param showCount
     * @return
     */
    List<Blog> findAllBlog(@Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 符合关键词的博客数量
     * @param searchText
     * @return
     */
    Long getSearchBlogCount(String searchText);

    /**
     * 符合关键字的博客数量
     * 所有状态
     * @param searchText
     * @return
     */
    Long getSearchAllBlogCount(String searchText);

    /**
     * 搜索博客
     * 所有状态
     * @param searchText
     * @param start
     * @param showCount
     * @return
     */
    List<Blog> searchAllBlog(@Param("searchText") String searchText, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 按月份归档博客
     * @param count 最近几个月
     * @return month 月
     * year 年
     * count 数量
     */
    List<Map<String,Object>> statisticalBlogByMonth(Integer count);

    /**
     * 查询此标签下是否有博客
     * @param tagId
     * @return
     */
    Integer findBlogCountByTagId(Integer tagId);

    /**
     * 查询博客记录数
     * 所有状态
     * @return
     */
    Long getAllBlogCount();

    /**
     * 根据博客id更新博客
     *
     * @param blog
     */
    void updateBlog(Blog blog);

}
