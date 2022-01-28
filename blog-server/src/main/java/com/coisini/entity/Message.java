package com.coisini.entity;

import lombok.Data;
import lombok.ToString;
import java.util.Date;

/**
 * @Description 留言
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Message {

    private Integer id;

	/**
	 * 游客显示为ip地址
	 */
	private String name;

	/**
	 * 留言内容
	 */
    private String body;

	/**
	 * 留言时间
	 */
    private Date time;

}
