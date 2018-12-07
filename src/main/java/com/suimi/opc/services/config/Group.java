/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.services.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Group {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String define;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<Item> items = new ArrayList<Item>();

    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    private List<Group> groups = new ArrayList<Group>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
