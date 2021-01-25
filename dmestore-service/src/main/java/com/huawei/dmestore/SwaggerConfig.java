package com.huawei.dmestore;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by andrewliu on 2017/2/14.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${swagger.ui.enable:true}") //该配置项在配置中心管理
    private boolean environmentSpecificBooleanFlag;

    @Bean
    public Docket docketFactory() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.huawei.dmestore"))
            .paths(PathSelectors.any())
            .build()
            .enable(environmentSpecificBooleanFlag);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("接口文档", "SpingCloud web接口列表", "1.0", "", "", "", "");
    }
}
