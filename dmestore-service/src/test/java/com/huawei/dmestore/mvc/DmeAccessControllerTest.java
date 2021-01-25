package com.huawei.dmestore.mvc;

import static org.mockito.Mockito.mock;

import com.google.gson.Gson;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author lianq
 * @className DmeAccessControllerTest
 * @description TODO
 * @date 2020/11/24 11:45
 */
public class DmeAccessControllerTest {
    private Gson gson = new Gson();
    DmeAccessController dmeAccessController;
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        dmeAccessController = new DmeAccessController();
        mockMvc = MockMvcBuilders.standaloneSetup(dmeAccessController).build();
    }

    @Test
    public void accessDme() throws Exception {
        Map params = mock(Map.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/accessdme/access")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(params)))
            .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void refreshDme() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessdme/refreshaccess")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getWorkLoads() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessdme/getworkloads")
            .contentType(MediaType.APPLICATION_JSON)
            .param("storageId","321"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void scanDatastore() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessdme/scandatastore")
            .contentType(MediaType.APPLICATION_JSON)
            .param("storageType","321"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void configureTaskTime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessdme/configuretasktime")
            .contentType(MediaType.APPLICATION_JSON)
            .param("taskId","321")
            .param("taskCron","321"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }


}