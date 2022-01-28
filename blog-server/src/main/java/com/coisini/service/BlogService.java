package com.coisini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.coisini.entity.Blog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

/**
 * @Description 博客接口
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Service
public interface BlogService {

    /**
     * 保存图片,返回url
     * @param file
     * @return
     * @throws IOException
     */
    String saveImg(MultipartFile file) throws IOException;

    /**
     * 保存博客
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     * @throws JsonProcessingException
     */
    void saveBlog(String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException;

    /**
     * 根据id查询博客以及博客标签
     * 正常状态
     * @param blogId
     * @param isHistory
     * @return
     * @throws IOException
     */
    Blog findBlogById(Integer blogId, boolean isHistory) throws IOException;

    /**
     * 根据用户查询博客以及标签
     * 正常状态
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    List<Blog> findBlogByUser(Integer page, Integer showCount);

    /**
     * 查询该用户的博客数量
     * 正常状态
     * @return
     */
    Long getBlogCountByUser();

    /**
     * 查询主页所有博客数量
     * 正常状态
     * @return
     */
    Long getHomeBlogCount();

    /**
     * 查询主页博客
     * 正常状态
     * @param page      页码
     * @param showCount 显示条数
     * @return
     * @throws IOException
     */
    List<Blog> findHomeBlog(Integer page, Integer showCount) throws IOException;

    /**
     * 查询热门博客
     * 正常状态
     * @return
     * @throws IOException
     */
    List<Blog> findHotBlog() throws IOException;

    /**
     * 搜索博客
     * 正常状态
     * @param searchText
     * @param page
     * @param showCount
     * @return
     */
    List<Blog> searchBlog(String searchText, Integer page, Integer showCount);

    /**
     * 符合关键词的博客数量
     * 正常状态
     * @param searchText
     * @return
     */
    Long getSearchBlogCount(String searchText);

    /**
     * 查询所有博客
     * 正常状态
     * @param page
     * @param showCount
     * @return
     */
    List<Blog> findAllBlog(Integer page, Integer showCount);

    /**
     * 修改博客
     * @param blogId
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     * @throws JsonProcessingException
     */
    void updateBlog(Integer blogId, String blogTitle, String blogBody, Integer[] tagIds) throws JsonProcessingException;

    /**
     * 用户删除博客
     * @param blogId
     * @throws JsonProcessingException
     */
    void deleteBlog(Integer blogId) throws JsonProcessingException;

    /**
     * 管理员删除博客
     * @param blogId
     * @throws JsonProcessingException
     */
    void adminDeleteBlog(Integer blogId) throws JsonProcessingException;

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
     * @param page
     * @param showCount
     * @return
     */
    List<Blog> searchAllBlog(String searchText, Integer page, Integer showCount);

    /**
     * 按月份归档博客
     * 正常状态
     * @return
     */
    List<Map<String, Object>> statisticalBlogByMonth() throws IOException;

    /**
     * 查询博客记录数
     * 所有状态
     * @return
     */
    Long getAllBlogCount();
}
