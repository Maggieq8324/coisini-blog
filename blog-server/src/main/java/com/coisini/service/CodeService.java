package com.coisini.service;

import com.coisini.entity.Code;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 邀请码接口
 * @author coisini
 * @date Jan 19， 2022
 * @version 2.0
 */
@Service
public interface CodeService {

    /**
     * 生成激活码
     * @return
     */
    Code generateCode();


    /**
     * 获取激活码记录条数
     * @return
     */
    Long getCodeCount();

    /**
     * 激活码查询
     * @param page
     * @param showCount
     * @return
     */
    List<Code> findCode(Integer page, Integer showCount);
}
