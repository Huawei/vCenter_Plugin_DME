package com.huawei.dmestore.mvc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
 * @className ServiceLevelControllerTest
 * @description TODO
 * @date 2020/11/17 16:18
 */
public class ServiceLevelControllerTest {

    private Gson gson = new Gson();
    private ServiceLevelController serviceLevelController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        serviceLevelController = new ServiceLevelController();
        mockMvc = MockMvcBuilders.standaloneSetup(serviceLevelController).build();
    }

    @Test
    public void listServiceLevel() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/servicelevel/listservicelevel").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void listStoragePoolsByServiceLevelId() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serviceLevelId","321");
        mockMvc.perform(MockMvcRequestBuilders.post("/servicelevel/listStoragePoolsByServiceLevelId").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(jsonObject)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listVolumesByServiceLevelId() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serviceLevelId","321");
        mockMvc.perform(MockMvcRequestBuilders.post("/servicelevel/listVolumesByServiceLevelId").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(jsonObject)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void manualupdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/servicelevel/manualupdate").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}