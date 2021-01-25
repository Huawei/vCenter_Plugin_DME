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
 * @className HostAccessControllerTest
 * @description TODO
 * @date 2020/11/17 17:55
 */
public class HostAccessControllerTest {

    private HostAccessController hostAccessController;
    private MockMvc mockMvc;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        hostAccessController = new HostAccessController();
        mockMvc = MockMvcBuilders.standaloneSetup(hostAccessController).build();
    }

    @Test
    public void configureIscsi() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accesshost/configureiscsi").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testConnectivity() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accesshost/testconnectivity").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}