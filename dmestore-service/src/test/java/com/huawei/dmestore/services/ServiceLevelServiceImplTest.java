package com.huawei.dmestore.services;

import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.model.RelationInstance;
import com.huawei.dmestore.model.StoragePool;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vmware.cis.tagging.TagModel;
import com.vmware.pbm.PbmProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * ServiceLevelServiceImpl Tester.
 *
 * @author wangxiangyong
 * @version 1.0
 * @since <pre>十一月 11, 2020</pre>
 */
public class ServiceLevelServiceImplTest {
    Gson gson = new Gson();
    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    DmeRelationInstanceService dmeRelationInstanceService;
    @Mock
    private DmeStorageService dmeStorageService;
    @Mock
    private VCenterInfoService vCenterInfoService;
    @Mock
    private VCSDKUtils vcsdkUtils;

    @InjectMocks
    private ServiceLevelService serviceLevelService = new ServiceLevelServiceImpl();

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: listServiceLevel(Map<String, Object> params)
     */
    @Test
    public void testListServiceLevel() throws Exception {
        String url = "/rest/service-policy/v1/service-levels";
        String jsonData = "{\"service-levels\":[{\"id\":\"2c8c02f7-a5a8-474f-ac8b-90c7cf026e7a\",\"name\":\"localhost\",\"description\":\"blockservice-levelfordj\",\"type\":\"BLOCK\",\"protocol\":null,\"total_capacity\":0.0,\"used_capacity\":0.0,\"free_capacity\":0.0,\"capabilities\":{\"resource_type\":\"thin\",\"compression\":null,\"deduplication\":null,\"iopriority\":null,\"smarttier\":{\"enabled\":true,\"policy\":2},\"qos\":{\"qos_param\":{\"latency\":10,\"latencyUnit\":\"ms\",\"minBandWidth\":1000,\"minIOPS\":1000,\"maxBandWidth\":null,\"maxIOPS\":null},\"enabled\":true}}}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        Map<String, Object> params = new HashMap<>();
        serviceLevelService.listServiceLevel(params);
    }

    /**
     * Method: updateVmwarePolicy()
     */
    @Test
    public void testUpdateVmwarePolicy() throws Exception {
        String server = "10.143.133.249";
        int port = 26335;
        String username = "test";
        String password = "Pbu4@123";
        VCenterInfo vCenterInfo = new VCenterInfo();
        vCenterInfo.setHostIp(server);
        vCenterInfo.setHostPort(port);
        vCenterInfo.setUserName(username);
        vCenterInfo.setPassword(password);
        when(vCenterInfoService.getVcenterInfo()).thenReturn(vCenterInfo);
        when(vcsdkUtils.getCategoryId(anyObject())).thenReturn("1123");
        String name = "D新策略-3";
        List<TagModel> tagModels = new ArrayList<>();
        TagModel tagModel = new TagModel();
        tagModel.setName(name);
        tagModels.add(tagModel);
        when(vcsdkUtils.getAllTagsByCategoryId(anyObject(), anyObject())).thenReturn(tagModels);
        List<PbmProfile> pbmProfiles = new ArrayList<>();
        PbmProfile pbmProfile = new PbmProfile();
        pbmProfile.setName(name);
        pbmProfiles.add(pbmProfile);
        when(vcsdkUtils.getAllSelfPolicyInallcontext()).thenReturn(pbmProfiles);
        String url = "/rest/service-policy/v1/service-levels";
        String jsonData = "{\"service-levels\":[{\"id\":\"354a2dec-5d84-4e66-afc5-f3564f087c3f\",\"name\":\"D新策略-3\",\"description\":\"blockservice-levelfordj\",\"type\":\"BLOCK\",\"protocol\":null,\"total_capacity\":0.0,\"used_capacity\":0.0,\"free_capacity\":0.0,\"capabilities\":{\"resource_type\":\"thin\",\"compression\":null,\"deduplication\":null,\"iopriority\":null,\"smarttier\":null,\"qos\":{\"qos_param\":{\"latency\":null,\"latencyUnit\":\"ms\",\"minBandWidth\":null,\"minIOPS\":null,\"maxBandWidth\":2333,\"maxIOPS\":null},\"enabled\":true}}}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        doNothing().when(vcsdkUtils).removePbmProfileInAllContext(pbmProfiles);
        doNothing().when(vcsdkUtils).removeAllTags(anyObject(), anyObject());
        serviceLevelService.updateVmwarePolicy();
    }

