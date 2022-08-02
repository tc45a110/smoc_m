package com.smoc.cloud.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;


@Slf4j
@Configuration
public class MultipartConfig {

    @Value("${spring.servlet.multipart.location}")
    private String tmpLocation;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File savefile = new File(tmpLocation) ;
        if (!savefile.exists())
            savefile.mkdirs();

        factory.setLocation(tmpLocation);
        return factory.createMultipartConfig();
    }
}
