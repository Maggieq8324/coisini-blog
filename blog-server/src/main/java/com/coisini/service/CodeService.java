package com.coisini.service;

import com.coisini.mapper.CodeMapper;
import com.coisini.entity.Code;
import com.coisini.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeService {

    @Autowired
    private CodeMapper codeDao;

    @Autowired
    private UUIDUtil uuidUtil;

    /**
     * 生成激活码
     *
     * @return
     */
    public Code generateCode() {
        Code code = new Code();
        code.setState(0);
        code.setId(uuidUtil.generateUUID().toUpperCase());
        codeDao.saveCode(code);
        return code;
    }


    /**
     * 获取激活码记录条数
     *
     * @return
     */
    public Long getCodeCount() {
        return codeDao.getCodeCount();
    }

    public List<Code> findCode(Integer page, Integer showCount) {
        return codeDao.findCode((page - 1) * showCount, showCount);
    }
}
