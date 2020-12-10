package com.hyd.user.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppDruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }

    @Bean
    public ServletRegistrationBean userServler(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        Map<String,String> initMap = new HashMap<>();
        initMap.put("loginUsername","admin");
        initMap.put("loginPassword","123");
//        initMap.put("dent","");//禁止访问的路径
        initMap.put("allow","");//允许访问所有内容
        bean.setInitParameters(initMap);
        return bean;
    }


    @Bean
    public FilterRegistrationBean statFilter(){
        FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new WebStatFilter());
        Map<String,String> initMap = new HashMap<>();
        initMap.put("exclusions","*.js,*.css,/druid/*");
        filterBean.setInitParameters(initMap);
        filterBean.setUrlPatterns(Arrays.asList("/*"));
        return filterBean;
    }

}
