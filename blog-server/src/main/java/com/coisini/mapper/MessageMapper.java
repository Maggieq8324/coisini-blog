package com.coisini.mapper;

import com.coisini.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Description 留言Mapper
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Repository
@Mapper
public interface MessageMapper {

    /**
     * 根据name查询留言
     * @param name
     * @return
     */
    Message findMessageByName(String name);

    /**
     * 保存留言
     * @param message
     */
    void saveMessage(Message message);

    /**
     * 根据id删除留言
     * @param messageId
     */
    void deleteMessageById(Integer messageId);

    /**
     * 获取留言数量
     * @return
     */
    Long getMessageCount();

    /**
     * 分页查询留言
     * @param start
     * @param showCount
     * @return
     */
    List<Message> findMessage(@Param("start") Integer start, @Param("showCount") Integer showCount);

}
