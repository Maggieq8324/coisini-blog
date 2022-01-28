package com.coisini.controller;

import com.coisini.model.*;
import com.coisini.entity.Message;
import com.coisini.service.MessageService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 留言API
 * @author coisini
 * @date Jan 18, 2021
 * @version 1.0
 */
@Api(tags = "留言api")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MessageController {

    private final MessageService messageService;

    private final FormatUtil formatUtil;

    /**
     * 留言
     * @param messageBody
     * @return
     */
    @ApiOperation(value = "留言", notes = "留言内容")
    @PostMapping
    public UnifyResponse message(String messageBody) {

        if (!formatUtil.checkStringNull(messageBody)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            messageService.saveMessage(messageBody);
        	return UnifyResponse.success(UnifyCode.MESSAGE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("留言失败: ", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "留言失败" + e.getMessage(), null);
        }
    }

    /**
     * 管理员删除留言
     * @param messageId
     * @return
     */
    @ApiOperation(value = "管理员删除留言", notes = "留言id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{messageId}")
    public UnifyResponse deleteMessage(@PathVariable Integer messageId) {

        if (!formatUtil.checkPositive(messageId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        messageService.deleteMessageById(messageId);
        return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
    }

    /**
     * 分页查询留言
     * @param page      起始页
     * @param showCount 显示条数
     * @return 查询结果
     */
    @ApiOperation(value = "分页查询留言", notes = "页码+显示数量")
    @GetMapping("/{page}/{showCount}")
    public UnifyResponse getMessage(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        PageResult<Message> pageResult =
                new PageResult<>(messageService.getMessageCount(), messageService.findMessage(page, showCount));

    	return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

}
