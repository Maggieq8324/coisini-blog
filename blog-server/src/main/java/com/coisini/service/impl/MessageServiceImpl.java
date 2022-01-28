package com.coisini.service.impl;

import com.coisini.entity.Message;
import com.coisini.entity.User;
import com.coisini.mapper.MessageMapper;
import com.coisini.mapper.UserMapper;
import com.coisini.service.MessageService;
import com.coisini.utils.DateUtil;
import com.coisini.utils.FormatUtil;
import com.coisini.utils.JwtTokenUtil;
import com.coisini.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 留言实现类
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageDao;

    private final UserMapper userDao;

    private final JwtTokenUtil jwtTokenUtil;

    private final RequestUtil requestUtil;

    private final HttpServletRequest request;

    private final FormatUtil formatUtil;

    private final DateUtil dateUtil;

    /**
     * 留言
     * @param messageBody
     */
    @Override
    public void saveMessage(String messageBody) {

        String name = null;
        try {
            User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));//已登录
            name = user.getName();
        } catch (NullPointerException e) { //token 校验失败 游客身份
            name = requestUtil.getIpAddr(request);
        }

        /* 20211231 去除已留言限制 */
        //查询此ip/name 是否留言过
//        if (messageDao.findMessageByName(name) != null) {
//            throw new RuntimeException("你已留过言");
//        }

        Message message = new Message();
        message.setName(name);
        message.setBody(messageBody);
        message.setTime(dateUtil.getCurrentDate());
        messageDao.saveMessage(message);
    }

    /**
     * 根据id删除留言
     * @param messageId
     */
    @Override
    public void deleteMessageById(Integer messageId) {
        messageDao.deleteMessageById(messageId);
    }

    /**
     * 获取留言数量
     * @return
     */
    @Override
    public Long getMessageCount() {
        return messageDao.getMessageCount();
    }

    /**
     * 分页查询留言
     * @param page
     * @param showCount
     * @return
     */
    @Override
    public List<Message> findMessage(Integer page, Integer showCount) {
        List<Message> messages = messageDao.findMessage((page - 1) * showCount, showCount);
        for (Message message : messages) {
            String ip = message.getName();
            if (formatUtil.checkIP(ip)) {//该name是ip
                //保留ip 前16位 ( [127.0].0.1 ) [] 内为前16位
                String[] subStrs = ip.split("\\.");
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < subStrs.length && i < 2; i++) {
                    buffer.append(subStrs[i]).append(".");
                }
                buffer.append("*.*");
                message.setName(buffer.toString());
            }
        }

        return messages;
    }
}
