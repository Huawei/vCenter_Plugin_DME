package com.huawei.dmestore.mvc;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lianq
 * @className NfsOperationControllerTest
 * @description TODO
 * @date 2020/11/11 10:38
 */
public class NfsOperationControllerTest {

    Gson gson=new Gson();
    NfsOperationController controller;
    MockMvc mockMvc;
    Map<String, Object> params;

    @Before
    public void setUp() throws Exception {
        controller = new NfsOperationController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        params = new HashMap<>();
    }

    @Test
    public void createNfsDatastore() throws Exception {
        params.put("storagId","b94bff9d-0dfb-11eb-bd3d-0050568491c9");
        params.put("storagePoolId","0");
        params.put("poolRawId","1");
        params.put("currentPortId","139269");
        params.put("nfsName","lqtestnfs00007");
        params.put("sameName",true);
        params.put("size","1");
        params.put("type","NFS");
        params.put("advance",true);//false
        params.put("qosFlag",true);//flase
        params.put("contolPolicy","up");
        params.put("maxBandwidth","2");
        params.put("maxIops","1");
        params.put("thin",true);
        params.put("deduplicationEnabled",false);
        params.put("compressionEnabled",false);
        params.put("autoSizeEnable",false);
        params.put("vkernelIp","192.168.200.13");
        params.put("hostObjectId","urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1");
        params.put("accessMode","readWrite");
        mockMvc.perform(MockMvcRequestBuilders.post("/operatenfs/createnfsdatastore").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(params))).andExpect(MockMvcResultMatchers.status().isOk());
    }
    /**
     * {
     *      "dataStoreObjectId":"urn:vmomi:Datastore:datastore-1060:674908e5-ab21-4079-9cb1-596358ee5dd1" ,
     *      "nfsName": "fs0001",
     *      "fileSystemId":"0C9A60E0A51C3AD38567C21B6881371C",
     *      "autoSizeEnable": true,
     *      "deduplicationEnabled": "false",
     *      "compressionEnabled": "false",
     *      "thin": "thin",
     *      "maxBandwidth": "1",
     *      "maxIops": "2"
     *      "shareId":"70C9358F595B3AA5A1DB2464F72AF0DA"
     *      "advance"false true  true 是有高级选项
     *      "qosFlag"qos策略开关 false true false关闭
     *      "contolPolicy"上下线选择标记  枚举值 up low
     *
     * }
     * @param
     * @return
     */
    @Test
    public void updateNfsDatastore() throws Exception {
        params.put("dataStoreObjectId","urn:vmomi:Datastore:datastore-1060:674908e5-ab21-4079-9cb1-596358ee5dd1");
        params.put("nfsName","lqtestnfs00007");
        params.put("fileSystemId","0C9A60E0A51C3AD38567C21B6881371C");
        params.put("deduplicationEnabled",false);
        params.put("compressionEnabled",false);
        params.put("autoSizeEnable",false);
        params.put("contolPolicy","low");
        params.put("maxBandwidth","2");
        params.put("maxIops","1");
        params.put("thin",true);
        params.put("qosFlag",true);//flase
        params.put("shareId","70C9358F595B3AA5A1DB2464F72AF0DA");
        params.put("advance", true);

        mockMvc.perform(MockMvcRequestBuilders.post("/operatenfs/updatenfsdatastore").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(params))).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void changeNfsCapacity() throws Exception {

        params.put("capacity",2.0);
        params.put("expand",true);
        params.put("fileSystemId","0C9A60E0A51C3AD38567C21B6881371C");
        params.put("storeObjectId","0C9A60E0A51C3AD38567C21B6881371C");
        mockMvc.perform(MockMvcRequestBuilders.put("/operatenfs/changenfsdatastore").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(params))).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getEditNfsStore() throws Exception {
        
        String storeObjectId = "urn:vmomi:Datastore:datastore-1060:674908e5-ab21-4079-9cb1-596358ee5dd1";
        mockMvc.perform(MockMvcRequestBuilders.get("/operatenfs/editnfsstore").contentType(MediaType.APPLICATION_JSON)
                .param("storeObjectId",storeObjectId)).andExpect(MockMvcResultMatchers.status().isOk());
    }


}