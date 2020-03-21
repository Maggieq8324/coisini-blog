package com.coisini.service;

import com.coisini.mapper.RoleMapper;
import com.coisini.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleService {

    @Autowired
    private RoleMapper roleDao;

    /**
     * 查询角色数量
     * @return
     */
    public Integer findAdminRoleCount(String roleName){
        return roleDao.findAdminRoleCount(roleName);
    }


    /**
     * 根据角色名查询
     * @param roleName
     * @return
     */
    public  Role findRoleByName(String roleName){
        return roleDao.findRoleByName(roleName);
    }

    /**
     * 查询所有角色
     * @return
     */
    public List<Role> findAllRole() {
        return roleDao.findAllRole();
    }
}
