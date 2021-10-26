package com.smoc.cloud.admin.configuration;

import com.smoc.cloud.admin.tag.dialect.MpmDictTagDialect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.HashSet;
import java.util.Set;


/**
 * spring mvc 配置类
 * Description:配置spring mvc设置视图解析器、静态路径等等
 */
@Slf4j
@Configuration
public class MpmSpringMvcAppConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 设置视图解析器
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {

        log.info("[系统启动][设置视图解析器]数据:ThymeleafViewResolver");
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine());
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.setCacheLimit(100);
        thymeleafViewResolver.setContentType("text/html");
        thymeleafViewResolver.setRedirectHttp10Compatible(false);
        return thymeleafViewResolver;
    }

    /**
     * 设置模板引擎
     * 模板引擎增加SpringSecurityDialect
     * 让模板能用到sec前缀，获取spring security的内容
     *
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine() {

        log.info("[系统启动][设置模板引擎]数据:SpringTemplateEngine");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        SpringSecurityDialect securityDialect = new SpringSecurityDialect();
        Set<IDialect> dialects = new HashSet<>();
        log.info("[系统启动][设置安全标签]数据:设置安全标签及自定义标签SpringSecurityDialect、MpmDictTagDialect");
        //安全标签
        dialects.add(securityDialect);
        //自定义字典标签
        dialects.add(new MpmDictTagDialect());
        engine.setAdditionalDialects(dialects);
        engine.setTemplateResolver(templateResolver());
        //允许在内容中使用spring EL表达式
        engine.setEnableSpringELCompiler(true);

        return engine;
    }

    /**
     * 模板解析引擎
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {

        log.info("[系统启动][模板解析引擎]数据:SpringResourceTemplateResolver,classpath:/templates/");
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);
        templateResolver.setTemplateMode("HTML");
        return templateResolver;
    }

    /**
     * 配置静态资源加载
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub

        log.info("[系统启动][配置静态资源]数据:/static/**");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

        super.addResourceHandlers(registry);
    }

    /**
     * 对静态资源配置生效 注意和@EnableWebMvc冲突，也不是冲突，是有你没我，有我没你的概念
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * URL匹配规则 setUseSuffixPatternMatch : 设置是否是后缀模式匹配，如“/user”是否匹配/user.*，默认真即匹配；
     * setUseTrailingSlashMatch : 设置是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”，默认真即匹配；
     */
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {

        log.info("[系统启动][设置URL匹配规则]数据:RequestMappingHandlerMapping");
        RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        requestMappingHandlerMapping.setUseTrailingSlashMatch(true);

        return requestMappingHandlerMapping;
    }



}
