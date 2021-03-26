package com.coisini.controller;

import com.coisini.model.ResponseModel;
import com.coisini.model.SysErrorCode;
import com.coisini.service.ReplyService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
*  回复API
* @author Coisini
* @date Mar 21, 2020
*/

@Api(tags = "回复api")
@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private FormatUtil formatUtil;

    @ApiOperation(value = "发布回复", notes = "回复内容+评论id (父回复节点)?")
//    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{discussId}")
    public ResponseModel reply(@PathVariable Integer discussId, String replyBody, Integer rootId) {
    	ResponseModel responseModel = new ResponseModel();

        if (!formatUtil.checkStringNull(replyBody)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }
        
        if (!formatUtil.checkPositive(discussId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }

        try {
            replyService.saveReply(discussId, replyBody, rootId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("回复成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("回复失败");
        	return responseModel;
        }
    }

    @ApiOperation(value = "删除回复", notes = "回复id")
//    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{replyId}")
    public ResponseModel deleteReply(@PathVariable Integer replyId) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(replyId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        try {
            replyService.deleteReply(replyId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("删除成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("删除失败" + e.getMessage());
        	return responseModel;
        }
    }

    @ApiOperation(value = "管理员删除回复", notes = "回复id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{replyId}")
    public ResponseModel adminDeleteDiscuss(@PathVariable Integer replyId) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(replyId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        try {
            replyService.adminDeleteReply(replyId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("删除成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("删除失败" + e.getMessage());
        	return responseModel;
        }

    }
}
