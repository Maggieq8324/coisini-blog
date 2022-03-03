package com.coisini.controller;

import com.coisini.model.UnifyCode;
import com.coisini.model.UnifyResponse;
import com.coisini.service.ReplyService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 回复API
 * @author coisini
 * @date Jan 18, 2021
 * @version 1.0
 */
@Api(tags = "回复api")
@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ReplyController {

    private final ReplyService replyService;

    private final FormatUtil formatUtil;

    /**
     * 发布回复
     * @param discussId
     * @param replyBody
     * @param rootId
     * @return
     */
    @ApiOperation(value = "发布回复", notes = "回复内容+评论id (父回复节点)?")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/{discussId}")
    public UnifyResponse<?> reply(@PathVariable Integer discussId, String replyBody, Integer rootId) {

        if (!formatUtil.checkStringNull(replyBody)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        if (!formatUtil.checkPositive(discussId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            replyService.saveReply(discussId, replyBody, rootId);
        	return UnifyResponse.success(UnifyCode.REPLY_SUCCESS);
        } catch (RuntimeException e) {
            log.error("回复失败：", e);
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "回复失败: " + e.getMessage(), null);
        }
    }

    /**
     * 删除回复
     * @param replyId
     * @return
     */
    @ApiOperation(value = "删除回复", notes = "回复id")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @DeleteMapping("/{replyId}")
    public UnifyResponse<?> deleteReply(@PathVariable Integer replyId) {

        if (!formatUtil.checkPositive(replyId)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR);
        }

        try {
            replyService.deleteReply(replyId);
        	return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("删除失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "删除失败：" + e.getMessage(), null);
        }
    }

    /**
     * 管理员删除回复
     * @param replyId
     * @return
     */
    @ApiOperation(value = "管理员删除回复", notes = "回复id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{replyId}")
    public UnifyResponse<?> adminDeleteDiscuss(@PathVariable Integer replyId) {

        if (!formatUtil.checkPositive(replyId)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR);
        }

        try {
            replyService.adminDeleteReply(replyId);
        	return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("删除失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "删除失败：" + e.getMessage(), null);
        }

    }
}
