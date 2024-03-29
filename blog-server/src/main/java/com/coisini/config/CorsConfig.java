package com.coisini.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
  * 跨域配置
 * @author litong
 * @date Dec 11, 2019
 */
@Configuration
public class CorsConfig {
	
	private final static String ALLOWORIGIN_CORS = "*";
	
	@Bean
    public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		// 允许跨域访问的源
		config.addAllowedOrigin(ALLOWORIGIN_CORS);
        config.setAllowCredentials(true);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE); // 允许请求方法 "GET","POST", "PUT", "DELETE", "OPTIONS"
        /**
         * "access-control-allow-headers",
         * "access-control-allow-methods",
         * "access-control-allow-origin",
         * "access-control-max-age",
         * "X-Frame-Options"
         */
        config.addAllowedHeader("*"); // 允许头部设置 
        
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

}
