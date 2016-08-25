package com.suimi.opc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.suimi.opc.services.config.Group;
import com.suimi.opc.services.config.Item;
import com.suimi.opc.services.config.Server;

public class ExcelUtilTest {
    @Test
    public void testGetExcelContext() throws FileNotFoundException, JAXBException {
        InputStream is = new FileInputStream("C:\\Users\\cxkl\\Desktop\\微信平台显示DCS数据整理.xlsx");
        JSONObject obj = ExcelUtil.getExcelContext(is);
        Integer size = obj.getInteger("size");
        Server server = new Server();
        int i = 0;
        while (i < size) {
            JSONObject subObj = obj.getJSONObject("" + i++);
            String sheetName = subObj.getString("sheetName");
            System.out.println(sheetName);
            Integer rows = subObj.getInteger("size");
            Group group = new Group();
            group.setDefine(sheetName);
            int r = 1;
            while (r <= rows) {
                System.out.println(r);
                JSONObject rowJson = subObj.getJSONObject("" + r);
                Item item = new Item();
                item.setName(rowJson.getString("2"));
                item.setPath(rowJson.getString("5"));
                item.setDefine(rowJson.getString("1"));
                item.setUnit(rowJson.getString("3"));
                item.setOrder(r);
                group.getItems().add(item);
                System.out.println(rowJson.toJSONString());
                r++;
            }
            server.getGroups().add(group);
        }
        JAXBContext context = JAXBContext.newInstance(Server.class);
        Marshaller unmarshaller = context.createMarshaller();
        unmarshaller.marshal(server,System.out);
        Assert.assertNotNull(obj);
    }
}
