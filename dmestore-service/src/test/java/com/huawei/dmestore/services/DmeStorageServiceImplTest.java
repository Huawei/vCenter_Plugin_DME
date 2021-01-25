package com.huawei.dmestore.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.BandPorts;
import com.huawei.dmestore.model.Dtrees;
import com.huawei.dmestore.model.EthPortInfo;
import com.huawei.dmestore.model.FileSystem;
import com.huawei.dmestore.model.LogicPorts;
import com.huawei.dmestore.model.NfsShares;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.model.StorageControllers;
import com.huawei.dmestore.model.StorageDetail;
import com.huawei.dmestore.model.VolumeListRestponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author lianq
 * @className DmeStorageServiceImplTest
 * @description TODO
 * @date 2020/11/12 10:09
 */
public class DmeStorageServiceImplTest {

    Gson gson = new Gson();
    String url;

    @Mock
    DmeAccessService dmeAccessService;

    @Mock
    DmeVmwareRalationDao dmeVmwareRalationDao;

    @Mock
    DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    @InjectMocks
    DmeStorageService dmeStorageService = new DmeStorageServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getStorages() throws DmeException {
        url = "/rest/storagemgmt/v1/storages";
        List<JsonObject> reqList = new ArrayList<>();
        //更换设定返回的数据
        String data = " {\n" +
            "    \"id\": \"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\n" +
            "    \"name\": \"Huawei.Storage\",\n" +
            "    \"ip\": \"10.143.133.201\",\n" +
            "    \"status\": \"1\",\n" +
            "    \"synStatus\": \"2\",\n" +
            "    \"sn\": \"2102351QLH9WK5800028\",\n" +
            "    \"vendor\": \"Huawei\",\n" +
            "    \"model\": \"5300 V5\",\n" +
            "    \"usedCapacity\": 297.5625,\n" +
            "    \"totalCapacity\": 6542.693389892578,\n" +
            "    \"totalEffectiveCapacity\": 3181,\n" +
            "    \"freeEffectiveCapacity\": 2883.44,\n" +
            "    \"subscriptionCapacity\": 309.57,\n" +
            "    \"protectionCapacity\": 0,\n" +
            "    \"fileCapacity\": 77.5,\n" +
            "    \"blockCapacity\": 220.06,\n" +
            "    \"dedupedCapacity\": 0,\n" +
            "    \"compressedCapacity\": 0,\n" +
            "    \"optimizeCapacity\": 297.5625,\n" +
            "    \"azIds\": [],\n" +
            "    \"storagePool\": null,\n" +
            "    \"volume\": null,\n" +
            "    \"fileSystem\": null,\n" +
            "    \"dTrees\": null,\n" +
            "    \"nfsShares\": null,\n" +
            "    \"bandPorts\": null,\n" +
            "    \"logicPorts\": null,\n" +
            "    \"storageControllers\": null,\n" +
            "    \"storageDisks\": null,\n" +
            "    \"productVersion\": \"V500R007C10\",\n" +
            "    \"warning\": null,\n" +
            "    \"event\": null,\n" +
            "    \"location\": \"\",\n" +
            "    \"patchVersion\": \"SPH013\",\n" +
            "    \"maintenanceStart\": null,\n" +
            "    \"maintenanceOvertime\": null\n" +
            "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        reqList.add(jsonObject);
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("datas", reqList);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(reqMap), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        List<Storage> storages = dmeStorageService.getStorages();
        System.out.println(storages);
    }

