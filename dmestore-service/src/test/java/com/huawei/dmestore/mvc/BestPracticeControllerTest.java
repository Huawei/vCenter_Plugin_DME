package com.huawei.dmestore.mvc;

import com.huawei.dmestore.model.BestPracticeUpdateByTypeRequest;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lianq
 * @className BestPracticeControllerTest
 * @description TODO
 * @date 2020/11/17 18:03
 */
public class BestPracticeControllerTest {

    BestPracticeController bestPracticeController;
    private Gson gson = new Gson();
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        bestPracticeController = new BestPracticeController();
        mockMvc = MockMvcBuilders.standaloneSetup(bestPracticeController).build();
    }

    @Test
    public void bestPractice() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/bestpractice/check").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getBestPracticeRecords() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/bestpractice/records/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getBySettingAndPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/bestpractice/records/bypage").contentType(MediaType.APPLICATION_JSON)
                .param("hostSetting","321")
                .param("pageNo","321")
                .param("pageSize","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void bylist() throws Exception{
        List<BestPracticeUpdateByTypeRequest> list = new ArrayList<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/bestpractice/update/bylist").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(list)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void upByHostSetting()throws Exception {
        BestPracticeUpdateByTypeRequest request = new BestPracticeUpdateByTypeRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/bestpractice/update/bytype").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void upByHosts()throws Exception {
        List<String> list = new ArrayList<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/bestpractice/update/byhosts").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(list)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void upAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/bestpractice/update/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void upByCluster() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/bestpractice/update/byCluster").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}