package com.coisini.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @Description 邀请码
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Code {

    private String id;

	/**
	 * 状态 0 未使用 1已使用 2 已删除
	 */
	private Integer state;

    private User user;

}
