package com.coisini.service;

import com.coisini.entity.User;
import org.springframework.stereotype.Service;

/**
 * @Description 登陆相关接口
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
public interface LoginService {

    /**
     * 保存登录信息
     * @param user
     */
    void saveLoginInfo(User user);

}