    /**
     * Method: getStoragePoolInfosByServiceLevelId(String serivceLevelId)
     */
    @Test
    public void testGetStoragePoolInfosByServiceLevelId() throws Exception {
        String levelId = "2c8c02f7-a5a8-474f-ac8b-90c7cf026e7a";
        String jsonData = "{\n" +
                "    \"objList\": [\n" +
                "        {\n" +
                "            \"class_Id\": 1142,\n" +
                "            \"remark\": \"block service-level for dj\",\n" +
                "            \"type\": \"block\",\n" +
                "            \"resId\": \"7FB8C3A5129C3E15B76823C90EC8FF1A\",\n" +
                "            \"poolTotalCapacity\": 0.0,\n" +
                "            \"is_Local\": true,\n" +
                "            \"specs\": \"\",\n" +
                "            \"poolUsedCapacity\": 0.0,\n" +
                "            \"class_Name\": \"SYS_DjTier\",\n" +
                "            \"name\": \"localhost\",\n" +
                "            \"id\": \"7FB8C3A5129C3E15B76823C90EC8FF1A\",\n" +
                "            \"last_Modified\": 1602730213569,\n" +
                "            \"nativeId\": \"2c8c02f7-a5a8-474f-ac8b-90c7cf026e7a\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        Map<String, Object> slMap = new HashMap<>();
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
        JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (JsonElement element : jsonArray) {
            JsonObject slJson = element.getAsJsonObject();
            slMap.put("resId", ToolUtils.jsonToOriginalStr(slJson.get("resId")));
            slMap.put("name", ToolUtils.jsonToOriginalStr(slJson.get("name")));
            slMap.put("id", ToolUtils.jsonToOriginalStr(slJson.get("id")));
            slMap.put("nativeId", ToolUtils.jsonToOriginalStr(slJson.get("nativeId")));
            map.put(ToolUtils.jsonToOriginalStr(slJson.get("nativeId")), slMap);
        }
        when(dmeRelationInstanceService.getServiceLevelInstance()).thenReturn(map);
        List<RelationInstance> relationInstances = new ArrayList<>();
        RelationInstance ri = new RelationInstance();
        ri.setSourceInstanceId("B124E7863D4E349EBC3C2B2356A81D25");
        ri.setTargetInstanceId("EED2057F2F11314AA95DC2EF0C06DCDF");
        ri.setRelationName("M_DjTierContainsStoragePool");
        ri.setId("7FE3DAC3D73D318B9FE2B6062FDC77A2");
        ri.setLastModified(1602730267856L);
        ri.setResId("7FE3DAC3D73D318B9FE2B6062FDC77A2");
        ri.setRelationId(1136);
        relationInstances.add(ri);
        when(dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(
       "M_DjTierContainsStoragePool", "7FB8C3A5129C3E15B76823C90EC8FF1A")).thenReturn(relationInstances);
        Object ss = "{\n" +
                "    \"ownerType\": \"eSight_Storage\",\n" +
                "    \"tier0RaidLv\": 0,\n" +
                "    \"storageDeviceId\": \"B94BFF9D0DFB11EBBD3D0050568491C9\",\n" +
                "    \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
                "    \"type\": \"block\",\n" +
                "    \"ownerName\": \"eSight_Storage\",\n" +
                "    \"lastMonitorTime\": 1605576657648,\n" +
                "    \"runningStatus\": \"normal\",\n" +
                "    \"totalCapacity\": 2157.0,\n" +
                "    \"tier2RaidLv\": 0,\n" +
                "    \"confirmStatus\": \"unconfirmed\",\n" +
                "    \"protectionCapacity\": 0.0,\n" +
                "    \"id\": \"EED2057F2F11314AA95DC2EF0C06DCDF\",\n" +
                "    \"last_Modified\": 1605576657723,\n" +
                "    \"diskPoolId\": \"1\",\n" +
                "    \"usedCapacity\": 220.88,\n" +
                "    \"tier0Capacity\": 0.0,\n" +
                "    \"class_Id\": 1029,\n" +
                "    \"dataStatus\": \"normal\",\n" +
                "    \"subscribedCapacity\": 201.0,\n" +
                "    \"tier1RaidLv\": 2,\n" +
                "    \"dedupedCapacity\": 0.0,\n" +
                "    \"is_Local\": true,\n" +
                "    \"class_Name\": \"SYS_StoragePool\",\n" +
                "    \"compressedCapacity\": 0.0,\n" +
                "    \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
                "    \"name\": \"StoragePool001\",\n" +
                "    \"poolId\": \"0\",\n" +
                "    \"tier1Capacity\": 2157.0,\n" +
                "    \"nativeId\": \"nedn=b94bff9d-0dfb-11eb-bd3d-0050568491c9,id=0,objecttype=216\",\n" +
                "    \"dataSource\": \"auto\",\n" +
                "    \"tier2Capacity\": 0.0,\n" +
                "    \"status\": \"normal\"\n" +
                "}";
        when(dmeRelationInstanceService.queryInstanceByInstanceNameId("SYS_StoragePool", "EED2057F2F11314AA95DC2EF0C06DCDF")).thenReturn(ss);
        List<StoragePool> pools = new ArrayList<>();
        StoragePool pool = new StoragePool();
        pool.setId("EED2057F2F11314AA95DC2EF0C06DCDF");
        pool.setMaxIops(220.88f);
        pools.add(pool);
        when(dmeStorageService.getStoragePools("B94BFF9D0DFB11EBBD3D0050568491C9", "all")).thenReturn(pools);
        serviceLevelService.getStoragePoolInfosByServiceLevelId(levelId);
    }

