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
 * @className PluginRegisterControllerTest
 * @description TODO
 * @date 2020/11/17 16:05
 */
public class PluginRegisterControllerTest {

    private PluginRegisterController pluginRegisterController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        pluginRegisterController = new PluginRegisterController();
        mockMvc = MockMvcBuilders.standaloneSetup(pluginRegisterController).build();
    }

    @Test
    public void pluginaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registerservice/pluginaction").contentType(MediaType.APPLICATION_JSON)
                .param("request", "321")
                .param("vcenterIP", "321")
                .param("vcenterPort", "321")
                .param("vcenterUsername", "321")
                .param("vcenterPassword", "321")
                .param("action", "uninstall")
                .param("removeData", "321")
                .param("dmeIp", "321")
                .param("dmePort", "321")
                .param("dmeUsername", "321")
                .param("dmePassword", "321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}