    @Test
    public void getStorageDetail() throws DmeException {
        String storageId = "123";
        url = "/rest/storagemgmt/v1/storages/123/detail";
        //更换设定返回的数据
        String data = " {\n" +
            "  \"id\" : \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            "  \"name\" : \"SPEC-1877\",\n" +
            "  \"ip\" : \"string\",\n" +
            "  \"status\" : \"string\",\n" +
            "  \"syn_status\" : \"string\",\n" +
            "  \"sn\" : \"string\",\n" +
            "  \"vendor\" : \"string\",\n" +
            "  \"model\" : \"string\",\n" +
            "  \"product_version\" : \"string\",\n" +
            "  \"patch_version\" : \"string\",\n" +
            "  \"used_capacity\" : 0.0,\n" +
            "  \"total_capacity\" : 0.0,\n" +
            "  \"total_effective_capacity\" : 0.0,\n" +
            "  \"free_effective_capacity\" : 0.0,\n" +
            "  \"location\" : \"string\",\n" +
            "  \"az_ids\" : [ \"string\" ],\n" +
            "  \"maintenance_start\" : 1564211245111,\n" +
            "  \"maintenance_overtime\" : 1564211245111\n" +
            "}";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(jsonObject), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        List<JsonObject> reqPool = new ArrayList<>();
        String pool = "{\n" +
            " \"ownerType\": \"eSight_Storage\",\n" +
            " \"storageDeviceId\": \"98BBDFB0579E11EAA20D005056ADF850\",\n" +
            " \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
            " \"wwn\": \"648435a10072df427691fabd00000013\",\n" +
            " \"ownerName\": \"eSight_Storage\",\n" +
            " \"lastMonitorTime\": 1582614988093,\n" +
            " \"totalCapacity\": 1.0,\n" +
            " \"protectionCapacity\": 0.0,\n" +
            " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"lunType\": \"normal\",\n" +
            " \"class_Id\": 1053,\n" +
            " \"lunId\": \"19\",\n" +
            " \"dataStatus\": \"normal\",\n" +
            " \"resId\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"dedupedCapacity\": 0.0,\n" +
            " \"class_Name\": \"SYS_Lun\",\n" +
            " \"compressedCapacity\": 0.0,\n" +
            " \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
            " \"name\": \"SPEC-1877\",\n" +
            " \"mapped\": false,\n" +
            " \"poolId\": \"30\",\n" +
            " \"nativeId\": \"nedn=98bbdfb0-579e-11ea-a20d-005056adf850,id=19,objecttype=11\",\n" +
            " \"dataSource\": \"auto\",\n" +
            " \"allocCapacity\": 1.19\n" +
            " }";
        JsonObject jsonPool = new JsonParser().parse(pool).getAsJsonObject();
        reqPool.add(jsonPool);
        Map<String, Object> mapPool = new HashMap<>();
        mapPool.put("objList", reqPool);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(mapPool), null, HttpStatus.OK);
        String poolUrl = "/rest/resourcedb/v1/instances/SYS_StoragePool?condition={json}&&pageSize=1000";
        String params =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"equal\",\"value\":\"123\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson(poolUrl, HttpMethod.GET, params)).thenReturn(responseEntity2);
        String diskUrl = "/rest/resourcedb/v1/instances/SYS_StorageDisk?condition={json} &&pageSize=1000";
        when(dmeAccessService.accessByJson(diskUrl, HttpMethod.GET, params)).thenReturn(responseEntity2);
        url = "/rest/resourcedb/v1/instances/SYS_DiskPool?condition={json}pageSize=1000";
        String params2 =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"poolId\",\"operator\":\"equal\",\"value\":\"30\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, params2)).thenReturn(responseEntity2);
        url = "/rest/resourcedb/v1/instances/SYS_StorageDisk?condition={json}&&pageSize=1000";
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, params)).thenReturn(responseEntity2);
        StorageDetail storageDetail = dmeStorageService.getStorageDetail(storageId);
        System.out.println(storageDetail);
    }


    @Test
    public void getLogicPorts() throws DmeException {
        String storageId = "123";
        url = "/rest/storagemgmt/v1/storage-port/logic-ports?storage_id=" + storageId;
        String data = " {\n" +
            "            \"id\": \"E25D7ED5FF043E66A32B96A6E7367AF4\", \n" +
            "            \"name\": \"test1\", \n" +
            "            \"running_status\": \"link down\", \n" +
            "            \"operational_status\": \"activated\", \n" +
            "            \"mgmt_ip\": \"192.169.200.4\", \n" +
            "            \"mgmt_ipv6\": \"\", \n" +
            "            \"home_port_id\": \"549772730374\", \n" +
            "            \"home_port_name\": \"CTE0.B.H3\", \n" +
            "            \"current_port_id\": \"549772730374\", \n" +
            "            \"current_port_name\": \"CTE0.B.H3\", \n" +
            "            \"role\": \"service\", \n" +
            "            \"ddns_status\": \"Enable\", \n" +
            "            \"failover_group_id\": \"0\", \n" +
            "            \"failover_group_name\": \"System-defined\", \n" +
            "            \"support_protocol\": \"NFS + CIFS\", \n" +
            "            \"listen_dns_query_enabled\": \"NO\", \n" +
            "            \"management_access\": \"--\", \n" +
            "            \"vstore_id\": null, \n" +
            "            \"vstore_name\": null\n" +
            "        }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("logic_ports", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        List<LogicPorts> logicPorts = dmeStorageService.getLogicPorts(storageId);
        System.out.println(logicPorts);

    }

    @Test
    public void getVolumesByPage() throws DmeException {
        String storageId = "123";
        String url = "/rest/blockservice/v1/volumes?storage_id=" + storageId + "&";
        String data = "{\n" +
            "    \"id\" : \"02bb989a-7ac2-40cd-852d-c9b26bb2ab5b\",\n" +
            "    \"name\" : \"volume001\",\n" +
            "    \"description\" : \"test volume\",\n" +
            "    \"status\" : \"normal\",\n" +
            "    \"attached\" : false,\n" +
            "    \"project_id\" : \"e554f301f99044bf8175e60fc1524ebc\",\n" +
            "    \"alloctype\" : \"thin\",\n" +
            "    \"capacity\" : 10,\n" +
            "    \"service_level_name\" : \"TEST\",\n" +
            "    \"attachments\" : [ {\n" +
            "      \"id\" : \"98c41e3f-de68-4b0a-8298-dccf81c1ca6c\",\n" +
            "      \"volume_id\" : \"98c41e3f-de68-4b0a-8298-dccf81c1ca6c\",\n" +
            "      \"host_id\" : \"434f80a9-22b4-4d44-b14e-783aa41689d5\",\n" +
            "      \"attached_at\" : \"2019-09-22T15:20:26.000000\",\n" +
            "      \"attached_host_group\" : \"534052e9-031e-49b3-a6f5-ac93c0ffc8a2\"\n" +
            "    } ],\n" +
            "    \"volume_raw_id\" : \"4631\",\n" +
            "    \"volume_wwn\" : \"6f898ef100c7074949b7711e00001217\",\n" +
            "    \"storage_id\" : \"02bb989a-7ac2-40cd-852d-c9b26bb2ab5b\",\n" +
            "    \"storage_sn\" : \"210048435a227e94\",\n" +
            "    \"pool_raw_id\" : \"0\",\n" +
            "    \"capacity_usage\" : \"20\",\n" +
            "    \"protected\" : false,\n" +
            "    \"updated_at\" : \"2019-05-06T18:49:27.046019\",\n" +
            "    \"created_at\" : \"2019-05-06T18:49:25.107161\"\n" +
            "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("volumes", list);
        map.put("count", 1);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        String result = "11";
        String volumeId = "22";
        when(dmeVmwareRalationDao.getVmfsNameByVolumeId(volumeId)).thenReturn(result);
        List<JsonObject> reqPool = new ArrayList<>();
        String pool = "{\n" +
            " \"ownerType\": \"eSight_Storage\",\n" +
            " \"storageDeviceId\": \"98BBDFB0579E11EAA20D005056ADF850\",\n" +
            " \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
            " \"wwn\": \"648435a10072df427691fabd00000013\",\n" +
            " \"ownerName\": \"eSight_Storage\",\n" +
            " \"lastMonitorTime\": 1582614988093,\n" +
            " \"totalCapacity\": 1.0,\n" +
            " \"protectionCapacity\": 0.0,\n" +
            " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"lunType\": \"normal\",\n" +
            " \"class_Id\": 1053,\n" +
            " \"lunId\": \"19\",\n" +
            " \"dataStatus\": \"normal\",\n" +
            " \"resId\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"dedupedCapacity\": 0.0,\n" +
            " \"class_Name\": \"SYS_Lun\",\n" +
            " \"compressedCapacity\": 0.0,\n" +
            " \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
            " \"name\": \"SPEC-1877\",\n" +
            " \"mapped\": false,\n" +
            " \"poolId\": \"30\",\n" +
            " \"nativeId\": \"nedn=98bbdfb0-579e-11ea-a20d-005056adf850,id=19,objecttype=11\",\n" +
            " \"dataSource\": \"auto\",\n" +
            " \"allocCapacity\": 1.19\n" +
            " }";
        JsonObject jsonPool = new JsonParser().parse(pool).getAsJsonObject();
        reqPool.add(jsonPool);
        Map<String, Object> mapPool = new HashMap<>();
        mapPool.put("objList", reqPool);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(mapPool), null, HttpStatus.OK);
        String poolUrl = "/rest/resourcedb/v1/instances/SYS_StoragePool?condition={json}&&pageSize=1000";
        String params =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"poolId\",\"operator\":\"equal\",\"value\":\"0\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson(poolUrl, HttpMethod.GET, params)).thenReturn(responseEntity2);
        VolumeListRestponse volumesByPage = dmeStorageService.getVolumesByPage(storageId, null, null);
        System.out.println(volumesByPage);

    }

    @Test
    public void getFileSystems() throws DmeException {
        String storageId = "123";
        url = "/rest/fileservice/v1/filesystems/query";
        String data = "{\n" +
            "    \"id\" : \"CC7EA6E662B830298D0D235CFC5125BB\",\n" +
            "    \"name\" : \"filesystem001\",\n" +
            "    \"health_status\" : \"normal\",\n" +
            "    \"alloc_type\" : \"thick\",\n" +
            "    \"type\" : \"normal\",\n" +
            "    \"capacity\" : 10.125,\n" +
            "    \"available_capacity\" : 10.125,\n" +
            "    \"capacity_usage_ratio\" : 50,\n" +
            "    \"storage_name\" : \"StorageDevice001\",\n" +
            "    \"storage_id\" : \"c54d8ae4-4e49-485e-9288-079e7b92cc05\",\n" +
            "    \"storage_pool_name\" : \"StorageDevice001\",\n" +
            "    \"tier_name\" : \"SAL001\",\n" +
            "    \"tier_id\" : \"ca4d8ae4-4e49-485e-9288-079e7b92cc05\",\n" +
            "    \"nfs_count\" : 20,\n" +
            "    \"cifs_count\" : 20,\n" +
            "    \"dtree_count\" : 40,\n" +
            "    \"vstore_name\" : \"vStore001\",\n" +
            "    \"project_name\" : \"Project001\",\n" +
            "    \"project_id\" : \"CC7EA6E662B830298D0D235CFC5125BB\"\n" +
            "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        Map<String, Object> params = new HashMap<>();
        params.put("storage_id", storageId);
        when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params))).thenReturn(responseEntity);
        List<FileSystem> fileSystems = dmeStorageService.getFileSystems(storageId);
        System.out.println(fileSystems);
    }

    @Test
    public void getDtrees() throws DmeException {
        String storageId = "123";
        url = "/rest/fileservice/v1/dtrees/summary";
        String data = "{\n" +
            "  \"id\" : \"319585d6-13da-4b9d-9591-33ce63b52c0a\",\n" +
            "  \"id_in_storage\" : \"01\",\n" +
            "  \"name\" : \"Dtree 001\",\n" +
            "  \"device_name\" : \"Device001\",\n" +
            "  \"storage_id\" : \"1\",\n" +
            "  \"ne_id\" : \"6a72c77b-9028-11ea-97f1-0050568fd541\",\n" +
            "  \"tier_name\" : \"ERP & HANA\",\n" +
            "  \"fs_name\" : \"FileSystem001\",\n" +
            "  \"fs_id\" : \"8B6B1E8F30CE3AD98B20CBBCDC8B6CC3\",\n" +
            "  \"quota_switch\" : false,\n" +
            "  \"nfs_count\" : 20,\n" +
            "  \"cifs_count\" : 20,\n" +
            "  \"security_style\" : \"Mixed\" \n" +
            "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("dtrees", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        Map<String, Object> params = new HashMap<>();
        params.put("storage_id", storageId);
        when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params))).thenReturn(responseEntity);
        List<Dtrees> dtrees = dmeStorageService.getDtrees(storageId);
        System.out.println(dtrees);
    }

    @Test
    public void getNfsShares() throws DmeException {
        String storageId = "123";
        url = "/rest/fileservice/v1/nfs-shares/summary";
        String data = "{\n" +
            "  \"id\" : \"319585d6-13da-4b9d-9591-33ce63b52c0a\",\n" +
            "  \"id_in_storage\" : \"01\",\n" +
            "  \"vstore_id_in_storage\" : \"01\",\n" +
            "  \"name\" : \"nfs share name\",\n" +
            "  \"share_path\" : \"/FileSytem01/Content01/Directory01\",\n" +
            "  \"device_name\" : \"Device001\",\n" +
            "  \"storage_id\" : \"Device001\",\n" +
            "  \"ne_id\" : \"1\",\n" +
            "  \"tier_name\" : \"ERP & HANA\",\n" +
            "  \"description\" : \"desc\",\n" +
            "  \"fs_name\" : \"FileSystem001\",\n" +
            "  \"fs_id\" : \"8B6B1E8F30CE3AD98B20CBBCDC8B6CC3\",\n" +
            "  \"character_encoding\" : \"utf-8\",\n" +
            "  \"enable_show_snapshot\" : false,\n" +
            "  \"audit_items\" : [ \"none\" ],\n" +
            "  \"owning_dtree_id\" : \"Dtree001\",\n" +
            "  \"owning_dtree_name\" : \"Dtree001\"\n" +
            "}";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("nfs_share_info_list", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        Map<String, Object> params = new HashMap<>();
        params.put("storage_id", storageId);
        when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params))).thenReturn(responseEntity);
        List<NfsShares> nfsShares = dmeStorageService.getNfsShares(storageId);
        System.out.println(nfsShares);
    }

    @Test
    public void getBandPorts() throws DmeException {
        String storageId = "123";
        url = "/rest/storagemgmt/v1/storage-port/bond-ports?storage_id=" + storageId;
        String data = "{\n" +
            "    \"id\" : \"id001\",\n" +
            "    \"name\" : \"BondPort001\",\n" +
            "    \"health_status\" : \"normal\",\n" +
            "    \"running_status\" : \"running\",\n" +
            "    \"mtu\" : \"1500\"\n" +
            "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("bond_ports", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        List<BandPorts> bandPorts = dmeStorageService.getBandPorts(storageId);
        System.out.println(bandPorts);
    }

    @Test
    public void getStorageControllers() throws DmeException {
        String storageDeviceId = "123";
        url = "/rest/resourcedb/v1/instances/SYS_Controller?condition={json}&&pageSize=1000";
        String pool = "{\n" +
            " \"ownerType\": \"eSight_Storage\",\n" +
            " \"storageDeviceId\": \"98BBDFB0579E11EAA20D005056ADF850\",\n" +
            " \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
            " \"wwn\": \"648435a10072df427691fabd00000013\",\n" +
            " \"ownerName\": \"eSight_Storage\",\n" +
            " \"lastMonitorTime\": 1582614988093,\n" +
            " \"totalCapacity\": 1.0,\n" +
            " \"protectionCapacity\": 0.0,\n" +
            " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"lunType\": \"normal\",\n" +
            " \"class_Id\": 1053,\n" +
            " \"lunId\": \"19\",\n" +
            " \"dataStatus\": \"normal\",\n" +
            " \"resId\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"dedupedCapacity\": 0.0,\n" +
            " \"class_Name\": \"SYS_Lun\",\n" +
            " \"compressedCapacity\": 0.0,\n" +
            " \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
            " \"name\": \"SPEC-1877\",\n" +
            " \"mapped\": false,\n" +
            " \"poolId\": \"30\",\n" +
            " \"nativeId\": \"nedn=98bbdfb0-579e-11ea-a20d-005056adf850,id=19,objecttype=11\",\n" +
            " \"dataSource\": \"auto\",\n" +
            " \"allocCapacity\": 1.19\n" +
            " }";
        JsonObject jsonObject = new JsonParser().parse(pool).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("objList", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        String params =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"equal\",\"value\":\"123\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, params)).thenReturn(responseEntity);

        String param1 = "SYS_Controller";
        String storageId = "123";
        List<String> list1 = new ArrayList<>();
        list1.add(storageId);
        Map<String, Object> map1 = new HashMap<>();
        map.put("obj_ids", list1);
        Map<String, Object> resp = new HashMap<>();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject.addProperty("1125908496842763", 20);
        jsonObject.addProperty("1125908496842755", 50);
        jsonObject.addProperty("1125908496842762", 30);
        jsonObject.addProperty("1125908496842753", 33);
        resp.put(storageId, jsonObject1);
        when(dataStoreStatisticHistoryService.queryCurrentStatistic(param1, map1)).thenReturn(resp);
        List<StorageControllers> storageControllers = dmeStorageService.getStorageControllers(storageDeviceId);
        System.out.println(storageControllers);
    }

    @Test
    public void getStorageEthPorts() throws Exception {
        String sn = "123";
        url = "/rest/resourcedb/v1/instances/SYS_StorDevice?condition={json}";
        String pool = "{\n" +
            " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"allocCapacity\": 1.19\n" +
            " }";
        JsonObject jsonObject = new JsonParser().parse(pool).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("objList", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        String params =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"sn\",\"operator\":\"equal\",\"value\":\"123\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, params)).thenReturn(responseEntity);
        String param =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"portType\",\"operator\":\"equal\",\"value\":\"ETH\"},\"logOp\":\"and\"}," +
                "{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"equal\",\"value\":\"3F6CC81D8C4C358F8888303479544ACB\"},\"logOp\":\"and\"}]}";
        String url2 = "/rest/resourcedb/v1/instances/SYS_StoragePort?condition={json}";
        String data2 = "{\n" +
            " \"ownerType\": \"eSight_Storage\",\n" +
            " \"storageDeviceId\": \"98BBDFB0579E11EAA20D005056ADF850\",\n" +
            " \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
            " \"wwn\": \"648435a10072df427691fabd00000013\",\n" +
            " \"ownerName\": \"eSight_Storage\",\n" +
            " \"lastMonitorTime\": 1582614988093,\n" +
            " \"totalCapacity\": 1.0,\n" +
            " \"protectionCapacity\": 0.0,\n" +
            " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"lunType\": \"normal\",\n" +
            " \"class_Id\": 1053,\n" +
            " \"lunId\": \"19\",\n" +
            " \"dataStatus\": \"normal\",\n" +
            " \"resId\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"dedupedCapacity\": 0.0,\n" +
            " \"class_Name\": \"SYS_Lun\",\n" +
            " \"compressedCapacity\": 0.0,\n" +
            " \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
            " \"name\": \"SPEC-1877\",\n" +
            " \"mapped\": false,\n" +
            " \"poolId\": \"30\",\n" +
            " \"nativeId\": \"nedn=98bbdfb0-579e-11ea-a20d-005056adf850,id=19,objecttype=11\",\n" +
            " \"dataSource\": \"auto\",\n" +
            " \"allocCapacity\": 1.19\n" +
            " }";
        JsonObject jsonObject2 = new JsonParser().parse(data2).getAsJsonObject();
        List<JsonObject> list2 = new ArrayList<>();
        list2.add(jsonObject2);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("objList", list2);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(map2), null, HttpStatus.OK);
        when(dmeAccessService.accessByJson(url2, HttpMethod.GET, param)).thenReturn(responseEntity2);
        List<EthPortInfo> storageEthPorts = dmeStorageService.getStorageEthPorts(sn);
        System.out.println(storageEthPorts);
    }

    @Test
    public void getVolume() throws DmeException {
        String volumeId = "123";
        url = "/rest/blockservice/v1/volumes/" + volumeId;
        //更换设定返回的数据
        String data = "{\n" +
            "    \"id\" : \"02bb989a-7ac2-40cd-852d-c9b26bb2ab5b\",\n" +
            "    \"name\" : \"volume001\",\n" +
            "    \"description\" : \"test volume\",\n" +
            "    \"status\" : \"normal\",\n" +
            "    \"attached\" : false,\n" +
            "    \"project_id\" : \"e554f301f99044bf8175e60fc1524ebc\",\n" +
            "    \"alloctype\" : \"thin\",\n" +
            "    \"capacity\" : 10,\n" +
            "    \"service_level_name\" : \"TEST\",\n" +
            "    \"attachments\" : [ {\n" +
            "      \"id\" : \"98c41e3f-de68-4b0a-8298-dccf81c1ca6c\",\n" +
            "      \"volume_id\" : \"98c41e3f-de68-4b0a-8298-dccf81c1ca6c\",\n" +
            "      \"host_id\" : \"434f80a9-22b4-4d44-b14e-783aa41689d5\",\n" +
            "      \"attached_at\" : \"2019-09-22T15:20:26.000000\",\n" +
            "      \"attached_host_group\" : \"534052e9-031e-49b3-a6f5-ac93c0ffc8a2\"\n" +
            "    } ],\n" +
            "    \"volume_raw_id\" : \"4631\",\n" +
            "    \"volume_wwn\" : \"6f898ef100c7074949b7711e00001217\",\n" +
            "    \"storage_id\" : \"02bb989a-7ac2-40cd-852d-c9b26bb2ab5b\",\n" +
            "    \"storage_sn\" : \"210048435a227e94\",\n" +
            "    \"pool_raw_id\" : \"0\",\n" +
            "    \"capacity_usage\" : \"20\",\n" +
            "    \"protected\" : false,\n" +
            "    \"updated_at\" : \"2019-05-06T18:49:27.046019\",\n" +
            "    \"created_at\" : \"2019-05-06T18:49:25.107161\"\n" +
            "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("volume", jsonObject);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(reqMap), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        Map<String, Object> volume = dmeStorageService.getVolume(volumeId);
        System.out.println(volume);

    }

    @Test
    public void getStoragePort() throws DmeException {
        String storageDeviceId = "123";
        url = "/rest/resourcedb/v1/instances/SYS_StoragePort?condition={json}&&pageSize=1000";
        String pool = "{\n" +
            " \"ownerType\": \"eSight_Storage\",\n" +
            " \"storageDeviceId\": \"98BBDFB0579E11EAA20D005056ADF850\",\n" +
            " \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
            " \"wwn\": \"648435a10072df427691fabd00000013\",\n" +
            " \"ownerName\": \"eSight_Storage\",\n" +
            " \"lastMonitorTime\": 1582614988093,\n" +
            " \"totalCapacity\": 1.0,\n" +
            " \"protectionCapacity\": 0.0,\n" +
            " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"lunType\": \"normal\",\n" +
            " \"class_Id\": 1053,\n" +
            " \"lunId\": \"19\",\n" +
            " \"dataStatus\": \"normal\",\n" +
            " \"resId\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"dedupedCapacity\": 0.0,\n" +
            " \"class_Name\": \"SYS_Lun\",\n" +
            " \"compressedCapacity\": 0.0,\n" +
            " \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
            " \"name\": \"SPEC-1877\",\n" +
            " \"mapped\": false,\n" +
            " \"poolId\": \"30\",\n" +
            " \"nativeId\": \"nedn=98bbdfb0-579e-11ea-a20d-005056adf850,id=19,objecttype=11\",\n" +
            " \"dataSource\": \"auto\",\n" +
            " \"allocCapacity\": 1.19\n" +
            " }";
        JsonObject jsonObject = new JsonParser().parse(pool).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("objList", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        String params =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"equal\",\"value\":\"123\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, params)).thenReturn(responseEntity);
        System.out.println(dmeStorageService.getStoragePort(storageDeviceId, "ALL"));

    }

    @Test
    public void getFailoverGroups() throws DmeException {
        String storageId = "123";
        url = "/rest/storagemgmt/v1/storage-port/failover-groups?storage_id=" + storageId;
        String data = "{\n" +
            "    \"id\" : \"id001\",\n" +
            "    \"name\" : \"Name001\",\n" +
            "    \"failover_group_type\" : \"system\"\n" +
            "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("failover_groups", list);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        System.out.println(dmeStorageService.getFailoverGroups(storageId));
    }

    @Test
    public void getFileSystemDetail() throws DmeException {
        String fileSystemId = "123";
        url = "/rest/fileservice/v1/filesystems/" + fileSystemId;
        String data = "{\n" +
            "  \"id\" : \"CC7EA6E662B830298D0D235CFC5125BB\",\n" +
            "  \"name\" : \"filesystem001\",\n" +
            "  \"health_status\" : \"normal\",\n" +
            "  \"alloc_type\" : \"thick\",\n" +
            "  \"type\" : \"normal\",\n" +
            "  \"capacity\" : 10.125,\n" +
            "  \"available_capacity\" : 10.125,\n" +
            "  \"capacity_usage_ratio\" : 50,\n" +
            "  \"storage_name\" : \"StorageDevice001\",\n" +
            "  \"storage_id\" : \"c54d8ae4-4e49-485e-9288-079e7b92cc05\",\n" +
            "  \"storage_pool_name\" : \"StorageDevice001\",\n" +
            "  \"tier_name\" : \"SAL001\",\n" +
            "  \"tier_id\" : \"ca4d8ae4-4e49-485e-9288-079e7b92cc05\",\n" +
            "  \"nfs_count\" : 20,\n" +
            "  \"cifs_count\" : 20,\n" +
            "  \"dtree_count\" : 40,\n" +
            "  \"vstore_name\" : \"vStore001\",\n" +
            "  \"project_name\" : \"Project001\",\n" +
            "  \"project_id\" : \"CC7EA6E662B830298D0D235CFC5125BB\",\n" +
            "  \"description\" : \"This is a file system\",\n" +
            "  \"owning_controller\" : \"0A\",\n" +
            "  \"checksum_enabled\" : true,\n" +
            "  \"security_mode\" : \"mixed\",\n" +
            "  \"automatic_update_time\" : true,\n" +
            "  \"automatic_update_mode\" : \"3600\",\n" +
            "  \"ads_enabled\" : true,\n" +
            "  \"initial_distribute_policy\" : \"string\",\n" +
            "  \"capacity_threshold\" : 80,\n" +
            "  \"allocate_quota_in_pool\" : 10.125,\n" +
            "  \"min_size_fs_capacity\" : 10.125,\n" +
            "  \"cifs_homedir_count\" : 0,\n" +
            "  \"enable_quota_dtree_count\" : 40,\n" +
            "  \"vstore_id_in_storage\" : \"40\",\n" +
            "  \"snapshot_expired_enabled\" : true,\n" +
            "  \"tuning\" : {\n" +
            "    \"deduplication_enabled\" : true,\n" +
            "    \"compression_enabled\" : false,\n" +
            "    \"application_scenario\" : \"user_defined\",\n" +
            "    \"block_size\" : 4,\n" +
            "    \"smart_qos\" : {\n" +
            "      \"max_bandwidth\" : 100,\n" +
            "      \"max_iops\" : 100,\n" +
            "      \"min_bandwidth\" : 100,\n" +
            "      \"min_iops\" : 100,\n" +
            "      \"latency\" : 100\n" +
            "    },\n" +
            "    \"smart_tier\" : {\n" +
            "      \"data_transfer_policy\" : \"string\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"capacity_auto_negotiation\" : {\n" +
            "    \"capacity_self_adjusting_mode\" : \"grow\",\n" +
            "    \"capacity_recycle_mode\" : \"expand_capacity\",\n" +
            "    \"auto_size_enable\" : true,\n" +
            "    \"auto_grow_threshold_percent\" : 50,\n" +
            "    \"auto_shrink_threshold_percent\" : 50,\n" +
            "    \"max_auto_size\" : 1.0,\n" +
            "    \"min_auto_size\" : 1.0,\n" +
            "    \"auto_size_increment\" : 64\n" +
            "  },\n" +
            "  \"worm\" : {\n" +
            "    \"type\" : \"compliance_mode\",\n" +
            "    \"min_protect_period\" : 1095,\n" +
            "    \"min_protect_period_unit\" : \"year\",\n" +
            "    \"max_protect_period\" : 25550,\n" +
            "    \"max_protect_period_unit\" : \"year\",\n" +
            "    \"def_protect_period\" : 2550,\n" +
            "    \"def_protect_period_unit\" : \"year\",\n" +
            "    \"auto_lock\" : true,\n" +
            "    \"auto_lock_time\" : 100,\n" +
            "    \"auto_lock_time_unit\" : \"hour\",\n" +
            "    \"auto_del\" : true\n" +
            "  }\n" +
            "}";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(jsonObject), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        System.out.println(dmeStorageService.getFileSystemDetail(fileSystemId));
    }

    @Test
    public void listStoragePerformance() throws DmeException {

        String param1 = "SYS_StorDevice";
        String storageId = "123";
        List<String> list = new ArrayList<>();
        list.add(storageId);
        Map<String, Object> map = new HashMap<>();
        map.put("obj_ids", list);
        Map<String, Object> resp = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("1125904201875458", 20);
        jsonObject.addProperty("1125904201875457", 50);
        jsonObject.addProperty("1125904201875464", 30);
        jsonObject.addProperty("1125904201875465", 33);
        resp.put(storageId, jsonObject);
        when(dataStoreStatisticHistoryService.queryCurrentStatistic(param1, map)).thenReturn(resp);
        System.out.println(dmeStorageService.listStoragePerformance(list));
    }

    @Test
    public void listStoragePoolPerformance() throws DmeException {
        String param1 = "SYS_StoragePool";
        String storageId = "123";
        List<String> list = new ArrayList<>();
        list.add(storageId);
        Map<String, Object> map = new HashMap<>();
        map.put("obj_ids", list);
        Map<String, Object> resp = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("1125904201875458", 20);
        jsonObject.addProperty("1125904201875457", 50);
        jsonObject.addProperty("1125904201875464", 30);
        jsonObject.addProperty("1125904201875465", 33);
        resp.put(storageId, jsonObject);
        when(dataStoreStatisticHistoryService.queryCurrentStatistic(param1, map)).thenReturn(resp);
        System.out.println(dmeStorageService.listStoragePoolPerformance(list));
    }

    @Test
    public void listStorageControllerPerformance() throws DmeException {
        String param1 = "SYS_Controller";
        String storageId = "123";
        List<String> list = new ArrayList<>();
        list.add(storageId);
        Map<String, Object> map = new HashMap<>();
        map.put("obj_ids", list);
        Map<String, Object> resp = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("1125908496842763", 20);
        jsonObject.addProperty("1125908496842755", 50);
        jsonObject.addProperty("1125908496842762", 30);
        jsonObject.addProperty("1125908496842753", 33);
        resp.put(storageId, jsonObject);
        when(dataStoreStatisticHistoryService.queryCurrentStatistic(param1, map)).thenReturn(resp);
        System.out.println(dmeStorageService.listStorageControllerPerformance(list));
    }

    @Test
    public void listStorageDiskPerformance() throws DmeException {
        String param1 = "SYS_StorageDisk";
        String storageId = "123";
        List<String> list = new ArrayList<>();
        list.add(storageId);
        Map<String, Object> map = new HashMap<>();
        map.put("obj_ids", list);
        Map<String, Object> resp = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("1125917086777347", 20);
        jsonObject.addProperty("1125917086777346", 50);
        jsonObject.addProperty("1125917086777351", 30);
        jsonObject.addProperty("1125917086777345", 33);
        resp.put(storageId, jsonObject);
        when(dataStoreStatisticHistoryService.queryCurrentStatistic(param1, map)).thenReturn(resp);
        System.out.println(dmeStorageService.listStorageDiskPerformance(list));
    }

    @Test
    public void listStoragePortPerformance() throws DmeException {
        String param1 = "SYS_StoragePort";
        String storageId = "123";
        List<String> list = new ArrayList<>();
        list.add(storageId);
        Map<String, Object> map = new HashMap<>();
        map.put("obj_ids", list);
        Map<String, Object> resp = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("1125925676711946", 20);
        jsonObject.addProperty("1125925676711938", 50);
        jsonObject.addProperty("1125925676711945", 30);
        jsonObject.addProperty("1125925676711951", 33);
        resp.put(storageId, jsonObject);
        when(dataStoreStatisticHistoryService.queryCurrentStatistic(param1, map)).thenReturn(resp);
        System.out.println(dmeStorageService.listStoragePortPerformance(list));
    }

    @Test
    public void listVolumesPerformance() throws DmeException {
        String param1 = "SYS_Lun";
        String storageId = "123";
        List<String> list = new ArrayList<>();
        list.add(storageId);
        Map<String, Object> map = new HashMap<>();
        map.put("obj_ids", list);
        Map<String, Object> resp = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("1125921381744641", 20);
        jsonObject.addProperty("1125921381744643", 50);
        jsonObject.addProperty("1125921381744642", 30);
        jsonObject.addProperty("1125925676711951", 33);
        resp.put(storageId, jsonObject);
        when(dataStoreStatisticHistoryService.queryCurrentStatistic(param1, map)).thenReturn(resp);
        System.out.println(dmeStorageService.listVolumesPerformance(list));
    }

    @Test
    public void queryVolumeByName() throws DmeException {
        String name = "123";
        url = "/rest/blockservice/v1/volumes?name=" + name;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count", 1);
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.toJson(jsonObject), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        System.out.println(dmeStorageService.queryVolumeByName(name));
    }

    @Test
    public void queryFsByName() throws DmeException {
        String storageId = "123";
        String name = "123";
        url = "/rest/fileservice/v1/filesystems/query";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("storage_id", storageId);
        String data = "{\n" +
            "    \"id\" : \"CC7EA6E662B830298D0D235CFC5125BB\",\n" +
            "    \"name\" : \"123\",\n" +
            "    \"project_id\" : \"CC7EA6E662B830298D0D235CFC5125BB\"\n" +
            "  }";
        JsonObject jsonObject1 = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject1);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(jsonObject))).thenReturn(responseEntity);
        System.out.println(dmeStorageService.queryFsByName(name, storageId));
    }

    @Test
    public void queryShareByName() throws DmeException {
        String storageId = "123";
        String name = "123";
        url = "/rest/fileservice/v1/nfs-shares/summary";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("storage_id", storageId);
        String data = "{\n" +
            "    \"id\" : \"CC7EA6E662B830298D0D235CFC5125BB\",\n" +
            "    \"name\" : \"123\",\n" +
            "    \"project_id\" : \"CC7EA6E662B830298D0D235CFC5125BB\"\n" +
            "  }";
        JsonObject jsonObject1 = new JsonParser().parse(data).getAsJsonObject();
        List<JsonObject> list = new ArrayList<>();
        list.add(jsonObject1);
        Map<String, Object> map = new HashMap<>();
        map.put("nfs_share_info_list", list);
        ResponseEntity<String> responseEntity = new ResponseEntity(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(jsonObject))).thenReturn(responseEntity);
        System.out.println(dmeStorageService.queryShareByName(name, storageId));
    }

    @Test
    public void getStorageDisks() throws DmeException {
        String storageDeviceId = "123";
        String pool = "{\n" +
            " \"ownerType\": \"eSight_Storage\",\n" +
            " \"storageDeviceId\": \"98BBDFB0579E11EAA20D005056ADF850\",\n" +
            " \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
            " \"wwn\": \"648435a10072df427691fabd00000013\",\n" +
            " \"ownerName\": \"eSight_Storage\",\n" +
            " \"lastMonitorTime\": 1582614988093,\n" +
            " \"totalCapacity\": 1.0,\n" +
            " \"protectionCapacity\": 0.0,\n" +
            " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"lunType\": \"normal\",\n" +
            " \"class_Id\": 1053,\n" +
            " \"lunId\": \"19\",\n" +
            " \"dataStatus\": \"normal\",\n" +
            " \"resId\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
            " \"dedupedCapacity\": 0.0,\n" +
            " \"class_Name\": \"SYS_Lun\",\n" +
            " \"compressedCapacity\": 0.0,\n" +
            " \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
            " \"name\": \"SPEC-1877\",\n" +
            " \"mapped\": false,\n" +
            " \"poolId\": \"30\",\n" +
            " \"nativeId\": \"nedn=98bbdfb0-579e-11ea-a20d-005056adf850,id=19,objecttype=11\",\n" +
            " \"dataSource\": \"auto\",\n" +
            " \"allocCapacity\": 1.19\n" +
            " }";
        JsonObject jsonPool = new JsonParser().parse(pool).getAsJsonObject();
        List<JsonObject> reqPool = new ArrayList<>();
        reqPool.add(jsonPool);
        Map<String, Object> mapPool = new HashMap<>();
        mapPool.put("objList", reqPool);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(mapPool), null, HttpStatus.OK);
        String params =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"equal\",\"value\":\"123\"},\"logOp\":\"and\"}]}";
        String diskUrl = "/rest/resourcedb/v1/instances/SYS_StorageDisk?condition={json} &&pageSize=1000";
        when(dmeAccessService.accessByJson(diskUrl, HttpMethod.GET, params)).thenReturn(responseEntity2);

        url = "/rest/resourcedb/v1/instances/SYS_DiskPool?condition={json}pageSize=1000";
        String params2 =
            "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"poolId\",\"operator\":\"equal\",\"value\":\"30\"},\"logOp\":\"and\"}]}";
        when(dmeAccessService.accessByJson(url, HttpMethod.GET, params2)).thenReturn(responseEntity2);
        System.out.println(dmeStorageService.getStorageDisks(storageDeviceId));

    }
}