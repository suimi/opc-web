/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.services.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class OpcConfig {

    private static final Logger logger = LoggerFactory.getLogger(OpcConfig.class);

    @javax.annotation.Resource
    private List<Resource> serverDefineResources;

    private List<Server> servers = new ArrayList<Server>();

    @PostConstruct
    private synchronized void initActionDefine() {
        for (Resource actionDefineResource : serverDefineResources) {
            JAXBContext context = null;
            InputStream inputStream = null;
            try {
                context = JAXBContext.newInstance(Server.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                inputStream = actionDefineResource.getInputStream();
                Server server = (Server)unmarshaller.unmarshal(inputStream);
                servers.add(server);
            } catch (JAXBException e) {
                logger.error("unmarshal server define file error", e);
            } catch (IOException e) {
                logger.error("open server define file error", e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    public List<Resource> getServerDefineResources() {
        return serverDefineResources;
    }

    public void setServerDefineResources(List<Resource> serverDefineResources) {
        this.serverDefineResources = serverDefineResources;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }
}
