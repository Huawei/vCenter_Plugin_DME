package com.huawei.dmestore.mvc;

import com.google.gson.Gson;
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
 * @className NfsAccessControllerTest
 * @description TODO
 * @date 2020/11/17 17:29
 */
public class NfsAccessControllerTest {

    private NfsAccessController nfsAccessController;
    private MockMvc mockMvc;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        nfsAccessController = new NfsAccessController();
        mockMvc = MockMvcBuilders.standaloneSetup(nfsAccessController).build();
    }

    @Test
    public void listNfs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/listnfs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listNfsPerformance() throws Exception {
        List<String> list = new ArrayList<>();
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/listnfsperformance").contentType(MediaType.APPLICATION_JSON)
                .param("fsIds", gson.toJson(list)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void mountNfs() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessnfs/mountnfs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void unmountNfs() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessnfs/unmountnfs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void deleteNfs() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessnfs/deletenfs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getHostsByStorageId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/gethostsbystorageid/{storageId}","321").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getHostGroupsByStorageId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessnfs/gethostgroupsbystorageid/{storageId}","321").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}