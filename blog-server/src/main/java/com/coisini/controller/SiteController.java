package com.coisini.controller;

import com.coisini.config.SiteIntroductionConfig;
import com.coisini.model.UnifyCode;
import com.coisini.model.UnifyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 站点API
 * @author coisini
 * @date Jan 18, 2021
 * @version 1.0
 */
@Api(tags = "站点api")
@RestController
@RequestMapping("/site")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SiteController {

    private final SiteIntroductionConfig siteIntroductionConfig;

    /**
     * 站点介绍
     * @return
     */
    @ApiOperation(value = "站点介绍", notes = "站点介绍")
    @GetMapping
    public UnifyResponse<?> getIntroduction() {
    	return UnifyResponse.success(UnifyCode.QUERY_SUCCESS, siteIntroductionConfig.getIntroduction());
    }

}
