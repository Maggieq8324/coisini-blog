package com.coisini.service.impl;

import com.coisini.entity.Login;
import com.coisini.entity.User;
import com.coisini.mapper.LoginMapper;
import com.coisini.mapper.UserMapper;
import com.coisini.service.LoginService;
import com.coisini.utils.DateUtil;
import com.coisini.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description 登陆相关实现类
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginDao;

    private final UserMapper userDao;

    private final HttpServletRequest request;

    private final DateUtil dateUtil;

    private final RequestUtil requestUtil;

    /**
     * 保存登录信息
     * @param user
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLoginInfo(User user) {
        user = userDao.findUserByName(user.getName());
        Login login = new Login();
        login.setUser(user);//绑定用户
        login.setIp(requestUtil.getIpAddress(request));//获取操作ip
        login.setTime(dateUtil.getCurrentDate());//操作时间
        loginDao.updateLogin(login);
    }

}
