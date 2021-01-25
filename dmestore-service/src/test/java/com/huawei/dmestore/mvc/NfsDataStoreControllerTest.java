package com.huawei.dmestore.mvc;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author lianq
 * @className NfsDataStoreControllerTest
 * @description TODO
 * @date 2020/11/17 15:30
 */
public class NfsDataStoreControllerTest {

    private Gson gson = new Gson();
    private NfsDataStoreController nfsDataStoreController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        nfsDataStoreController = new NfsDataStoreController();
        mockMvc = MockMvcBuilders.standaloneSetup(nfsDataStoreController).build();
    }

    @Test
    public void portAttr() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/logicport/{storageObjectId}", "123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shareAttr() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/share/{storageObjectId}", "123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void fsAttr() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/fileservice/{storageObjectId}", "123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void scannfs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/scannfs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}