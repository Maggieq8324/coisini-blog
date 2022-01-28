package com.coisini.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @Description 角色
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Role {

	/**
	 * 角色id
	 */
    private Integer id;

	/**
	 * 角色名
	 */
    private String name;

}
