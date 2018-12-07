/*
 * Copyright (c) 2013-2015
 * Created by lichengcai on 2016-07-07.
 */

package com.suimi.opc.controllers;

import com.suimi.opc.bean.OpcData;
import com.suimi.opc.services.OpcReadService;
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
    public OpcReadService opcReadService;

    @RequestMapping(value = "readData")
    public ModelAndView readData(String groupId) {
        OpcData opcResponse = opcReadService.readData(groupId);
        return new ModelAndView("data", "data", opcResponse);
    }

    @RequestMapping(value = "ajaxReadData")
    @ResponseBody
    public OpcData ajaxReadData(String groupId) {
        return opcReadService.readData(groupId);
    }

    @RequestMapping(value = "getGroups")
    public ModelAndView getGroups() {
        List<Server> servers = opcReadService.getGroups();
        return new ModelAndView("groups", "servers", servers);
    }

}
