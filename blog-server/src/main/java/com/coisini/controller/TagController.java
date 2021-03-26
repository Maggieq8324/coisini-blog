package com.coisini.controller;

import com.coisini.model.ResponseModel;
import com.coisini.model.SysErrorCode;
import com.coisini.service.TagService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
* 标签API
* @author Coisini
* @date Mar 21, 2020
*/
@Api(tags = "标签api")
@RestController
@RequestMapping("/tag")
public class TagController {


    @Autowired
    private TagService tagService;

    @Autowired
    private FormatUtil formatUtil;

    /**
     * 新增一个标签
     * @param tagName 标签名
     * @return
     */
    @ApiOperation(value = "新增标签", notes = "标签名")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseModel newTag(String tagName) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkStringNull(tagName)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }

        try {
            tagService.saveTag(tagName);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("新增成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("新增失败," + e.getMessage());
        	return responseModel;
        }
    }
    

    /**
     * 删除一个标签
     * @param tagId 标签id
     * @return
     */
    @ApiOperation(value = "删除标签", notes = "标签id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{tagId}")
    public ResponseModel deleteTag(@PathVariable Integer tagId) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkObjectNull(tagId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }
        try {
            tagService.deleteTagById(tagId);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("删除成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("删除失败," + e.getMessage());
        	return responseModel;
        }
    }


    /**
     * 修改一个标签
     * @param tagId   标签id
     * @param tagName 新标签名
     * @return
     */
    @ApiOperation(value = "修改标签", notes = "标签id+新标签名")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public ResponseModel updateTag(Integer tagId, String tagName) {
    	ResponseModel responseModel = new ResponseModel();
    	
        if (!formatUtil.checkObjectNull(tagId)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }
        if (!formatUtil.checkStringNull(tagName)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }

        try {
            tagService.updateTag(tagId, tagName);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("修改成功");
        	return responseModel;
        } catch (RuntimeException e) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("修改失败," + e.getMessage());
        	return responseModel;
        }
    }


    /**
     * 获取某用户下的所有标签
     * @return
     */
    @ApiOperation(value = "获取用户标签", notes = "用户id")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseModel findTagByUserId() {
    	ResponseModel responseModel = new ResponseModel();
    	responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("查询成功");
    	responseModel.setData(tagService.findTagByUserId());
    	return responseModel;
    }

}
