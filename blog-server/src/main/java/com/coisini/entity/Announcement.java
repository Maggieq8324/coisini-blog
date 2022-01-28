package com.coisini.entity;

import lombok.Data;
import lombok.ToString;
import java.util.Date;

/**
 * @Description 公告
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Announcement {

	private Integer id;

	/**
	 * 公告标题
	 */
    private String title;

	/**
	 * 公告内容
	 */
    private String body;

	/**
	 * 是否置顶0 置顶 1未置顶
	 */
    private Integer top;

	/**
	 * 发布时间
	 */
    private Date time;

}
