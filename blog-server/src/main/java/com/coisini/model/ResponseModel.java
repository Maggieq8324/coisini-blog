package com.coisini.model;

/**
 * 返回结果封装
 * @Auther: litong
 * @Date: 2019/6/14 11:16
 */
public class ResponseModel {

    private String sta;
    private String message;
    private Object data;

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
