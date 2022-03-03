package com.coisini.controller;

import com.coisini.model.*;
import com.coisini.entity.Announcement;
import com.coisini.service.AnnouncementService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 公告API
 * @author coisini
 * @date Jan 16, 2022
 * @version 2.0
 */
@Api(tags = "公告api")
@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementController {

    private final AnnouncementService announcementService;

    private final FormatUtil formatUtil;

    /**
     * 发布公告
     * @param title
     * @param body
     * @return
     */
    @ApiOperation(value = "发布公告", notes = "公告标题+公告内容")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public UnifyResponse<?> newAnnouncement(String title, String body) {

        if (!formatUtil.checkStringNull(title, body)) {
        	return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        if (title.length() > 255) {
            return UnifyResponse.fail(UnifyCode.ERROR, "标题过长", null);
        }

        announcementService.saveAnnouncement(title, body);
        return UnifyResponse.success(UnifyCode.SUCCESS, "发布成功", null);
    }

    /**
     * 删除公告
     * @param announcementId
     * @return
     */
    @ApiOperation(value = "删除公告", notes = "公告id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{announcementId}")
    public UnifyResponse<?> deleteAnnouncement(@PathVariable Integer announcementId) {

        if (!formatUtil.checkPositive(announcementId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        announcementService.deleteAnnouncementById(announcementId);
        return UnifyResponse.success(UnifyCode.DELETE_SUCCESS);
    }

    /**
     * 置顶
     * @param announcementId
     * @param top
     * @return
     */
    @ApiOperation(value = "置顶/取消置顶公告", notes = "公告id+置顶状态 0置顶 1正常")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/top/{announcementId}/{top}")
    public UnifyResponse<?> top(@PathVariable Integer announcementId, @PathVariable Integer top) {

        if (!formatUtil.checkPositive(announcementId)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }
        
        if (!formatUtil.checkObjectNull(top)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        announcementService.updateAnnouncementTop(announcementId, top);
    	return UnifyResponse.success(UnifyCode.SUCCESS);
    }

    /**
     * 分页获取公告
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页查询公告", notes = "页码+显示条数")
    @GetMapping("/{page}/{showCount}")
    public UnifyResponse<?> getAnnouncement(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
            return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
        }

        PageResult<Announcement> pageResult =
                new PageResult<>(announcementService.getAnnouncementCount(), announcementService.findAnnouncement(page, showCount));
        
    	return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, pageResult);
    }

}
