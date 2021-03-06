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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.suimi.opc.bean.ItemBean;
import com.suimi.opc.bean.OpcData;
import com.suimi.opc.services.config.Group;
import com.suimi.opc.services.config.Item;
import com.suimi.opc.services.config.OpcConfig;
import com.suimi.opc.services.config.Server;

import javafish.clients.opc.JOpc;
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
public class OpcReadService2 {

    private static final Logger logger = LoggerFactory.getLogger(OpcReadService2.class);

    @Autowired
    private OpcConfig opcConfig;

    private Map<String, OpcData> cacheData = new HashMap<String, OpcData>();

    private Map<String, JOpc> groupOpc = new HashMap<String, JOpc>();

    private Map<String, OpcGroup> opcGroupCache = new HashMap<String, OpcGroup>();

    private Map<String, Group> defineGroup = new HashMap<String, Group>();

    private List<JOpc> opcList = new ArrayList<JOpc>();

    private long timestamp = 0L;

    @Value("${updateTimes}")
    private long updateTimes;


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
            JOpc jOpc = new JOpc(server.getIp(), server.getName(), id);
            for (Group group : server.getGroups()) {
                OpcGroup opcGroup = new OpcGroup(group.getName() + group.getId(), true, 1000, 0.0f);
                for (Item item : group.getItems()) {
                    OpcItem opcItem = new OpcItem(item.getName(), true, item.getPath());
                    opcGroup.addItem(opcItem);
                }
                opcGroupCache.put(group.getId(), opcGroup);
                jOpc.addGroup(opcGroupCache.get(group.getId()));
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
        if (System.currentTimeMillis() - timestamp > updateTimes) {
            readRemoteAll();
            printData(null);
        }
        return cacheData.get(groupId);
    }

    public synchronized void readRemoteAll() {
        if (System.currentTimeMillis() - timestamp > updateTimes) {
            init();
            for (String groupId : defineGroup.keySet()) {
                readRemoteData(groupId);
            }
            timestamp = System.currentTimeMillis();
            destroy();
        }
    }

    public void readRemoteData(String groupId) {
        logger.info("read remote data:{}", groupId);
        JOpc jOpc = groupOpc.get(groupId);
        OpcGroup opcGroup = this.opcGroupCache.get(groupId);
        Group defineGroup = this.defineGroup.get(groupId);
        OpcGroup resultGroup = null;
        try {
            logger.debug("opc server:{}", jOpc.getServerProgID());
            logger.debug("opc group:{}", opcGroup.getGroupName());
            resultGroup = jOpc.synchReadGroup(opcGroup);
            ArrayList<OpcItem> items = resultGroup.getItems();
            Map<String, OpcItem> itemMap = new HashMap<String, OpcItem>();
            for (OpcItem item : items) {
                itemMap.put(item.getAccessPath() + item.getItemName(), item);
            }
            List<Item> defineItems = defineGroup.getItems();
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
                    itemBeans.add(defineItem.getOrder() - 1, new ItemBean(defineItem, String.format("%.2f", Double.valueOf(strValue))));
                }
            }
            cacheData.put(groupId, new OpcData(groupId, defineGroup.getName(), itemBeans));
        } catch (SynchReadException e) {
            logger.error("读取失败", e);
        }
    }

    private void printData(String groupId) {
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

    private void destroy() {
        try {
            for (JOpc opc : opcList) {
                opc.unregisterGroups();
            }
            JOpc.coUninitialize();
            groupOpc.clear();
            opcGroupCache.clear();
            defineGroup.clear();
            opcList.clear();
        } catch (CoUninitializeException e) {
            logger.error("uninitialize 出错", e);
        } catch (UnableRemoveGroupException e) {
            logger.error("destroy 出错", e);
        }
        logger.info("uninitialize");
    }
}