    /**
     * Method: getVolumeInfosByServiceLevelId(String serviceLevelId)
     */
    @Test
    public void testGetVolumeInfosByServiceLevelId() throws Exception {
        String url = "/rest/blockservice/v1/volumes?service_level_id=123";
        String jsonData = "{\n" +
                "    \"volumes\": [\n" +
                "        {\n" +
                "            \"id\": \"8f6d93f1-4214-46bc-ae7a-85f8349ebbd2\",\n" +
                "            \"name\": \"Drdm17\",\n" +
                "            \"description\": null,\n" +
                "            \"status\": \"normal\",\n" +
                "            \"attached\": true,\n" +
                "            \"project_id\": null,\n" +
                "            \"alloctype\": \"thick\",\n" +
                "            \"capacity\": 1,\n" +
                "            \"service_level_name\": \"lgqtest\",\n" +
                "            \"attachments\": [\n" +
                "                {\n" +
                "                    \"id\": \"caa34ee1-e935-4958-b106-022d7beef447\",\n" +
                "                    \"volume_id\": \"8f6d93f1-4214-46bc-ae7a-85f8349ebbd2\",\n" +
                "                    \"host_id\": \"9cbd24b5-fb5b-4ad9-9393-cf05b9b97339\",\n" +
                "                    \"attached_at\": \"2020-11-12T02:27:40.000000\",\n" +
                "                    \"attached_host_group\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"volume_raw_id\": \"323\",\n" +
                "            \"volume_wwn\": \"67c1cf1100589345402376ae00000143\",\n" +
                "            \"storage_id\": \"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\n" +
                "            \"storage_sn\": \"2102351QLH9WK5800028\",\n" +
                "            \"pool_raw_id\": \"0\",\n" +
                "            \"capacity_usage\": null,\n" +
                "            \"protected\": false,\n" +
                "            \"updated_at\": \"2020-11-12T02:27:40.000000\",\n" +
                "            \"created_at\": \"2020-11-12T02:27:34.000000\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"count\": 25\n" +
                "}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        Map<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> lunMap = new HashMap<>();
        lunMap.put("resId", "1312");
        lunMap.put("name", "aa");
        lunMap.put("id", "13");
        String wwn = "67c1cf1100589345402376ae00000143";
        lunMap.put("wwn", wwn);
        map.put(wwn, lunMap);
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map);
        serviceLevelService.getVolumeInfosByServiceLevelId("123");
    }

}

