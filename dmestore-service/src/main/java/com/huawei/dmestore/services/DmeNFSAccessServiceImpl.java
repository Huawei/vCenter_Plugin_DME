package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DmeIndicatorConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.AuthClient;
import com.huawei.dmestore.model.NfsDataInfo;
import com.huawei.dmestore.model.NfsDataStoreFsAttr;
import com.huawei.dmestore.model.NfsDataStoreLogicPortAttr;
import com.huawei.dmestore.model.NfsDataStoreShareAttr;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.model.TaskDetailInfo;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DmeNFSAccessServiceImpl
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public class DmeNFSAccessServiceImpl implements DmeNFSAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(DmeNFSAccessServiceImpl.class);

    private static final int DIGIT_100 = 100;

    private static final int PAGE_SIZE_1000 = 1000;

    private static final int DIGIT_2 = 2;

    private static final int DIGIT_202 = 202;

    private static final String TASK_ID = "task_id";

    private static final String HOSTVKERNELIP = "hostVkernelIp";

    private static final String SHAREID = "shareId";

    private static final String MAX = "max";

    private static final String STORAGE_NAME = "storage_name";

    private static final String STORAGE_POOL_NAME = "storage_pool_name";

    private static final String NFS_SHARE_ID = "{nfs_share_id}";

    private static final String NAME_FIELD = "name";

    private static final String ID_FIELD = "id";

    private static final String FS_NAME = "fs_name";

    private static final String STORAGE_ID = "storage_id";

    private static final String SHARE_PATH = "share_path";

    private static final String DEVICE_NAME = "device_name";

    private static final String DATASTOREOBJECTID = "dataStoreObjectId";

    private static final String OWNING_DTREE_NAME = "owning_dtree_name";

    private static final String OWNING_DTREE_ID = "owning_dtree_id";

    private static final String MGMT_IP = "mgmt_ip";

    private static final String HOME_PORT_NAME = "home_port_name";

    private static final String TOTAL = "total";

    private static final String OBJECTID = "objectId";

    private static final String NFS_SHARE_ID_FIELD = "nfs_share_id";

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private DmeAccessService dmeAccessService;

    private DmeStorageService dmeStorageService;

    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    private TaskService taskService;

    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    public void setDataStoreStatisticHistoryService(DataStoreStatisticHistoryService dataStoreStatisticHistoryService) {
        this.dataStoreStatisticHistoryService = dataStoreStatisticHistoryService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public NfsDataStoreShareAttr getNfsDatastoreShareAttr(String storageObjectId) throws DmeException {
        // 根据存储ID 获取逻nfs_share_id
        String nfsShareId = dmeVmwareRalationDao.getShareIdByStorageId(storageObjectId);
        String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace(NFS_SHARE_ID, nfsShareId);
        LOG.error("getNfsDatastoreShareAttr!method=get, nfsShareId={},url={}, body=null", nfsShareId, url);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() / DIGIT_100 != DIGIT_2) {
            LOG.error("get NFS Share failed！response：{}", responseEntity.getBody());
            return null;
        }
        String resBody = responseEntity.getBody();
        JsonObject share = gson.fromJson(resBody, JsonObject.class);
        NfsDataStoreShareAttr shareAttr = new NfsDataStoreShareAttr();
        shareAttr.setName(share.get(NAME_FIELD).getAsString());
        shareAttr.setFsName(ToolUtils.jsonToStr(share.get(FS_NAME), null));
        shareAttr.setSharePath(ToolUtils.jsonToStr(share.get(SHARE_PATH)));
        shareAttr.setDescription(ToolUtils.jsonToStr(share.get("description")));
        shareAttr.setDeviceName(ToolUtils.jsonToStr(share.get(DEVICE_NAME), null));
        shareAttr.setOwningDtreeName(ToolUtils.jsonToStr(share.get(OWNING_DTREE_NAME), null));
        shareAttr.setOwningDtreeId(ToolUtils.jsonToStr(share.get(OWNING_DTREE_ID), null));

        // 查询客户端列表
        List<AuthClient> authClientList = getNfsDatastoreShareAuthClients(nfsShareId);
        if (null != authClientList && authClientList.size() > 0) {
            shareAttr.setAuthClientList(authClientList);
        }
        return shareAttr;
    }

    private List<AuthClient> getNfsDatastoreShareAuthClients(String shareId) throws DmeException {
        List<AuthClient> clientList = new ArrayList<>();
        Map<String, Object> body = new HashMap<>();
        body.put(NFS_SHARE_ID_FIELD, shareId);
        body.put("page_no", 1);
        body.put("page_size", PAGE_SIZE_1000);
        String url = DmeConstants.DME_NFS_SHARE_AUTH_CLIENTS_URL;
        String shareIdKey = NFS_SHARE_ID_FIELD;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(body));
        if (responseEntity.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
            String resBody = responseEntity.getBody();
            JsonObject resObject = gson.fromJson(resBody, JsonObject.class);
            int total = resObject.get(TOTAL).getAsInt();
            if (total > 0) {
                JsonArray array = resObject.getAsJsonArray("auth_client_list");
                for (int index = 0; index < total; index++) {
                    JsonObject client = array.get(index).getAsJsonObject();
                    AuthClient authClient = new AuthClient();
                    authClient.setName(ToolUtils.jsonToStr(client.get(NAME_FIELD)));
                    authClient.setType(ToolUtils.jsonToStr(client.get("type")));
                    authClient.setAccessval(ToolUtils.jsonToStr(client.get("permission")));
                    authClient.setId(ToolUtils.jsonToStr(client.get(shareIdKey)));
                    authClient.setClientIdInStorage(ToolUtils.jsonToStr(client.get("client_id_in_storage")));
                    clientList.add(authClient);
                }
            }
        }
        LOG.info("query share authclient response:{}", gson.toJson(clientList));
        return clientList;
    }

    @Override
    public NfsDataStoreLogicPortAttr getNfsDatastoreLogicPortAttr(String storageObjectId) throws DmeException {
        // 根据存储ID 获取逻辑端口ID
        String logicPortId = dmeVmwareRalationDao.getLogicPortIdByStorageId(storageObjectId);
        String url = DmeConstants.DME_NFS_LOGICPORT_DETAIL_URL.replace("{logic_port_id}", logicPortId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() / DIGIT_100 != DIGIT_2) {
            LOG.error("get NFS logic port failed！logic_port_id={},response={}", logicPortId, responseEntity.getBody());
            return null;
        }
        JsonObject logicPort = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        NfsDataStoreLogicPortAttr logicPortAttr = new NfsDataStoreLogicPortAttr();
        logicPortAttr.setName(ToolUtils.jsonToStr(logicPort.get(NAME_FIELD)));
        String ipv4 = ToolUtils.jsonToStr(logicPort.get(MGMT_IP), null);
        ipv4 = null == ipv4 ? "" : ipv4.trim();
        String ipv6 = ToolUtils.jsonToStr(logicPort.get("mgmt_ipv6"), null);
        logicPortAttr.setIp(!StringUtils.isEmpty(ipv4) ? ipv4 : ipv6);
        String runningStatus = ToolUtils.jsonToStr(logicPort.get("running_status"));
        logicPortAttr.setStatus(ToolUtils.jsonToStr(logicPort.get("operational_status"), null));
        logicPortAttr.setRunningStatus(runningStatus);
        logicPortAttr.setCurrentPort(ToolUtils.jsonToStr(logicPort.get("current_port_name")));
        logicPortAttr.setActivePort(ToolUtils.jsonToStr(logicPort.get(HOME_PORT_NAME)));
        logicPortAttr.setFailoverGroup(ToolUtils.jsonToStr(logicPort.get("failover_group_name"), null));
        return logicPortAttr;
    }

    @Override
    public List<NfsDataStoreFsAttr> getNfsDatastoreFsAttr(String storageObjectId) throws DmeException {
        // 根据存储ID获取fs
        List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storageObjectId);
        List<NfsDataStoreFsAttr> list = new ArrayList<>();
        for (int index = 0; index < fsIds.size(); index++) {
            NfsDataStoreFsAttr fsAttr = new NfsDataStoreFsAttr();
            String fileSystemId = fsIds.get(index);
            if (StringUtils.isEmpty(fileSystemId)) {
                continue;
            }
            String url = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace("{file_system_id}", fileSystemId);
            ResponseEntity<String> responseTuning = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseTuning.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
                fsAttr.setFileSystemId(fileSystemId);
                JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
                fsAttr.setName(fsDetail.get(NAME_FIELD).getAsString());
                fsAttr.setProvisionType(fsDetail.get("alloc_type").getAsString());
                fsAttr.setDevice(fsDetail.get(STORAGE_NAME).getAsString());
                fsAttr.setStoragePoolName(fsDetail.get(STORAGE_POOL_NAME).getAsString());
                fsAttr.setController(fsDetail.get("owning_controller").getAsString());
                JsonObject tuning = fsDetail.getAsJsonObject("tuning");
                if (!tuning.isJsonNull()) {
                    fsAttr.setApplicationScenario(ToolUtils.jsonToStr(tuning.get("application_scenario"), null));
                    fsAttr.setDataDeduplication(ToolUtils.jsonToBoo(tuning.get("deduplication_enabled"), null));
                    fsAttr.setDateCompression(ToolUtils.jsonToBoo(tuning.get("compression_enabled"), null));
                }
                list.add(fsAttr);
            }
        }

        if (list.size() > 0) {
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean scanNfs() throws DmeException {
        LOG.error("===scanNfs begin====");
        String storeType = ToolUtils.STORE_TYPE_NFS;
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(storeType);
        if (StringUtils.isEmpty(listStr)) {
            LOG.error("vmware get nfs error!listStr={}", listStr == null ? "" : listStr);
            return false;
        }
        LOG.error("vmware list nfs success!");

        // 将DME的存储设备集合转换为map key:ip value:Storage
        List<Storage> storages = dmeStorageService.getStorages();
        Map<String, Storage> storageMap = converStorage(storages);
        if (storageMap == null || storageMap.size() == 0) {
            LOG.error("get dme storage failed!storages is null!");
            return false;
        }
        LOG.info("get dme storage success!storages size={}", storages.size());
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int index = 0; index < jsonArray.size(); index++) {
            JsonObject nfsDatastore = jsonArray.get(index).getAsJsonObject();
            relationList.addAll(parseNfsDatastore(storeType, storageMap, nfsDatastore));
        }
        LOG.info("scanNfs end!relationList size={}", relationList.size());
        if (relationList.size() > 0) {
            return dmeVmWareRelationDbProcess(relationList, storeType);
        }
        return false;
    }

    private List<DmeVmwareRelation> parseNfsDatastore(String storeType, Map<String, Storage> storageMap,
        JsonObject nfsDatastore) throws DmeException {
        String nfsStorageId = ToolUtils.jsonToStr(nfsDatastore.get(OBJECTID));
        String nfsDatastoreIp = ToolUtils.jsonToStr(nfsDatastore.get("remoteHost"));
        String nfsDataStoreSharePath = ToolUtils.jsonToStr(nfsDatastore.get("remotePath"));
        String nfsDataStorageName = ToolUtils.jsonToStr(nfsDatastore.get(NAME_FIELD));
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        Map<String, String> storageIds = new HashMap<>();
        for (Map.Entry<String, Storage> entry : storageMap.entrySet()) {
            Storage storageInfo = entry.getValue();
            String storageId = storageInfo.getId();
            DmeVmwareRelation relation = new DmeVmwareRelation();
            relation.setStoreId(nfsStorageId);
            relation.setStorageDeviceId(storageId);
            relation.setStoreName(nfsDataStorageName);
            relation.setStoreType(storeType);

            //获取存储池型号
            if (storageIds.get("storageId") == null) {
                String storageModel = getStorageModel(storageId);
                storageIds.put(storageId, storageModel);
                relation.setStorageType(storageModel);
            }else {
                relation.setStorageType(storageIds.get("storageId"));
            }


            // 获取logicPort信息
            boolean withLogicPort = getLogicPort(nfsDatastoreIp, storageId, relation);
            String fsName = "";
            boolean withShare = false;
            Map<String, Object> shareInfo = queryShareInfo(nfsDataStoreSharePath);
            if (null != shareInfo && shareInfo.size() > 0) {
                fsName = ToolUtils.getStr(shareInfo.get(FS_NAME));
                String id = ToolUtils.getStr(shareInfo.get(ID_FIELD));
                String name = ToolUtils.getStr(shareInfo.get(NAME_FIELD));
                relation.setShareId(id);
                relation.setShareName(name);
                withShare = true;
            }
            boolean withFs = false;
            if (!StringUtils.isEmpty(fsName)) {
                Map<String, Object> fsInfo = queryFsInfo(storageId, fsName);
                if (null != fsInfo && fsInfo.size() > 0) {
                    String id = ToolUtils.getStr(fsInfo.get(ID_FIELD));
                    String name = ToolUtils.getStr(fsInfo.get(NAME_FIELD));
                    relation.setFsId(id);
                    relation.setFsName(name);
                    withFs = true;
                }
            }

            if (withFs || withShare || withLogicPort) {
                relationList.add(relation);
            }
        }
        return relationList;
    }

    private boolean getLogicPort(String nfsDatastoreIp, String storageId, DmeVmwareRelation relation)
        throws DmeException {
        boolean isFound = false;
        List<Map<String, Object>> logicPortInfos = queryLogicPortInfo(storageId);
        if (null != logicPortInfos && logicPortInfos.size() > 0) {
            for (Map<String, Object> logicPortInfo : logicPortInfos) {
                String id = ToolUtils.getStr(logicPortInfo.get(ID_FIELD));
                String name = ToolUtils.getStr(logicPortInfo.get(HOME_PORT_NAME));
                String mgmtIp = ToolUtils.getStr(logicPortInfo.get(MGMT_IP));
                if (nfsDatastoreIp.equals(mgmtIp)) {
                    relation.setLogicPortId(id);
                    relation.setLogicPortName(name);
                    isFound = true;
                    break;
                }
            }
        }
        return isFound;
    }

    private boolean dmeVmWareRelationDbProcess(List<DmeVmwareRelation> relationList, String storeType)
        throws DmeSqlException {
        // 本地全量查询NFS
        List<String> storageIds = dmeVmwareRalationDao.getAllStorageIdByType(storeType);
        List<DmeVmwareRelation> newList = new ArrayList<>();
        List<DmeVmwareRelation> upList = new ArrayList<>();
        for (DmeVmwareRelation relation : relationList) {
            String storegeId = relation.getStoreId();
            if (storageIds.contains(storegeId)) {
                upList.add(relation);
                storageIds.remove(storegeId);
            } else {
                newList.add(relation);
            }
        }

        // 更新
        if (!upList.isEmpty()) {
            dmeVmwareRalationDao.update(upList);
            LOG.info("update nfs relation success!");
        }

        // 新增
        if (!newList.isEmpty()) {
            dmeVmwareRalationDao.save(newList);
            LOG.info("add nfs relation success!");
        }

        // 删除
        if (!storageIds.isEmpty()) {
            dmeVmwareRalationDao.deleteByStorageId(storageIds);
            LOG.info("delete nfs relation success!");
        }
        return true;
    }

    private Map<String, Object> queryShareInfo(String sharePath) throws DmeException {
        ResponseEntity<String> responseEntity = listShareBySharePath(sharePath);
        if (responseEntity.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
            String object = responseEntity.getBody();
            List<Map<String, Object>> list = converShare(object);
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return Collections.EMPTY_MAP;
    }

    private ResponseEntity<String> listShareBySharePath(String sharePath) throws DmeException {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(SHARE_PATH, sharePath);
        String url = DmeConstants.DME_NFS_SHARE_URL;
        return dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestbody));
    }

    private List<Map<String, Object>> converShare(String object) {
        List<Map<String, Object>> shareList = new ArrayList<>();
        if (object.contains(TOTAL) && object.contains("nfs_share_info_list")) {
            JsonObject temp = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = temp.get("nfs_share_info_list").getAsJsonArray();
            for (int index = 0; index < jsonArray.size(); index++) {
                Map<String, Object> shareMap = new HashMap<>();
                JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();
                shareMap.put(ID_FIELD, ToolUtils.jsonToStr(jsonObject.get(ID_FIELD)));
                shareMap.put(NAME_FIELD, ToolUtils.jsonToStr(jsonObject.get(NAME_FIELD)));
                shareMap.put(SHARE_PATH, ToolUtils.jsonToStr(jsonObject.get(SHARE_PATH)));
                shareMap.put(STORAGE_ID, ToolUtils.jsonToStr(jsonObject.get(STORAGE_ID)));
                shareMap.put(DEVICE_NAME, ToolUtils.jsonToStr(jsonObject.get(DEVICE_NAME)));
                shareMap.put(OWNING_DTREE_ID, ToolUtils.jsonToStr(jsonObject.get(OWNING_DTREE_ID)));
                shareMap.put(OWNING_DTREE_NAME, ToolUtils.jsonToStr(jsonObject.get(OWNING_DTREE_NAME)));
                shareMap.put(FS_NAME, ToolUtils.jsonToStr(jsonObject.get(FS_NAME)));
                shareList.add(shareMap);
            }
        }
        return shareList;
    }

    private Map<String, Object> queryFsInfo(String storageId, String fsName) throws DmeException {
        ResponseEntity responseEntity = listFsByStorageId(storageId, fsName);
        if (responseEntity.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = converFs(object);
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return Collections.EMPTY_MAP;
    }

    private ResponseEntity listFsByStorageId(String storageId, String fsName) throws DmeException {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(STORAGE_ID, storageId);
        if (!StringUtils.isEmpty(fsName)) {
            requestbody.put(NAME_FIELD, fsName);
        }
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FILESERVICE_QUERY_URL,
            HttpMethod.POST, gson.toJson(requestbody));
        return responseEntity;
    }

    private List<Map<String, Object>> converFs(Object object) {
        List<Map<String, Object>> fsList = new ArrayList<>();
        JsonArray jsonArray;
        String strObject = gson.toJson(object);
        if (strObject.contains(TOTAL) && strObject.contains("data")) {
            JsonObject temp = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = temp.get("data").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            Map<String, Object> fsMap = new HashMap<>();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            fsMap.put(ID_FIELD, ToolUtils.jsonToStr(jsonObject.get(ID_FIELD)));
            fsMap.put(NAME_FIELD, ToolUtils.jsonToStr(jsonObject.get(NAME_FIELD)));
            fsMap.put(STORAGE_ID, ToolUtils.jsonToStr(jsonObject.get(STORAGE_ID)));
            fsMap.put(STORAGE_NAME, ToolUtils.jsonToStr(jsonObject.get(STORAGE_NAME)));
            fsMap.put(STORAGE_POOL_NAME, ToolUtils.jsonToStr(jsonObject.get(STORAGE_POOL_NAME)));
            fsMap.put("tier_id", ToolUtils.jsonToStr(jsonObject.get("tier_id")));
            fsMap.put("tier_name", ToolUtils.jsonToStr(jsonObject.get("tier_name")));
            fsList.add(fsMap);
        }
        return fsList;
    }

    private List<Map<String, Object>> queryLogicPortInfo(String storageId) throws DmeException {
        ResponseEntity responseEntity = listLogicPortByStorageId(storageId);
        if (responseEntity.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = convertLogicPort(storageId, object);
            if (list.size() > 0) {
                return list;
            }
        }
        return Collections.EMPTY_LIST;
    }

    private ResponseEntity listLogicPortByStorageId(String storageId) throws DmeException {
        String url = DmeConstants.API_LOGICPORTS_LIST;
        JsonObject param = null;
        HttpMethod method = HttpMethod.POST;
        param = new JsonObject();
        param.addProperty("storage_id", storageId);
        ResponseEntity responseEntity = dmeAccessService.access(url, method, gson.toJson(param));
        return responseEntity;
    }

    private List<Map<String, Object>> convertLogicPort(String storageId, Object object) {
        List<Map<String, Object>> logicPortList = new ArrayList<>();
        JsonArray jsonArray;
        String strObject = gson.toJson(object);
        if (strObject.contains(TOTAL) && strObject.contains("logic_ports")) {
            JsonObject temp = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = temp.get("logic_ports").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            Map<String, Object> logicPortMap = new HashMap<>();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            logicPortMap.put(ID_FIELD, ToolUtils.jsonToStr(jsonObject.get(ID_FIELD)));
            logicPortMap.put(NAME_FIELD, ToolUtils.jsonToStr(jsonObject.get(NAME_FIELD)));
            logicPortMap.put(STORAGE_ID, storageId);
            logicPortMap.put("home_port_id", ToolUtils.jsonToStr(jsonObject.get("home_port_raw_id")));
            logicPortMap.put(HOME_PORT_NAME, ToolUtils.jsonToStr(jsonObject.get(HOME_PORT_NAME)));
            logicPortMap.put("current_port_id", ToolUtils.jsonToStr(jsonObject.get("current_port_raw_id")));
            logicPortMap.put(MGMT_IP, ToolUtils.jsonToStr(jsonObject.get(MGMT_IP)));
            logicPortList.add(logicPortMap);
        }
        return logicPortList;
    }

    private Map<String, Storage> converStorage(List<Storage> storages) {
        Map<String, Storage> storageMap = new HashMap<>();
        for (Storage storage : storages) {
            String ip = storage.getIp();
            storageMap.put(ip, storage);
        }
        return storageMap;
    }

    @Override
    public List<NfsDataInfo> listNfs() throws DmeException {

        List<NfsDataInfo> relists = new ArrayList<>();
        // 从关系表中取得DME卷与vcenter存储的对应关系
        List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_NFS);
        LOG.info("listNfs dvrlist={}", dvrlist == null ? null : dvrlist.size());
        if (dvrlist == null || dvrlist.size() == 0) {
            return relists;
        }
        Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_NFS);
        if (StringUtils.isEmpty(listStr)) {
            throw new DmeException("list NFS from vcenter failed!");
        }
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        for (int index = 0; index < jsonArray.size(); index++) {
            JsonObject jo = jsonArray.get(index).getAsJsonObject();
            String vmwareObjectId = ToolUtils.jsonToStr(jo.get(OBJECTID));
            if (!StringUtils.isEmpty(vmwareObjectId)) {
                if (dvrMap != null && dvrMap.get(vmwareObjectId) != null) {
                    NfsDataInfo nfsDataInfo = new NfsDataInfo();
                    double capacity = ToolUtils.getDouble(jo.get("capacity")) / ToolUtils.GI;
                    double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                    double reserveCapacity =
                        (ToolUtils.getDouble(jo.get("capacity")) - ToolUtils.getDouble(jo.get("freeSpace")))
                            / ToolUtils.GI;
                    nfsDataInfo.setName(ToolUtils.jsonToStr(jo.get(NAME_FIELD)));
                    nfsDataInfo.setCapacity(capacity);
                    nfsDataInfo.setFreeSpace(freeSpace);
                    nfsDataInfo.setReserveCapacity(reserveCapacity);
                    nfsDataInfo.setShareIp(ToolUtils.jsonToStr(jo.get("remoteHost")));
                    DmeVmwareRelation dvr = dvrMap.get(vmwareObjectId);
                    nfsDataInfo.setSharePath(dvr.getShareName());
                    nfsDataInfo.setLogicPort(dvr.getLogicPortName());
                    nfsDataInfo.setLogicPortId(dvr.getLogicPortId());
                    nfsDataInfo.setShare(dvr.getShareName());
                    nfsDataInfo.setShareId(dvr.getShareId());
                    nfsDataInfo.setFs(dvr.getFsName());
                    nfsDataInfo.setFsId(dvr.getFsId());
                    nfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get(OBJECTID)));
                    getFsDetailInfo(nfsDataInfo, dvr.getFsId());
                    relists.add(nfsDataInfo);
                }
            }
        }

        return relists;
    }

    private void getFsDetailInfo(NfsDataInfo nfsDataInfo, String fsId) {
        try {
            if (StringUtils.isEmpty(fsId)) {
                return;
            }
            String fsUrl = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace("{file_system_id}", fsId);
            ResponseEntity<String> responseTuning = dmeAccessService.access(fsUrl, HttpMethod.GET, null);
            if (responseTuning.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
                JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
                nfsDataInfo.setStatus(ToolUtils.jsonToStr(fsDetail.get("health_status")));
                nfsDataInfo.setDeviceId(ToolUtils.jsonToStr(fsDetail.get(STORAGE_ID)));
                nfsDataInfo.setDevice(ToolUtils.jsonToStr(fsDetail.get(STORAGE_NAME)));
            }
        } catch (DmeException e) {
            LOG.error("list Nfs from DME failed!");
        }
    }

    private Map<String, DmeVmwareRelation> getDvrMap(List<DmeVmwareRelation> dvrlist) {
        Map<String, DmeVmwareRelation> remap = null;
        if (dvrlist != null && dvrlist.size() > 0) {
            remap = new HashMap<>();
            for (DmeVmwareRelation dvr : dvrlist) {
                remap.put(dvr.getStoreId(), dvr);
            }
        }
        return remap;
    }

    @Override
    public List<NfsDataInfo> listNfsPerformance(List<String> fsIds) throws DmeException {
        List<NfsDataInfo> relists = null;
        try {
            if (fsIds == null || fsIds.size() == 0) {
                return relists;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("obj_ids", fsIds);
            Map<String, Object> remap = dataStoreStatisticHistoryService.queryNfsStatisticCurrent(params);
            if (remap != null && remap.size() > 0) {
                JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                if (dataJson != null) {
                    relists = new ArrayList<>();
                    for (String fsId : fsIds) {
                        JsonObject statisticObject = dataJson.getAsJsonObject(fsId);
                        if (statisticObject != null) {
                            NfsDataInfo nfsDataInfo = new NfsDataInfo();
                            nfsDataInfo.setFsId(fsId);
                            nfsDataInfo.setOps(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                DmeIndicatorConstants.COUNTER_ID_FS_THROUGHPUT, MAX), null));
                            nfsDataInfo.setBandwidth(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                DmeIndicatorConstants.COUNTER_ID_FS_BANDWIDTH, MAX), null));
                            nfsDataInfo.setReadResponseTime(ToolUtils.jsonToFloat(
                                ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_FS_READRESPONSETIME, MAX), null));
                            nfsDataInfo.setWriteResponseTime(ToolUtils.jsonToFloat(
                                ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_FS_WRITERESPONSETIME, MAX), null));
                            relists.add(nfsDataInfo);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list nfs performance error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    /**
     * Mount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * logicPortIp 存储逻辑端口IP
     * hostObjectId:主机objectid
     * hostVkernelIp:主机vkernelip
     * str mountType: 挂载模式（只读或读写）  readOnly/readWrite
     *
     * @param params include dataStoreObjectId,hosts,mountType
     */
    @Override
    public void mountNfs(Map<String, Object> params) throws DmeException {
        if (params == null && null == params.get(DATASTOREOBJECTID)) {
            throw new DmeException("Mount nfs parameter exception:" + params);
        }
        String dataStoreObjectId = ToolUtils.getStr(params.get(DATASTOREOBJECTID));
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
        if (dvr == null || dvr.getShareId() == null) {
            throw new DmeException("DME dataStore ObjectId is null!");
        }
        params.put(SHAREID, dvr.getShareId());
        String taskId = mountnfsToHost(params);
        if (!StringUtils.isEmpty(taskId)) {
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            boolean isMounted = taskService.checkTaskStatus(taskIds);
            if (isMounted) {
                String hostObjectId = null;
                String logicPortIp = null;
                if (params.get("hostObjectId") != null) {
                    hostObjectId = ToolUtils.getStr(params.get("hostObjectId"));
                    logicPortIp = ToolUtils.getStr(params.get(HOSTVKERNELIP));
                }
                vcsdkUtils.mountNfs(dataStoreObjectId, hostObjectId, logicPortIp,
                    ToolUtils.getStr(params.get("mountType")));
            } else {
                TaskDetailInfo taskDetailInfo = taskService.queryTaskById(taskId);
                LOG.error("mountnfs error!{}", taskDetailInfo.getDetail());
                throw new DmeException(taskDetailInfo.getDetail());
            }
        } else {
            throw new DmeException("DME mount nfs error(task is null)!");
        }
    }

    private String mountnfsToHost(Map<String, Object> params) {
        String taskId = "";
        try {
            if (StringUtils.isEmpty(params.get(SHAREID))) {
                throw new DmeException("mount nfs To Host fail: share Id is null");
            }
            if (params.get(HOSTVKERNELIP) == null) {
                throw new DmeException("mount nfs To Host fail: vkernelIp is null");
            }
            Map<String, Object> requestbody = new HashMap<>();
            String vkernelIp = ToolUtils.getStr(params.get(HOSTVKERNELIP));
            Map<String, Object> addition = new HashMap<>();
            addition.put(NAME_FIELD, vkernelIp);
            String accessval = ("readOnly".equalsIgnoreCase(ToolUtils.getStr(params.get("mountType"))))
                ? "read-only"
                : "read/write";
            addition.put("permission", accessval);
            addition.put("permission_constraint", "no_all_squash");
            addition.put("root_permission_constraint", "root_squash");
            addition.put("write_mode", "synchronization");
            addition.put("source_port_verification", "insecure");

            List<Map<String, Object>> listAddition = new ArrayList<>();
            listAddition.add(addition);
            requestbody.put("nfs_share_client_addition", listAddition);
            String nfsShareId = ToolUtils.getStr(params.get(SHAREID));
            String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace(NFS_SHARE_ID, nfsShareId);
            LOG.error("mountnfsToHost!method=put, nfsShareId={},url={}, body={}", nfsShareId, url,
                gson.toJson(requestbody));
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(requestbody));
            if (responseEntity.getStatusCodeValue() == DIGIT_202) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(TASK_ID) != null) {
                    taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                }
            }
        } catch (DmeException e) {
            LOG.error("mountnfs error:", e);
        }
        return taskId;
    }

    @Override
    public void unmountNfs(Map<String, Object> params) throws DmeException {
        String dataStoreObjectId = ToolUtils.getStr(params.get(DATASTOREOBJECTID));
        String hostObjId = ToolUtils.getStr(params.get("hostId"));
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
        if (dvr == null) {
            LOG.error("unmountNfs get relation error!dataStoreObjectId={}", dataStoreObjectId);
            return;
        }
        if (!StringUtils.isEmpty(hostObjId)) {
            unmountNfsFromHost(dataStoreObjectId, hostObjId);
        }
        String shareId = dvr.getShareId();
        List<AuthClient> authClientList = getNfsDatastoreShareAuthClients(shareId);
        if (authClientList != null && authClientList.size() > 0) {
            Map<String, String> authIdIpMap = new HashMap<>();
            for (AuthClient authClient : authClientList) {
                String authId = authClient.getId();
                String ip = authClient.getName();
                if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(authId)) {
                    authIdIpMap.put(authClient.getClientIdInStorage(), ip);
                }
            }
            String taskId = deleteAuthClient(shareId, authIdIpMap);
            if (!StringUtils.isEmpty(taskId)) {
                List<String> taskIds = new ArrayList<>();
                taskIds.add(taskId);
                boolean isUnmounted = taskService.checkTaskStatus(taskIds);
                if (!isUnmounted) {
                    LOG.error("unmountNfs mountnfs error!taskId={}", taskId);
                    throw new DmeException("DME mount nfs error(task status)!");
                }
            }
        }
    }

    @Override
    public void deleteNfs(Map<String, Object> params) throws DmeException {
        String dataStorageId = ToolUtils.getStr(params.get(DATASTOREOBJECTID));

        // DME侧删除nfs
        dmeDeleteNfs(dataStorageId);

        List<Map<String, Object>> hosts = getHostsMountDataStoreByDsObjectId(dataStorageId);
        LOG.info("get vmware hosts success!hosts={}", gson.toJson(hosts));
        List<String> hostIds = new ArrayList<>();
        if (hosts != null && hosts.size() > 0) {
            for (Map<String, Object> hostMap : hosts) {
                String hostId = ToolUtils.getStr(hostMap.get("hostId"));
                hostIds.add(hostId);
            }
        }
        if (hostIds.size() > 0) {
            LOG.info("vmware deleteNfs begin!hostIds={}", gson.toJson(hostIds));
            vcsdkUtils.deleteNfs(dataStorageId, hostIds);
            LOG.info("vmware deleteNfs end!");
        }
    }

    private void dmeDeleteNfs(String dataStorageId) throws DmeException {
        LOG.info("dme delete nfs begin!dataStorageId={}", dataStorageId);
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStorageId);
        if (dvr == null) {
            LOG.info("nfs delete!dme nfs relation is null!");
            return;
        }
        String shareId = dvr.getShareId();
        String fsId = dvr.getFsId();
        List<String> taskIds = new ArrayList<>();
        String taskId = "";
        if (!StringUtils.isEmpty(shareId)) {
            taskId = deleteNfsShare(Arrays.asList(shareId));
            if (!StringUtils.isEmpty(taskId)) {
                taskIds.add(taskId);
            }
            LOG.info("dme delete share end,taskId={}", taskId);
            boolean isShareDelete = taskService.checkTaskStatus(taskIds);
            if (!isShareDelete) {
                LOG.info("dme delete share failed!,taskId={}", taskId);
                throw new DmeException("DME delete share failed!taskId=" + taskId);
            }
        }
        taskIds.clear();
        if (!StringUtils.isEmpty(fsId)) {
            taskId = deleteNfsFs(Arrays.asList(fsId));
            if (!StringUtils.isEmpty(taskId)) {
                taskIds.add(taskId);
            }
            LOG.info("dme delete nfs filesystem end,taskId={}", taskId);
            if (taskIds.size() > 0) {
                boolean isDeleted = taskService.checkTaskStatus(taskIds);
                if (isDeleted) {
                    // 关系解除
                    dmeVmwareRalationDao.deleteByStorageId(Arrays.asList(dataStorageId));
                    LOG.info("dme nfs delete filesystem success!");
                } else {
                    throw new DmeException("DME delete nfs filesystem error!");
                }
            }
        }

        LOG.info("dme delete nfs end!");
    }

    private void unmountNfsFromHost(String dataStoreObjectId, String hostId) throws VcenterException {
        vcsdkUtils.unmountNfsOnHost(dataStoreObjectId, hostId);
    }

    private String deleteAuthClient(String shareId, Map<String, String> authClientIdIpMap) {
        String taskId = "";
        Map<String, Object> requestbody = new HashMap<>();
        List<Map<String, Object>> listAddition = new ArrayList<>();
        for (Map.Entry<String, String> authClient : authClientIdIpMap.entrySet()) {
            Map<String, Object> addtion = new HashMap<>();
            addtion.put("nfs_share_client_id_in_storage", authClient.getKey());
            addtion.put(NAME_FIELD, authClient.getValue());
            listAddition.add(addtion);
        }
        requestbody.put(ID_FIELD, shareId);
        requestbody.put("nfs_share_client_deletion", listAddition);
        try {
            String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace(NFS_SHARE_ID, shareId);
            LOG.error("deleteAuthClient!method=put, nfsShareId={},url={}, body={}", shareId, url,
                gson.toJson(requestbody));
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(requestbody));
            if (responseEntity.getStatusCodeValue() == DIGIT_202) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(TASK_ID) != null) {
                    taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                }
            }
        } catch (DmeException e) {
            LOG.error("unmountnfs authClient error:", e);
        }
        return taskId;
    }

    private String deleteNfsShare(List<String> shareIds) {
        LOG.info("DME delete NFS share begin!shareIds={}", gson.toJson(shareIds));
        String taskId = "";
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("nfs_share_ids", shareIds);
        try {
            ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_SHARE_DELETE_URL,
                HttpMethod.POST, gson.toJson(requestbody));
            if (responseEntity.getStatusCodeValue() == DIGIT_202) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(TASK_ID) != null) {
                    taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                }
            }
        } catch (DmeException e) {
            LOG.error("DME delete nfs share error:", e);
        }
        LOG.info("DME delete NFS share end!taskId={}", taskId);
        return taskId;
    }

    private String deleteNfsFs(List<String> fsIds) {
        LOG.info("DME delete NFS filesystem begin!fsIds={}", gson.toJson(fsIds));
        String taskId = "";
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("file_system_ids", fsIds);
        try {
            ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FS_DELETE_URL, HttpMethod.POST,
                gson.toJson(requestbody));
            if (responseEntity.getStatusCodeValue() == DIGIT_202) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(TASK_ID) != null) {
                    taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                }
            }
        } catch (DmeException e) {
            LOG.error("delete nfs fs error:", e);
        }
        LOG.info("DME delete NFS filesystem end!taskId={}", taskId);
        return taskId;
    }

    @Override
    public List<Map<String, Object>> getHostsMountDataStoreByDsObjectId(String dataStoreObjectId) throws DmeException {
        List<Map<String, Object>> lists = null;
        try {
            // 取得vcenter中的所有挂载了当前存储的host
            String listStr = vcsdkUtils.getHostsByDsObjectId(dataStoreObjectId, true);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get Hosts MountDataStore By DsObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, Object>> getClusterMountDataStoreByDsObjectId(String dataStoreObjectId)
        throws DmeException {
        List<Map<String, Object>> lists = null;
        try {
            // 取得vcenter中的所有挂载了当前存储的host
            String listStr = vcsdkUtils.getClustersByDsObjectId(dataStoreObjectId);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get Clusters MountDataStore By DsObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    public boolean isNfs(String objectId) throws Exception {
        List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_NFS);
        for (DmeVmwareRelation dmeVmwareRelation : dvrlist) {
            if (dmeVmwareRelation.getStoreId().equalsIgnoreCase(objectId)) {
                return true;
            }
        }
        return false;
    }

    private String getStorageModel(String storageId) throws DmeException {
        return dmeStorageService.getStorageDetail(storageId).getModel();
    }
}
