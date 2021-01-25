package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DmeIndicatorConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.BandPorts;
import com.huawei.dmestore.model.CapacityAutonegotiation;
import com.huawei.dmestore.model.DiskPool;
import com.huawei.dmestore.model.Dtrees;
import com.huawei.dmestore.model.EthPortInfo;
import com.huawei.dmestore.model.FailoverGroup;
import com.huawei.dmestore.model.FileSystem;
import com.huawei.dmestore.model.FileSystemDetail;
import com.huawei.dmestore.model.FileSystemTurning;
import com.huawei.dmestore.model.LogicPorts;
import com.huawei.dmestore.model.NfsShares;
import com.huawei.dmestore.model.SmartQos;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.model.StorageControllers;
import com.huawei.dmestore.model.StorageDetail;
import com.huawei.dmestore.model.StorageDisk;
import com.huawei.dmestore.model.StoragePool;
import com.huawei.dmestore.model.StoragePort;
import com.huawei.dmestore.model.StorageTypeShow;
import com.huawei.dmestore.model.Volume;
import com.huawei.dmestore.model.VolumeListRestponse;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DmeStorageServiceImpl
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public class DmeStorageServiceImpl implements DmeStorageService {
    private static final Logger LOG = LoggerFactory.getLogger(DmeStorageServiceImpl.class);

    private static final String CAPACITY_FILED = "capacity";

    private static final String DATASTATUS_FILED = "dataStatus";

    private static final String OPERATOR = "operator";

    private static final String SIMPLE = "simple";

    private static final String PORTTYPE = "portType";

    private static final String SPEED = "speed";

    private static final String STATUS = "status";

    private static final String CODE_403 = "403";

    private static final String ID_FILED = "id";

    private static final String SPLIT_CHAR = "-";

    private static final String VALUE_FILED = "value";

    private static final String EQUAL_FILED = "equal";

    private static final String LOGICAL_TYPE = "logicalType";

    private static final String USED_CAPACITY = "usedCapacity";

    private static final String LAST_MONITOR_TIME = "lastMonitorTime";

    private static final String NATIVE_ID = "nativeId";

    private static final String STORAGE_ID = "storage_id";

    private static final String OBJ_IDS = "obj_ids";

    private static final String POOL_RAW_ID = "pool_raw_id";

    private static final String POOL_ID = "poolId";

    private static final String NAME_FIELD = "name";

    private static final String SN_FIELD = "sn";

    private static final String RES_ID = "resId";

    private static final String STORAGE_DEVICE_ID = "storageDeviceId";

    private static final String OBJ_LIST = "objList";

    private static final String CODE_503 = "503";

    private static final String LOCATION = "location";

    private static final String CONDITION = "?condition={json}&&pageSize=1000";

    private static final int CONSTANT_100 = 100;

    private static final int ONE_KB = 1024;

    private Gson gson = new Gson();

    private DmeAccessService dmeAccessService;

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private VCSDKUtils vcsdkUtils;

    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    public void setDataStoreStatisticHistoryService(DataStoreStatisticHistoryService dataStoreStatisticHistoryService) {
        this.dataStoreStatisticHistoryService = dataStoreStatisticHistoryService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public DmeVmwareRalationDao getDmeVmwareRalationDao() {
        return dmeVmwareRalationDao;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
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

    @Override
    public List<Storage> getStorages() throws DmeException {
        List<Storage> list = new ArrayList<>();
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.API_STORAGES, HttpMethod.GET,
                null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list storage error !");
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray storage = jsonObject.get("datas").getAsJsonArray();
                for (JsonElement jsonElement : storage) {
                    JsonObject jsonObj = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
                    Storage storageObj = new Storage();
                    parseStorageBaseInfo(jsonObj, storageObj);
                    if (!StringUtils.isEmpty(storageObj.getModel())){
                        StorageTypeShow storageTypeShow = ToolUtils.getStorageTypeShow(storageObj.getModel());
                        storageObj.setStorageTypeShow(storageTypeShow);
                    }
                    storageObj.setSubscriptionCapacity(ToolUtils.jsonToDou(jsonObj.get("subscription_capacity")));
                    storageObj.setUsedCapacity(ToolUtils.jsonToDou(jsonObj.get("used_capacity"), 0.0));
                    storageObj.setTotalCapacity(ToolUtils.jsonToDou(jsonObj.get("total_capacity"), 0.0));
                    storageObj.setTotalEffectiveCapacity(
                        ToolUtils.jsonToDou(jsonObj.get("total_effective_capacity"), 0.0));
                    storageObj.setFreeEffectiveCapacity(
                        ToolUtils.jsonToDou(jsonObj.get("free_effective_capacity"), 0.0));
                    storageObj.setMaxCpuUtilization(ToolUtils.jsonToDou(jsonObj.get("max_cpu_utilization"), 0.0));
                    storageObj.setMaxIops(ToolUtils.jsonToDou(jsonObj.get("max_iops"), 0.0));
                    storageObj.setMaxBandwidth(ToolUtils.jsonToDou(jsonObj.get("max_bandwidth"), 0.0));
                    storageObj.setMaxLatency(ToolUtils.jsonToDou(jsonObj.get("max_latency"), 0.0));
                    storageObj.setTotalPoolCapacity(ToolUtils.jsonToDou(jsonObj.get("total_pool_capacity"), 0.0));
                    JsonElement jsonAzIds = jsonObj.get("az_ids");
                    if (!ToolUtils.jsonIsNull(jsonAzIds)) {
                        String azIds = ToolUtils.jsonToStr(jsonAzIds);
                        String[] azIdsArr = {azIds};
                        storageObj.setAzIds(azIdsArr);
                    } else {
                        String[] azIdsArr = {};
                        storageObj.setAzIds(azIdsArr);
                    }
                    list.add(storageObj);
                }
            }
        } catch (DmeException e) {
            LOG.error("list storage error", e);
            String message = e.getMessage();
            throw new DmeException(CODE_503, message);
        }
        return list;
    }

    private void parseStorageBaseInfo(JsonObject jsonObj, Storage storageObj) {
        storageObj.setId(ToolUtils.jsonToStr(jsonObj.get(ID_FILED)));
        storageObj.setName(ToolUtils.jsonToStr(jsonObj.get(NAME_FIELD)));
        storageObj.setIp(ToolUtils.jsonToStr(jsonObj.get("ip")));
        storageObj.setStatus(ToolUtils.jsonToStr(jsonObj.get(STATUS)));
        storageObj.setSynStatus(ToolUtils.jsonToStr(jsonObj.get("syn_status")));
        storageObj.setVendor(ToolUtils.jsonToStr(jsonObj.get("vendor")));
        storageObj.setModel(ToolUtils.jsonToStr(jsonObj.get("model")));
        storageObj.setSn(ToolUtils.jsonToStr(jsonObj.get(SN_FIELD)));
        storageObj.setVersion(ToolUtils.jsonToStr(jsonObj.get("version")));
        storageObj.setLocation(ToolUtils.jsonToStr(jsonObj.get(LOCATION), null));
        storageObj.setPatchVersion(ToolUtils.jsonToStr(jsonObj.get("patch_version"), null));
        storageObj.setMaintenanceStart(ToolUtils.jsonToDateStr(jsonObj.get("maintenance_start"), null));
        storageObj.setMaintenanceOvertime(ToolUtils.jsonToDateStr(jsonObj.get("maintenance_overtime"), null));
    }

    @Override
    public StorageDetail getStorageDetail(String storageId) throws DmeException {
        StorageDetail storageObj;
        String url = DmeConstants.API_STORAGES + "/" + storageId + "/detail";
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "search oriented storage error");
            }
            String object = responseEntity.getBody();
            JsonObject element = new JsonParser().parse(object).getAsJsonObject();
            storageObj = new StorageDetail();
            parseStorageDetail(storageObj, element);
            if (!StringUtils.isEmpty(storageObj.getModel())){
                StorageTypeShow storageTypeShow = ToolUtils.getStorageTypeShow(storageObj.getModel());
                storageObj.setStorageTypeShow(storageTypeShow);
            }
            parseStoragePoolDetail(storageId, storageObj);
            JsonArray ids = element.get("az_ids").getAsJsonArray();
            if (ids.size() != 0) {
                String[] azIdsArr = {ToolUtils.jsonToStr(ids)};
                storageObj.setAzIds(azIdsArr);
            } else {
                String[] azIdsArr = {};
                storageObj.setAzIds(azIdsArr);
            }
        } catch (DmeException e) {
            LOG.error("search oriented storage error!", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return storageObj;
    }

    private void parseStoragePoolDetail(String storageId, StorageDetail storageObj) throws DmeException {
        List<StoragePool> storagePools = getStoragePools(storageId, "all");
        Double totalPoolCapicity = 0.0;
        Double subscriptionCapacity = 0.0;
        Double protectionCapacity = 0.0;
        Double fileCapacity = 0.0;
        Double blockCapacity = 0.0;
        Double dedupedCapacity = 0.0;
        Double compressedCapacity = 0.0;
        for (StoragePool storagePool : storagePools) {
            totalPoolCapicity += storagePool.getTotalCapacity();
            subscriptionCapacity += storagePool.getSubscribedCapacity();
            protectionCapacity += storagePool.getProtectionCapacity();
            if ("file".equalsIgnoreCase(storagePool.getMediaType())) {
                fileCapacity += storagePool.getConsumedCapacity();
            }
            if ("block".equalsIgnoreCase(storagePool.getMediaType())) {
                blockCapacity += storagePool.getConsumedCapacity();
            }
            dedupedCapacity += storagePool.getDedupedCapacity();
            compressedCapacity += storagePool.getCompressedCapacity();
        }
        storageObj.setTotalEffectiveCapacity(totalPoolCapicity);
        storageObj.setFreeEffectiveCapacity(totalPoolCapicity - fileCapacity - blockCapacity - protectionCapacity);
        storageObj.setSubscriptionCapacity(subscriptionCapacity);
        storageObj.setProtectionCapacity(protectionCapacity);
        storageObj.setFileCapacity(fileCapacity);
        storageObj.setBlockCapacity(blockCapacity);
        storageObj.setDedupedCapacity(dedupedCapacity);
        storageObj.setCompressedCapacity(compressedCapacity);
        storageObj.setOptimizeCapacity(storageObj.getUsedCapacity() - dedupedCapacity - compressedCapacity);
    }

    private void parseStorageDetail(StorageDetail storageObj, JsonObject element) {
        storageObj.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
        storageObj.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
        storageObj.setIp(ToolUtils.jsonToStr(element.get("ip")));
        storageObj.setStatus(ToolUtils.jsonToStr(element.get(STATUS)));
        storageObj.setSynStatus(ToolUtils.jsonToStr(element.get("syn_status")));
        storageObj.setVendor(ToolUtils.jsonToStr(element.get("vendor")));
        storageObj.setModel(ToolUtils.jsonToStr(element.get("model")));
        storageObj.setUsedCapacity(ToolUtils.jsonToDou(element.get("used_capacity"), 0.0) / ONE_KB);
        storageObj.setTotalCapacity(ToolUtils.jsonToDou(element.get("total_capacity"), 0.0) / ONE_KB);
        storageObj.setTotalEffectiveCapacity(ToolUtils.jsonToDou(element.get("total_effective_capacity"), 0.0));
        storageObj.setFreeEffectiveCapacity(ToolUtils.jsonToDou(element.get("free_effective_capacity"), 0.0));
        storageObj.setLocation(ToolUtils.jsonToStr(element.get(LOCATION), null));
        storageObj.setPatchVersion(ToolUtils.jsonToStr(element.get("patch_version"), null));
        storageObj.setMaintenanceStart(ToolUtils.jsonToDateStr(element.get("maintenance_start"), null));
        storageObj.setMaintenanceOvertime(ToolUtils.jsonToDateStr(element.get("maintenance_overtime"), null));
        storageObj.setProductVersion(ToolUtils.jsonToStr(element.get("product_version")));
        storageObj.setSn(ToolUtils.jsonToStr(element.get(SN_FIELD), null));
    }

    @Override
    public List<StoragePool> getStoragePools(String storageId, String mediaType) throws DmeException {
        String replace = storageId.replace(SPLIT_CHAR, "").toUpperCase();
        String className = "SYS_StoragePool";
        List<StoragePool> resList = new ArrayList<>();
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className) + CONDITION;
        String params = ToolUtils.getRequsetParams(STORAGE_DEVICE_ID, replace);
        List<Storage> storages = getStorages();
        Map<String, String> ids = new HashMap<>();
        if (storages != null && storages.size() > 0) {
            for (Storage storage : storages) {
                String id = storage.getId().replace(SPLIT_CHAR, "").toUpperCase();
                ids.put(id, storage.getName());
            }
        }
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "search oriented storage pool error");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                // 得到存储池与服务等级的关系
                Map<String, Object> djofspMap = getDjTierOfStoragePool();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StoragePool storagePool = new StoragePool();
                    parseStoragePoolBaseInfo(element, storagePool);
                    parseStoragePoolCapacity(element, storagePool);
                    String diskPoolId = ToolUtils.jsonToStr(element.get("diskPoolId"));
                    storagePool.setDiskPoolId(diskPoolId);
                    String diskType = getDiskType(storageId, diskPoolId);
                    storagePool.setPhysicalType(diskType.toUpperCase());
                    String resId = ToolUtils.jsonToStr(element.get(RES_ID));
                    if (djofspMap != null && djofspMap.get(resId) != null) {
                        storagePool.setServiceLevelName(gson.toJson(djofspMap.get(resId)));
                    }
                    if (ids.get(replace) != null) {
                        storagePool.setStorageName(ids.get(replace));
                    }
                    String type = ToolUtils.jsonToStr(element.get("type"));
                    storagePool.setMediaType(type);
                    if (mediaType.equals(type)) {
                        resList.add(storagePool);
                    } else if ("all".equals(mediaType)) {
                        resList.add(storagePool);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("search oriented storage pool error", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resList;
    }

    private void parseStoragePoolCapacity(JsonObject element, StoragePool storagePool) {
        storagePool.setConsumedCapacity(ToolUtils.jsonToDou(element.get(USED_CAPACITY), 0.0));
        Double totalCapacity = ToolUtils.jsonToDou(element.get("totalCapacity"), 0.0);
        storagePool.setTotalCapacity(totalCapacity);
        Double consumedCapacity = ToolUtils.jsonToDou(element.get(USED_CAPACITY), 0.0);
        storagePool.setConsumedCapacity(consumedCapacity);
        Double dedupedCapacity = ToolUtils.jsonToDou(element.get("dedupedCapacity"), 0.0);
        storagePool.setDedupedCapacity(dedupedCapacity);
        Double compressedCapacity = ToolUtils.jsonToDou(element.get("compressedCapacity"), 0.0);
        storagePool.setCompressedCapacity(compressedCapacity);
        Double protectionCapacity = ToolUtils.jsonToDou(element.get("protectionCapacity"), 0.0);
        storagePool.setProtectionCapacity(protectionCapacity);
        Double subscribedCapacity = ToolUtils.jsonToDou(element.get("subscribedCapacity"), 0.0);
        storagePool.setSubscribedCapacity(subscribedCapacity);
        DecimalFormat df = new DecimalFormat("#.00");
        Double subscribedCapacityRate = 0.0;
        if (totalCapacity != 0) {
            subscribedCapacityRate = Double.valueOf(df.format(subscribedCapacity / totalCapacity)) * CONSTANT_100;
        }
        Double freeCapacity = 0.0;
        String consumedPercent = "0.0";
        if (Double.max(totalCapacity, consumedCapacity) == totalCapacity) {
            freeCapacity = totalCapacity - consumedCapacity;
            consumedPercent = df.format(consumedCapacity / totalCapacity);
        }
        storagePool.setConsumedCapacityPercentage(consumedPercent);
        storagePool.setSubscriptionRate(subscribedCapacityRate);
        storagePool.setFreeCapacity(freeCapacity);
    }

    private void parseStoragePoolBaseInfo(JsonObject element, StoragePool storagePool) {
        storagePool.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
        storagePool.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
        storagePool.setHealthStatus(ToolUtils.jsonToStr(element.get(STATUS)));
        storagePool.setRunningStatus(ToolUtils.jsonToStr(element.get("runningStatus")));
        storagePool.setStoragePoolId(ToolUtils.jsonToStr(element.get(STORAGE_DEVICE_ID)));
        storagePool.setStorageId(ToolUtils.jsonToStr(element.get(STORAGE_DEVICE_ID)));
        String tier0RaidLv = ToolUtils.jsonToStr(element.get("tier0RaidLv"));
        String tier1RaidLv = ToolUtils.jsonToStr(element.get("tier1RaidLv"));
        String tier2RaidLv = ToolUtils.jsonToStr(element.get("tier2RaidLv"));
        storagePool.setTier0RaidLv(tier0RaidLv);
        storagePool.setTier1RaidLv(tier1RaidLv);
        storagePool.setTier2RaidLv(tier2RaidLv);
        String raidLevel = parseRaidLv(tier0RaidLv) + parseRaidLv(tier1RaidLv) + parseRaidLv(tier2RaidLv);
        storagePool.setRaidLevel(raidLevel.substring(0, raidLevel.lastIndexOf("/")));
        storagePool.setPoolId(ToolUtils.jsonToStr(element.get(POOL_ID)));
        storagePool.setStorageInstanceId(ToolUtils.jsonToStr(element.get(RES_ID)));
    }

    private String parseRaidLv(String raidLv){
        String raidLvValue = DmeConstants.RAID_LEVEL_MAP.get(raidLv);
        if(!StringUtils.isEmpty(raidLvValue)){
            return raidLvValue + "/";
        }
        return "";
    }

    @Override
    public List<LogicPorts> getLogicPorts(String storageId) throws DmeException {
        List<LogicPorts> resList = new ArrayList<>();
        String url = DmeConstants.API_LOGICPORTS_LIST;
        JsonObject param = null;
        HttpMethod method = HttpMethod.POST;
        param = new JsonObject();
        param.addProperty("storage_id", storageId);
        try {
            LOG.info("{}, getLogic begin!storageId={}", url, storageId);
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, method,
                param == null ? null : gson.toJson(param));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                LOG.error("getLogic failed!response = {}", gson.toJson(responseEntity));
                throw new DmeException(CODE_503, responseEntity.getBody());
            }
            LOG.info("{}, getLogic end!storageId={} success!", url, storageId);
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("logic_ports").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    LogicPorts logicPorts = new LogicPorts();
                    logicPorts.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
                    logicPorts.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                    logicPorts.setRunningStatus(ToolUtils.jsonToStr(element.get("running_status")));
                    logicPorts.setOperationalStatus(ToolUtils.jsonToStr(element.get("operational_status")));
                    logicPorts.setMgmtIp(ToolUtils.jsonToStr(element.get("mgmt_ip")));
                    logicPorts.setMgmtIpv6(ToolUtils.jsonToStr(element.get("mgmt_ipv6")));

                    // 老版本DEM为home_port_id
                    logicPorts.setHomePortId(ToolUtils.jsonToStr(element.get("home_port_raw_id")));
                    logicPorts.setHomePortName(ToolUtils.jsonToStr(element.get("home_port_name")));
                    logicPorts.setRole(ToolUtils.jsonToStr(element.get("role")));
                    logicPorts.setDdnsStatus(ToolUtils.jsonToStr(element.get("ddns_status")));

                    // 老版本DEM为current_port_id
                    logicPorts.setCurrentPortId(ToolUtils.jsonToStr(element.get("current_port_raw_id")));
                    logicPorts.setCurrentPortName(ToolUtils.jsonToStr(element.get("current_port_name")));
                    logicPorts.setSupportProtocol(ToolUtils.jsonToStr(element.get("support_protocol")));
                    logicPorts.setManagementAccess(ToolUtils.jsonToStr(element.get("management_access")));
                    logicPorts.setVstoreId(ToolUtils.jsonToStr(element.get("vstore_id")));
                    logicPorts.setVstoreName(ToolUtils.jsonToStr(element.get("vstore_name")));

                    resList.add(logicPorts);
                }
            }
        } catch (DmeException e) {
            LOG.error("list bandports error", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resList;
    }

    @Override
    public VolumeListRestponse getVolumesByPage(String storageId, String pageSize, String pageNo) throws DmeException {
        VolumeListRestponse volumeListRestponse = new VolumeListRestponse();
        List<Volume> volumes = new ArrayList<>();
        String url = DmeConstants.DME_VOLUME_BASE_URL + "?";
        if (!"".equals(storageId)) {
            url = url + "storage_id=" + storageId + "&";
        }

        if (!StringUtils.isEmpty(pageSize)) {
            url = url + "limit=" + pageSize + "&";
        }

        if (!StringUtils.isEmpty(pageNo)) {
            url = url + "offset=" + pageNo;
        }

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list volumes error!");
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                int count = jsonObject.get("count").getAsInt();
                JsonArray jsonArray = jsonObject.get("volumes").getAsJsonArray();
                Map<String, String> poolnamecacheMap = new HashMap<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    Volume volume = parseVolume(element);
                    String poolRawId = ToolUtils.jsonToStr(element.get(POOL_RAW_ID));
                    if (poolnamecacheMap.get(poolRawId) == null) {
                        poolnamecacheMap.put(poolRawId, getStorageByPoolRawId(poolRawId));
                    }
                    volume.setStoragePoolName(poolnamecacheMap.get(poolRawId));
                    volumes.add(volume);
                }

                volumeListRestponse.setVolumeList(volumes);
                volumeListRestponse.setCount(count);
            }
        } catch (DmeException e) {
            LOG.error("list volume error!", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return volumeListRestponse;
    }

    private Volume parseVolume(JsonObject element) throws DmeException {
        Volume volume = new Volume();
        String volumeId = ToolUtils.jsonToStr(element.get(ID_FILED));
        volume.setId(volumeId);
        volume.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
        volume.setStatus(ToolUtils.jsonToStr(element.get(STATUS)));
        volume.setAttached(ToolUtils.jsonToBoo(element.get("attached")));
        volume.setAlloctype(ToolUtils.jsonToStr(element.get("alloctype")));
        volume.setServiceLevelName(ToolUtils.jsonToStr(element.get("service_level_name")));
        volume.setStorageId(ToolUtils.jsonToStr(element.get(STORAGE_ID)));
        String poolRawId = ToolUtils.jsonToStr(element.get(POOL_RAW_ID));
        volume.setPoolRawId(poolRawId);
        volume.setWwn(ToolUtils.jsonToStr(element.get("volume_wwn")));
        volume.setCapacityUsage(ToolUtils.jsonToStr(element.get("capacity_usage")));
        volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protected")));
        volume.setCapacity(ToolUtils.jsonToInt(element.get(CAPACITY_FILED), 0));
        volume.setDatastores(getDataStoreOnVolume(volumeId));
        return volume;
    }

    @Override
    public List<FileSystem> getFileSystems(String storageId) throws DmeException {
        List<FileSystem> fileSystems = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put(STORAGE_ID, storageId);
        String jsonParams = gson.toJson(params);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FILESERVICE_QUERY_URL,
                HttpMethod.POST, jsonParams);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list filesystem error!");
            }
            String respObject = responseEntity.getBody();
            if (!StringUtils.isEmpty(respObject)) {
                JsonObject jsonObject = new JsonParser().parse(respObject).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    FileSystem fileSystem = new FileSystem();
                    fileSystem.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
                    fileSystem.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                    fileSystem.setType(ToolUtils.jsonToStr(element.get("type")));
                    fileSystem.setHealthStatus(ToolUtils.jsonToStr(element.get("health_status")));
                    fileSystem.setAllocType(ToolUtils.jsonToStr(element.get("alloc_type")));
                    fileSystem.setCapacity(ToolUtils.jsonToDou(element.get(CAPACITY_FILED), 0.0));
                    fileSystem.setCapacityUsageRatio(ToolUtils.jsonToInt(element.get("capacity_usage_ratio"), 0));
                    fileSystem.setStoragePoolName(ToolUtils.jsonToStr(element.get("storage_pool_name")));
                    fileSystem.setNfsCount(ToolUtils.jsonToInt(element.get("nfs_count"), 0));
                    fileSystem.setCifsCount(ToolUtils.jsonToInt(element.get("cifs_count"), 0));
                    fileSystem.setDtreeCount(ToolUtils.jsonToInt(element.get("dtree_count"), 0));
                    fileSystem.setAvailableCapacity(ToolUtils.jsonToDou(element.get("available_capacity"), 0.0));
                    fileSystems.add(fileSystem);
                }
            }
        } catch (DmeException e) {
            LOG.error("list filesystem error!", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return fileSystems;
    }

    @Override
    public List<Dtrees> getDtrees(String storageId) throws DmeException {
        List<Dtrees> resList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put(STORAGE_ID, storageId);
        params.put("page_size", DmeConstants.DEFAULT_PAGE_SIZE);
        try {
            String url = DmeConstants.API_DTREES_LIST;
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list dtree error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("dtrees").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    Dtrees dtrees = new Dtrees();
                    dtrees.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                    dtrees.setFsName(ToolUtils.jsonToStr(element.get("fs_name")));
                    dtrees.setQuotaSwitch(ToolUtils.jsonToBoo(element.get("quota_switch")));
                    dtrees.setSecurityStyle(ToolUtils.jsonToStr(element.get("security_mode")));
                    dtrees.setTierName(ToolUtils.jsonToStr(element.get("tier_name")));
                    dtrees.setNfsCount(ToolUtils.jsonToInt(element.get("nfs_count"), 0));
                    dtrees.setCifsCount(ToolUtils.jsonToInt(element.get("cifs_count"), 0));
                    resList.add(dtrees);
                }
            }
        } catch (DmeException e) {
            LOG.error("list dtree error!", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resList;
    }

    @Override
    public List<NfsShares> getNfsShares(String storageId) throws DmeException {
        List<NfsShares> resList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put(STORAGE_ID, storageId);
        params.put("page_size", DmeConstants.DEFAULT_PAGE_SIZE);
        try {
            String url = DmeConstants.DME_NFS_SHARE_URL;
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list nfsshares error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("nfs_share_info_list").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    NfsShares nfsShares = new NfsShares();
                    nfsShares.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                    nfsShares.setSharePath(ToolUtils.jsonToStr(element.get("share_path")));
                    nfsShares.setStorageId(ToolUtils.jsonToStr(element.get(STORAGE_ID)));
                    nfsShares.setTierName(ToolUtils.jsonToStr(element.get("tier_name")));
                    nfsShares.setOwningDtreeName(ToolUtils.jsonToStr(element.get("owning_dtree_name")));
                    nfsShares.setOwningDtreeId(ToolUtils.jsonToStr(element.get("owning_dtree_id")));
                    nfsShares.setFsName(ToolUtils.jsonToStr(element.get("fs_name")));
                    resList.add(nfsShares);
                }
            }
        } catch (DmeException e) {
            LOG.error("list nfsshares error!", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resList;
    }

    @Override
    public List<BandPorts> getBandPorts(String storageId) throws DmeException {
        List<BandPorts> resList = new ArrayList<>();
        String url = DmeConstants.API_BANDPORTS_LIST + storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list bandports error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("bond_ports").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    BandPorts bandPorts = new BandPorts();
                    bandPorts.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
                    bandPorts.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                    bandPorts.setHealthStatus(ToolUtils.jsonToStr(element.get("health_status")));
                    bandPorts.setRunningStatus(ToolUtils.jsonToStr(element.get("running_status")));
                    bandPorts.setMtu(ToolUtils.jsonToStr(element.get("mtu")));
                    resList.add(bandPorts);
                }
            }
        } catch (DmeException e) {
            LOG.error("list bandports error!", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resList;
    }

    @Override
    public List<StorageControllers> getStorageControllers(String storageDeviceId) throws DmeException {
        String replace = storageDeviceId.replace(SPLIT_CHAR, "");
        String className = "SYS_Controller";
        List<StorageControllers> resList = new ArrayList<>();
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className) + CONDITION;
        String params = ToolUtils.getRequsetParams(STORAGE_DEVICE_ID, replace);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list storage controller error!");
            }
            Object object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
                List<String> resids = new ArrayList<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String id = ToolUtils.jsonToStr(element.get(ID_FILED));
                    resids.add(id);
                }
                List<StorageControllers> storageControllersperf = listStorageControllerPerformance(resids);
                Map<String, StorageControllers> storageControllersMap = new HashMap<>();
                for (StorageControllers storageControllers : storageControllersperf) {
                    storageControllersMap.put(storageControllers.getId(), storageControllers);
                }
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StorageControllers storageControllers = parseStorageController(storageControllersMap, element);
                    resList.add(storageControllers);
                }
            }
        } catch (DmeException e) {
            LOG.error("list storage controller error!");
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resList;
    }

    private StorageControllers parseStorageController(Map<String, StorageControllers> storageControllersMap,
        JsonObject element) {
        StorageControllers storageControllers = new StorageControllers();
        storageControllers.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
        storageControllers.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
        storageControllers.setSoftVer(ToolUtils.jsonToStr(element.get("softVer")));
        storageControllers.setStatus(ToolUtils.jsonToStr(element.get(STATUS)));
        storageControllers.setCpuInfo(ToolUtils.jsonToStr(element.get("cpuInfo")));
        if (storageControllersMap.get(storageControllers.getId()) != null) {
            storageControllers.setLantency(storageControllersMap.get(storageControllers.getId()).getLantency());
            storageControllers.setBandwith(storageControllersMap.get(storageControllers.getId()).getBandwith());
            storageControllers.setIops(storageControllersMap.get(storageControllers.getId()).getIops());
            storageControllers.setCpuUsage(storageControllersMap.get(storageControllers.getId()).getCpuUsage());
        }
        return storageControllers;
    }

    @Override
    public List<StorageDisk> getStorageDisks(String storageDeviceId) throws DmeException {
        String replace = storageDeviceId.replace(SPLIT_CHAR, "");
        String className = "SYS_StorageDisk";
        List<StorageDisk> resList = new ArrayList<>();
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className) + "?condition={json} "
            + "&pageSize=1000";
        String params = ToolUtils.getRequsetParams(STORAGE_DEVICE_ID, replace);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list storage disk error!");
            }
            Object object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
                List<String> resids = new ArrayList<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String id = ToolUtils.jsonToStr(element.get(ID_FILED));
                    resids.add(id);
                }
                List<StorageDisk> storageDiskperf = listStorageDiskPerformance(resids);
                Map<String, StorageDisk> storageDiskMap = new HashMap<>();
                for (StorageDisk storageDisk : storageDiskperf) {
                    storageDiskMap.put(storageDisk.getId(), storageDisk);
                }
                Map<String, String> storageDiskPool = new HashMap<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StorageDisk storageDisk = parseStorageDisk(storageDiskMap, storageDiskPool, element);
                    resList.add(storageDisk);
                }
            }
        } catch (DmeException e) {
            LOG.error("list storage disk error!", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resList;
    }

    private StorageDisk parseStorageDisk(Map<String, StorageDisk> storageDiskMap, Map<String, String> storageDiskPool,
        JsonObject element) throws DmeException {
        StorageDisk storageDisk = new StorageDisk();
        storageDisk.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
        storageDisk.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
        storageDisk.setStatus(ToolUtils.jsonToStr(element.get(STATUS)));
        storageDisk.setCapacity(ToolUtils.jsonToDou(element.get(CAPACITY_FILED), 0.0));
        storageDisk.setSpeed(ToolUtils.jsonToLon(element.get(SPEED), 0L));
        storageDisk.setLogicalType(ToolUtils.jsonToStr(element.get(LOGICAL_TYPE)));
        storageDisk.setPhysicalType(ToolUtils.jsonToStr(element.get("physicalType")));
        String poolId = ToolUtils.jsonToStr(element.get(POOL_ID));
        storageDisk.setPoolId(poolId);
        storageDisk.setStorageDeviceId(ToolUtils.jsonToStr(element.get(STORAGE_DEVICE_ID)));
        if (storageDiskPool.get(poolId) == null) {
            storageDiskPool.put(poolId, getDiskPoolByPoolId(poolId).getName());
            storageDisk.setDiskDomain(storageDiskPool.get(poolId));
        } else {
            storageDisk.setDiskDomain(storageDiskPool.get(poolId));
        }
        if (storageDiskMap.get(storageDisk.getId()) != null) {
            storageDisk.setLantency(storageDiskMap.get(storageDisk.getId()).getLantency());
            storageDisk.setBandwith(storageDiskMap.get(storageDisk.getId()).getBandwith());
            storageDisk.setIops(storageDiskMap.get(storageDisk.getId()).getIops());
            storageDisk.setUseage(storageDiskMap.get(storageDisk.getId()).getUseage());
        }
        return storageDisk;
    }

    @Override
    public List<EthPortInfo> getStorageEthPorts(String storageSn) throws DmeException {
        List<EthPortInfo> relists = null;
        try {
            if (!StringUtils.isEmpty(storageSn)) {
                // 通过存储设备的sn查询 存储设备的资源ID
                String dsResId = getStorageResIdBySn(storageSn);
                if (!StringUtils.isEmpty(dsResId)) {
                    relists = getEthPortsByResId(dsResId);
                } else {
                    throw new DmeException("get Storage ResId By Sn error:resId is null");
                }
            }
        } catch (DmeException e) {
            LOG.error("get Storage Eth Ports error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    // 通过存储设备的sn查询 存储设备的资源ID
    public String getStorageResIdBySn(String storageSn) {
        String dsResId = null;
        String stordeviceIdUrl = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, "SYS_StorDevice");
        String params = ToolUtils.getRequsetParams(SN_FIELD, storageSn);
        stordeviceIdUrl = stordeviceIdUrl + CONDITION;
        try {
            ResponseEntity responseEntity = dmeAccessService.accessByJson(stordeviceIdUrl, HttpMethod.GET, params);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(OBJ_LIST) != null) {
                    JsonArray objList = jsonObject.getAsJsonArray(OBJ_LIST);
                    if (objList != null && objList.size() > 0) {
                        JsonObject vjson = objList.get(0).getAsJsonObject();
                        dsResId = ToolUtils.jsonToStr(vjson.get(ID_FILED));
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("DME link error url:{},error:{}", stordeviceIdUrl, e.toString());
        }
        return dsResId;
    }

    // 通过资源管理API查询Eth接口
    public List<EthPortInfo> getEthPortsByResId(String dsResId) {
        List<EthPortInfo> relists = null;
        String ethPortUrl = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, "SYS_StoragePort");
        JsonArray constraint = getJsonParams(dsResId);
        JsonObject condition = new JsonObject();
        condition.add("constraint", constraint);
        ethPortUrl = ethPortUrl + CONDITION;
        try {
            ResponseEntity responseEntity = dmeAccessService.accessByJson(ethPortUrl, HttpMethod.GET,
                condition.toString());
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonArray objList = jsonObject.getAsJsonArray(OBJ_LIST);
                if (objList != null && objList.size() > 0) {
                    relists = new ArrayList<>();
                    for (int index = 0; index < objList.size(); index++) {
                        JsonObject vjson = objList.get(index).getAsJsonObject();
                        EthPortInfo ethPort = parseEthPortInfo(vjson);
                        relists.add(ethPort);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("{}, error:{}", ethPortUrl, e.toString());
        }
        return relists;
    }

    private JsonArray getJsonParams(String dsResId) {
        JsonObject simple = new JsonObject();
        simple.addProperty(NAME_FIELD, DATASTATUS_FILED);
        simple.addProperty(OPERATOR, EQUAL_FILED);
        simple.addProperty(VALUE_FILED, "normal");
        JsonObject consObj = new JsonObject();
        consObj.add(SIMPLE, simple);
        JsonArray constraint = new JsonArray();
        constraint.add(consObj);
        JsonObject simple1 = new JsonObject();
        simple1.addProperty(NAME_FIELD, PORTTYPE);
        simple1.addProperty(OPERATOR, EQUAL_FILED);
        simple1.addProperty(VALUE_FILED, "ETH");
        JsonObject consObj1 = new JsonObject();
        consObj1.add(SIMPLE, simple1);
        consObj1.addProperty("logOp", "and");
        constraint.add(consObj1);
        JsonObject simple2 = new JsonObject();
        simple2.addProperty(NAME_FIELD, STORAGE_DEVICE_ID);
        simple2.addProperty(OPERATOR, EQUAL_FILED);
        simple2.addProperty(VALUE_FILED, dsResId);
        JsonObject consObj2 = new JsonObject();
        consObj2.add(SIMPLE, simple2);
        consObj2.addProperty("logOp", "and");
        constraint.add(consObj2);
        return constraint;
    }

    private EthPortInfo parseEthPortInfo(JsonObject vjson) {
        EthPortInfo ethPort = new EthPortInfo();
        ethPort.setOwnerType(ToolUtils.jsonToStr(vjson.get("ownerType")));
        ethPort.setIpv4Mask(ToolUtils.jsonToStr(vjson.get("ipv4Mask")));
        ethPort.setLogicalType(ToolUtils.jsonToStr(vjson.get(LOGICAL_TYPE)));
        ethPort.setStorageDeviceId(ToolUtils.jsonToStr(vjson.get(STORAGE_DEVICE_ID)));
        ethPort.setPortName(ToolUtils.jsonToStr(vjson.get("portName")));
        ethPort.setOwnerId(ToolUtils.jsonToStr(vjson.get("ownerId")));
        ethPort.setPortId(ToolUtils.jsonToStr(vjson.get("portId")));
        ethPort.setBondName(ToolUtils.jsonToStr(vjson.get("bondName")));
        ethPort.setMac(ToolUtils.jsonToStr(vjson.get("mac")));
        ethPort.setMgmtIpv6(ToolUtils.jsonToStr(vjson.get("mgmtIpv6")));
        ethPort.setIscsiName(ToolUtils.jsonToStr(vjson.get("iscsiName")));
        ethPort.setOwnerName(ToolUtils.jsonToStr(vjson.get("ownerName")));
        ethPort.setLastMonitorTime(ToolUtils.jsonToLon(vjson.get(LAST_MONITOR_TIME), 0L));
        ethPort.setMgmtIp(ToolUtils.jsonToStr(vjson.get("mgmtIp")));
        ethPort.setConfirmStatus(ToolUtils.jsonToStr(vjson.get("confirmStatus")));
        ethPort.setId(ToolUtils.jsonToStr(vjson.get(ID_FILED)));
        ethPort.setLastModified(ToolUtils.jsonToLon(vjson.get("last_Modified"), 0L));
        ethPort.setConnectStatus(ToolUtils.jsonToStr(vjson.get("connectStatus")));
        ethPort.setClassId(ToolUtils.jsonToInt(vjson.get("classId"), 0));
        ethPort.setDataStatus(ToolUtils.jsonToStr(vjson.get(DATASTATUS_FILED)));
        ethPort.setMaxSpeed(ToolUtils.jsonToInt(vjson.get("maxSpeed"), 0));
        ethPort.setResId(ToolUtils.jsonToStr(vjson.get(RES_ID)));
        ethPort.setLocal(ToolUtils.jsonToBoo(vjson.get("isLocal")));
        ethPort.setPortType(ToolUtils.jsonToStr(vjson.get(PORTTYPE)));
        ethPort.setClassName(ToolUtils.jsonToStr(vjson.get("className")));
        ethPort.setNumberOfInitiators(ToolUtils.jsonToInt(vjson.get("numberOfInitiators"), 0));
        ethPort.setBondId(ToolUtils.jsonToStr(vjson.get("bondId")));
        ethPort.setRegionId(ToolUtils.jsonToStr(vjson.get("regionId")));
        ethPort.setName(ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
        ethPort.setLocation(ToolUtils.jsonToStr(vjson.get(LOCATION)));
        ethPort.setNativeId(ToolUtils.jsonToStr(vjson.get(NATIVE_ID)));
        ethPort.setDataSource(ToolUtils.jsonToStr(vjson.get("dataSource")));
        ethPort.setIpv6Mask(ToolUtils.jsonToStr(vjson.get("ipv6Mask")));
        ethPort.setStatus(ToolUtils.jsonToStr(vjson.get(STATUS)));
        ethPort.setSpeed(ToolUtils.jsonToInt(vjson.get(SPEED), 0));
        ethPort.setWwn(ToolUtils.jsonToStr(vjson.get("wwn")));
        ethPort.setSfpStatus(ToolUtils.jsonToStr(vjson.get("sfpStatus")));
        return ethPort;
    }

    @Override
    public Map<String, Object> getVolume(String volumeId) throws DmeException {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", HttpStatus.OK.value());
        resMap.put("msg", "get volume success!");
        resMap.put("volume", volumeId);
        String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(String.valueOf(code), "get volume error!");
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonObject element = jsonObject.get("volume").getAsJsonObject();
                Volume volume = new Volume();
                volume.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
                volume.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                volume.setStatus(ToolUtils.jsonToStr(element.get(STATUS)));
                volume.setAttached(ToolUtils.jsonToBoo(element.get("attached")));
                volume.setAlloctype(ToolUtils.jsonToStr(element.get("alloctype")));
                volume.setServiceLevelName(ToolUtils.jsonToStr(element.get("service_level_name")));
                volume.setStorageId(ToolUtils.jsonToStr(element.get(STORAGE_ID)));
                volume.setPoolRawId(ToolUtils.jsonToStr(element.get(POOL_RAW_ID)));
                volume.setCapacityUsage(ToolUtils.jsonToStr(element.get("capacity_usage")));
                volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protectionStatus")));
                JsonArray jsonArray = element.get("attachments").getAsJsonArray();
                volumeAttachments(jsonArray, volume);
                resMap.put("data", volume);
            }
        } catch (DmeException e) {
            LOG.error("get volume error!");
            throw new DmeException(CODE_503, e.getMessage());
        }
        return resMap;
    }

    @Override
    public List<StoragePort> getStoragePort(String storageDeviceId, String portType) throws DmeException {
        String replace = storageDeviceId.replace(SPLIT_CHAR, "");
        if (StringUtils.isEmpty(storageDeviceId) || StringUtils.isEmpty(portType)) {
            throw new DmeException(CODE_403, "request storageDeviceId or portType is null!");
        }
        List<StoragePort> storagePorts = new ArrayList<>();
        String className = "SYS_StoragePort";
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className) + CONDITION;
        String params = ToolUtils.getRequsetParams(STORAGE_DEVICE_ID, replace);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "get storage port failed!");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            List<String> resids = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = ToolUtils.jsonToStr(element.get(ID_FILED));
                resids.add(id);
            }
            List<StoragePort> storagePortperf = listStoragePortPerformance(resids);
            Map<String, StoragePort> storagePortMap = new HashMap<>();
            for (StoragePort storagePort : storagePortperf) {
                storagePortMap.put(storagePort.getId(), storagePort);
            }

            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                StoragePort storagePort = parseStoragePortInfo(storagePortMap, element);
                String type = ToolUtils.jsonToStr(element.get(PORTTYPE));
                if (portType.equals(type)) {
                    storagePorts.add(storagePort);
                } else if ("ALL".equals(portType)) {
                    storagePorts.add(storagePort);
                }
            }
        } catch (DmeException e) {
            LOG.error("list storage port error!");
            throw new DmeException(CODE_503, e.getMessage());
        }
        return storagePorts;
    }

    private StoragePort parseStoragePortInfo(Map<String, StoragePort> storagePortMap, JsonObject element) {
        StoragePort storagePort = new StoragePort();
        storagePort.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
        storagePort.setNativeId(ToolUtils.jsonToStr(element.get(NATIVE_ID)));
        storagePort.setLast_Modified(ToolUtils.jsonToLon(element.get("last_Modified"), 0L));
        storagePort.setLastMonitorTime(ToolUtils.jsonToLon(element.get(LAST_MONITOR_TIME), 0L));
        storagePort.setDataStatus(ToolUtils.jsonToStr(element.get(DATASTATUS_FILED)));
        storagePort.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
        storagePort.setPortId(ToolUtils.jsonToStr(element.get("portId")));
        storagePort.setPortName(ToolUtils.jsonToStr(element.get("portName")));
        storagePort.setLocation(ToolUtils.jsonToStr(element.get(LOCATION)));
        storagePort.setConnectStatus(ToolUtils.jsonToStr(element.get("connectStatus")));
        storagePort.setStatus(ToolUtils.jsonToStr(element.get(STATUS)));
        String type = ToolUtils.jsonToStr(element.get(PORTTYPE));
        storagePort.setPortType(type);
        storagePort.setMac(ToolUtils.jsonToStr(element.get("mac")));
        storagePort.setMgmtIp(ToolUtils.jsonToStr(element.get("mgmtIp")));
        storagePort.setIpv4Mask(ToolUtils.jsonToStr(element.get("ipv4Mask")));
        storagePort.setMgmtIpv6(ToolUtils.jsonToStr(element.get("mgmtIpv6")));
        storagePort.setIpv6Mask(ToolUtils.jsonToStr(element.get("ipv6Mask")));
        storagePort.setIscsiName(ToolUtils.jsonToStr(element.get("iscsiName")));
        storagePort.setBondId(ToolUtils.jsonToStr(element.get("bondId")));
        storagePort.setBondName(ToolUtils.jsonToStr(element.get("bondName")));
        storagePort.setWwn(ToolUtils.jsonToStr(element.get("wwn")));
        storagePort.setSfpStatus(ToolUtils.jsonToStr(element.get("sfpStatus")));
        storagePort.setLogicalType(ToolUtils.jsonToStr(element.get(LOGICAL_TYPE)));
        storagePort.setNumOfInitiators(ToolUtils.jsonToInt(element.get("numOfInitiators")));
        storagePort.setSpeed(ToolUtils.jsonToInt(element.get(SPEED)));
        storagePort.setMaxSpeed(ToolUtils.jsonToInt(element.get("maxSpeed")));
        storagePort.setStorageDeviceId(ToolUtils.jsonToStr(element.get(STORAGE_DEVICE_ID)));
        performanceParse(storagePortMap, storagePort);
        return storagePort;
    }

    private void performanceParse(Map<String, StoragePort> storagePortMap, StoragePort storagePort) {
        if (storagePortMap.get(storagePort.getId()) != null) {
            storagePort.setLantency(storagePortMap.get(storagePort.getId()).getLantency());
            storagePort.setBandwith(storagePortMap.get(storagePort.getId()).getBandwith());
            storagePort.setIops(storagePortMap.get(storagePort.getId()).getIops());
            storagePort.setUseage(storagePortMap.get(storagePort.getId()).getUseage());
        }
    }

    @Override
    public List<FailoverGroup> getFailoverGroups(String storageId) throws DmeException {
        if (StringUtils.isEmpty(storageId)) {
            throw new DmeException(CODE_403, "request param storage_id error!");
        }
        List<FailoverGroup> failoverGroups = new ArrayList<>();
        String url = DmeConstants.API_FAILOVERGROUPS + storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list failover group failed!");
            }
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("failover_groups").getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                FailoverGroup failoverGroup = new FailoverGroup();
                failoverGroup.setFailoverGroupType(ToolUtils.jsonToStr(element.get("failover_group_type")));
                failoverGroup.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
                failoverGroup.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                failoverGroups.add(failoverGroup);
            }
        } catch (DmeException e) {
            LOG.error("list failover group error!");
            throw new DmeException(CODE_503, e.getMessage());
        }
        return failoverGroups;
    }

    @Override
    public FileSystemDetail getFileSystemDetail(String fileSystemId) throws DmeException {
        if (StringUtils.isEmpty(fileSystemId)) {
            throw new DmeException(CODE_403, "param error!");
        }
        FileSystemDetail fileSystemDetail = new FileSystemDetail();
        String url = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace("{file_system_id}", fileSystemId);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "get file system detail error!");
            }

            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            fileSystemDetail.setId(ToolUtils.jsonToStr(jsonObject.get(ID_FILED)));
            fileSystemDetail.setName(ToolUtils.jsonToStr(jsonObject.get(NAME_FIELD)));
            CapacityAutonegotiation capacityAutonegotiation = new CapacityAutonegotiation();
            JsonObject json = jsonObject.get("capacity_auto_negotiation").getAsJsonObject();
            capacityAutonegotiation.setAutoSizeEnable(ToolUtils.jsonToBoo(json.get("auto_size_enable")));
            fileSystemDetail.setCapacityAutonegotiation(capacityAutonegotiation);
            JsonObject tuning = jsonObject.get("tuning").getAsJsonObject();
            FileSystemTurning fileSystemTurning = new FileSystemTurning();
            fileSystemTurning.setAllocationType(ToolUtils.jsonToStr(jsonObject.get("alloc_type")));
            fileSystemTurning.setCompressionEnabled(ToolUtils.jsonToBoo(tuning.get("compression_enabled")));
            fileSystemTurning.setDeduplicationEnabled(ToolUtils.jsonToBoo(tuning.get("deduplication_enabled")));
            SmartQos smartQos = new SmartQos();
            JsonObject jsonSmartQos = tuning.get("smart_qos").getAsJsonObject();
            String smartQosStr = gson.toJson(jsonSmartQos);
            if (!StringUtils.isEmpty(smartQosStr)) {
                JsonObject qosPolicy = new JsonParser().parse(smartQosStr).getAsJsonObject();
                smartQos.setMaxbandwidth(ToolUtils.jsonToInt(qosPolicy.get("max_bandwidth")));
                smartQos.setMaxiops(ToolUtils.jsonToInt(qosPolicy.get("max_iops")));
                smartQos.setLatency(ToolUtils.jsonToInt(qosPolicy.get("latency")));
                smartQos.setMinbandwidth(ToolUtils.jsonToInt(qosPolicy.get("min_bandwidth")));
                smartQos.setMiniops(ToolUtils.jsonToInt(qosPolicy.get("min_iops")));
            }
            fileSystemTurning.setSmartQos(smartQos);
            fileSystemDetail.setFileSystemTurning(fileSystemTurning);
        } catch (DmeException e) {
            LOG.error("get file system detail error");
            throw new DmeException(CODE_503, e.getMessage());
        }
        return fileSystemDetail;
    }

    private String getDataStoreOnVolume(String volumeId) throws DmeSqlException {
        return dmeVmwareRalationDao.getVmfsNameByVolumeId(volumeId);
    }

    private String getDiskType(String storageDeviceId, String diskPoolId) throws DmeException {
        String replace = storageDeviceId.replace(SPLIT_CHAR, "");
        String result = "";
        String className = "SYS_StorageDisk";
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className) + CONDITION;
        String params = ToolUtils.getRequsetParams(STORAGE_DEVICE_ID, replace);
        ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
        int code = responseEntity.getStatusCodeValue();
        if (code == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String diskId = ToolUtils.jsonToStr(element.get("diskId"));
                if (diskPoolId.equals(diskId)) {
                    result = ToolUtils.jsonToStr(element.get("physicalType"));
                    break;
                }
            }
        }
        return result;
    }

    private void volumeAttachments(JsonArray array, Volume volume) {
        if (array != null && array.size() > 0) {
            List<String> hostIds = new ArrayList<>();
            List<String> hostGroupIds = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject json = element.getAsJsonObject();
                String hostId = ToolUtils.jsonToStr(json.get("host_id"));
                String hostGroupId = ToolUtils.jsonToStr(json.get("attached_host_group"));
                hostIds.add(hostId);
                hostGroupIds.add(hostGroupId);
            }
            volume.setHostIds(hostIds);
            volume.setHostGroupIds(hostGroupIds);
        }
    }

    @Override
    public List<Storage> listStoragePerformance(List<String> storageIds) throws DmeException {
        List<Storage> relists = new ArrayList<>();
        try {
            if (storageIds != null && storageIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put(OBJ_IDS, storageIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(
                    DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDEVICE, params);
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    relists = new ArrayList<>();
                    for (String storageId : storageIds) {
                        JsonObject statisticObject = dataJson.getAsJsonObject(storageId);
                        if (statisticObject != null) {
                            Storage storage = new Storage();
                            storage.setId(storageId);
                            storage.setMaxBandwidth(ToolUtils.jsonToDou(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_BANDWIDTH)));
                            storage.setMaxCpuUtilization(ToolUtils.jsonToDou(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_CPUUSAGE)));
                            storage.setMaxLatency(ToolUtils.jsonToDou(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_RESPONSETIME)));
                            storage.setMaxOps(ToolUtils.jsonToDou(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_THROUGHPUT)));
                            relists.add(storage);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list Storage performance error:{}", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public List<StoragePool> listStoragePoolPerformance(List<String> storagePoolIds) throws DmeException {
        List<StoragePool> relists = new ArrayList<>();
        try {
            if (storagePoolIds != null && storagePoolIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put(OBJ_IDS, storagePoolIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(
                    DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPOOL, params);
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    relists = new ArrayList<>();
                    for (String storagePoolId : storagePoolIds) {
                        JsonObject statisticObject = dataJson.getAsJsonObject(storagePoolId);
                        if (statisticObject != null) {
                            StoragePool sp = new StoragePool();
                            sp.setId(storagePoolId);
                            sp.setMaxIops(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_THROUGHPUT)));
                            sp.setMaxBandwidth(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_BANDWIDTH)));
                            sp.setMaxLatency(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_RESPONSETIME)));
                            relists.add(sp);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list StoragePool performance error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public List<StorageControllers> listStorageControllerPerformance(List<String> storageControllerIds)
        throws DmeException {
        List<StorageControllers> relists = new ArrayList<>();
        try {
            if (storageControllerIds != null && storageControllerIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put(OBJ_IDS, storageControllerIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(
                    DmeIndicatorConstants.RESOURCE_TYPE_NAME_CONTROLLER, params);
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    relists = new ArrayList<>();
                    for (String storagecontrollerid : storageControllerIds) {
                        JsonObject statisticObject = dataJson.getAsJsonObject(storagecontrollerid);
                        if (statisticObject != null) {
                            StorageControllers sp = new StorageControllers();
                            sp.setId(storagecontrollerid);
                            sp.setIops(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_THROUGHPUT)));
                            sp.setBandwith(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_BANDWIDTH)));
                            sp.setLantency(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_RESPONSETIME)));
                            sp.setCpuUsage(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_CPUUSAGE)));
                            relists.add(sp);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list StorageController performance error:{}", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public List<StorageDisk> listStorageDiskPerformance(List<String> storageDiskIds) throws DmeException {
        List<StorageDisk> relists = new ArrayList<>();
        try {
            if (storageDiskIds != null && storageDiskIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put(OBJ_IDS, storageDiskIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(
                    DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDISK, params);
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    relists = new ArrayList<>();
                    for (String storageDiskId : storageDiskIds) {
                        JsonObject statisticObject = dataJson.getAsJsonObject(storageDiskId);
                        if (statisticObject != null) {
                            StorageDisk sp = new StorageDisk();
                            sp.setId(storageDiskId);
                            sp.setIops(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_READTHROUGHPUT)));
                            sp.setBandwith(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_BANDWIDTH)));
                            sp.setLantency(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_RESPONSETIME)));
                            sp.setUseage(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_UTILITY)));
                            relists.add(sp);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list StorageDisk performance error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public List<StoragePort> listStoragePortPerformance(List<String> storagePortIds) throws DmeException {
        List<StoragePort> relists = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(OBJ_IDS, storagePortIds);
            Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(
                DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPORT, params);
            if (remap != null && remap.size() > 0) {
                JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                relists = new ArrayList<>();
                for (String storagePortId : storagePortIds) {
                    JsonObject statisticObject = dataJson.getAsJsonObject(storagePortId);
                    if (statisticObject != null) {
                        StoragePort sp = new StoragePort();
                        sp.setId(storagePortId);
                        sp.setIops(ToolUtils.jsonToFloat(
                            statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_THROUGHPUT)));
                        sp.setBandwith(ToolUtils.jsonToFloat(
                            statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_BANDWIDTH)));
                        sp.setLantency(ToolUtils.jsonToFloat(
                            statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_RESPONSETIME)));
                        sp.setUseage(ToolUtils.jsonToFloat(
                            statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_UTILITY)));
                        relists.add(sp);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list StoragePort performance error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public List<Volume> listVolumesPerformance(List<String> volumeWwns) throws DmeException {
        List<Volume> relists = new ArrayList<>();
        try {
            if (volumeWwns != null && volumeWwns.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put(OBJ_IDS, volumeWwns);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(
                    DmeIndicatorConstants.RESOURCE_TYPE_NAME_LUN, params);
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    relists = new ArrayList<>();
                    for (String wwnid : volumeWwns) {
                        JsonObject statisticObject = dataJson.getAsJsonObject(wwnid);
                        if (statisticObject != null) {
                            Volume sp = new Volume();
                            sp.setWwn(wwnid);
                            sp.setIops(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT)));
                            sp.setBandwith(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH)));
                            sp.setLantency(ToolUtils.jsonToFloat(
                                statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME)));
                            relists.add(sp);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list volume performance error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public Boolean queryVolumeByName(String name) throws DmeException {
        boolean isExist = true;
        String url = DmeConstants.DME_VOLUME_BASE_URL + "?name=" + name;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            if (ToolUtils.jsonToInt(jsonObject.get("count")) != 0) {
                isExist = false;
            }
        }
        return isExist;
    }

    @Override
    public Boolean queryFsByName(String name, String storageId) throws DmeException {
        boolean isExist = true;
        List<FileSystem> fileSystems = getFileSystems(storageId);
        for (FileSystem fileSystem : fileSystems) {
            if (name.equals(fileSystem.getName())) {
                isExist = false;
                break;
            }
        }
        return isExist;
    }

    @Override
    public Boolean queryShareByName(String name, String storageId) throws DmeException {
        boolean isExist = true;
        List<NfsShares> nfsShares = getNfsShares(storageId);
        for (NfsShares nfsShare : nfsShares) {
            if (name.equals(nfsShare.getName())) {
                isExist = false;
                break;
            }
        }
        return isExist;
    }

    public Map<String, Object> getDjTierContainsStoragePool() throws DmeException {
        Map<String, Object> map = new HashMap<>();
        String url = DmeConstants.LIST_RELATION_URL.replace("{relationName}", "M_DjTierContainsStoragePool");
        try {
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonArray objList = vjson.getAsJsonArray(OBJ_LIST);
                for (int index = 0; index < objList.size(); index++) {
                    JsonObject objJson = objList.get(index).getAsJsonObject();
                    if (!objJson.isJsonNull()) {
                        String sourceInstanceId = ToolUtils.jsonToStr(objJson.get("source_Instance_Id"));
                        String targetInstanceId = ToolUtils.jsonToStr(objJson.get("target_Instance_Id"));
                        if (map.get(targetInstanceId) != null) {
                            List<String> siIds = (List<String>) map.get(targetInstanceId);
                            siIds.add(sourceInstanceId);
                        } else {
                            List<String> siIds = new ArrayList<>();
                            siIds.add(sourceInstanceId);
                            map.put(targetInstanceId, siIds);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("{},error:{}", url, e.getMessage());
            throw new DmeException(e.getMessage());
        }
        return map;
    }

    private Map<String, Object> getDjtier() throws DmeException {
        Map<String, Object> map = new HashMap<>();
        String getDjtierUrl = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST + "?pageSize=1000", "SYS_DjTier");
        try {
            ResponseEntity responseEntity = dmeAccessService.access(getDjtierUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonArray objList = vjson.getAsJsonArray(OBJ_LIST);
                if (objList != null && objList.size() > 0) {
                    for (int index = 0; index < objList.size(); index++) {
                        JsonObject objJson = objList.get(index).getAsJsonObject();
                        if (!objJson.isJsonNull()) {
                            String resId = ToolUtils.jsonToStr(objJson.get(RES_ID));
                            String name = ToolUtils.jsonToStr(objJson.get(NAME_FIELD));
                            map.put(resId, name);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("{} access error:{}", getDjtierUrl, e.getMessage());
            throw new DmeException(e.getMessage());
        }
        return map;
    }

    private Map<String, Object> getDjTierOfStoragePool() {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> djtierMap = getDjtier();
            Map<String, Object> djTierStoragePoolMap = getDjTierContainsStoragePool();
            Set<String> sps = djTierStoragePoolMap.keySet();
            for (String spkey : sps) {
                if (djTierStoragePoolMap.get(spkey) != null) {
                    List<String> djIds = (List<String>) djTierStoragePoolMap.get(spkey);
                    if (djIds != null && djIds.size() > 0) {
                        List<String> diNames = new ArrayList<>();
                        for (String djId : djIds) {
                            diNames.add(ToolUtils.getStr(djtierMap.get(djId)));
                        }
                        map.put(spkey, diNames);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("getDjTierOfStoragePool error:{}", e.getMessage());
        }
        return map;
    }

    @Override
    public String getStorageByPoolRawId(String poolRawId) throws DmeException {
        String className = "SYS_StoragePool";
        String poolName = "";
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className) + CONDITION;
        String params = ToolUtils.getRequsetParams(POOL_ID, poolRawId);
        ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                poolName = ToolUtils.jsonToStr(element.get(NAME_FIELD));
                break;
            }
        }
        return poolName;
    }

    private DiskPool getDiskPoolByPoolId(String poolId) throws DmeException {
        String className = "SYS_DiskPool";
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className) + CONDITION;
        String params = ToolUtils.getRequsetParams(POOL_ID, poolId);
        DiskPool retdiskPool = new DiskPool();
        ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                DiskPool diskPool = new DiskPool();
                diskPool.setId(ToolUtils.jsonToStr(element.get(ID_FILED)));
                diskPool.setNativeId(ToolUtils.jsonToStr(element.get(NATIVE_ID)));
                diskPool.setLastModified(ToolUtils.jsonToStr(element.get("lastModified")));
                diskPool.setLastMonitorTime(ToolUtils.jsonToStr(element.get(LAST_MONITOR_TIME)));
                diskPool.setDataStatus(ToolUtils.jsonToStr(element.get(DATASTATUS_FILED)));
                diskPool.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                diskPool.setStatus(ToolUtils.jsonToStr(element.get(STATUS)));
                diskPool.setRunningStatus(ToolUtils.jsonToStr(element.get("runningStatus")));
                diskPool.setEncryptDiskType(ToolUtils.jsonToStr(element.get("encryptDiskType")));
                double totalCapacity = ToolUtils.jsonToDou(element.get("totalCapacity"));
                diskPool.setTotalCapacity(totalCapacity);
                double usedCapacity = ToolUtils.jsonToDou(element.get(USED_CAPACITY));
                diskPool.setUsedCapacity(usedCapacity);
                diskPool.setFreeCapacity(ToolUtils.jsonToDou(element.get("freeCapacity")));
                diskPool.setSpareCapacity(ToolUtils.jsonToDou(element.get("spareCapacity")));
                diskPool.setUsedSpareCapacity(ToolUtils.jsonToDou(element.get("usedSpareCapacity")));
                diskPool.setPoolId(ToolUtils.jsonToStr(element.get(POOL_ID)));
                DecimalFormat df = new DecimalFormat("#.00");
                double usageCapacityRate = 0.0;
                if (totalCapacity != 0) {
                    usageCapacityRate = Double.valueOf(df.format(usedCapacity / totalCapacity)) * CONSTANT_100;
                }
                diskPool.setUsageRate(usageCapacityRate);
                diskPool.setStorageDeviceId(ToolUtils.jsonToStr(element.get(STORAGE_DEVICE_ID)));
                retdiskPool = diskPool;
            }
        }
        return retdiskPool;
    }

    /**
     * 判断数据存储中是否有注册的虚拟机，有则返回true，没有返回false
     *
     * @param objectid 数据存储的objectid
     * @return 是否存在vm
     */
    public boolean hasVmOnDatastore(String objectid) {
        return vcsdkUtils.hasVmOnDatastore(objectid);
    }
}
