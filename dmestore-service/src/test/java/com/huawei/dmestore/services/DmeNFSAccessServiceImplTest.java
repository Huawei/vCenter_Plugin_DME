package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.NfsDataInfo;
import com.huawei.dmestore.model.NfsDataStoreFsAttr;
import com.huawei.dmestore.model.NfsDataStoreLogicPortAttr;
import com.huawei.dmestore.model.NfsDataStoreShareAttr;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className DmeNFSAccessServiceImplTest
 * @description TODO
 * @date 2020/11/19 14:07
 */
public class DmeNFSAccessServiceImplTest {

    private Gson gson = new Gson();
    @Mock
    private DmeVmwareRalationDao dmeVmwareRalationDao;
    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    private DmeStorageService dmeStorageService;
    @Mock
    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;
    @Mock
    private TaskService taskService;
    @Mock
    private VCSDKUtils vcsdkUtils;

    @InjectMocks
    DmeNFSAccessService dmeNFSAccessService = new DmeNFSAccessServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNfsDatastoreShareAttr() throws DmeException {
        when(dmeVmwareRalationDao.getShareIdByStorageId("321")).thenReturn("321");
        String resp = "{\n" +
                "    \"id\": \"321\", \n" +
                "    \"id_in_storage\": \"01\", \n" +
                "    \"vstore_id_in_storage\": \"01\", \n" +
                "    \"name\": \"nfs share name\", \n" +
                "    \"share_path\": \"/FileSytem01/Content01/Directory01\", \n" +
                "    \"device_name\": \"Device001\", \n" +
                "    \"storage_id\": \"Device001\", \n" +
                "    \"ne_id\": \"1\", \n" +
                "    \"tier_name\": \"ERP & HANA\", \n" +
                "    \"description\": \"desc\", \n" +
                "    \"fs_name\": \"FileSystem001\", \n" +
                "    \"fs_id\": \"8B6B1E8F30CE3AD98B20CBBCDC8B6CC3\", \n" +
                "    \"character_encoding\": \"utf-8\", \n" +
                "    \"enable_show_snapshot\": false, \n" +
                "    \"audit_items\": [\n" +
                "        \"none\"\n" +
                "    ], \n" +
                "    \"owning_dtree_id\": \"Dtree001\", \n" +
                "    \"owning_dtree_name\": \"Dtree001\"\n" +
                "}\n";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(resp, null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/fileservice/v1/nfs-shares/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        String resp2 = "{\n" +
                "  \"accessval\" : \"NfsShare name\",\n" +
                "  \"id\" : \"321\",\n" +
                "  \"name\" : \"Device001\",\n" +
                "  \"all_squash\" : \"Device001\",\n" +
                "  \"parent_id\" : \"ERP & HANA\",\n" +
                "  \"root_squash\" : \"321\",\n" +
                "  \"secure\" : \"321\",\n" +
                "  \"sync\" : \"321\",\n" +
                "  \"type\" : \"Dtree\",\n" +
                "  \"vstore_id_in_storage\" : \"FileSystem001\",\n" +
                "  \"vstore_name\" : \"8B6B1E8F30CE3AD98B20CBBCDC8B6CC3\" \n" +
                "}";
        JsonObject jsonObject = gson.fromJson(resp2, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("auth_client_list", jsonObjects);
        map.put("total",1);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/fileservice/v1/nfs-shares/321/auth_clients", HttpMethod.POST, gson.toJson(new HashMap<>()))).thenReturn(responseEntity2);
        NfsDataStoreShareAttr nfsDatastoreShareAttr = dmeNFSAccessService.getNfsDatastoreShareAttr("321");
        System.out.println(nfsDatastoreShareAttr);

    }

    @Test
    public void getNfsDatastoreLogicPortAttr() throws DmeException {
        when(dmeVmwareRalationDao.getLogicPortIdByStorageId("321")).thenReturn("321");
        String resp = "{\n" +
                "    \"id\": \"string\", \n" +
                "    \"name\": \"string\", \n" +
                "    \"running_status\": \"string\", \n" +
                "    \"operational_status\": \"string\", \n" +
                "    \"mgmt_ip\": \"string\", \n" +
                "    \"mgmt_ipv6\": \"string\", \n" +
                "    \"home_port_id\": \"string\", \n" +
                "    \"home_port_name\": \"string\", \n" +
                "    \"current_port_id\": \"string\", \n" +
                "    \"current_port_name\": \"string\", \n" +
                "    \"role\": \"string\", \n" +
                "    \"ddns_status\": \"string\", \n" +
                "    \"failover_group_id\": \"string\", \n" +
                "    \"failover_group_name\": \"string\", \n" +
                "    \"support_protocol\": \"string\", \n" +
                "    \"listen_dns_query_enabled\": \"string\", \n" +
                "    \"management_access\": \"string\", \n" +
                "    \"vstore_id\": \"string\", \n" +
                "    \"vstore_name\": \"string\"\n" +
                "}\n";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(resp, null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/storagemgmt/v1/storage-port/logic-ports/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        NfsDataStoreLogicPortAttr nfsDatastoreLogicPortAttr = dmeNFSAccessService.getNfsDatastoreLogicPortAttr("321");
        System.out.println(nfsDatastoreLogicPortAttr);

    }

    @Test
    public void getNfsDatastoreFsAttr() throws DmeException {
        List<String> fsIds = new ArrayList<>();
        fsIds.add("321");
        when(dmeVmwareRalationDao.getFsIdsByStorageId("321")).thenReturn(fsIds);
        String resp = "{\n" +
                "    \"id\": \"CC7EA6E662B830298D0D235CFC5125BB\", \n" +
                "    \"name\": \"filesystem001\", \n" +
                "    \"health_status\": \"normal\", \n" +
                "    \"alloc_type\": \"thick\", \n" +
                "    \"type\": \"normal\", \n" +
                "    \"capacity\": 10.125, \n" +
                "    \"available_capacity\": 10.125, \n" +
                "    \"capacity_usage_ratio\": 50, \n" +
                "    \"storage_name\": \"StorageDevice001\", \n" +
                "    \"storage_id\": \"c54d8ae4-4e49-485e-9288-079e7b92cc05\", \n" +
                "    \"storage_pool_name\": \"StorageDevice001\", \n" +
                "    \"tier_name\": \"SAL001\", \n" +
                "    \"tier_id\": \"ca4d8ae4-4e49-485e-9288-079e7b92cc05\", \n" +
                "    \"nfs_count\": 20, \n" +
                "    \"cifs_count\": 20, \n" +
                "    \"dtree_count\": 40, \n" +
                "    \"vstore_name\": \"vStore001\", \n" +
                "    \"project_name\": \"Project001\", \n" +
                "    \"project_id\": \"CC7EA6E662B830298D0D235CFC5125BB\", \n" +
                "    \"description\": \"This is a file system\", \n" +
                "    \"owning_controller\": \"0A\", \n" +
                "    \"checksum_enabled\": true, \n" +
                "    \"security_mode\": \"mixed\", \n" +
                "    \"automatic_update_time\": true, \n" +
                "    \"automatic_update_mode\": \"3600\", \n" +
                "    \"ads_enabled\": true, \n" +
                "    \"initial_distribute_policy\": \"string\", \n" +
                "    \"capacity_threshold\": 80, \n" +
                "    \"allocate_quota_in_pool\": 10.125, \n" +
                "    \"min_size_fs_capacity\": 10.125, \n" +
                "    \"cifs_homedir_count\": 0, \n" +
                "    \"enable_quota_dtree_count\": 40, \n" +
                "    \"vstore_id_in_storage\": \"40\", \n" +
                "    \"snapshot_expired_enabled\": true, \n" +
                "    \"tuning\": {\n" +
                "        \"deduplication_enabled\": true, \n" +
                "        \"compression_enabled\": false, \n" +
                "        \"application_scenario\": \"user_defined\", \n" +
                "        \"block_size\": 4, \n" +
                "        \"smart_qos\": {\n" +
                "            \"max_bandwidth\": 100, \n" +
                "            \"max_iops\": 100, \n" +
                "            \"min_bandwidth\": 100, \n" +
                "            \"min_iops\": 100, \n" +
                "            \"latency\": 100\n" +
                "        }, \n" +
                "        \"smart_tier\": {\n" +
                "            \"data_transfer_policy\": \"string\"\n" +
                "        }\n" +
                "    }, \n" +
                "    \"capacity_auto_negotiation\": {\n" +
                "        \"capacity_self_adjusting_mode\": \"grow\", \n" +
                "        \"capacity_recycle_mode\": \"expand_capacity\", \n" +
                "        \"auto_size_enable\": true, \n" +
                "        \"auto_grow_threshold_percent\": 50, \n" +
                "        \"auto_shrink_threshold_percent\": 50, \n" +
                "        \"max_auto_size\": 1, \n" +
                "        \"min_auto_size\": 1, \n" +
                "        \"auto_size_increment\": 64\n" +
                "    }, \n" +
                "    \"worm\": {\n" +
                "        \"type\": \"compliance_mode\", \n" +
                "        \"min_protect_period\": 1095, \n" +
                "        \"min_protect_period_unit\": \"year\", \n" +
                "        \"max_protect_period\": 25550, \n" +
                "        \"max_protect_period_unit\": \"year\", \n" +
                "        \"def_protect_period\": 2550, \n" +
                "        \"def_protect_period_unit\": \"year\", \n" +
                "        \"auto_lock\": true, \n" +
                "        \"auto_lock_time\": 100, \n" +
                "        \"auto_lock_time_unit\": \"hour\", \n" +
                "        \"auto_del\": true\n" +
                "    }\n" +
                "}\n";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(resp, null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/fileservice/v1/filesystems/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        List<NfsDataStoreFsAttr> nfsDatastoreFsAttr = dmeNFSAccessService.getNfsDatastoreFsAttr("321");
        System.out.println(nfsDatastoreFsAttr);

    }

    @Test
    public void scanNfs() throws DmeException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("objectid","321");
        jsonObject.addProperty("remoteHost","321");
        jsonObject.addProperty("remotePath","321");
        jsonObject.addProperty("name","321");
        List<Object> list = new ArrayList<>();
        list.add(jsonObject);
        when(vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_NFS)).thenReturn(gson.toJson(list));
        List<Storage> storages = new ArrayList<>();
        Storage storage = new Storage();
        storage.setFreeEffectiveCapacity(20.0);
        storage.setCapacityUtilization(50.0);
        storage.setId("321");
        storage.setIp("321");
        storage.setLocation("321");
        storage.setTotalCapacity(60.0);
        storages.add(storage);
        when(dmeStorageService.getStorages()).thenReturn(storages);
        String resp = "{\n" +
                "    \"id\": \"string\", \n" +
                "    \"name\": \"string\", \n" +
                "    \"running_status\": \"string\", \n" +
                "    \"operational_status\": \"string\", \n" +
                "    \"mgmt_ip\": \"321\", \n" +
                "    \"mgmt_ipv6\": \"string\", \n" +
                "    \"home_port_id\": \"string\", \n" +
                "    \"home_port_name\": \"string\", \n" +
                "    \"current_port_id\": \"string\", \n" +
                "    \"current_port_name\": \"string\", \n" +
                "    \"role\": \"string\", \n" +
                "    \"ddns_status\": \"string\", \n" +
                "    \"failover_group_id\": \"string\", \n" +
                "    \"failover_group_name\": \"string\", \n" +
                "    \"support_protocol\": \"string\", \n" +
                "    \"listen_dns_query_enabled\": \"string\", \n" +
                "    \"management_access\": \"string\", \n" +
                "    \"vstore_id\": \"string\", \n" +
                "    \"vstore_name\": \"string\"\n" +
                "}\n";
        JsonObject jsonObject1 = gson.fromJson(resp, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject1);
        Map<String, Object> map = new HashMap<>();
        map.put("logic_ports", jsonObjects);
        map.put("total", 1);
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/storagemgmt/v1/storage-port/logic-ports?storage_id=321", HttpMethod.GET, null)).thenReturn(responseEntity);
        String resp2 = "{\n" +
                "    \"id\": \"319585d6-13da-4b9d-9591-33ce63b52c0a\", \n" +
                "    \"id_in_storage\": \"01\", \n" +
                "    \"vstore_id_in_storage\": \"01\", \n" +
                "    \"name\": \"nfs share name\", \n" +
                "    \"share_path\": \"321\", \n" +
                "    \"device_name\": \"Device001\", \n" +
                "    \"storage_id\": \"Device001\", \n" +
                "    \"ne_id\": \"1\", \n" +
                "    \"tier_name\": \"ERP & HANA\", \n" +
                "    \"description\": \"desc\", \n" +
                "    \"fs_name\": \"FileSystem001\", \n" +
                "    \"fs_id\": \"8B6B1E8F30CE3AD98B20CBBCDC8B6CC3\", \n" +
                "    \"character_encoding\": \"utf-8\", \n" +
                "    \"enable_show_snapshot\": false, \n" +
                "    \"audit_items\": [\n" +
                "        \"none\"\n" +
                "    ], \n" +
                "    \"owning_dtree_id\": \"Dtree001\", \n" +
                "    \"owning_dtree_name\": \"Dtree001\"\n" +
                "}\n";
        JsonObject jsonObject2 = gson.fromJson(resp2, JsonObject.class);
        List<JsonObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(jsonObject2);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("nfs_share_info_list", jsonObjectList);
        map1.put("total", 1);
        ResponseEntity responseEntity1 = new ResponseEntity(gson.toJson(map1), null, HttpStatus.OK);
        when(dmeAccessService.access(DmeConstants.DME_NFS_SHARE_URL, HttpMethod.POST, "{\"share_path\":\"321\"}")).thenReturn(responseEntity1);
        String resp3 = "{\n" +
                "    \"id\": \"CC7EA6E662B830298D0D235CFC5125BB\", \n" +
                "    \"name\": \"filesystem001\", \n" +
                "    \"health_status\": \"normal\", \n" +
                "    \"alloc_type\": \"thick\", \n" +
                "    \"type\": \"normal\", \n" +
                "    \"capacity\": 10.125, \n" +
                "    \"available_capacity\": 10.125, \n" +
                "    \"capacity_usage_ratio\": 50, \n" +
                "    \"storage_name\": \"StorageDevice001\", \n" +
                "    \"storage_id\": \"321\", \n" +
                "    \"storage_pool_name\": \"StorageDevice001\", \n" +
                "    \"tier_name\": \"SAL001\", \n" +
                "    \"tier_id\": \"ca4d8ae4-4e49-485e-9288-079e7b92cc05\", \n" +
                "    \"nfs_count\": 20, \n" +
                "    \"cifs_count\": 20, \n" +
                "    \"dtree_count\": 40, \n" +
                "    \"vstore_name\": \"vStore001\", \n" +
                "    \"project_name\": \"Project001\", \n" +
                "    \"project_id\": \"CC7EA6E662B830298D0D235CFC5125BB\"\n" +
                "}\n";
        JsonObject jsonObject3 = gson.fromJson(resp3, JsonObject.class);
        List<JsonObject> objects = new ArrayList<>();
        objects.add(jsonObject3);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("data", objects);
        map2.put("total", 1);
        ResponseEntity responseEntity2 = new ResponseEntity(gson.toJson(map2), null, HttpStatus.OK);
        when(dmeAccessService.access(DmeConstants.DME_NFS_FILESERVICE_QUERY_URL, HttpMethod.POST, "{\"storage_id\":\"321\",\"name\":\"FileSystem001\"}")).thenReturn(responseEntity2);
        dmeNFSAccessService.scanNfs();

    }

    @Test
    public void listNfs() throws DmeException {
        DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
        dmeVmwareRelation.setFsId("321");
        dmeVmwareRelation.setFsName("321");
        dmeVmwareRelation.setId(321);
        dmeVmwareRelation.setShareId("321");
        dmeVmwareRelation.setLogicPortName("321");
        dmeVmwareRelation.setShareName("321");
        dmeVmwareRelation.setStorageDeviceId("321");
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setStoreName("321");
        dmeVmwareRelation.setLogicPortId("321");
        List<DmeVmwareRelation> dvrlist = new ArrayList<>();
        dvrlist.add(dmeVmwareRelation);
        when(dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_NFS)).thenReturn(dvrlist);
        Map<String, Object> dsmap = new HashMap<>();
        dsmap.put("objectid", "321");
        dsmap.put("remoteHost", "321");
        dsmap.put("remotePath", "321");
        dsmap.put("nfsStorageId", "321");
        List<String> list = new ArrayList<>();
        list.add("321");
        dsmap.put("vmfsWwnList", list);
        List<Map<String, Object>> list2 = new ArrayList<>();
        list2.add(dsmap);
        when(vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_NFS)).thenReturn(gson.toJson(list2));
        String resp = "{\n" +
                "    \"id\": \"CC7EA6E662B830298D0D235CFC5125BB\", \n" +
                "    \"name\": \"filesystem001\", \n" +
                "    \"health_status\": \"normal\", \n" +
                "    \"alloc_type\": \"thick\", \n" +
                "    \"type\": \"normal\", \n" +
                "    \"capacity\": 10.125, \n" +
                "    \"available_capacity\": 10.125, \n" +
                "    \"capacity_usage_ratio\": 50, \n" +
                "    \"storage_name\": \"StorageDevice001\", \n" +
                "    \"storage_id\": \"c54d8ae4-4e49-485e-9288-079e7b92cc05\", \n" +
                "    \"storage_pool_name\": \"StorageDevice001\", \n" +
                "    \"tier_name\": \"SAL001\", \n" +
                "    \"tier_id\": \"ca4d8ae4-4e49-485e-9288-079e7b92cc05\", \n" +
                "    \"nfs_count\": 20, \n" +
                "    \"cifs_count\": 20, \n" +
                "    \"dtree_count\": 40, \n" +
                "    \"vstore_name\": \"vStore001\", \n" +
                "    \"project_name\": \"Project001\", \n" +
                "    \"project_id\": \"CC7EA6E662B830298D0D235CFC5125BB\", \n" +
                "    \"description\": \"This is a file system\", \n" +
                "    \"owning_controller\": \"0A\", \n" +
                "    \"checksum_enabled\": true, \n" +
                "    \"security_mode\": \"mixed\", \n" +
                "    \"automatic_update_time\": true, \n" +
                "    \"automatic_update_mode\": \"3600\", \n" +
                "    \"ads_enabled\": true, \n" +
                "    \"initial_distribute_policy\": \"string\", \n" +
                "    \"capacity_threshold\": 80, \n" +
                "    \"allocate_quota_in_pool\": 10.125, \n" +
                "    \"min_size_fs_capacity\": 10.125, \n" +
                "    \"cifs_homedir_count\": 0, \n" +
                "    \"enable_quota_dtree_count\": 40, \n" +
                "    \"vstore_id_in_storage\": \"40\", \n" +
                "    \"snapshot_expired_enabled\": true, \n" +
                "    \"tuning\": {\n" +
                "        \"deduplication_enabled\": true, \n" +
                "        \"compression_enabled\": false, \n" +
                "        \"application_scenario\": \"user_defined\", \n" +
                "        \"block_size\": 4, \n" +
                "        \"smart_qos\": {\n" +
                "            \"max_bandwidth\": 100, \n" +
                "            \"max_iops\": 100, \n" +
                "            \"min_bandwidth\": 100, \n" +
                "            \"min_iops\": 100, \n" +
                "            \"latency\": 100\n" +
                "        }, \n" +
                "        \"smart_tier\": {\n" +
                "            \"data_transfer_policy\": \"string\"\n" +
                "        }\n" +
                "    }, \n" +
                "    \"capacity_auto_negotiation\": {\n" +
                "        \"capacity_self_adjusting_mode\": \"grow\", \n" +
                "        \"capacity_recycle_mode\": \"expand_capacity\", \n" +
                "        \"auto_size_enable\": true, \n" +
                "        \"auto_grow_threshold_percent\": 50, \n" +
                "        \"auto_shrink_threshold_percent\": 50, \n" +
                "        \"max_auto_size\": 1, \n" +
                "        \"min_auto_size\": 1, \n" +
                "        \"auto_size_increment\": 64\n" +
                "    }, \n" +
                "    \"worm\": {\n" +
                "        \"type\": \"compliance_mode\", \n" +
                "        \"min_protect_period\": 1095, \n" +
                "        \"min_protect_period_unit\": \"year\", \n" +
                "        \"max_protect_period\": 25550, \n" +
                "        \"max_protect_period_unit\": \"year\", \n" +
                "        \"def_protect_period\": 2550, \n" +
                "        \"def_protect_period_unit\": \"year\", \n" +
                "        \"auto_lock\": true, \n" +
                "        \"auto_lock_time\": 100, \n" +
                "        \"auto_lock_time_unit\": \"hour\", \n" +
                "        \"auto_del\": true\n" +
                "    }\n" +
                "}\n";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(resp, null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/fileservice/v1/filesystems/321", HttpMethod.GET, null)).thenReturn(responseEntity);
        List<NfsDataInfo> nfsDataInfos = dmeNFSAccessService.listNfs();
        System.out.println(nfsDataInfos);
    }

    @Test
    public void listNfsPerformance() throws DmeException {
        List<String> list = new ArrayList<>();
        list.add("321");
        String resp = "{\n" +
                "    \"47FEBD5002AB344D90EC6CFCD6127BA3\": {\n" +
                "        \"1407379178651656\": {\n" +
                "            \"min\": {\n" +
                "                \"1552480740000\": \"80.0\"\n" +
                "            }, \n" +
                "            \"avg\": {\n" +
                "                \"1552480943834\": \"80.0\"\n" +
                "            }, \n" +
                "            \"max\": {\n" +
                "                \"1552480740000\": \"80.0\"\n" +
                "            }, \n" +
                "            \"series\": [\n" +
                "                {\n" +
                "                    \"1552480500000\": \"80.0\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"1552480800000\": \"80.0\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }, \n" +
                "        \"1407379178586113\": {\n" +
                "            \"min\": {\n" +
                "                \"1552480890000\": \"48.0\"\n" +
                "            }, \n" +
                "            \"avg\": {\n" +
                "                \"1552480943834\": \"48.55555555555556\"\n" +
                "            }, \n" +
                "            \"max\": {\n" +
                "                \"1552480740000\": \"49.0\"\n" +
                "            }, \n" +
                "            \"series\": [\n" +
                "                {\n" +
                "                    \"1552480500000\": \"49.0\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"1552480800000\": \"48.42857142857143\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        Map<String, Object> map = gson.fromJson(resp, Map.class);
        String param = "{\"obj_ids\":[\"321\"]}";
        Map<String, Object> params = gson.fromJson(param, Map.class);
        when(dataStoreStatisticHistoryService.queryNfsStatisticCurrent(params)).thenReturn(map);
        List<NfsDataInfo> nfsDataInfos = dmeNFSAccessService.listNfsPerformance(list);
        System.out.println(nfsDataInfos);

    }

    @Test
    public void mountNfs() throws DmeException {
        Map<String, Object> params = new HashMap<>();
        params.put("hostVkernelIp", "321");
        params.put("dataStoreObjectId", "321");
        params.put("mountType", "read-only");
        params.put("hostObjectId", "321");
        DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
        dmeVmwareRelation.setStoreName("321");
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setStorageDeviceId("321");
        dmeVmwareRelation.setShareName("321");
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setShareId("321");
        dmeVmwareRelation.setLogicPortId("321");
        dmeVmwareRelation.setId(321);
        dmeVmwareRelation.setFsId("321");
        dmeVmwareRelation.setStoreType(ToolUtils.STORE_TYPE_NFS);
        when(dmeVmwareRalationDao.getDmeVmwareRelationByDsId("321")).thenReturn(dmeVmwareRelation);
        String param = "{\"nfs_share_client_addition\":[{\"all_squash\":\"no_all_squash\",\"accessval\":\"read/write\",\"name\":\"321\"," +
                "\"root_squash\":\"root_squash\",\"secure\":\"insecure\",\"sync\":\"synchronization\"}],\"id\":\"321\"}";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("task_id", "321");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(jsonObject), null, HttpStatus.ACCEPTED);
        when(dmeAccessService.access("/rest/fileservice/v1/nfs-shares/321", HttpMethod.PUT, param)).thenReturn(responseEntity);
        List<String> list = new ArrayList<>();
        list.add("321");
        when(taskService.checkTaskStatus(list)).thenReturn(true);
        dmeNFSAccessService.mountNfs(params);

    }

