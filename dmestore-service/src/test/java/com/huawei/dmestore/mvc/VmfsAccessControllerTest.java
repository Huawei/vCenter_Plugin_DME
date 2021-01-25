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

public class VmfsAccessControllerTest {

    private VmfsAccessController vmfsAccessController;
    private MockMvc mockMvc;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        vmfsAccessController = new VmfsAccessController();
        mockMvc = MockMvcBuilders.standaloneSetup(vmfsAccessController).build();
    }

    @Test
    public void listVmfsTest() throws Exception {
        VmfsAccessController controller = new VmfsAccessController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/listvmfs"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createvmfsTest() throws Exception {
        Map<String, Object> params = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessvmfs/createvmfs").contentType(MediaType.APPLICATION_JSON).content(params.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void mountvmfsTest() throws Exception {
        Map<String, Object> params = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessvmfs/mountvmfs").contentType(MediaType.APPLICATION_JSON).content(params.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void service_listVmfs_Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/listvmfs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listVmfsPerformance() throws Exception {
        List<String> list = new ArrayList<>();
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/listvmfsperformance").contentType(MediaType.APPLICATION_JSON)
                .param("wwns", gson.toJson(list)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void unmountVmfs() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessvmfs/ummountvmfs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void deleteVmfs() throws Exception {
        Map<String, Object> map = new HashMap<>();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessvmfs/deletevmfs").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void volumeDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/volume/{storageObjectId}","321").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void scanvmfs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/scanvmfs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getHostsByStorageId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/gethostsbystorageid/{storageId}","321").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getHostGroupsByStorageId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/gethostgroupsbystorageid/{storageId}","321").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void queryVmfs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/queryvmfs").contentType(MediaType.APPLICATION_JSON)
                .param("dataStoreObjectId", "321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void queryDatastoreByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/querydatastorebyname").contentType(MediaType.APPLICATION_JSON)
                .param("name", "321"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}