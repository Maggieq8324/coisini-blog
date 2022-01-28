package com.coisini.service.impl;

import com.coisini.entity.Role;
import com.coisini.entity.User;
import com.coisini.mapper.RoleMapper;
import com.coisini.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 用户Detail
 * @author coisini
 * @date Jan 21, 2022
 * @version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserMapper userDao;

    private final RoleMapper roleDao;

    /**
     * 根据用户名查询用户
     * @param name
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findUserByName(name);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        // TODO 用于添加用户的权限。将用户权限添加到authorities
        List<Role> roles = roleDao.findUserRoles(user.getId());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
    }

}
