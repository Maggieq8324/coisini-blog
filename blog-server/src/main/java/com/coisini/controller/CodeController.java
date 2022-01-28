package com.coisini.controller;

import com.coisini.model.*;
import com.coisini.entity.Code;
import com.coisini.service.CodeService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 邀请码API
 * @author coisini
 * @date Jan 18, 2021
 * @version 2.0
 */
@Api(tags = "邀请码api")
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CodeController {

    private final CodeService codeService;

    private final FormatUtil formatUtil;

    /**
     * 激活码
     * @return
     */
    @ApiOperation(value = "生成激活码", notes = "生成激活码")
    @PostMapping
    public UnifyResponse generateCode() {
    	return UnifyResponse.success(UnifyCode.SUCCESS, codeService.generateCode());
    }

    /**
     * 分页查询激活码
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页查询激活码", notes = "页码+显示条数")
    @GetMapping("/{page}/{showCount}")
    public UnifyResponse findCode(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        PageResult<Code> pageResult =
                new PageResult<>(codeService.getCodeCount(), codeService.findCode(page, showCount));
        
        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

}
