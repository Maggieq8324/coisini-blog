package com.coisini.controller;

import com.coisini.model.PageResult;
import com.coisini.model.ResponseModel;
import com.coisini.model.SysErrorCode;
import com.coisini.entity.Discuss;
import com.coisini.service.DiscussService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
* 评论API
* @author Coisini
* @date Mar 21, 2020
*/
@Api(tags = "评论api")
@RestController
@RequestMapping("/discuss")
public class DiscussController {

    @Autowired
    private DiscussService discussService;

    @Autowired
    private FormatUtil formatUtil;


    @ApiOperation(value = "发布评论", notes = "评论内容+博文id")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{blogId}")
    public ResponseModel discuss(String discussBody, @PathVariable Integer blogId) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkStringNull(discussBody)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }
        if (!formatUtil.checkPositive(blogId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数错误");
        	return responseModel;
        }

        discussService.saveDiscuss(discussBody, blogId);
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("评论成功");
    	return responseModel;
    }


    @ApiOperation(value = "删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{discussId}")
    public ResponseModel deleteDiscuss(@PathVariable Integer discussId) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(discussId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }
        try {
            discussService.deleteDiscuss(discussId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("删除评论成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("删除失败" + e.getMessage());
        	return responseModel;
        }
    }

    @ApiOperation(value = "管理员删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{discussId}")
    public ResponseModel adminDeleteDiscuss(@PathVariable Integer discussId) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkPositive(discussId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }

        try {
            discussService.adminDeleteDiscuss(discussId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("删除评论成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("删除失败" + e.getMessage());
        	return responseModel;
        }
    }

    @ApiOperation(value = "分页查询博文评论以及回复", notes = "博文id+页码+显示数")
    @GetMapping("/{blogId}/{page}/{showCount}")
    public ResponseModel getDiscussByBlog(@PathVariable Integer blogId,
                                   @PathVariable Integer page,
                                   @PathVariable Integer showCount) {
    	
    	ResponseModel responseModel = new ResponseModel();

        if (!formatUtil.checkPositive(blogId, page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }
        
        PageResult<Discuss> pageResult = new PageResult<>(discussService.getDiscussCountByBlogId(blogId), discussService.findDiscussByBlogId(blogId, page, showCount));

        responseModel.setSta(SysErrorCode.CODE_00);
        responseModel.setMessage("查询成功");
        responseModel.setData(pageResult);
        return responseModel;
    }

    @ApiOperation(value = "首页获取最新评论", notes = "获取最新六条评论")
    @GetMapping("/newDiscuss")
    public ResponseModel newDiscuss() {
    	ResponseModel responseModel = new ResponseModel();
    	responseModel.setSta(SysErrorCode.CODE_00);
        responseModel.setMessage("查询成功");
        responseModel.setData(discussService.findNewDiscuss());
		return responseModel;
    }

    @ApiOperation(value = "获取用户发布的所有博文下的评论", notes = "获取用户发布的所有博文下的评论")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/userNewDiscuss")
    public ResponseModel userNewDiscuss() {
    	ResponseModel responseModel = new ResponseModel();
    	responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("查询成功");
    	responseModel.setData(discussService.findUserNewDiscuss());
    	return responseModel;
    }

}
