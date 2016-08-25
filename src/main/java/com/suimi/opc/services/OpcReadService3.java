/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suimi.opc.bean.ItemBean;
import com.suimi.opc.bean.OpcData;
import com.suimi.opc.services.config.Group;
import com.suimi.opc.services.config.Item;
import com.suimi.opc.services.config.OpcConfig;
import com.suimi.opc.services.config.Server;

import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.asynch.AsynchEvent;
import javafish.clients.opc.asynch.OpcAsynchGroupListener;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.CoInitializeException;
import javafish.clients.opc.exception.CoUninitializeException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import javafish.clients.opc.variant.Variant;

@Service
public class OpcReadService3 {

    private static final Logger logger = LoggerFactory.getLogger(OpcReadService3.class);

    @Autowired
    private OpcConfig opcConfig;

    private Map<String, OpcData> cacheData = new HashMap<String, OpcData>();

    private Map<String, JOpc> groupOpc = new HashMap<String, JOpc>();

    private Map<String, OpcGroup> opcGroupCache = new HashMap<String, OpcGroup>();

    private Map<String, Group> defineGroup = new HashMap<String, Group>();

    private List<JOpc> opcList = new ArrayList<JOpc>();

    private long timestamp = 0L;


    @PostConstruct
    private void init() {
        try {
            JOpc.coInitialize();
            logger.info("init");
        } catch (CoInitializeException e) {
            logger.error("初始化出错", e);
        }
        List<Server> servers = opcConfig.getServers();
        for (Server server : servers) {
            String id = server.getName() + "@" + server.getIp();
            JOpc jOpc = new JEasyOpc(server.getIp(), server.getName(), id);
            for (Group group : server.getGroups()) {
                OpcGroup opcGroup = new OpcGroup(group.getId(), true, 1000, 0.0f);
                for (Item item : group.getItems()) {
                    OpcItem opcItem = new OpcItem(item.getName(), true, item.getPath());
                    opcGroup.addItem(opcItem);
                }
                opcGroupCache.put(group.getId(), opcGroup);
                opcGroup.addAsynchListener(new OpcAsynchGroupListener() {
                    @Override
                    public void getAsynchEvent(AsynchEvent event) {
                        JOpc jopc = (JOpc)event.getSource();
                        OpcGroup eventOPCGroup = event.getOPCGroup();
                        logger.info("asynch event, opc:{}, group:{}", jopc.getFullOpcServerName(), eventOPCGroup.getGroupName());
                        String groupName = eventOPCGroup.getGroupName();
                        OpcGroup opcGroup = opcGroupCache.get(groupName);
                        Group deGroup = defineGroup.get(groupName);
                        try {
                            OpcGroup resultGroup = jopc.synchReadGroup(opcGroup);
                            ArrayList<OpcItem> items = resultGroup.getItems();
                            Map<String, OpcItem> itemMap = new HashMap<String, OpcItem>();
                            for (OpcItem item : items) {
                                itemMap.put(item.getAccessPath() + item.getItemName(), item);
                            }
                            List<Item> defineItems = deGroup.getItems();
                            List<ItemBean> itemBeans = new ArrayList<ItemBean>();
                            for (Item defineItem : defineItems) {
                                String id = defineItem.getPath() + defineItem.getName();
                                OpcItem opcItem = itemMap.get(id);
                                if (opcItem != null) {
                                    logger.debug("id:{}, value:{}", id, opcItem.getValue());
                                    Variant value = opcItem.getValue();
                                    String strValue = value.getString();
                                    if (StringUtils.isBlank(strValue)) {
                                        strValue = "0";
                                    }
                                    itemBeans.add(new ItemBean(defineItem, String.format("%.2f", Double.valueOf(strValue))));
                                } else {
                                    logger.warn("item({}) is null.", id);
                                }
                            }
                            cacheData.put(groupName, new OpcData(deGroup.getName(), itemBeans));
                        } catch (SynchReadException e) {
                            logger.error("读取失败", e);
                        }
                    }
                });
                jOpc.addGroup(opcGroup);
                jOpc.start();
                groupOpc.put(group.getId(), jOpc);
                defineGroup.put(group.getId(), group);
            }
            try {
                jOpc.connect();
                jOpc.registerGroups();
                logger.info("connect opc server:{}", id);
            } catch (UnableAddGroupException e) {
                logger.error("初始化出错" + id, e);
            } catch (UnableAddItemException e) {
                logger.error("初始化出错" + id, e);
            } catch (ConnectivityException e) {
                logger.error("初始化出错" + id, e);
            }
        }
    }

    public List<Server> getGroups() {
        return opcConfig.getServers();
    }

    public OpcData readData(String groupId) {
        return cacheData.get(groupId);
    }

    public void printData(String groupId) {
        if (StringUtils.isNotBlank(groupId)) {
            OpcData opcData = cacheData.get(groupId);
            if (opcData == null) {
                logger.info("data: {}", groupId);
            } else {
                logger.info("data: {}", opcData.toString());
            }
        } else {
            for (Entry<String, OpcData> entry : cacheData.entrySet()) {
                logger.info("data: {}", entry.getValue().toString());
            }
        }
    }

    @PreDestroy
    private void destroy() {
        try {
            for (JOpc opc : opcList) {
                opc.unregisterGroups();
            }
            JOpc.coUninitialize();
        } catch (CoUninitializeException e) {
            logger.error("uninitialize 出错", e);
        } catch (UnableRemoveGroupException e) {
            logger.error("destroy 出错", e);
        }
        logger.info("uninitialize");
    }
}
