package com.coisini.controller;

import com.coisini.model.PageResult;
import com.coisini.model.ResponseModel;
import com.coisini.model.SysErrorCode;
import com.coisini.entity.Announcement;
import com.coisini.service.AnnouncementService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *  公告API
* @author Coisini
* @date Mar 21, 2020
*/

@Api(tags = "公告api")
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private FormatUtil formatUtil;


    @ApiOperation(value = "发布公告", notes = "公告标题+公告内容")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseModel newAnnouncement(String title, String body) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkStringNull(title, body)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        if (title.length() > 255) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("标题过长");
        	return responseModel;
        }

        announcementService.saveAnnouncement(title, body);
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("发布成功");
    	return responseModel;
    }

    @ApiOperation(value = "删除公告", notes = "公告id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{announcementId}")
    public ResponseModel deleteAnnouncement(@PathVariable Integer announcementId) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(announcementId)) {
            responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        announcementService.deleteAnnouncementById(announcementId);
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("删除成功");
    	return responseModel;
    }

    /**
          *  置顶
     * @param announcementId
     * @param top
     * @return
     */
    @ApiOperation(value = "置顶/取消置顶公告", notes = "公告id+置顶状态 0置顶 1正常")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/top/{announcementId}/{top}")
    public ResponseModel top(@PathVariable Integer announcementId, @PathVariable Integer top) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(announcementId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        
        if (!formatUtil.checkObjectNull(top)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        announcementService.updateAnnouncementTop(announcementId, top);
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("操作成功");
    	return responseModel;
    }


    /**
          *  分页获取公告
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页查询公告", notes = "页码+显示条数")
    @GetMapping("/{page}/{showCount}")
    public ResponseModel getAnnouncement(@PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        PageResult<Announcement> pageResult =
                new PageResult<>(announcementService.getAnnouncementCount(), announcementService.findAnnouncement(page, showCount));
        
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("查询成功");
    	responseModel.setData(pageResult);
    	return responseModel;
    }


}
