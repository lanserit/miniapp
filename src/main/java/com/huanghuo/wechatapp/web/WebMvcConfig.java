package com.huanghuo.wechatapp.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by huangcheng on 2018/5/28.
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Bean
    public MiniAppVerifyInterceptor tokenVerifyInterceptor() {
        return new MiniAppVerifyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenVerifyInterceptor()).addPathPatterns("/login","/index");
        super.addInterceptors(registry);
    }
}
