package com.coisini.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 留言
 */
@Data
@ToString
public class Message {
    private Integer id;//id
    private String name;//游客显示为ip地址
    private String body;//留言内容
    private Date time;//留言时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}


}
