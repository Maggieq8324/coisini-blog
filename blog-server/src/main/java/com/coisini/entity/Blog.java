package com.coisini.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 博文
 */
@Data
@ToString(exclude = "body")
public class Blog implements Serializable {
    /**
     * blog(36) => 541312(10)
     */
    private static final long serialVersionUID = 541312L;
    /**
     * id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String body;

    /**
     * 评论数
     */
    private Integer discussCount;

    /**
     * 浏览数
     */
    private Integer blogViews;

    /**
     * 发布时间
     */
    private Date time;
    /**
     * 博文状态--0删除 1正常
     */
    private Integer state;

    /**
     * 所属用户
     */
    private User user;
    /**
     * 博文对应的标签
     */
    private List<Tag> tags;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Integer getDiscussCount() {
		return discussCount;
	}
	public void setDiscussCount(Integer discussCount) {
		this.discussCount = discussCount;
	}
	public Integer getBlogViews() {
		return blogViews;
	}
	public void setBlogViews(Integer blogViews) {
		this.blogViews = blogViews;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
}
