package com.huawei.dmestore.mvc;

import com.huawei.dmestore.model.VmRdmCreateBean;
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
 * @className VmRdmControllerTest
 * @description TODO
 * @date 2020/11/17 16:46
 */
public class VmRdmControllerTest {

    private Gson gson = new Gson();
    private VmRdmController vmRdmController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        vmRdmController = new VmRdmController();
        mockMvc = MockMvcBuilders.standaloneSetup(vmRdmController).build();
    }

    @Test
    public void createRdm() throws Exception {
        VmRdmCreateBean vmRdmCreateBean = new VmRdmCreateBean();
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/vmrdm/createRdm").contentType(MediaType.APPLICATION_JSON)
                .param("vmObjectId", "321")
                .content(gson.toJson(vmRdmCreateBean))
                .param("dataStoreObjectId", "321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void dmeHosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/vmrdm/dmeHosts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDatastoreMountsOnHost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/vmrdm/vCenter/datastoreOnHost").contentType(MediaType.APPLICATION_JSON)
                .param("vmObjectId", "321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}