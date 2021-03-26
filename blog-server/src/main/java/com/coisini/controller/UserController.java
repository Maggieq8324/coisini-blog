package com.coisini.controller;

import com.coisini.config.MailConfig;
import com.coisini.model.PageResult;
import com.coisini.model.ResponseModel;
import com.coisini.model.Result;
import com.coisini.model.StatusCode;
import com.coisini.model.SysErrorCode;
import com.coisini.entity.User;
import com.coisini.service.LoginService;
import com.coisini.service.UserService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Objects;

/**
*  用户API
* @author Coisini
* @date Mar 21, 2020
*/

@Api(tags = "用户api")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private FormatUtil formatUtil;

    /**
          * 登录返回token
     * @param user
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户名+密码 name+password 返回token")
    @PostMapping("/login")
    public ResponseModel login(User user) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkStringNull(user.getName(), user.getPassword())) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
            responseModel.setMessage("参数错误");
            return responseModel;
        }

        try {
            Map<String, Object> map = userService.login(user);
            loginService.saveLoginInfo(user);
            responseModel.setSta(SysErrorCode.CODE_00);
            responseModel.setData(map);
            responseModel.setMessage("登录成功");
            return responseModel;
        } catch (UsernameNotFoundException unfe) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
            responseModel.setMessage("登录失败，用户名或密码错误");
            return responseModel;
        } catch (RuntimeException re) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
            responseModel.setMessage(re.getMessage());
            return responseModel;
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
    public ResponseModel logout() {
    	userService.logout();
    	ResponseModel responseModel = new ResponseModel();
    	responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("退出成功");
        return responseModel;
    }

//    /**
//     * 创建管理员
//     *
//     * @param user
//     * @return
//     */
//    @PreAuthorize("hasAuthority('USER')")
//    @ApiOperation(value = "创建管理员", notes = "用户名+密码+邮箱 name+password+mail")
//    @PostMapping("/createAdmin")
//    public Result createAdmin(User user) {
//        if (!formatUtil.checkStringNull(user.getName(), user.getPassword(), user.getMail())) {
//            return Result.create(StatusCode.ERROR, "参数错误");
//        }
//
//        //查询是否已有管理员
//        Integer count = roleService.findAdminRoleCount("ADMIN");
//        if (count > 0) {
//            return Result.create(StatusCode.ACCESSERROR, "拒绝访问");
//        } else {
//            //无 创建
//            try {
//                userService.createAdmin(user);
//                return Result.create(StatusCode.OK, "管理员创建成功");
//            } catch (RuntimeException e) {
//                return Result.create(StatusCode.OK, "创建失败，" + e.getMessage());
//            }
//        }
//    }

    /**
          *  用户注册
     * @param user
     * @param mailCode   邮箱验证码
     * @param inviteCode 邀请码
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "用户名+密码+邮箱+邮箱验证码+邀请码 name+password+mail+mailCode")
    @PostMapping("/register")
    public ResponseModel register(User user, String mailCode) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkStringNull(
                user.getName(),
                mailCode,
                user.getPassword(),
                user.getMail())) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("注册失败，字段不完整");
        	return responseModel;
        }
        
        try {
            userService.register(user, mailCode);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("注册成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("注册失败，" + e.getMessage());
        	return responseModel;
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
    public Result banUser(@PathVariable Integer id, @PathVariable Integer state) {

        if (!formatUtil.checkObjectNull(id, state)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        if (state == 0) {
            userService.updateUserState(id, state);
            return Result.create(StatusCode.OK, "封禁成功");
        } else if (state == 1) {
            userService.updateUserState(id, state);
            return Result.create(StatusCode.OK, "解禁成功");
        } else {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
    }

    /**
          *  发送验证邮件
          *  异步发送
     * @param mail
     * @return
     */
    @ApiOperation(value = "发送验证邮件", notes = "mail 冷却五分钟")
    @PostMapping("/sendMail")
    public ResponseModel sendMail(String mail) {
    	ResponseModel responseModel = new ResponseModel();

        //邮箱格式校验
        if (!(formatUtil.checkStringNull(mail)) || (!formatUtil.checkMail(mail))) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("邮箱格式错误");
        	return responseModel;
        }
        
        String redisMailCode = userService.getMailCodeFromRedis(mail);

        //此邮箱发送过验证码
        if (redisMailCode != null) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage(MailConfig.EXPIRED_TIME + "分钟内不可重发验证码");
        	return responseModel;
        } else {
            userService.sendMail(mail);
            
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("发送成功");
        	return responseModel;
        }
    }

    /**
          * 更新用户打赏码
     * @return
     */
    @ApiOperation(value = "更新用户打赏码", notes = "更新用户打赏码")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/updateReward")
    public ResponseModel updateReward(String imgPath) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkStringNull(imgPath)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("格式错误");
        	return responseModel;
        }
        
        userService.updateUserReward(imgPath);
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("更新成功");
    	return responseModel;
    }

    /**
          * 获取用户绑定的邮箱
     * @return
     */
    @ApiOperation(value = "获取用户绑定的邮箱", notes = "获取用户绑定的邮箱")
