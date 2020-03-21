package com.coisini.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 公告
 */
@Data
@ToString

public class Announcement {
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Integer getTop() {
		return top;
	}
	public void setTop(Integer top) {
		this.top = top;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	private Integer id;//id
    private String title;//公告标题
    private String body;//公告内容
    private Integer top;//是否置顶0 置顶 1未置顶
    private Date time;//发布时间
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}




}
