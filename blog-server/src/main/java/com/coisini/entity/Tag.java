package com.coisini.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @Description 标签
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Data
@ToString
public class Tag implements Serializable {

    /**
     * tag(36) => 37960(10)
     */
    private static final long serialVersionUID = 37960L;

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 标签名
	 */
    private String name;

	/**
	 * 用户
	 */
    @JsonIgnore
    private User user;

}
