package com.coisini.mapper;

import com.coisini.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Description 回复Mapper
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Repository
@Mapper
public interface ReplyMapper {
    /**
     * 保存回复
     * @param reply
     */
    void saveReply(Reply reply);

    /**
     * 根据id查询回复
     * @param replyId
     * @return
     */
    Reply findReplyById(Integer replyId);

    /**
     * 根据id删除回复
     * @param replyId
     */
    void deleteReplyById(Integer replyId);

    /**
     * 根据评论id查询回复
     * @param id
     * @return
     */
    List<Reply> findReplyByDiscussId(Integer id);

    /**
     * 根据评论id删除回复
     * @param discussId
     * @return 受影响行数
     */
    Integer deleteReplyByDiscussId(Integer discussId);

}
