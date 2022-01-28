package com.coisini.controller;

import com.coisini.model.UnifyCode;
import com.coisini.model.UnifyResponse;
import com.coisini.service.TagService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @author coisini
 * @date Jan 18, 2021
 * @version 1.0
 */
@Api(tags = "标签api")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TagController {

    private final TagService tagService;

    private final FormatUtil formatUtil;

    /**
     * 新增标签
     * @param tagName 标签名
     * @return
     */
    @ApiOperation(value = "新增标签", notes = "标签名")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping
    public UnifyResponse newTag(String tagName) {

        if (!formatUtil.checkStringNull(tagName)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            tagService.saveTag(tagName);
        	return UnifyResponse.success(UnifyCode.ADD_SUCCESS);
        } catch (RuntimeException e) {
            log.error("新增失败：", e);
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "新增失败：" + e.getMessage(), null);
        }
    }
    
    /**
     * 删除标签
     * @param tagId 标签id
     * @return
     */
    @ApiOperation(value = "删除标签", notes = "标签id")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @DeleteMapping("/{tagId}")
    public UnifyResponse deleteTag(@PathVariable Integer tagId) {

        if (!formatUtil.checkObjectNull(tagId)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            tagService.deleteTagById(tagId);
        	return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("删除失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "删除失败：" + e.getMessage(), null);
        }
    }

    /**
     * 修改标签
     * @param tagId   标签id
     * @param tagName 新标签名
     * @return
     */
    @ApiOperation(value = "修改标签", notes = "标签id+新标签名")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PutMapping
    public UnifyResponse updateTag(Integer tagId, String tagName) {

        if (!formatUtil.checkObjectNull(tagId)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        if (!formatUtil.checkStringNull(tagName)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        try {
            tagService.updateTag(tagId, tagName);
        	return UnifyResponse.success(UnifyCode.UPDATE_SUCCESS);
        } catch (RuntimeException e) {
            log.error("修改失败：", e);
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "修改失败：" + e.getMessage(), null);
        }
    }

    /**
     * 获取某用户下的所有标签
     * @return
     */
    @ApiOperation(value = "获取用户标签", notes = "用户id")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping
    public UnifyResponse findTagByUserId() {
    	return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, tagService.findTagByUserId());
    }

}
