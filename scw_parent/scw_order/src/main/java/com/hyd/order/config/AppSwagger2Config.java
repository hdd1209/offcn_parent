package com.hyd.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class AppSwagger2Config {
    @Bean
    public Docket createDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hyd.order.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("众筹项目中的订单模块")
                .description("展示订单模块中的请求路径和测试数据")
                .version("6.6.6")
                .build();
    }

}
