package com.coisini.service;

import com.coisini.mapper.MessageMapper;
import com.coisini.mapper.UserMapper;
import com.coisini.entity.Message;
import com.coisini.entity.User;
import com.coisini.utils.DateUtil;
import com.coisini.utils.FormatUtil;
import com.coisini.utils.JwtTokenUtil;
import com.coisini.utils.RequestUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageDao;

    @Autowired
    private UserMapper userDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FormatUtil formatUtil;

    @Autowired
    private DateUtil dateUtil;

    /**
     * 留言
     *
     * @param messageBody
     */
    public void saveMessage(String messageBody) {

        String name = null;
        try {
            User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));//已登录
            name = user.getName();
        } catch (NullPointerException e) { //token 校验失败 游客身份
            name = requestUtil.getIpAddress(request);
        }
        //查询此ip/name 是否留言过
        if (messageDao.findMessageByName(name) != null) {
            throw new RuntimeException("你已留过言");
        }

        Message message = new Message();
        message.setName(name);
        message.setBody(messageBody);
        message.setTime(dateUtil.getCurrentDate());
        messageDao.saveMessage(message);
    }

    /**
     * 根据id删除留言
     *
     * @param messageId
     */
    public void deleteMessageById(Integer messageId) {
        messageDao.deleteMessageById(messageId);
    }

    /**
     * 获取留言数量
     *
     * @return
     */
    public Long getMessageCount() {
        return messageDao.getMessageCount();
    }

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
