/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.bean;

import com.suimi.opc.services.config.Item;

public class ItemBean {

    public ItemBean(Item item, String value) {
        this.define = item.getDefine();
        this.unit = item.getUnit();
        this.order = item.getOrder();
        this.id = item.getPath() + item.getName();
        this.value = value;
    }

    private int order;

    private String id;

    private String define;

    private String unit;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getDefine() + " : " + getValue() + getUnit();
    }
}
