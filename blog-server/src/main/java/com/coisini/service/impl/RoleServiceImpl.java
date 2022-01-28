package com.coisini.service.impl;

import com.coisini.entity.Role;
import com.coisini.mapper.RoleMapper;
import com.coisini.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 用户角色实现类
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleDao;

    /**
     * 查询角色数量
     * @return
     */
    @Override
    public Integer findAdminRoleCount(String roleName){
        return roleDao.findAdminRoleCount(roleName);
    }

    /**
     * 根据角色名查询
     * @param roleName
     * @return
     */
    @Override
    public Role findRoleByName(String roleName){
        return roleDao.findRoleByName(roleName);
    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<Role> findAllRole() {
        return roleDao.findAllRole();
    }

}
