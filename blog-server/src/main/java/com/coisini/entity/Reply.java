package com.coisini.entity;

import lombok.Data;
import lombok.ToString;
import java.util.Date;

/**
 * @Description 回复
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Reply {

    private Integer id;

	/**
	 * 回复内容
	 */
	private String body;

	/**
	 * 回复时间
	 */
    private Date time;

	/**
	 * 用户
	 */
    private User user;

	/**
	 * 评论
	 */
    private Discuss discuss;

	/**
	 * 父节点回复
	 */
    private Reply reply;

}
