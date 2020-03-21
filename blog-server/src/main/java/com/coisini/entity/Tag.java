package com.coisini.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 标签
 */
@Data
@ToString

public class Tag implements Serializable {

    /**
     * tag(36) => 37960(10)
     */
    private static final long serialVersionUID = 37960L;
    private Integer id;//id
    private String name;//标签名

    @JsonIgnore
    private User user;//用户

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
