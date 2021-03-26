package com.coisini.controller;

import com.coisini.config.SiteIntroductionConfig;
import com.coisini.model.ResponseModel;
import com.coisini.model.SysErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 站点API
* @author Coisini
* @date Mar 21, 2020
*/

@Api(tags = "站点api")
@RestController
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteIntroductionConfig siteIntroductionConfig;


    @ApiOperation(value = "站点介绍", notes = "站点介绍")
    @GetMapping
    public ResponseModel getIntroduction() {
    	ResponseModel response = new ResponseModel();
    	response.setSta(SysErrorCode.CODE_00);
    	response.setMessage("获取成功");
    	response.setData(siteIntroductionConfig.getIntroduction());
    	return response;
    }


}
