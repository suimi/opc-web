/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.controllers;

import com.suimi.opc.bean.OpcData;
import com.suimi.opc.services.OpcReadService2;
import com.suimi.opc.services.config.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "opc")
public class OpcController {

    @Autowired
    public OpcReadService2 opcReadService2;

    @RequestMapping(value = "readData")
    public ModelAndView readData(String groupId) {
        OpcData opcResponse = opcReadService2.readData(groupId);
        return new ModelAndView("data", "data", opcResponse);
    }

    @RequestMapping(value = "ajaxReadData")
    @ResponseBody
    public OpcData ajaxReadData(String groupId) {
        return opcReadService2.readData(groupId);
    }

    @RequestMapping(value = "getGroups")
    public ModelAndView getGroups() {
        List<Server> servers = opcReadService2.getGroups();
        return new ModelAndView("groups", "servers", servers);
    }

}
