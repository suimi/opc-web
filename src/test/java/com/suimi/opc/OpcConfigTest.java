/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.suimi.opc.services.OpcReadService;
import com.suimi.opc.services.config.OpcConfig;
import com.suimi.opc.services.config.Server;

@ContextConfiguration("/spring-config.xml")
public class OpcConfigTest extends AbstractJUnit4SpringContextTests {
    private static final Logger logger = LoggerFactory.getLogger(OpcConfigTest.class);

    @Autowired
    private OpcConfig opcConfig;

    @Autowired
    private OpcReadService opcReadService;

    @Test
    public void test() {
        List<Server> servers = opcConfig.getServers();
        logger.debug(servers.toString());
    }

    public void testRead() {
        opcReadService.readRemoteAll();
        opcReadService.printData(null);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                opcReadService.readRemoteAll();
                opcReadService.printData(null);
            }
        });
        thread.start();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
