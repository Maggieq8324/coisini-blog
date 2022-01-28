package com.coisini.service;

import com.coisini.entity.Role;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 用户角色接口
 * @author coisini
 * @date Jan 21, 2022
 * @version 2.0
 */
@Service
public interface RoleService {

    /**
     * 查询角色数量
     * @return
     */
    Integer findAdminRoleCount(String roleName);

    /**
     * 根据角色名查询
     * @param roleName
     * @return
     */
    Role findRoleByName(String roleName);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAllRole();

}
