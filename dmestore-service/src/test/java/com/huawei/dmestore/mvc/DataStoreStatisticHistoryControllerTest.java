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
 * @className DataStoreStatisticHistoryControllerTest
 * @description TODO
 * @date 2020/11/17 18:02
 */
public class DataStoreStatisticHistoryControllerTest {

    DataStoreStatisticHistoryController dataStoreStatisticHistoryController;
    private Gson gson = new Gson();
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        dataStoreStatisticHistoryController = new DataStoreStatisticHistoryController();
        mockMvc = MockMvcBuilders.standaloneSetup(dataStoreStatisticHistoryController).build();
    }

    @Test
    public void getVmfsVolumeStatistic() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/vmfs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getVolumeStatistic() throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/volume").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getNfsVolumeStatistic() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/nfs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getFsStatistic()throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/fs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getServiceLevelStatistic()throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/servicelevel").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getServiceLevelLunStatistic()throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/servicelevelLun").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getServiceLevelStoragePoolStatistic() throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/servicelevelStoragePool").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragePoolStatistic() throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storagePool").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragePoolCurrentStatistic()throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storagePoolCurrent").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragDeviceStatistic() throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storageDevice").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragDeviceCurrentStatistic() throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storageDeviceCurrent").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getControllerStatistic() throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/controller").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getControllerCurrenStatistic()throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/controllerCurrent").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragPortStatistic() throws Exception{
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storagePort").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragPortCurrentStatistic()throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storagePortCurrent").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragDiskStatistic()throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storageDisk").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStoragDiskCurrentStatistic()throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/datastorestatistichistrory/storageDiskCurrent").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}