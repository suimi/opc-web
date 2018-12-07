package com.suimi.opc.services.example;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.CoInitializeException;
import javafish.clients.opc.exception.CoUninitializeException;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;

public class SynchReadGroupExample {
  public static void main(String[] args) throws InterruptedException {

    try {
      JOpc.coInitialize();
    }
    catch (CoInitializeException e1) {
      e1.printStackTrace();
    }
    
    JOpc jopc = new JOpc("172.16.1.50", "Freelance2000OPCServer.58.1","Jopc3");
    
    OpcItem item1 = new OpcItem("I_1XRFLZXQ", true, "PS");

    OpcGroup group = new OpcGroup("group1", true, 10, 0.0f);

    group.addItem(item1);


    try {
      jopc.connect();
      jopc.addGroup(group);
      System.out.println("JOPC client is connected...");
    }
    catch (ConnectivityException e2) {
      e2.printStackTrace();
    }
    
    try {
      jopc.registerGroups();
      System.out.println("OPCGroup are registered...");
    }
    catch (UnableAddGroupException e2) {
      e2.printStackTrace();
    }
    catch (UnableAddItemException e2) {
      e2.printStackTrace();
    }
    
//    synchronized(test) {
//      test.wait(2000);
//    }
    
    // Synchronous reading of group
    int cycles = 100;
    int acycle = 0;
    while (acycle++ < cycles) {
//      synchronized(test) {
//        test.wait(50);
//      }

      try {
        OpcGroup responseGroup = jopc.synchReadGroup(group);
        System.out.println(responseGroup);
      }
      catch (ComponentNotFoundException e1) {
        e1.printStackTrace();
      }
      catch (SynchReadException e1) {
        e1.printStackTrace();
      }
    }
    
    try {
      JOpc.coUninitialize();
    }
    catch (CoUninitializeException e) {
      e.printStackTrace();
    }
  }
}
