package com.coisini.entity;

import lombok.Data;
import lombok.ToString;
import java.util.Date;

/**
 * @Description 登录
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Login {

	/**
	 * 最后登录时间
	 */
    private Date time;

	/**
	 * 登录ip
	 */
    private String ip;

	/**
	 * 用户
	 */
    private User user;

}
