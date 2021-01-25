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
 * @className DmeRelationInstanceControllerTest
 * @description TODO
 * @date 2020/11/17 18:02
 */
public class DmeRelationInstanceControllerTest {

    private DmeRelationInstanceController dmeRelationInstanceController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        dmeRelationInstanceController = new DmeRelationInstanceController();
        mockMvc = MockMvcBuilders.standaloneSetup(dmeRelationInstanceController).build();
    }

    @Test
    public void queryByRelationName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/relationinstance/listbyrelationname").contentType(MediaType.APPLICATION_JSON)
                .param("relationName", "321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void queryByRelationNameInstanceId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/relationinstance/getbyrelationnameinstanceid").contentType(MediaType.APPLICATION_JSON)
                .param("relationName", "321")
                .param("instanceId","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void queryByRelationNameInstanceIdCondition() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/relationinstance/getbyrelationnameinstanceidcondition").contentType(MediaType.APPLICATION_JSON)
                .param("relationName", "321")
                .param("instanceId","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void refreshResourceInstance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/relationinstance/refreshresourceinstance").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}