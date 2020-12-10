package com.hyd.project.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class AppSwaggerConfig {

    @Bean
    public Docket createDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hyd.project.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("众筹项目中的项目模块")
                .description("用户展示项目模块中的请求路径和测试数据")
                .version("6.6.6")
                .build();
    }

}
