package com.coisini.service;

import org.springframework.stereotype.Service;

/**
 * @Description 回复接口
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
public interface ReplyService {

    /**
     * 保存回复
     * @param discussId
     * @param replyBody
     * @param rootId    可为null
     */
    void saveReply(Integer discussId, String replyBody, Integer rootId);

    /**
     * 删除回复
     * @param replyId
     */
    void deleteReply(Integer replyId);

    /**
     * 管理员删除回复
     * @param replyId
     */
    void adminDeleteReply(Integer replyId);

}
