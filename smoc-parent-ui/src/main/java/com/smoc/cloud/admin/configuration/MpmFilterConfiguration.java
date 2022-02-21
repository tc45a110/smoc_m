package com.smoc.cloud.admin.configuration;

import com.smoc.cloud.admin.filter.MpmDataInitializer;
import com.smoc.cloud.admin.filter.XssFilter;
import com.smoc.cloud.admin.security.verifycode.VerifyCodeInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.HashMap;
import java.util.Map;


/**
 * 配置系统拦截器、过滤器、servlet类
 * Description:配置拦截器、过滤器、servlet
 */
@Slf4j
@Configuration
public class MpmFilterConfiguration {

    /**
     * xss过滤拦截器
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {

        log.info("[系统启动][注册过滤拦截器]数据:注册xss过滤拦截器Filter：XssFilter");
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<XssFilter>();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap();
        initParameters.put("excludes", "/static/*");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }



    /**
     * 初始化 加载系统数据的 servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<MpmDataInitializer> servletRegistrationBean() {

        log.info("[系统启动][系统数据初始化]数据:MpmDataInitializer");
        ServletRegistrationBean<MpmDataInitializer> servlet = new ServletRegistrationBean<MpmDataInitializer>(
                new MpmDataInitializer(), "/mpmDataInitializer");
        servlet.setLoadOnStartup(10);

        return servlet;
    }

    /**
     * 加载系统图形验证码 servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<VerifyCodeInitializer> verifyCodeRegistration() {

        log.info("注册图形验证码生成器 servlet：VerifyCodeInitializer");
        ServletRegistrationBean<VerifyCodeInitializer> servlet = new ServletRegistrationBean<VerifyCodeInitializer>(
                new VerifyCodeInitializer(), "/verifyCode/*");
        servlet.setLoadOnStartup(10);
        return servlet;
    }


    //显示声明CommonsMultipartResolver为mutipartResolver
    /*@Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(2048);
        resolver.setMaxUploadSize(50*1024*1024);//上传文件大小 50M 50*1024*1024
        return resolver;
    }*/
}
