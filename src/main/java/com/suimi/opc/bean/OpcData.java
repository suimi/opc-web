/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.bean;

import java.util.ArrayList;
import java.util.List;

public class OpcData {

    public OpcData(String groupId, String groupName, List<ItemBean> items) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.items = items;
    }

    private String groupName;

    private String groupId;

    private List<ItemBean> items = new ArrayList<ItemBean>();

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ItemBean> getItems() {
        return items;
    }

    public void setItems(List<ItemBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OpcData{" +
                "groupName='" + groupName + '\'' +
                ", items=" + items +
                '}';
    }
}
