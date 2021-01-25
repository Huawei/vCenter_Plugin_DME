package com.huawei.dmestore.mvc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author lianq
 * @className OverviewControllerTest
 * @description TODO
 * @date 2020/11/17 15:49
 */
public class OverviewControllerTest {

    private OverviewController overviewController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        overviewController = new OverviewController();
        mockMvc = MockMvcBuilders.standaloneSetup(overviewController).build();
    }

    @Test
    public void getStorageNum() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/overview/getstoragenum").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getDataStoreOverview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/overview/getdatastoreoverview").contentType(MediaType.APPLICATION_JSON)
                .param("type","NFS"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDataStoreTopN() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/overview/getdatastoretopn").contentType(MediaType.APPLICATION_JSON)
                .param("type", "NFS").param("topn", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getBestPracticeViolations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/overview/getbestpracticeviolations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}