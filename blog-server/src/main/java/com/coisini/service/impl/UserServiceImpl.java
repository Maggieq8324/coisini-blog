package com.coisini.service.impl;

import com.coisini.config.JwtConfig;
import com.coisini.config.MailConfig;
import com.coisini.config.RabbitMqConfig;
import com.coisini.entity.Role;
import com.coisini.entity.User;
import com.coisini.mapper.RoleMapper;
import com.coisini.mapper.UserMapper;
import com.coisini.service.RoleService;
import com.coisini.service.UserService;
import com.coisini.utils.JwtTokenUtil;
import com.coisini.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description 用户实现类
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserMapper userDao;

    private final RoleMapper roleDao;

    private final RoleService roleService;

    private final HttpServletRequest request;

    private final BCryptPasswordEncoder encoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final RandomUtil randomUtil;

    private final JwtConfig jwtConfig;

    private final RedisTemplate<String, String> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    private final UserDetailsService userDetailsService;

    /**
     * 登录
     * 返回token，用户名，用户角色
     * @param user
     * @return
     * @throws UsernameNotFoundException
     * @throws RuntimeException
     */
    @Override
    public Map<String, Object> login(User user) throws UsernameNotFoundException, RuntimeException {

        User dbUser = this.findUserByName(user.getName());
        
        // TODO 此用户不存在 或 密码错误
        if (null == dbUser || !encoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // TODO 用户已被封禁
        if (0 == dbUser.getState()) {
            throw new RuntimeException("你已被封禁");
        }

        // TODO 用户名 密码 匹配 签发token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());

        final String token = jwtTokenUtil.generateToken(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }

        Map<String, Object> map = new HashMap<>(3);

        map.put("token", jwtConfig.getPrefix() + token);
        map.put("name", user.getName());
        map.put("roles", roles);

        // TODO 将token存入redis 过期时间 jwtConfig.time 单位[s]
        redisTemplate.opsForValue().
                set(JwtConfig.REDIS_TOKEN_KEY_PREFIX + user.getName(), jwtConfig.getPrefix() + token, jwtConfig.getTime(), TimeUnit.SECONDS);

        return map;
    }

    /**
     * 注册
     * @param userToAdd
     * @param mailCode
     * @throws RuntimeException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(User userToAdd, String mailCode) throws RuntimeException {

        // TODO 验证码无效 throw 异常
        if (!checkMailCode(userToAdd.getMail(), mailCode)) {
            throw new RuntimeException("验证码错误");
        }

        // TODO 有效 保存用户
        final String username = userToAdd.getName();
        if (userDao.findUserByName(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        if (userDao.findUserByMail(userToAdd.getMail()) != null) {
            throw new RuntimeException("邮箱已使用");
        }

        List<Role> roles = new ArrayList<>(1);
        roles.add(roleService.findRoleByName("USER"));
        // TODO 新注册用户赋予USER权限
        userToAdd.setRoles(roles);

        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));//加密密码
        userToAdd.setState(1);//正常状态
        userDao.saveUser(userToAdd);//保存角色

        // TODO 保存该用户的所有角色
        for (Role role : roles) {
            roleDao.saveUserRoles(userToAdd.getId(), role.getId());
        }
    }

    /**
     * 从token中提取信息
     * @param authHeader
     * @return
     */
    @Override
    public UserDetails loadUserByToken(String authHeader) {
        final String authToken = authHeader.substring(jwtConfig.getPrefix().length());//除去前缀，获取token

        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        // TODO token非法
        if (null == username) {
            return null;
        }

        String redisToken = redisTemplate.opsForValue().get(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username);

        // TODO 从redis中取不到值 或 值 不匹配
        if (!authHeader.equals(redisToken)) {
            return null;
        }
        User user = new User();
        user.setName(username);

        List<String> roles = jwtTokenUtil.getRolesFromToken(authToken);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
    }

    /**
     *封禁或解禁用户
     * @param id
     * @param state
     */
    @Override
    public void updateUserState(Integer id, Integer state) {
        User user = new User();
        user.setId(id);
        user.setState(state);
        // TODO 更改用户状态
        userDao.updateUser(user);

        User userById = userDao.findUserById(id);

        // TODO 封禁 从redis中移除token
        if (0 == state) {
            redisTemplate.delete(JwtConfig.REDIS_TOKEN_KEY_PREFIX + userById.getName());
        }
    }

    /**
     * 创建管理员
     * @param user
     */
    @Override
    public void createAdmin(User user) {
        final String username = user.getName();
        if (userDao.findUserByName(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        user.setRoles(roleService.findAllRole());//管理员默认拥有所有权限
        final String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));//加密密码
        user.setState(1);//正常状态
        userDao.saveUser(user);//保存角色

        // TODO 保存该用户的所有角色
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            roleDao.saveUserRoles(user.getId(), role.getId());
        }
    }

    /**
     * 修改用户密码
     * @param oldPassword
     * @param newPassword
     * @param code
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserPassword(String oldPassword, String newPassword, String code) {
        // TODO 校验原密码
        String name = jwtTokenUtil.getUsernameFromRequest(request);
        User user = new User();
        user.setName(name);

        user = userDao.findUserByName(user.getName()); //
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // TODO 校验邮箱验证码
        if (!checkMailCode(user.getMail(), code)) {
            throw new RuntimeException("验证码无效");
        }
        // TODO 更新密码
        user.setPassword(encoder.encode(newPassword));
        userDao.updateUser(user);
    }

    /**
     * 更新用户邮箱
     * @param newMail     新邮箱
     * @param oldMailCode 旧邮箱验证码
     * @param newMailCode 新邮箱验证码
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserMail(String newMail, String oldMailCode, String newMailCode) {

        // TODO 获取向旧邮箱发出的验证码
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(userName);

        // TODO 与用户输入的旧邮箱验证码进行匹配
        if (!checkMailCode(user.getMail(), oldMailCode)) {
            throw new RuntimeException("旧邮箱无效验证码");
        }

        // TODO 检查新邮箱是否已存在
        if (userDao.findUserByMail(newMail) != null) {
            throw new RuntimeException("此邮箱已使用");
        }

        // TODO 校验新邮箱验证码
        if (!checkMailCode(newMail, newMailCode)) {
            throw new RuntimeException("新邮箱无效验证码");
        }

        user.setMail(newMail);
        // TODO 更新用户邮箱信息
        userDao.updateUser(user);
    }

    /**
     * 重置密码
     * @param userName
     * @param mailCode
     * @param newPassword
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void forgetPassword(String userName, String mailCode, String newPassword) {
        User user = userDao.findUserByName(userName);
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }

        // TODO 与用户输入的邮箱验证码进行匹配
        if (!checkMailCode(user.getMail(), mailCode)) {
            throw new RuntimeException("无效验证码");
        }
        user.setPassword(encoder.encode(newPassword));
        // TODO 更新密码
        userDao.updateUser(user);

    }

    /**
     * 根据用户名分页搜索用户
     * @param userName
     * @param page
     * @param showCount
     * @return
     */
    @Override
    public List<User> searchUserByName(String userName, Integer page, Integer showCount) {
        return userDao.searchUserByName(userName, (page - 1) * showCount, showCount);
    }

    /**
     * 分页查询用户
     * @param page
     * @param showCount
     * @return
     */
    @Override
    public List<User> findUser(Integer page, Integer showCount) {
        return userDao.findUser((page - 1) * showCount, showCount);
    }

    /**
     * 查询用户数
     * @return
     */
    @Override
    public Long getUserCount() {
        return userDao.getUserCount();
    }

    /**
     * 查询用户邮箱
     * @return
     */
    @Override
    public String findUserMail() {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return user.getMail();
    }

    /**
     * 模糊查询用户名 返回记录数
     * @param userName
     * @return
     */
    @Override
    public Long getUserCountByName(String userName) {
        return userDao.getUserCountByName(userName);
    }

    /**
     * 将 邮件 和 验证码发送到消息队列
     * @param mail
     */
    @Override
    public void sendMail(String mail) {
        Integer random = randomUtil.nextInt(100000, 999999);
        Map<String, String> map = new HashMap<>();
        String code = random.toString();
        map.put("mail", mail);
        map.put("code", code);

        // TODO 保存发送记录
        redisTemplate.opsForValue()
                .set(MailConfig.REDIS_MAIL_KEY_PREFIX + mail, code, MailConfig.EXPIRED_TIME, TimeUnit.MINUTES);

        rabbitTemplate.convertAndSend(RabbitMqConfig.MAIL_QUEUE, map);
    }

    /**
     * 从redis中提取 验证码
     * @param mail
     * @return
     */
    @Override
    public String getMailCodeFromRedis(String mail) {
        return redisTemplate.opsForValue().get(MailConfig.REDIS_MAIL_KEY_PREFIX + mail);
    }

    /**
     * 校验验证码是否正确
     * @param mail
     * @param code
     * @return
     */
    @Override
    public boolean checkMailCode(String mail, String code) {
        String mailCode = getMailCodeFromRedis(mail);

        if (code.equals(mailCode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据用户名查询用户
     * @param name
     * @return
     */
    @Override
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    /**
     * 查询用户打赏码
     * @return
     */
    @Override
    public String findUserReward() {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return user.getReward();
    }

    /**
     * 更改用户打赏码
     * @param imgPath
     */
    @Override
    public void updateUserReward(String imgPath) {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        user.setReward(imgPath);
        userDao.updateUser(user);
    }

    /**
     * 退出登录
     * 删除redis中的key
     */
    @Override
    public void logout() {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        redisTemplate.delete(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username);
    }
}
