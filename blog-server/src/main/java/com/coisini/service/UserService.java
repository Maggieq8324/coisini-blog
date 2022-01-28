package com.coisini.service;

import com.coisini.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @Description 用户接口
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
public interface UserService {

    /**
     * 登录
     * 返回token，用户名，用户角色
     * @param user
     * @return
     * @throws UsernameNotFoundException
     * @throws RuntimeException
     */
    Map<String, Object> login(User user) throws UsernameNotFoundException, RuntimeException;

    /**
     * 注册
     * @param userToAdd
     * @param mailCode
     * @throws RuntimeException
     */
    void register(User userToAdd, String mailCode) throws RuntimeException;

    /**
     * 从token中提取信息
     * @param authHeader
     * @return
     */
    UserDetails loadUserByToken(String authHeader);

    /**
     * 封禁或解禁用户
     * @param id
     * @param state
     */
    void updateUserState(Integer id, Integer state);

    /**
     * 创建管理员
     * @param user
     */
    void createAdmin(User user);

    /**
     * 修改用户密码
     * @param oldPassword
     * @param newPassword
     * @param code
     */
    void updateUserPassword(String oldPassword, String newPassword, String code);

    /**
     * 更新用户邮箱
     * @param newMail
     * @param oldMailCode
     * @param newMailCode
     */
    void updateUserMail(String newMail, String oldMailCode, String newMailCode);

    /**
     * 重置密码
     * @param userName
     * @param mailCode
     * @param newPassword
     */
    void forgetPassword(String userName, String mailCode, String newPassword);

    /**
     * 根据用户名分页搜索用户
     * @param userName
     * @param page
     * @param showCount
     * @return
     */
    List<User> searchUserByName(String userName, Integer page, Integer showCount);

    /**
     * 分页查询用户
     * @param page
     * @param showCount
     * @return
     */
    List<User> findUser(Integer page, Integer showCount);

    /**
     * 查询用户数
     * @return
     */
    Long getUserCount();

    /**
     * 查询用户邮箱
     * @return
     */
    String findUserMail();

    /**
     * 模糊查询用户名 返回记录数
     * @param userName
     * @return
     */
    Long getUserCountByName(String userName);

    /**
     * 将邮件和验证码发送到消息队列
     * @param mail
     */
    void sendMail(String mail);

    /**
     * 从redis中提取 验证码
     * @param mail
     * @return
     */
    String getMailCodeFromRedis(String mail);

    /**
     * 校验验证码是否正确
     * @param mail
     * @param code
     * @return
     */
    boolean checkMailCode(String mail, String code);

    /**
     * 根据用户名查询用户
     * @param name
     * @return
     */
    User findUserByName(String name);

    /**
     * 查询用户打赏码
     * @return
     */
    String findUserReward();

    /**
     * 更改用户打赏码
     * @param imgPath
     */
    void updateUserReward(String imgPath);

    /**
     * 退出登录
     * 删除redis中的key
     */
    void logout();

}
