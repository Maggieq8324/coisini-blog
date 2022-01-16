package com.coisini.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 站点介绍
 * @author coisini
 * @date Jan 16, 2022
 * @version 2.0
 */
@ConfigurationProperties(prefix = "site")
@Component
public class SiteIntroductionConfig {

    private String introduction;//介绍

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
