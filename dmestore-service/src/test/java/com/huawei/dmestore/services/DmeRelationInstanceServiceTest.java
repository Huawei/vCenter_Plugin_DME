package com.huawei.dmestore.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName DmeRelationInstanceServiceTest.java
 * @Description TODO
 * @createTime 2020年11月19日 16:05:00
 */
public class DmeRelationInstanceServiceTest {
    private Gson gson = new Gson();
    @Mock
    private DmeAccessService dmeAccessService;
    @InjectMocks
    private DmeRelationInstanceService dmeRelationInstanceService = new DmeRelationInstanceServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void queryRelationByRelationName() throws Exception{
        String reName = "test";
        String url = "/rest/resourcedb/v1/relations/{relationName}/instances";
        url = url.replace("{relationName}", reName);
        String jsonData = "{\"objList\":[{\"source_Instance_Id\":\"D26B52294CBE353191BCACB710FECA23\",\"target_Instance_Id\":\"EED2057F2F11314AA95DC2EF0C06DCDF\",\"relation_Name\":\"M_DjTierContainsStoragePool\",\"id\":\"4D201BB024A73C83906A2E0DFD0AD16E\",\"last_Modified\":1602667507465,\"resId\":\"4D201BB024A73C83906A2E0DFD0AD16E\",\"relation_Id\":1136}],\"totalNum\":3,\"pageSize\":20,\"totalPageNo\":1,\"currentPage\":1}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeRelationInstanceService.queryRelationByRelationName(reName);
    }

    @Test
    public void queryRelationByRelationNameConditionSourceInstanceId() throws Exception{
        String relationName = "1232";
        String sourceInstanceId = "11";
        String url = "/rest/resourcedb/v1/relations/{relationName}/instances";
        url = url.replace("{relationName}", relationName);
        url = url + "?condition={json}";
        JsonArray constraint = new JsonArray();
        JsonObject consObj2 = new JsonObject();
        JsonObject simple2 = new JsonObject();
        simple2.addProperty("name", "source_Instance_Id");
        simple2.addProperty("value", sourceInstanceId);
        consObj2.add("simple", simple2);
        constraint.add(consObj2);

        String jsonData = "{\"objList\":[{\"source_Instance_Id\":\"D26B52294CBE353191BCACB710FECA23\",\"target_Instance_Id\":\"EED2057F2F11314AA95DC2EF0C06DCDF\",\"relation_Name\":\"M_DjTierContainsStoragePool\",\"id\":\"4D201BB024A73C83906A2E0DFD0AD16E\",\"last_Modified\":1602667507465,\"resId\":\"4D201BB024A73C83906A2E0DFD0AD16E\",\"relation_Id\":1136}],\"totalNum\":3,\"pageSize\":20,\"totalPageNo\":1,\"currentPage\":1}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, constraint.toString())).thenReturn(responseEntity);
        dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(relationName, sourceInstanceId);
    }

    @Test
    public void queryRelationByRelationNameInstanceId() throws Exception{
        String relationName = "1232";
        String sourceInstanceId = "11";
        String url = "/rest/resourcedb/v1/relations/{relationName}/instances/{instanceId}";
        url = url.replace("{relationName}", relationName);
        url = url.replace("{instanceId}", relationName);
        String jsonData = "{\n" +
                "    \"source_Instance_Id\": \"56C492EB37273527B629B6B360F0B6C8\",\n" +
                "    \"target_Instance_Id\": \"EED2057F2F11314AA95DC2EF0C06DCDF\",\n" +
                "    \"relation_Name\": \"M_DjTierContainsStoragePool\",\n" +
                "    \"id\": \"489BE97F558B34BA8342D28B271AFB80\",\n" +
                "    \"last_Modified\": 1602730248815,\n" +
                "    \"resId\": \"489BE97F558B34BA8342D28B271AFB80\",\n" +
                "    \"relation_Id\": 1136\n" +
                "}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeRelationInstanceService.queryRelationByRelationNameInstanceId(relationName, sourceInstanceId);
    }

