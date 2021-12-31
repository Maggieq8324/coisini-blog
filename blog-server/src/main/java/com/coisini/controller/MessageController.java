package com.coisini.controller;

import com.coisini.model.PageResult;
import com.coisini.model.ResponseModel;
import com.coisini.model.Result;
import com.coisini.model.StatusCode;
import com.coisini.model.SysErrorCode;
import com.coisini.entity.Message;
import com.coisini.service.MessageService;
import com.coisini.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
  *  留言API
 * @author Coisini
 * @date Mar 21, 2020
 */

@Api(tags = "留言api")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private FormatUtil formatUtil;


    @ApiOperation(value = "留言", notes = "留言内容")
    @PostMapping
    public ResponseModel message(String messageBody) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkStringNull(messageBody)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }

        try {
            messageService.saveMessage(messageBody);
            responseModel.setSta(SysErrorCode.CODE_00);
        	responseModel.setMessage("留言成功");
        	return responseModel;
        } catch (RuntimeException e) {
            responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("留言失败 " + (e.getMessage() == null ? "" : e.getMessage()));
        	return responseModel;
//            return Result.create(StatusCode.ERROR, "留言失败" + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }


    //管理员删除
    @ApiOperation(value = "管理员删除留言", notes = "留言id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{messageId}")
    public Result deleteMessage(@PathVariable Integer messageId) {
        if (!formatUtil.checkPositive(messageId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        messageService.deleteMessageById(messageId);
        return Result.create(StatusCode.OK, "删除成功");
    }

    /**
     * 分页查询留言
     * @param page      起始页
     * @param showCount 显示条数
     * @return 查询结果
     */
    @ApiOperation(value = "分页查询留言", notes = "页码+显示数量")
    @GetMapping("/{page}/{showCount}")
    public ResponseModel getMessage(@PathVariable Integer page, @PathVariable Integer showCount) {
    	ResponseModel responseModel = new ResponseModel();
        if (!formatUtil.checkPositive(page, showCount)) {
        	responseModel.setSta(SysErrorCode.CODE_99999);
        	responseModel.setMessage("参数异常");
        	return responseModel;
        }

        PageResult<Message> pageResult =
                new PageResult<>(messageService.getMessageCount(), messageService.findMessage(page, showCount));
        
        responseModel.setSta(SysErrorCode.CODE_00);
    	responseModel.setMessage("查询成功");
    	responseModel.setData(pageResult);
    	return responseModel;

//        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

}
