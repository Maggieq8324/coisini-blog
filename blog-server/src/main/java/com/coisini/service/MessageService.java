package com.coisini.service;

import com.coisini.entity.Message;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 留言接口
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
public interface MessageService {

    /**
     * 留言
     * @param messageBody
     */
    void saveMessage(String messageBody);

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
     * @param page
     * @param showCount
     * @return
     */
    List<Message> findMessage(Integer page, Integer showCount);

}
