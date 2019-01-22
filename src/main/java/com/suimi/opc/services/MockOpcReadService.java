/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.services;

import com.suimi.opc.bean.OpcData;
import com.suimi.opc.services.config.OpcConfig;
import com.suimi.opc.services.config.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("test")
public class MockOpcReadService implements IOpcReadService {

    private static final Logger logger = LoggerFactory.getLogger(MockOpcReadService.class);

    @Autowired
    private OpcConfig opcConfig;

    @Override public List<Server> getGroups() {
        return opcConfig.getServers();
    }

    @Override public OpcData readData(String groupId) {
        return null;
    }


}
