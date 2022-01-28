package com.coisini.model;

/**
 * @Description 统一状态码
 * @author coisini
 * @date Jan 16, 2021
 * @version 1.0
 */
public enum UnifyCode {

    /* 成功状态码 */
    SUCCESS("00", "成功"),
    ERROR("01", "操作失败"),
    QUERY_SUCCESS("00", "查询成功"),
    UPDATE_SUCCESS("00", "更新成功"),
    ADD_SUCCESS("00", "新增成功"),
    DELETE_SUCCESS("00", "删除成功"),
    UPLOAD_SUCCESS("00", "上传成功"),
    PUBLISH_SUCCESS("00", "发布成功"),
    DISCUSS_SUCCESS("00", "评论成功"),
    MESSAGE_SUCCESS("00", "留言成功"),
    REPLY_SUCCESS("00", "回复成功"),
    LOGIN_SUCCESS("00", "登录成功"),
    LOGOUT_SUCCESS("00", "退出成功"),
    REGISTER_SUCCESS("00", "注册成功"),
    SEND_SUCCESS("00", "发送成功"),

    /* web */
    SERVER_201("201","请求方式错误"),
    SERVER_203("203","token过期"),
    SERVER_400("400","Bad Request"),
    SERVER_403("403","拒绝访问"),
    SERVER_404("404","资源未找到"),
    SERVER_500("500","Internal Server Error"),
    SERVER_UNKNOWN("999","未知错误"),


    /* 参数错误：10001-99999 */
    SERVER_ERROR_PARAM("10001", "参数异常"),
    SERVER_ERROR("99999", "服务异常");

    String msg;
    String code;

    UnifyCode(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