    @Test
    public void queryInstanceByInstanceNameId() throws Exception{
        String instanceName = "aa";
        String instanceId = "133";
        String url = "/rest/resourcedb/v1/instances/{className}/{instanceId}";
        url = url.replace("{className}", instanceName);
        url = url.replace("{instanceId}", instanceId);
        String jsonData = "{\n" +
                "    \"source_Instance_Id\": \"56C492EB37273527B629B6B360F0B6C8\",\n" +
                "    \"target_Instance_Id\": \"EED2057F2F11314AA95DC2EF0C06DCDF\",\n" +
                "    \"relation_Name\": \"M_DjTierContainsStoragePool\",\n" +
                "    \"id\": \"489BE97F558B34BA8342D28B271AFB80\",\n" +
                "    \"last_Modified\": 1602730248815,\n" +
                "    \"resId\": \"489BE97F558B34BA8342D28B271AFB80\",\n" +
                "    \"relation_Id\": 1136\n" +
                "}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeRelationInstanceService.queryInstanceByInstanceNameId(instanceName, instanceId);
    }

    @Test
    public void getServiceLevelInstance() throws Exception{
        String instanceName = "SYS_DjTier";
        String url = "/rest/resourcedb/v1/instances/{className}?pageSize=1000";
        url = url.replace("{className}", instanceName);
        String jsonData = "{\"objList\":[{\"source_Instance_Id\":\"D26B52294CBE353191BCACB710FECA23\",\"target_Instance_Id\":\"EED2057F2F11314AA95DC2EF0C06DCDF\",\"relation_Name\":\"M_DjTierContainsStoragePool\",\"id\":\"4D201BB024A73C83906A2E0DFD0AD16E\",\"last_Modified\":1602667507465,\"resId\":\"4D201BB024A73C83906A2E0DFD0AD16E\",\"relation_Id\":1136}],\"totalNum\":3,\"pageSize\":20,\"totalPageNo\":1,\"currentPage\":1}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeRelationInstanceService.getServiceLevelInstance();
    }

    @Test
    public void getLunInstance() throws Exception{
        String instanceName = "SYS_Lun";
        String url = "/rest/resourcedb/v1/instances/{className}?pageSize=1000";
        url = url.replace("{className}", instanceName);
        String jsonData = "{\"objList\":[{\"ownerType\":\"eSight_Storage\",\"storageDeviceId\":\"B94BFF9D0DFB11EBBD3D0050568491C9\",\"ownerId\":\"3BEAFDD429EB32C2AC03EFA25608054D\",\"wwn\":\"67c1cf11005893452d301938000000dd\",\"ownerName\":\"eSight_Storage\",\"lastMonitorTime\":1603876362840,\"totalCapacity\":1.0,\"confirmStatus\":\"unconfirmed\",\"protectionCapacity\":0.0,\"id\":\"0AE76D42D5FD3F85A4A848B035004F90\",\"last_Modified\":1603876362863,\"djTierId\":\"D26B52294CBE353191BCACB710FECA23\",\"lunType\":\"normal\",\"class_Id\":1090,\"lunId\":\"221\",\"dataStatus\":\"normal\",\"resId\":\"0AE76D42D5FD3F85A4A848B035004F90\",\"dedupedCapacity\":0.0,\"is_Local\":true,\"class_Name\":\"SYS_Lun\",\"compressedCapacity\":0.0,\"regionId\":\"C4CA4238A0B933828DCC509A6F75849B\",\"name\":\"vmfsclusterliuxh102806\",\"mapped\":true,\"poolId\":\"0\",\"nativeId\":\"nedn=b94bff9d-0dfb-11eb-bd3d-0050568491c9,id=221,objecttype=11\",\"dataSource\":\"auto\",\"allocCapacity\":1.19}],\"totalNum\":106,\"pageSize\":20,\"totalPageNo\":6,\"currentPage\":1}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeRelationInstanceService.getLunInstance();
    }

