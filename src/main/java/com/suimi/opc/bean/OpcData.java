/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.bean;

import java.util.ArrayList;
import java.util.List;

public class OpcData {



    private String groupName;

    private String groupId;

    private List<ItemBean> items = new ArrayList<ItemBean>();

    private List<OpcData> subGroups = new ArrayList<OpcData>();

    public OpcData(String groupId, String groupName, List<ItemBean> items) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.items = items;
    }

    public OpcData( String groupId,String groupName, List<ItemBean> items, List<OpcData> subGroups) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.items = items;
        this.subGroups = subGroups;
    }

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

    public List<OpcData> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<OpcData> subGroups) {
        this.subGroups = subGroups;
    }

    @Override
    public String toString() {
        return "OpcData{" +
                "groupName='" + groupName + '\'' +
                ", items=" + items +
                '}';
    }
}
