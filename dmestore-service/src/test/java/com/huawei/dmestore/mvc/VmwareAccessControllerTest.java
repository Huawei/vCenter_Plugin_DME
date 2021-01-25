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
 * @className VmwareAccessControllerTest
 * @description TODO
 * @date 2020/11/17 16:53
 */
public class VmwareAccessControllerTest {

    private VmwareAccessController vmwareAccessController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        vmwareAccessController = new VmwareAccessController();
        mockMvc = MockMvcBuilders.standaloneSetup(vmwareAccessController).build();
    }

    @Test
    public void listHosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/listhost").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getHostsByDsObjectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/gethostsbydsobjectid").contentType(MediaType.APPLICATION_JSON)
                .param("dataStoreObjectId", "321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listClusters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/listcluster").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getClustersByDsObjectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/getclustersbydsobjectid").contentType(MediaType.APPLICATION_JSON)
                .param("dataStoreObjectId","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDataStoresByHostObjectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/getdatastoresbyhostobjectid").contentType(MediaType.APPLICATION_JSON)
                .param("hostObjectId","321")
                .param("dataStoreType","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDataStoresByClusterObjectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/getdatastoresbyclusterobjectid").contentType(MediaType.APPLICATION_JSON)
                .param("clusterObjectId","321")
                .param("dataStoreType","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getVmKernelIpByHostObjectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/getvmkernelipbyhostobjectid").contentType(MediaType.APPLICATION_JSON)
                .param("hostObjectId","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getMountDataStoresByHostObjectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/getmountdatastoresbyhostobjectid").contentType(MediaType.APPLICATION_JSON)
                .param("hostObjectId","321")
                .param("dataStoreType","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getMountDataStoresByClusterObjectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmware/getmountdatastoresbyclusterobjectid").contentType(MediaType.APPLICATION_JSON)
                .param("clusterObjectId","321")
                .param("dataStoreType","321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}