package com.suimi.opc.services;

import com.suimi.opc.bean.OpcData;
import com.suimi.opc.services.config.Server;

import java.util.List;

/**
 * @author suimi
 * @date 2019/1/22
 */
public interface IOpcReadService {
    List<Server> getGroups();

    OpcData readData(String groupId);
}
