package com.coisini.entity;


import lombok.Data;
import lombok.ToString;

/**
 * 角色
 */
@Data
@ToString

public class Role {

    private Integer id;//角色id
    private String name;//角色名

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }
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


}
