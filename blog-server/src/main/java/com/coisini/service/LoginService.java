package com.coisini.service;

import com.coisini.mapper.LoginMapper;
import com.coisini.mapper.UserMapper;
import com.coisini.entity.Login;
import com.coisini.entity.User;
import com.coisini.utils.DateUtil;
import com.coisini.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service
public class LoginService {

    @Autowired
    private LoginMapper loginDao;

    @Autowired
    private UserMapper userDao;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private RequestUtil requestUtil;

    /**
     * 保存登录信息
     *
     * @param user
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveLoginInfo(User user) {

        user = userDao.findUserByName(user.getName());
        Login login = new Login();
        login.setUser(user);//绑定用户
        login.setIp(requestUtil.getIpAddress(request));//获取操作ip
        login.setTime(dateUtil.getCurrentDate());//操作时间
        loginDao.updateLogin(login);

    }

}
