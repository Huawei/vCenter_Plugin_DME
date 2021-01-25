package com.huawei.dmestore.mvc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lianq
 * @className DmeStorageControllerTest
 * @description TODO
 * @date 2020/11/11 14:08
 */
public class DmeStorageControllerTest {

    DmeStorageController dmeStorageController;
    MockMvc mockMvc;
    Map<String, Object> params;

    @Before
    public void setUp() throws Exception {
        System.out.println("======== 开始执行：setUp ========");
        dmeStorageController = new DmeStorageController();
        params = new HashMap<>();
        mockMvc = MockMvcBuilders.standaloneSetup(dmeStorageController).build();
        System.out.println("======== 执行完毕：setUp ========");
    }

    @Test
    public void getStorages() throws Exception {
        System.out.println("======== 开始执行：getStorages ========");
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/storages").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        System.out.println("======== 执行完毕：getStorages ========");
    }

    @Test
    public void getStorageDetail() throws Exception {

        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        params.put("storageId", storageId);
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/storage").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getStoragePools() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/storagepools").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getLogicPorts() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/logicports").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getVolumesByPage() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/volumes/byPage").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getFileSystems() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/filesystems").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDtrees() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/dtrees").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getNfsShares() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/nfsshares").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getBandPorts() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/bandports",storageId).contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStorageControllers() throws Exception {
        String storageDeviceId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/storagecontrollers").contentType(MediaType.APPLICATION_JSON)
                .param("storageDeviceId",storageDeviceId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStorageDisks() throws Exception {
        String storageDeviceId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/storagedisks").contentType(MediaType.APPLICATION_JSON)
                .param("storageDeviceId",storageDeviceId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStorageEthPorts() throws Exception {
        String storageSn = "2102351QLH9WK5800028";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/getstorageethports").contentType(MediaType.APPLICATION_JSON)
                .param("storageSn",storageSn))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragePort() throws Exception {
        String storageDeviceId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/storageport").contentType(MediaType.APPLICATION_JSON)
                .param("storageDeviceId",storageDeviceId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getFailoverGroups() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/failovergroups").contentType(MediaType.APPLICATION_JSON)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listStoragePerformance() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        List<String> storageIds = new ArrayList<>();
        storageIds.add(storageId);
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/liststorageperformance").contentType(MediaType.APPLICATION_JSON)
                .param("storageIds",storageIds.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listStoragePoolPerformance() throws Exception {
        String storagePoolId = "1";
        List<String> storagePoolIds = new ArrayList<>();
        storagePoolIds.add(storagePoolId);
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/liststoragepoolperformance").contentType(MediaType.APPLICATION_JSON)
                .param("storagePoolIds",storagePoolIds.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listStorageControllerPerformance() throws Exception {
        String storageControllerId = "6418D9B890E63A3D8BECAE4CBBA6A930";
        List<String> storageControllerIds = new ArrayList<>();
        storageControllerIds.add(storageControllerId);
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/listStorageControllerPerformance").contentType(MediaType.APPLICATION_JSON)
                .param("storageControllerIds",storageControllerIds.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listStorageDiskPerformance() throws Exception {
        String storageDiskId = "26EC1729E88A390F8F3B93CBFFC3F5BC";
        List<String> storageDiskIds = new ArrayList<>();
        storageDiskIds.add(storageDiskId);
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/listStorageDiskPerformance").contentType(MediaType.APPLICATION_JSON)
                .param("storageDiskIds",storageDiskIds.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listStoragePortPerformance() throws Exception {
        String storagePortId = "44461822A44B37C980860A8217375130";
        List<String> storagePortIds = new ArrayList<>();
        storagePortIds.add(storagePortId);
        mockMvc.perform(MockMvcRequestBuilders.post("/dmestorage/listStoragePortPerformance").contentType(MediaType.APPLICATION_JSON)
                .param("storagePortIds",storagePortIds.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listVolumesPerformance() throws Exception {
        String volume = "cd1f832f-5170-451f-b878-07f9dae9a16b";
        List<String> volumeId = new ArrayList<>();
        volumeId.add(volume);
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/listVolumesPerformance").contentType(MediaType.APPLICATION_JSON)
                .param("volumeId",volumeId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void queryVolumeByName() throws Exception {
        String name = "cd1f832f-5170-451f-b878-07f9dae9a16b";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/queryvolumebyname").contentType(MediaType.APPLICATION_JSON)
                .param("name",name))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void queryFsByName() throws Exception {
        String name = "1026002";
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/queryfsbyname").contentType(MediaType.APPLICATION_JSON)
                .param("name",name)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void queryShareByName() throws Exception {
        String name = "/1029001";
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        mockMvc.perform(MockMvcRequestBuilders.get("/dmestorage/querysharebyname").contentType(MediaType.APPLICATION_JSON)
                .param("name",name)
                .param("storageId",storageId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}