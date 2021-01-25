package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className VmwareAccessServiceImplTest
 * @description TODO
 * @date 2020/11/20 14:07
 */
public class VmwareAccessServiceImplTest {

    private Gson gson = new Gson();
    @Mock
    private DmeVmwareRalationDao dmeVmwareRalationDao;
    @Mock
    private VCSDKUtils vcsdkUtils;
    @InjectMocks
    VmwareAccessService vmwareAccessService = new VmwareAccessServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void listHosts() throws DmeException {
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("hostId", "321");
        map.put("objectId", "321");
        map.put("hostName", "321");
        lists.add(map);
        when(vcsdkUtils.getAllHosts()).thenReturn(gson.toJson(lists));
        vmwareAccessService.listHosts();
    }

    @Test
    public void getHostsByDsObjectId() throws DmeException {
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("hostId", "321");
        map.put("hostName", "321");
        lists.add(map);
        when(vcsdkUtils.getHostsByDsObjectId("321")).thenReturn(gson.toJson(lists));
        vmwareAccessService.getHostsByDsObjectId("321");
    }

    @Test
    public void listClusters() throws DmeException {
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("clusterId", "321");
        map.put("clusterName", "321");
        lists.add(map);
        when(vcsdkUtils.getAllClusters()).thenReturn(gson.toJson(lists));
        vmwareAccessService.listClusters();
    }

    @Test
    public void getClustersByDsObjectId() throws DmeException {
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("clusterId", "321");
        map.put("clusterName", "321");
        lists.add(map);
        when(vcsdkUtils.getClustersByDsObjectId("321")).thenReturn(gson.toJson(lists));
        vmwareAccessService.getClustersByDsObjectId("321");
    }

    @Test
    public void getDataStoresByHostObjectId() throws DmeException {
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", "321");
        map.put("name", "321");
        map.put("objectId", "321");
        map.put("status", "321");
        map.put("type", "321");
        map.put("capacity", 321);
        map.put("freeSpace",21);
        lists.add(map);
        when(vcsdkUtils.getDataStoresByHostObjectId("321", "321")).thenReturn(gson.toJson(lists));
        List<String> list = new ArrayList<>();
        list.add("321");
        when(dmeVmwareRalationDao.getAllStorageIdByType("321")).thenReturn(list);
        vmwareAccessService.getDataStoresByHostObjectId("321", "321");
    }

    @Test
    public void getDataStoresByClusterObjectId() throws DmeException {
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", "321");
        map.put("name", "321");
        map.put("objectId", "321");
        map.put("status", "321");
        map.put("type", "321");
        map.put("capacity", 321);
        map.put("freeSpace",21);
        lists.add(map);
        when(vcsdkUtils.getDataStoresByClusterObjectId("321", "321")).thenReturn(gson.toJson(lists));
        List<String> list = new ArrayList<>();
        list.add("321");
        when(dmeVmwareRalationDao.getAllStorageIdByType("321")).thenReturn(list);
        vmwareAccessService.getDataStoresByClusterObjectId("321", "321");

    }

    @Test
    public void getVmKernelIpByHostObjectId() throws DmeException {
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("device", "321");
        map.put("key", "321");
        map.put("portgroup", "321");
        map.put("ipAddress", "321");
        map.put("mac", "321");
        map.put("port", 321);
        lists.add(map);
        when(vcsdkUtils.getVmKernelIpByHostObjectId("321")).thenReturn(gson.toJson(lists));
        vmwareAccessService.getVmKernelIpByHostObjectId("321");

    }

    @Test
    public void getMountDataStoresByHostObjectId() throws Exception {
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", "321");
        map.put("name", "321");
        map.put("objectId", "321");
        map.put("status", "321");
        map.put("type", "321");
        map.put("capacity", 321);
        map.put("freeSpace", 21);
        lists.add(map);
        when(vcsdkUtils.getMountDataStoresByHostObjectId("321", "321")).thenReturn(gson.toJson(lists));
        List<String> list = new ArrayList<>();
        list.add("321");
        when(dmeVmwareRalationDao.getAllStorageIdByType("321")).thenReturn(list);
        vmwareAccessService.getMountDataStoresByHostObjectId("321", "321");
    }

    @Test
    public void getMountDataStoresByClusterObjectId() throws Exception {
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", "321");
        map.put("name", "321");
        map.put("objectId", "321");
        map.put("status", "321");
        map.put("type", "321");
        map.put("capacity", 321);
        map.put("freeSpace", 21);
        lists.add(map);
        when(vcsdkUtils.getMountDataStoresByClusterObjectId("321", "321")).thenReturn(gson.toJson(lists));
        List<String> list = new ArrayList<>();
        list.add("321");
        when(dmeVmwareRalationDao.getAllStorageIdByType("321")).thenReturn(list);
        vmwareAccessService.getMountDataStoresByClusterObjectId("321", "321");
    }
}