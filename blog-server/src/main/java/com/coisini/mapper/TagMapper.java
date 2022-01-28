package com.coisini.mapper;

import com.coisini.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Description 标签Mapper
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Repository
@Mapper
public interface TagMapper {

    /**
     * 新增标签
     * @param tag
     */
    void saveTag(Tag tag);

    /**
     * 根据tag id更改标签
     * @param tag
     */
    void updateTagName(Tag tag);

    /**
     *根据tag id删除标签
     * @param id
     */
    void deleteTag(Integer id);

    /**
     * 根据id查询标签
     * @param tagId
     * @return
     */
    Tag findTagById(Integer tagId);

    /**
     * 查询该user id下的所有标签
     * @param userId
     */
    List<Tag> findTagByUserId(Integer userId);

    /**
     * 查询博客的所有标签
     * @param blogId
     * @return
     */
    List<Tag> findTagByBlogId(Integer blogId);

    /**
     * 根据博客id删除标签
     * @param blogId
     */
    void deleteTagByBlogId(Integer blogId);

    /**
     * 根据tagId删除blog_tag 的记录
     * @param tagId
     */
    void deleteTagByTagId(Integer tagId);

    /**
     * 根据标签名查询标签
     * @return
     */
    Tag findTagByTagName(String tagName);
}
