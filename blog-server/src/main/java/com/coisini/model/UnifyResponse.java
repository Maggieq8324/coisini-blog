package com.coisini.model;

import lombok.Data;

/**
 * @Description 统一返回定义
 * @author coisini
 * @date Jan 16, 2021
 * @version 1.0
 */
@Data
public class UnifyResponse<T> {

    private String sta;

    private String msg;

    private T data;

    private UnifyResponse(UnifyCode unifyCode, T data) {
        this.sta = unifyCode.getCode();
        this.msg = unifyCode.getMsg();
        this.data = data;
    }

    private UnifyResponse(UnifyCode unifyCode, String msg, T data) {
        this.sta = unifyCode.getCode();
        this.msg = msg;
        this.data = data;
    }

    /**
     * 业务成功返回
     * @return
     */
    public static UnifyResponse<Void> success() {
        return new UnifyResponse<Void>(UnifyCode.SUCCESS, null);
    }

    public static <T> UnifyResponse<T> success(T data) {
        return new UnifyResponse<T>(UnifyCode.SUCCESS, data);
    }

    public static <T> UnifyResponse<T> success(UnifyCode unifyCode, T data) {
        return new UnifyResponse<T>(unifyCode, data);
    }

    public static <T> UnifyResponse<T> success(UnifyCode unifyCode, String msg, T data) {
        return new UnifyResponse<T>(unifyCode, msg, data);
    }

    /**
     * 业务失败返回
     * @param <T>
     * @return
     */
    public static <T> UnifyResponse<T> fail() {
        return new UnifyResponse<T>(UnifyCode.SERVER_ERROR, null);
    }

    public static <T> UnifyResponse<T> fail(UnifyCode unifyCode) {
        return fail(unifyCode, null);
    }

    public static <T> UnifyResponse<T> fail(UnifyCode unifyCode, T data) {
        return new UnifyResponse<T>(unifyCode, data);
    }

    public static <T> UnifyResponse<T> fail(UnifyCode unifyCode, String msg, T data) {
        return new UnifyResponse<T>(unifyCode, msg, data);
    }

}
