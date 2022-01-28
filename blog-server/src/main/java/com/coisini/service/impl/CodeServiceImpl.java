package com.coisini.service.impl;

import com.coisini.entity.Code;
import com.coisini.mapper.CodeMapper;
import com.coisini.service.CodeService;
import com.coisini.utils.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 邀请码实现类
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CodeServiceImpl implements CodeService {

    private final CodeMapper codeDao;

    private final UUIDUtil uuidUtil;

    /**
     * 生成邀请码
     * @return
     */
    @Override
    public Code generateCode() {
        Code code = new Code();
        code.setState(0);
        code.setId(uuidUtil.generateUUID().toUpperCase());
        codeDao.saveCode(code);
        return code;
    }


    /**
     * 获取邀请码记录条数
     * @return
     */
    @Override
    public Long getCodeCount() {
        return codeDao.getCodeCount();
    }

    /**
     * 邀请码查询
     * @param page
     * @param showCount
     * @return
     */
    @Override
    public List<Code> findCode(Integer page, Integer showCount) {
        return codeDao.findCode((page - 1) * showCount, showCount);
    }
}
