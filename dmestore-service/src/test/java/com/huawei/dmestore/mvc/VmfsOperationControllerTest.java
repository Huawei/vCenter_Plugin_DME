package com.huawei.dmestore.mvc;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author lianq
 * @className VmfsOperationControllerTest
 * @description TODO
 * @date 2020/11/17 9:15
 */
public class VmfsOperationControllerTest {

    VmfsOperationController vmfsOperationController;
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        vmfsOperationController = new VmfsOperationController();
        mockMvc = MockMvcBuilders.standaloneSetup(vmfsOperationController).build();
    }

    @Test
    public void updateVmfs() throws Exception {
        String param = "{\n" +
            "    \"name\": \"Dvmfs999\", \n" +
            "    \"isSameName\": true, \n" +
            "    \"volumeId\": \"d6a20f27-fe4c-4a40-ac28-529aad1a7550\", \n" +
            "    \"control_policy\": \"\", \n" +
            "    \"max_iops\": null, \n" +
            "    \"maxiopsChoose\": false, \n" +
            "    \"max_bandwidth\": null, \n" +
            "    \"maxbandwidthChoose\": false, \n" +
            "    \"newVoName\": \"Dvmfs999\", \n" +
            "    \"oldDsName\": \"Dvmfs99\", \n" +
            "    \"newDsName\": \"Dvmfs999\", \n" +
            "    \"min_iops\": null, \n" +
            "    \"miniopsChoose\": false, \n" +
            "    \"min_bandwidth\": null, \n" +
            "    \"minbandwidthChoose\": false, \n" +
            "    \"dataStoreObjectId\": \"urn:vmomi:Datastore:datastore-1233:674908e5-ab21-4079-9cb1-596358ee5dd1\", \n" +
            "    \"service_level_name\": \"lgqtest\", \n" +
            "    \"latency\": null, \n" +
            "    \"latencyChoose\": false\n" +
            "}";
        String[] params = {"d6a20f27-fe4c-4a40-ac28-529aad1a7550", ""};
        for (int i = 0; i < params.length; i++) {
            mockMvc.perform(
                MockMvcRequestBuilders.put("/operatevmfs/updatevmfs").contentType(MediaType.APPLICATION_JSON)
                    .content(param)
                    .param("volumeId", params[i]))
                .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Test
    public void expandVmfs() throws Exception {
        String param = "{\n" +
            "    \"vo_add_capacity\": 2, \n" +
            "    \"capacityUnit\": \"GB\", \n" +
            "    \"volume_id\": \"d6a20f27-fe4c-4a40-ac28-529aad1a7550\", \n" +
            "    \"ds_name\": \"Dvmfs999\", \n" +
            "    \"obj_id\": \"urn:vmomi:Datastore:datastore-1233:674908e5-ab21-4079-9cb1-596358ee5dd1\"\n" +
            "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/operatevmfs/expandvmfs").contentType(MediaType.APPLICATION_JSON)
            .content(param)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void recycleVmfs() throws Exception {
        String param = "[\"Dvmfs999\"]";
        mockMvc.perform(MockMvcRequestBuilders.post("/operatevmfs/recyclevmfs").contentType(MediaType.APPLICATION_JSON)
            .content(param))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void recycleVmfsByDatastoreIds() throws Exception {
        String param = "[\"Dvmfs999\"]";
        mockMvc.perform(MockMvcRequestBuilders.post("/operatevmfs/recyclevmfsbydatastoreids")
            .contentType(MediaType.APPLICATION_JSON)
            .content(param)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listServiceLevelVmfs() throws Exception {
        String param = "{\n" +
            "    \"vo_add_capacity\": 2, \n" +
            "    \"capacityUnit\": \"GB\", \n" +
            "    \"volume_id\": \"d6a20f27-fe4c-4a40-ac28-529aad1a7550\", \n" +
            "    \"ds_name\": \"Dvmfs999\", \n" +
            "    \"obj_id\": \"urn:vmomi:Datastore:datastore-1233:674908e5-ab21-4079-9cb1-596358ee5dd1\"\n" +
            "}";
        mockMvc.perform(
            MockMvcRequestBuilders.put("/operatevmfs/listvmfsservicelevel").contentType(MediaType.APPLICATION_JSON)
                .content(param)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateServiceLevelVmfs() throws Exception {
        String param = "{\n" +
            "    \"vo_add_capacity\": 2, \n" +
            "    \"capacityUnit\": \"GB\", \n" +
            "    \"volume_id\": \"d6a20f27-fe4c-4a40-ac28-529aad1a7550\", \n" +
            "    \"ds_name\": \"Dvmfs999\", \n" +
            "    \"obj_id\": \"urn:vmomi:Datastore:datastore-1233:674908e5-ab21-4079-9cb1-596358ee5dd1\"\n" +
            "}";
        mockMvc.perform(
            MockMvcRequestBuilders.post("/operatevmfs/updatevmfsservicelevel").contentType(MediaType.APPLICATION_JSON)
                .content(param)).andExpect(MockMvcResultMatchers.status().isOk());
    }
}