package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.LogicPorts;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className NfsOperationServiceImplTest
 * @description TODO
 * @date 2020/11/13 16:36
 */
public class NfsOperationServiceImplTest {

    @Mock
    DmeAccessService dmeAccessService;
    @Mock
    TaskService taskService;
    @Mock
    DmeStorageService dmeStorageService;
    @Mock
    VCSDKUtils vcsdkUtils;
    @Mock
    DmeVmwareRalationDao dmeVmwareRalationDao;

    Gson gson = new Gson();
    String url;

    @InjectMocks
    NfsOperationService nfsOperationService = new NfsOperationServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createNfsDatastore() throws DmeException {
        String param = "{\"tuning\":{\"allocation_type\":\"thin\",\"compression_enabled\":false,\"deduplication_enabled\":false},\"qos_policy\":{\"max_bandwidth\":\"2\",\"max_iops\":\"1\"},\"storage_pool_id\":\"0\",\"accessMode\":\"readWrite\",\"type\":\"NFS\"," +
                "\"exportPath\":\"/lqtestnfs00007\",\"filesystem_specs\":[{\"count\":1,\"name\":\"lqtestnfs00007\",\"capacity\":\"1\"}],\"pool_raw_id\":\"1\",\"nfs_share_client_addition\":[{\"name\":\"192.168.200.13\",\"objectId\":\"urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1\"}]," +
                "\"nfsName\":\"lqtestnfs00007\",\"current_port_id\":\"139269\",\"storage_id\":\"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\"capacity_autonegotiation\":{\"capacity_self_adjusting_mode\":\"off\",\"auto_size_enable\":false},\"create_nfs_share_param\":{\"character_encoding\":\"utf-8\"," +
                "\"name\":\"/lqtestnfs00007\",\"share_path\":\"/lqtestnfs00007/\"}}";
        Map<String,Object> map = gson.fromJson(param, Map.class);
        String taskId = "123";
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("task_id", taskId);
        url = "/rest/resourcedb/v1/instances/SYS_StoragePool?condition={json}";
        String data = "{\n" +
                "            \"ownerType\": \"eSight_Storage\", \n" +
                "            \"tier0RaidLv\": 0, \n" +
                "            \"storageDeviceId\": \"B94BFF9D0DFB11EBBD3D0050568491C9\", \n" +
                "            \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\", \n" +
                "            \"type\": \"block\", \n" +
                "            \"ownerName\": \"eSight_Storage\", \n" +
                "            \"lastMonitorTime\": 1605252563670, \n" +
                "            \"runningStatus\": \"normal\", \n" +
                "            \"totalCapacity\": 2157, \n" +
                "            \"tier2RaidLv\": 0, \n" +
                "            \"confirmStatus\": \"unconfirmed\", \n" +
                "            \"protectionCapacity\": 0, \n" +
                "            \"id\": \"EED2057F2F11314AA95DC2EF0C06DCDF\", \n" +
                "            \"last_Modified\": 1605252564448, \n" +
                "            \"diskPoolId\": \"1\", \n" +
                "            \"usedCapacity\": 218.88, \n" +
                "            \"tier0Capacity\": 0, \n" +
                "            \"class_Id\": 1029, \n" +
                "            \"dataStatus\": \"normal\", \n" +
                "            \"subscribedCapacity\": 199, \n" +
                "            \"resId\": \"EED2057F2F11314AA95DC2EF0C06DCDF\", \n" +
                "            \"tier1RaidLv\": 2, \n" +
                "            \"dedupedCapacity\": 0, \n" +
                "            \"is_Local\": true, \n" +
                "            \"class_Name\": \"SYS_StoragePool\", \n" +
                "            \"compressedCapacity\": 0, \n" +
                "            \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\", \n" +
                "            \"name\": \"StoragePool001\", \n" +
                "            \"poolId\": \"0\", \n" +
                "            \"tier1Capacity\": 2157, \n" +
                "            \"nativeId\": \"nedn=b94bff9d-0dfb-11eb-bd3d-0050568491c9,id=0,objecttype=216\", \n" +
                "            \"dataSource\": \"auto\", \n" +
                "            \"tier2Capacity\": 0, \n" +
                "            \"status\": \"normal\"\n" +
                "        }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("objList", list);
        String param1 = "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"storageDeviceId\",\"value\":\"b94bff9d-0dfb-11eb-bd3d-0050568491c9\"},\"logOp\":\"and\"},{\"simple\":{\"name\":\"pool_id\",\"operator\":\"equal\",\"value\":\"0\"},\"logOp\":\"and\"}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map1), null, HttpStatus.OK);
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, param1)).thenReturn(responseEntity);
        url = "/rest/fileservice/v1/filesystems/customize";
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(jsonObject1), null, HttpStatus.ACCEPTED);
        String param2 = "{\"tuning\":{\"allocation_type\":\"thin\",\"compression_enabled\":false,\"deduplication_enabled\":false,\"qos_policy\":{\"max_bandwidth\":\"2\",\"max_iops\":\"1\"}},\"storage_id\":\"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\"capacity_autonegotiation\":{\"capacity_self_adjusting_mode\":\"off\",\"auto_size_enable\":false},\"filesystem_specs\":[{\"name\":\"lqtestnfs00007\",\"count\":1,\"capacity\":1.0}],\"pool_raw_id\":\"1\"}";
        when(dmeAccessService.access(url, HttpMethod.POST, param2)).thenReturn(responseEntity2);
        List<String> list1 = new ArrayList<>();
        list1.add(taskId);
        when(taskService.checkTaskStatus(list1)).thenReturn(true);
        url = "/rest/fileservice/v1/filesystems/query";
        String param3 = "{\"name\":\"lqtestnfs00007\"}";
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("name", "lqtestnfs00007");
        String fsId = "123";
        jsonObject2.addProperty("id", fsId);
        List<JsonObject> list2 = new ArrayList<>();
        list2.add(jsonObject2);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("data", list2);
        ResponseEntity<String> responseEntity3 = new ResponseEntity<>(gson.toJson(map2), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.POST, param3)).thenReturn(responseEntity3);
        url = "/rest/fileservice/v1/nfs-shares";
        String param4 = "{\"create_nfs_share_param\":{\"character_encoding\":\"utf-8\",\"name\":\"/lqtestnfs00007\",\"share_path\":\"/lqtestnfs00007/\",\"nfs_share_client_addition\":[{\"all_squash\":\"all_squash\",\"accessval\":\"read-only\",\"name\":\"192.168.200.13\",\"root_squash\":\"root_squash\",\"secure\":\"insecure\",\"sync\":\"synchronization\"}]},\"fs_id\":\"123\"}";
        ResponseEntity<String> responseEntity1 = new ResponseEntity<>(gson.toJson(jsonObject1),null,HttpStatus.ACCEPTED);
        when(dmeAccessService.access(url, HttpMethod.POST, param4)).thenReturn(responseEntity1);
        url = "/rest/fileservice/v1/nfs-shares/summary";
        String param5 = "{\"name\":\"/lqtestnfs00007\",\"fs_name\":\"lqtestnfs00007\"}";
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("name", "/lqtestnfs00007");
        jsonObject3.addProperty("id", "123");
        List<JsonObject> list3 = new ArrayList<>();
        list3.add(jsonObject3);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("nfs_share_info_list", list3);
        ResponseEntity<String> responseEntity4 = new ResponseEntity<>(gson.toJson(map3),null,HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.POST, param5)).thenReturn(responseEntity4);
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        LogicPorts logicPorts = new LogicPorts();
        logicPorts.setMgmtIp("192.169.200.4");
        logicPorts.setHomePortId("139269");
        logicPorts.setName("test1");
        List<LogicPorts> list4 = new ArrayList<>();
        list4.add(logicPorts);
        when(dmeStorageService.getLogicPorts(storageId)).thenReturn(list4);
        Map<String, String> map4 = new HashMap<>();
        map4.put("urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1","192.168.200.13");
        List<Map<String, String>> list5 = new ArrayList<>();
        list5.add(map4);
        DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
        dmeVmwareRelation.setStoreId("datastoreObjectId");
        dmeVmwareRelation.setStoreName("nfsName");
        dmeVmwareRelation.setStoreType(ToolUtils.STORE_TYPE_NFS);
        String serverHost = "192.169.200.4";
        String exportPath = "/lqtestnfs00007";
        String nfsName = "lqtestnfs00007";
        String accessMode = "readWrite";
        when(vcsdkUtils.createNfsDatastore(serverHost, exportPath, nfsName, accessMode, list5, ToolUtils.STORE_TYPE_NFS, "")).thenReturn(gson.toJson(dmeVmwareRelation));
        nfsOperationService.createNfsDatastore(map);

    }

    @Test
    public void updateNfsDatastore() throws DmeException {
        String param = "{\"nfsName\":\"lqtestnfs00007\",\"tuning\":{\"allocation_type\":\"thin\",\"compression_enabled\":false," +
                "\"deduplication_enabled\":false},\"qos_policy\":{}," + "\"capacity_autonegotiation\":{\"capacity_self_adjusting_mode\":\"off\"," +
                "\"auto_size_enable\":false},\"dataStoreObjectId\":\"urn:vmomi:Datastore:datastore-1060:674908e5-ab21-4079-9cb1-596358ee5dd1\"," +
                "\"nfs_share_id\":\"70C9358F595B3AA5A1DB2464F72AF0DA\",\"file_system_id\":\"0C9A60E0A51C3AD38567C21B6881371C\"}";
        Map<String,Object> map = gson.fromJson(param, Map.class);
        url = "/rest/fileservice/v1/filesystems/0C9A60E0A51C3AD38567C21B6881371C";
        String params = "{\"tuning\":{\"allocation_type\":\"thin\",\"compression_enabled\":false,\"deduplication_enabled\":false}," +
                "\"capacity_autonegotiation\":{\"capacity_self_adjusting_mode\":\"off\",\"auto_size_enable\":false}}";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("task_id", "123");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(jsonObject), null, HttpStatus.ACCEPTED);
        when(dmeAccessService.access(url, HttpMethod.PUT, params)).thenReturn(responseEntity);
        List<String> list = new ArrayList<>();
        list.add("123");
        when(taskService.checkTaskStatus(list)).thenReturn(true);
        when(vcsdkUtils.renameDataStore("lqtestnfs00007","urn:vmomi:Datastore:datastore-1060:674908e5-ab21-4079-9cb1-596358ee5dd1")).thenReturn("success");
        nfsOperationService.updateNfsDatastore(map);

    }

    @Test
    public void changeNfsCapacity() throws DmeException {
        String param = "{\"expand\":true,\"fileSystemId\":\"0C9A60E0A51C3AD38567C21B6881371C\",\"storeObjectId\":\"0C9A60E0A51C3AD38567C21B6881371C\",\"capacity\":2.0}";
        Map<String,Object> map = gson.fromJson(param, Map.class);
        String resp = " {\n" +
                "            \"id\": \"6BB865BC85F43E2EBF4E9DBF2B726A53\", \n" +
                "            \"name\": \"1026002\", \n" +
                "            \"health_status\": \"normal\", \n" +
                "            \"alloc_type\": \"thin\", \n" +
                "            \"type\": \"normal\", \n" +
                "            \"capacity\": 1, \n" +
                "            \"available_capacity\": 0.87, \n" +
                "            \"capacity_usage_ratio\": 12, \n" +
                "            \"storage_name\": \"Huawei.Storage\", \n" +
                "            \"storage_id\": \"b94bff9d-0dfb-11eb-bd3d-0050568491c9\", \n" +
                "            \"storage_pool_name\": \"fileStoragePool002\", \n" +
                "            \"tier_name\": null, \n" +
                "            \"tier_id\": null, \n" +
                "            \"nfs_count\": 1, \n" +
                "            \"cifs_count\": 0, \n" +
                "            \"dtree_count\": 1, \n" +
                "            \"vstore_name\": null, \n" +
                "            \"project_name\": null, \n" +
                "            \"project_id\": null\n" +
                "        }";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(resp, null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/fileservice/v1/filesystems/0C9A60E0A51C3AD38567C21B6881371C", HttpMethod.GET, null)).thenReturn(responseEntity);
        String param2 = " {\n" +
                "            \"ownerType\": \"eSight_Storage\", \n" +
                "            \"tier0RaidLv\": 0, \n" +
                "            \"storageDeviceId\": \"B94BFF9D0DFB11EBBD3D0050568491C9\", \n" +
                "            \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\", \n" +
                "            \"type\": \"file\", \n" +
                "            \"ownerName\": \"eSight_Storage\", \n" +
                "            \"lastMonitorTime\": 1605514994516, \n" +
                "            \"runningStatus\": \"normal\", \n" +
                "            \"totalCapacity\": 1024, \n" +
                "            \"tier2RaidLv\": 0, \n" +
                "            \"confirmStatus\": \"unconfirmed\", \n" +
                "            \"protectionCapacity\": 0, \n" +
                "            \"id\": \"3FB520372B753CFB8594F4C969587E5E\", \n" +
                "            \"last_Modified\": 1605514994584, \n" +
                "            \"diskPoolId\": \"1\", \n" +
                "            \"usedCapacity\": 77.5, \n" +
                "            \"tier0Capacity\": 0, \n" +
                "            \"class_Id\": 1029, \n" +
                "            \"dataStatus\": \"normal\", \n" +
                "            \"subscribedCapacity\": 109.57, \n" +
                "            \"resId\": \"3FB520372B753CFB8594F4C969587E5E\", \n" +
                "            \"tier1RaidLv\": 2, \n" +
                "            \"dedupedCapacity\": 0, \n" +
                "            \"is_Local\": true, \n" +
                "            \"class_Name\": \"SYS_StoragePool\", \n" +
                "            \"compressedCapacity\": 0, \n" +
                "            \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\", \n" +
                "            \"name\": \"fileStoragePool002\", \n" +
                "            \"poolId\": \"1\", \n" +
                "            \"tier1Capacity\": 1024, \n" +
                "            \"nativeId\": \"nedn=b94bff9d-0dfb-11eb-bd3d-0050568491c9,id=1,objecttype=216\", \n" +
                "            \"dataSource\": \"auto\", \n" +
                "            \"tier2Capacity\": 0, \n" +
                "            \"status\": \"normal\"\n" +
                "        }";
        JsonObject jsonObject1 = gson.fromJson(param2, JsonObject.class);
        List<JsonObject> list2 = new ArrayList<>();
        list2.add(jsonObject1);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("objList", list2);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(map1), null, HttpStatus.OK);
        String param3 = "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"storageDeviceId\",\"value\":\"b94bff9d-0dfb-11eb-bd3d-0050568491c9\"},\"logOp\":\"and\"},{\"simple\":{\"name\":\"storage_pool_name\",\"operator\":\"equal\",\"value\":\"fileStoragePool002\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson("/rest/resourcedb/v1/instances/SYS_StoragePool?condition={json}", HttpMethod.GET, param3)).thenReturn(responseEntity2);
        String param4 = "{\"file_system_id\":\"0C9A60E0A51C3AD38567C21B6881371C\",\"capacity\":3.0}";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("task_id", "123");
        ResponseEntity<String> responseEntity3 = new ResponseEntity<>(gson.toJson(jsonObject), null, HttpStatus.ACCEPTED);
        when(dmeAccessService.access("/rest/fileservice/v1/filesystems/0C9A60E0A51C3AD38567C21B6881371C", HttpMethod.PUT, param4)).thenReturn(responseEntity3);
        List<String> list = new ArrayList<>();
        list.add("123");
        when(taskService.checkTaskStatus(list)).thenReturn(true);
        nfsOperationService.changeNfsCapacity(map);
    }

    @Test
    public void getEditNfsStore() throws DmeException {
//        String storeObjectId = "123";
//        when(dmeAccessService.access());
//        nfsOperationService.getEditNfsStore(storeObjectId);
    }
}