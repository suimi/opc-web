/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.opc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author suimi
 * @date 2018/12/7
 */
@SpringBootApplication public class OpcApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(OpcApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OpcApplication.class);
    }

}