//    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/mail")
    public ResponseModel getUserMail() {
    	ResponseModel responseModel = new ResponseModel();
    	responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage( "查询成功");
    	responseModel.setData(userService.findUserMail());
    	return responseModel;
    }

    /**
          * 获取用户的打赏码
     * @return
     */
    @ApiOperation(value = "获取用户的打赏码", notes = "获取用户的打赏码")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getReward")
    public ResponseModel getUserReward() {
    	ResponseModel responseModel = new ResponseModel();
    	
    	String userReward = userService.findUserReward();
    	
    	if(Objects.isNull(userReward)) {
    		responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("查询失败");
    	}else {
    		responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("查询成功");
        	responseModel.setData(userReward);
    	}

    	return responseModel;
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
    public ResponseModel updatePassword(String oldPassword, String newPassword, String code) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkStringNull(oldPassword, newPassword, code)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        try {
            userService.updateUserPassword(oldPassword, newPassword, code);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("修改密码成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("修改密码失败，" + e.getMessage());
        	return responseModel;
        }
    }

    /**
          *  改绑邮箱
     * @param newMail     新邮箱
     * @param oldMailCode 旧邮箱验证码
     * @param newMailCode 新邮箱验证码
     * @return
     */
    @ApiOperation(value = "改绑邮箱", notes = "新邮箱+旧邮箱验证码+新邮箱验证码")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/updateMail")
    public ResponseModel updateMail(String newMail, String oldMailCode, String newMailCode) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkStringNull(newMail, oldMailCode, newMailCode)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        //检查邮箱格式
        if (!formatUtil.checkMail(newMail)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        try {
            userService.updateUserMail(newMail, oldMailCode, newMailCode);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("改绑成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("改绑失败，" + e.getMessage());
        	return responseModel;
        }
    }

    /**
          *  重置密码
     * @param mailCode
     * @param newPassword
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "用户名+验证码+新密码")
//    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/forgetPassword")
    public ResponseModel forgetPassword(String userName, String mailCode, String newPassword) {
    	ResponseModel responseModel = new ResponseModel();

        if (!formatUtil.checkStringNull(userName, mailCode, newPassword)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        try {
            userService.forgetPassword(userName, mailCode, newPassword);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("重置成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("重置失败," + e.getMessage());
        	return responseModel;
        }
    }

//    /**
//     * 只能由com.zzx.filter.JwtTokenFilter 转发token过期的请求进行访问
//     * 需[刷新]权限，由上文提到的过滤器赋予权限，[刷新]与[游客] 类似，都没有写入数据库角色表
//     * 刷新token
//     *
//     * @param request
//     * @return
//     */
//    @PreAuthorize("hasAuthority('REFRESH')")
//    @GetMapping("/refresh")
//    public Result refresh(HttpServletRequest request) {
//        String username = jwtTokenUtil.getUsernameFromRequest(request);
//        UserDetails userDetails = userService.loadUserByUsername(username);
//        String token = jwtTokenUtil.generateToken(userDetails);
//        return Result.create(StatusCode.TOKENEXPIREE, "登录状态已刷新，请重新执行当前操作", jwtConfig.getPrefix() + token);
//    }

    @ApiOperation(value = "分页查询用户", notes = "页码+显示数量")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{page}/{showCount}")
    public ResponseModel findUser(@PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        PageResult<User> pageResult =
                new PageResult<>(userService.getUserCount(), userService.findUser(page, showCount));
  
        responseModel.setSta(SysErrorCode.CODE_00);
        responseModel.setMessage("查询成功");
        responseModel.setData(pageResult);
        return responseModel;
    }

    @ApiOperation(value = "根据用户名分页搜索用户", notes = "页码+显示数量+搜索内容")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search/{page}/{showCount}")
    public ResponseModel searchUser(String userName, @PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkStringNull(userName)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        if (!formatUtil.checkPositive(page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        PageResult<User> pageResult =
                new PageResult<>(userService.getUserCountByName(userName), userService.searchUserByName(userName, page, showCount));

        responseModel.setSta(SysErrorCode.CODE_00);
        responseModel.setMessage("查询成功");
        responseModel.setData(pageResult);
        return responseModel;
    }


    //以下是权限测试方法

    //    @ApiOperation(value = "游客权限测试", notes = "测试")
//    @GetMapping
//    public Result test() {
//        return Result.create(StatusCode.OK, "游客");
//    }
//
//
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
//    @GetMapping("/adminoruser")
//    public Result adminoruser() {
//        return Result.create(StatusCode.OK, "管理员 或 用户");
//    }
//
    @PreAuthorize("hasAuthority('ADMIN') AND hasAnyAuthority('USER')")
    @GetMapping("/adminanduser")
    public String adminanduser() {
        return "管理员 且 用户";
    }
//
//    @ApiOperation(value = "管理员权限测试", notes = "测试")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/admin")
//    public Result admin() {
//        return Result.create(StatusCode.OK, "管理员");
//    }
//
//
//    @PreAuthorize("hasAnyAuthority('USER')")
//    @GetMapping("/user")
//    public Result user() {
//        return Result.create(StatusCode.OK, "用户");
//    }


}