    @Test
    public void unmountNfs() throws DmeException {
        Map<String, Object> params = new HashMap<>();
        params.put("dataStoreObjectId", "321");
        params.put("hostId", "321");
        params.put("clusterId", "");
        DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
        dmeVmwareRelation.setStoreName("321");
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setStorageDeviceId("321");
        dmeVmwareRelation.setShareName("321");
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setShareId("321");
        dmeVmwareRelation.setLogicPortId("321");
        dmeVmwareRelation.setId(321);
        dmeVmwareRelation.setFsId("321");
        dmeVmwareRelation.setStoreType(ToolUtils.STORE_TYPE_NFS);
        when(dmeVmwareRalationDao.getDmeVmwareRelationByDsId("321")).thenReturn(dmeVmwareRelation);
        String resp2 = "{\n" +
                "  \"accessval\" : \"NfsShare name\",\n" +
                "  \"id\" : \"321\",\n" +
                "  \"name\" : \"Device001\",\n" +
                "  \"all_squash\" : \"Device001\",\n" +
                "  \"parent_id\" : \"ERP & HANA\",\n" +
                "  \"root_squash\" : \"321\",\n" +
                "  \"secure\" : \"321\",\n" +
                "  \"sync\" : \"321\",\n" +
                "  \"type\" : \"Dtree\",\n" +
                "  \"vstore_id_in_storage\" : \"FileSystem001\",\n" +
                "  \"vstore_name\" : \"8B6B1E8F30CE3AD98B20CBBCDC8B6CC3\" \n" +
                "}";
        JsonObject jsonObject = gson.fromJson(resp2, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        Map<String, Object> map = new HashMap<>();
        map.put("auth_client_list", jsonObjects);
        map.put("total",1);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/fileservice/v1/nfs-shares/321/auth_clients", HttpMethod.POST, gson.toJson(new HashMap<>()))).thenReturn(responseEntity2);
        String param = "{\"id\":\"321\",\"nfs_share_client_deletion\":[{\"nfs_share_client_id_in_storage\":\"321\",\"name\":\"Device001\"}]}";
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("task_id", "321");
        ResponseEntity<String> responseEntity3 = new ResponseEntity<>(gson.toJson(jsonObject1), null, HttpStatus.ACCEPTED);
        when(dmeAccessService.access("/rest/fileservice/v1/nfs-shares/321", HttpMethod.PUT, param)).thenReturn(responseEntity3);
        List<String> list = new ArrayList<>();
        list.add("321");
        when(taskService.checkTaskStatus(list)).thenReturn(true);
        dmeNFSAccessService.unmountNfs(params);

    }

    @Test
    public void deleteNfs() throws DmeException {

        Map<String, Object> params = new HashMap<>();
        params.put("dataStoreObjectId", "321");
        Map<String, String> map = new HashMap<>();
        map.put("hostId", "321");
        map.put("hostName", "321");
        List<Map<String, String>> lists = new ArrayList<>();
        lists.add(map);
        when(vcsdkUtils.getHostsByDsObjectId("321", true)).thenReturn(gson.toJson(lists));
        DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
        dmeVmwareRelation.setStoreName("321");
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setStorageDeviceId("321");
        dmeVmwareRelation.setShareName("321");
        dmeVmwareRelation.setStoreId("321");
        dmeVmwareRelation.setShareId("321");
        dmeVmwareRelation.setLogicPortId("321");
        dmeVmwareRelation.setId(321);
        dmeVmwareRelation.setFsId("321");
        dmeVmwareRelation.setStoreType(ToolUtils.STORE_TYPE_NFS);
        when(dmeVmwareRalationDao.getDmeVmwareRelationByDsId("321")).thenReturn(dmeVmwareRelation);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("task_id", "321");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(jsonObject), null, HttpStatus.ACCEPTED);
        when(dmeAccessService.access(DmeConstants.DME_NFS_SHARE_DELETE_URL, HttpMethod.POST, "{\"nfs_share_ids\":[\"321\"]}")).thenReturn(responseEntity);
        when(dmeAccessService.access(DmeConstants.DME_NFS_FS_DELETE_URL, HttpMethod.POST, "{\"file_system_ids\":[\"321\"]}")).thenReturn(responseEntity);
        List<String> list = new ArrayList<>();
        list.add("321");
        list.add("321");
        when(taskService.checkTaskStatus(list)).thenReturn(true);
        dmeNFSAccessService.deleteNfs(params);

    }

    @Test
    public void getHostsMountDataStoreByDsObjectId() throws DmeException {
        Map<String, String> map = new HashMap<>();
        map.put("hostId", "321");
        map.put("hostName", "321");
        List<Map<String, String>> lists = new ArrayList<>();
        lists.add(map);
        when(vcsdkUtils.getHostsByDsObjectId("321", true)).thenReturn(gson.toJson(lists));
        dmeNFSAccessService.getHostsMountDataStoreByDsObjectId("321");
    }

    @Test
    public void getClusterMountDataStoreByDsObjectId() throws DmeException {
        Map<String, String> map = new HashMap<>();
        map.put("clusterId", "321");
        map.put("clusterName", "321");
        List<Map<String, String>> lists = new ArrayList<>();
        lists.add(map);
        when(vcsdkUtils.getClustersByDsObjectId("321")).thenReturn(gson.toJson(lists));
        List<Map<String, Object>> clusterMountDataStoreByDsObjectId =
            dmeNFSAccessService.getClusterMountDataStoreByDsObjectId("321");
        System.out.println(clusterMountDataStoreByDsObjectId);
    }
}