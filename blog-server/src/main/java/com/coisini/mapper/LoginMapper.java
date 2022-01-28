package com.coisini.mapper;

import com.coisini.entity.Login;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description 登录Mapper
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Repository
@Mapper
public interface LoginMapper {

    /**
     * 根据用户id删除登录记录
     * @param id
     */
    void deleteLoginByUserId(Integer id);

    /**
     * 保存登录信息
     */
    void saveLogin(Login login);

    /**
     * 根据用户id修改登录操作表
     * @param login
     */
    void updateLogin(Login login);

}
