package com.coisini.controller;

import com.coisini.model.*;
import com.coisini.entity.Discuss;
import com.coisini.service.DiscussService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 评论API
 * @author coisini
 * @date Jan 18, 2021
 * @version 1.0
 */
@Api(tags = "评论api")
@RestController
@RequestMapping("/discuss")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DiscussController {

    private final DiscussService discussService;

    private final FormatUtil formatUtil;

    /**
     * 发布评论
     * @param discussBody
     * @param blogId
     * @return
     */
    @ApiOperation(value = "发布评论", notes = "评论内容+博客id")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{blogId}")
    public UnifyResponse<?> discuss(String discussBody, @PathVariable Integer blogId) {

        if (!formatUtil.checkStringNull(discussBody)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        if (!formatUtil.checkPositive(blogId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        discussService.saveDiscuss(discussBody, blogId);
    	return UnifyResponse.success(UnifyCode.DISCUSS_SUCCESS);
    }

    /**
     * 删除评论
     * @param discussId
     * @return
     */
    @ApiOperation(value = "删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{discussId}")
    public UnifyResponse<?> deleteDiscuss(@PathVariable Integer discussId) {

        if (!formatUtil.checkPositive(discussId)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        try {
            discussService.deleteDiscuss(discussId);
        	return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("删除失败: ", e);
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "删除失败" + e.getMessage(), null);
        }
    }

    /**
     * 管理员删除评论
     * @param discussId
     * @return
     */
    @ApiOperation(value = "管理员删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{discussId}")
    public UnifyResponse<?> adminDeleteDiscuss(@PathVariable Integer discussId) {

        if (!formatUtil.checkPositive(discussId)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            discussService.adminDeleteDiscuss(discussId);
        	return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("删除失败: ", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "删除失败" + e.getMessage(), null);
        }
    }

    /**
     * 分页查询博客评论以及回复
     * @param blogId
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页查询博客评论以及回复", notes = "博客id+页码+显示数")
    @GetMapping("/{blogId}/{page}/{showCount}")
    public UnifyResponse<?> getDiscussByBlog(@PathVariable Integer blogId,
                                   @PathVariable Integer page,
                                   @PathVariable Integer showCount) {
    	
        if (!formatUtil.checkPositive(blogId, page, showCount)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        PageResult<Discuss> pageResult = new PageResult<>(
                discussService.getDiscussCountByBlogId(blogId),
                discussService.findDiscussByBlogId(blogId, page, showCount)
        );

        return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

    /**
     * 首页获取最新评论
     * @return
     */
    @ApiOperation(value = "首页获取最新评论", notes = "获取最新六条评论")
    @GetMapping("/newDiscuss")
    public UnifyResponse<?> newDiscuss() {
		return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, discussService.findNewDiscuss());
    }

    /**
     * 获取用户发布的所有博客下的评论
     * @return
     */
    @ApiOperation(value = "获取用户发布的所有博客下的评论", notes = "获取用户发布的所有博客下的评论")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/userNewDiscuss")
    public UnifyResponse<?> userNewDiscuss() {
    	return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, discussService.findUserNewDiscuss());
    }

}
