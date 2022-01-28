package com.coisini.entity;

import lombok.Data;
import lombok.ToString;
import java.util.Date;
import java.util.List;

/**
 * @Description 评论
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Discuss {

    private Integer id;

	/**
	 * 评论内容
	 */
	private String body;

	/**
	 * 评论时间
	 */
    private Date time;

	/**
	 * 评论用户
	 */
    private User user;

	/**
	 * 评论博客
	 */
    private Blog blog;

    private List<Reply> replyList;

}