    @Test
    public void getStorageDeviceInstance() throws Exception {
        String instanceName = "SYS_StorDevice";
        String url = "/rest/resourcedb/v1/instances/{className}?pageSize=1000";
        url = url.replace("{className}", instanceName);
        String jsonData = "{\"objList\":[{\"ownerType\":\"eSight_Storage\",\"ownerId\":\"3BEAFDD429EB32C2AC03EFA25608054D\",\"deviceName\":\"Huawei.Storage\",\"productName\":\"5300V5\",\"manufacturer\":\"Huawei\",\"lastMonitorTime\":1605759392684,\"ownerName\":\"eSight_Storage\",\"totalCapacity\":6542.69,\"confirmStatus\":\"unconfirmed\",\"id\":\"B94BFF9D0DFB11EBBD3D0050568491C9\",\"last_Modified\":1605759393424,\"sn\":\"2102351QLH9WK5800028\",\"freeDisksCapacity\":0.0,\"usedCapacity\":298.38,\"class_Id\":1085,\"hotSpareCapacity\":0.0,\"ipAddress\":\"10.143.133.201\",\"dataStatus\":\"normal\",\"version\":\"V500R007C10\",\"resId\":\"B94BFF9D0DFB11EBBD3D0050568491C9\",\"is_Local\":true,\"usableCapacity\":3181.0,\"class_Name\":\"SYS_StorDevice\",\"regionId\":\"C4CA4238A0B933828DCC509A6F75849B\",\"name\":\"Huawei.Storage\",\"nativeId\":\"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\"category\":\"StorageDevice\",\"dataSource\":\"auto\",\"assetStatus\":\"running\",\"status\":\"normal\"}],\"totalNum\":1,\"pageSize\":20,\"totalPageNo\":1,\"currentPage\":1}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeRelationInstanceService.getStorageDeviceInstance();
    }

    @Test
    public void getStoragePoolInstance() throws Exception {
        String instanceName = "SYS_StoragePool";
        String url = "/rest/resourcedb/v1/instances/{className}?pageSize=1000";
        url = url.replace("{className}", instanceName);
        String jsonData = "{\"objList\":[{\"ownerType\":\"eSight_Storage\",\"tier0RaidLv\":0,\"storageDeviceId\":\"B94BFF9D0DFB11EBBD3D0050568491C9\",\"ownerId\":\"3BEAFDD429EB32C2AC03EFA25608054D\",\"type\":\"file\",\"ownerName\":\"eSight_Storage\",\"lastMonitorTime\":1605775868397,\"runningStatus\":\"normal\",\"totalCapacity\":1024.0,\"tier2RaidLv\":0,\"confirmStatus\":\"unconfirmed\",\"protectionCapacity\":0.0,\"id\":\"3FB520372B753CFB8594F4C969587E5E\",\"last_Modified\":1605775868438,\"diskPoolId\":\"1\",\"usedCapacity\":77.5,\"tier0Capacity\":0.0,\"class_Id\":1029,\"dataStatus\":\"normal\",\"subscribedCapacity\":116.57,\"resId\":\"3FB520372B753CFB8594F4C969587E5E\",\"tier1RaidLv\":2,\"dedupedCapacity\":0.0,\"is_Local\":true,\"class_Name\":\"SYS_StoragePool\",\"compressedCapacity\":0.0,\"regionId\":\"C4CA4238A0B933828DCC509A6F75849B\",\"name\":\"fileStoragePool002\",\"poolId\":\"1\",\"tier1Capacity\":1024.0,\"nativeId\":\"nedn=b94bff9d-0dfb-11eb-bd3d-0050568491c9,id=1,objecttype=216\",\"dataSource\":\"auto\",\"tier2Capacity\":0.0,\"status\":\"normal\"}],\"totalNum\":1,\"pageSize\":20,\"totalPageNo\":1,\"currentPage\":1}";
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.fromJson(jsonData, JsonObject.class), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeRelationInstanceService.getStoragePoolInstance();
    }

    @Test
    public void refreshResourceInstance() throws Exception {
        dmeRelationInstanceService.refreshResourceInstance();
    }
}