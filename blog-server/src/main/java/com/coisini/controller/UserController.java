package com.coisini.controller;

import com.coisini.config.JwtConfig;
import com.coisini.config.MailConfig;
import com.coisini.model.*;
import com.coisini.entity.User;
import com.coisini.service.LoginService;
import com.coisini.service.RoleService;
import com.coisini.service.UserService;
import com.coisini.utils.FormatUtil;
import com.coisini.utils.JwtTokenUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 用户API
 * @author coisini
 * @date Jan 18, 2021
 * @version 1.0
 */
@Api(tags = "用户api")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final LoginService loginService;

    private final RoleService roleService;

    private final FormatUtil formatUtil;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtConfig jwtConfig;

    /**
     * 登录返回token
     * @param user
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户名+密码 name+password 返回token")
    @PostMapping("/login")
    public UnifyResponse<?> login(User user) {

        if (!formatUtil.checkStringNull(user.getName(), user.getPassword())) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            Map<String, Object> map = userService.login(user);
            loginService.saveLoginInfo(user);
            return UnifyResponse.success(UnifyCode.LOGIN_SUCCESS, map);
        } catch (UsernameNotFoundException e) {
            log.error("登录失败，用户名或密码错误：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "登录失败，用户名或密码错误", null);
        } catch (RuntimeException e) {
            log.error("登录失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "登录失败：" + e.getMessage(), null);
        }
    }

    /**
     * 用户退出登录
     * 删除redis中的token
     * @param
     * @return
     */
    @ApiOperation(value = "用户退出登录")
    @GetMapping("/logout")
    public UnifyResponse<?> logout() {
    	userService.logout();
        return UnifyResponse.success(UnifyCode.LOGOUT_SUCCESS);
    }

    /**
     * 创建管理员
     * @param user
     * @return
     */
    @PreAuthorize("hasAuthority('USER')")
    @ApiOperation(value = "创建管理员", notes = "用户名+密码+邮箱 name+password+mail")
    @PostMapping("/createAdmin")
    public UnifyResponse<?> createAdmin(User user) {

        if (!formatUtil.checkStringNull(user.getName(), user.getPassword(), user.getMail())) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        // TODO 查询是否已有管理员
        Integer count = roleService.findAdminRoleCount("ADMIN");
        if (count > 0) {
            return UnifyResponse.fail(UnifyCode.SERVER_403,"拒绝访问", null);
        } else {
            // TODO 无 创建
            try {
                userService.createAdmin(user);
                return UnifyResponse.success(UnifyCode.SUCCESS, "管理员创建成功");
            } catch (RuntimeException e) {
                log.error("创建失败：", e);
                return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "创建失败：" + e.getMessage(), null);
            }
        }
    }

    /**
     * 用户注册
     * @param user
     * @param mailCode   邮箱验证码
     * @param inviteCode 邀请码
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "用户名+密码+邮箱+邮箱验证码+邀请码 name+password+mail+mailCode")
    @PostMapping("/register")
    public UnifyResponse<?> register(User user, String mailCode) {
        if (!formatUtil.checkStringNull(
                user.getName(),
                mailCode,
                user.getPassword(),
                user.getMail())) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        try {
            userService.register(user, mailCode);
        	return UnifyResponse.success(UnifyCode.REGISTER_SUCCESS);

        } catch (RuntimeException e) {
            log.error("注册失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "注册失败：" + e.getMessage(), null);
        }
    }

    /**
     * 用户封禁或解禁
     * @param id
     * @param state
     * @return
     */
    @ApiOperation(value = "用户封禁或解禁", notes = "用户id+状态 id+state")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/ban/{id}/{state}")
    public UnifyResponse<?> banUser(@PathVariable Integer id, @PathVariable Integer state) {

        if (!formatUtil.checkObjectNull(id, state)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        if (state == 0) {
            userService.updateUserState(id, state);
            return UnifyResponse.success(UnifyCode.SUCCESS, "封禁成功", null);
        } else if (state == 1) {
            userService.updateUserState(id, state);
            return UnifyResponse.success(UnifyCode.SUCCESS, "解禁成功", null);
        } else {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
    }

    /**
     * 异步发送验证邮件
     * @param mail
     * @return
     */
    @ApiOperation(value = "发送验证邮件", notes = "mail 冷却五分钟")
    @PostMapping("/sendMail")
    public UnifyResponse<?> sendMail(String mail) {

        // TODO 邮箱格式校验
        if (!(formatUtil.checkStringNull(mail)) || (!formatUtil.checkMail(mail))) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        String redisMailCode = userService.getMailCodeFromRedis(mail);

        // TODO 邮箱发送过验证码
        if (redisMailCode != null) {
        	return UnifyResponse.fail(UnifyCode.ERROR, MailConfig.EXPIRED_TIME + "分钟内不可重发验证码", null);
        } else {
            userService.sendMail(mail);
        	return UnifyResponse.success(UnifyCode.SEND_SUCCESS);
        }
    }

    /**
     * 更新用户打赏码
     * @return
     */
    @ApiOperation(value = "更新用户打赏码", notes = "更新用户打赏码")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/updateReward")
    public UnifyResponse<?> updateReward(String imgPath) {

        if (!formatUtil.checkStringNull(imgPath)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        userService.updateUserReward(imgPath);
    	return UnifyResponse.success(UnifyCode.UPDATE_SUCCESS);
    }

    /**
     * 获取用户绑定的邮箱
     * @return
     */
    @ApiOperation(value = "获取用户绑定的邮箱", notes = "获取用户绑定的邮箱")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/mail")
    public UnifyResponse<?> getUserMail() {
    	return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, userService.findUserMail());
    }

    /**
     * 获取用户的打赏码
     * @return
     */
    @ApiOperation(value = "获取用户的打赏码", notes = "获取用户的打赏码")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/getReward")
    public UnifyResponse getUserReward() {
    	String userReward = userService.findUserReward();
    	
    	if(Objects.isNull(userReward)) {
        	return UnifyResponse.fail(UnifyCode.ERROR, "查询失败", null);
    	}else {
        	return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, userReward);
    	}
    }

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param code        邮箱验证码
     * @return
     */
    @ApiOperation(value = "用户修改密码", notes = "旧密码+新密码+验证码")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/updatePassword")
    public UnifyResponse<?> updatePassword(String oldPassword, String newPassword, String code) {

        if (!formatUtil.checkStringNull(oldPassword, newPassword, code)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        try {
            userService.updateUserPassword(oldPassword, newPassword, code);
        	return UnifyResponse.success(UnifyCode.UPDATE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("修改密码失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "修改密码失败：" + e.getMessage(), null);
        }
    }

    /**
     * 改绑邮箱
     * @param newMail     新邮箱
     * @param oldMailCode 旧邮箱验证码
     * @param newMailCode 新邮箱验证码
     * @return
     */
    @ApiOperation(value = "改绑邮箱", notes = "新邮箱+旧邮箱验证码+新邮箱验证码")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/updateMail")
    public UnifyResponse<?> updateMail(String newMail, String oldMailCode, String newMailCode) {

        if (!formatUtil.checkStringNull(newMail, oldMailCode, newMailCode)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        // TODO 检查邮箱格式
        if (!formatUtil.checkMail(newMail)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        try {
            userService.updateUserMail(newMail, oldMailCode, newMailCode);
        	return UnifyResponse.success(UnifyCode.SUCCESS, "改绑成功", null);
        } catch (RuntimeException e) {
            log.error("改绑失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "改绑失败：" + e.getMessage(), null);
        }
    }

    /**
     * 重置密码
     * @param mailCode
     * @param newPassword
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "用户名+验证码+新密码")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/forgetPassword")
    public UnifyResponse<?> forgetPassword(String userName, String mailCode, String newPassword) {

        if (!formatUtil.checkStringNull(userName, mailCode, newPassword)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            userService.forgetPassword(userName, mailCode, newPassword);
        	return UnifyResponse.success(UnifyCode.SUCCESS, "重置成功", null);
        } catch (RuntimeException e) {
            log.error("重置失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "重置失败：" + e.getMessage(), null);
        }
    }

    /**
     * 只能由com.coisini.filter.JwtTokenFilter 转发token过期的请求进行访问
     * 需[刷新]权限，由上文提到的过滤器赋予权限，[刷新]与[游客] 类似，都没有写入数据库角色表
     * 刷新token
     * @param request
     * @return
     */
    @PreAuthorize("hasAuthority('REFRESH')")
    @GetMapping("/refresh")
    public UnifyResponse<?> refresh(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        return UnifyResponse.success(UnifyCode.SERVER_203, "登录状态已刷新，请重新执行当前操作", jwtConfig.getPrefix() + token);
    }

    /**
     * 分页查询用户
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页查询用户", notes = "页码+显示数量")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{page}/{showCount}")
    public UnifyResponse<?> findUser(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        PageResult<User> pageResult =
                new PageResult<>(userService.getUserCount(), userService.findUser(page, showCount));
  
        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

    /**
     * 根据用户名分页搜索用户
     * @param userName
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "根据用户名分页搜索用户", notes = "页码+显示数量+搜索内容")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search/{page}/{showCount}")
    public UnifyResponse<?> searchUser(String userName, @PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkStringNull(userName) || !formatUtil.checkPositive(page, showCount)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        PageResult<User> pageResult =
                new PageResult<>(userService.getUserCountByName(userName), userService.searchUserByName(userName, page, showCount));

        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

    /**
     * 游客权限测试
     * @return
     */
    @ApiOperation(value = "游客权限测试", notes = "测试")
    @GetMapping
    public UnifyResponse<?> test() {
        return UnifyResponse.success(UnifyCode.SUCCESS, "游客");
    }

    /**
     * 管理员或用户
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/adminOrUser")
    public UnifyResponse<?> adminOrUser() {
        return UnifyResponse.success(UnifyCode.SUCCESS, "管理员或用户");
    }

    /**
     * 管理员且用户
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') AND hasAnyAuthority('USER')")
    @GetMapping("/adminAndUser")
    public UnifyResponse<?> adminAndUser() {
        return UnifyResponse.success(UnifyCode.SUCCESS, "管理员且用户");
    }

    /**
     * 管理员权限测试
     * @return
     */
    @ApiOperation(value = "管理员权限测试", notes = "测试")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public UnifyResponse<?> admin() {
        return UnifyResponse.success(UnifyCode.SUCCESS, "管理员");
    }

    /**
     * 用户权限测试
     * @return
     */
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/user")
    public UnifyResponse<?> user() {
        return UnifyResponse.success(UnifyCode.SUCCESS, "用户");
    }

}
