package com.hyd.project.config;


import com.hyd.common.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    @ConfigurationProperties(prefix = "oss")
    @Bean
    public OSSTemplate ossTemplate(){
        return new OSSTemplate();
    }
}
