package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.CapabilitiesQos;
import com.huawei.dmestore.model.CapabilitiesSmarttier;
import com.huawei.dmestore.model.DmeDatasetsQueryResponse;
import com.huawei.dmestore.model.QosParam;
import com.huawei.dmestore.model.RelationInstance;
import com.huawei.dmestore.model.SimpleCapabilities;
import com.huawei.dmestore.model.SimpleServiceLevel;
import com.huawei.dmestore.model.StoragePool;
import com.huawei.dmestore.model.Volume;
import com.huawei.dmestore.utils.CipherUtils;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.autosdk.SessionHelper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.vmware.cis.tagging.TagModel;
import com.vmware.pbm.PbmProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ServiceLevelService
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public class ServiceLevelServiceImpl implements ServiceLevelService {
    private static final Logger log = LoggerFactory.getLogger(ServiceLevelServiceImpl.class);

    private static final String ID_FIELD = "id";

    private static final String NAME_FIELD = "name";

    private static final String CODE_503 = "503";

    private DmeAccessService dmeAccessService;

    private DmeRelationInstanceService dmeRelationInstanceService;

    private DmeStorageService dmeStorageService;

    private VCenterInfoService vcenterinfoservice;

    private VCSDKUtils vcsdkUtils;

    private Gson gson = new Gson();

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public DmeRelationInstanceService getDmeRelationInstanceService() {
        return dmeRelationInstanceService;
    }

    public void setDmeRelationInstanceService(DmeRelationInstanceService dmeRelationInstanceService) {
        this.dmeRelationInstanceService = dmeRelationInstanceService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public VCenterInfoService getVcenterinfoservice() {
        return vcenterinfoservice;
    }

    public void setVcenterinfoservice(VCenterInfoService vcenterinfoservice) {
        this.vcenterinfoservice = vcenterinfoservice;
    }

    @Override
    public List<SimpleServiceLevel> listServiceLevel(Map<String, Object> params) throws DmeException {
        ResponseEntity responseEntity;
        List<SimpleServiceLevel> slis;
        try {
            responseEntity = dmeAccessService.access(DmeConstants.LIST_SERVICE_LEVEL_URL, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (HttpStatus.OK.value() != code) {
                throw new DmeException(CODE_503, "list serviceLevel response error!");
            }
            Object object = responseEntity.getBody();
            slis = convertBean(object);
        } catch (DmeException e) {
            log.error("list serviceLevel error", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return slis;
    }

    @Override
    public void updateVmwarePolicy() throws DmeException {
        try {
            VCenterInfo vcenterInfo = vcenterinfoservice.getVcenterInfo();
            if (null != vcenterInfo) {
                SessionHelper sessionHelper = new SessionHelper();
                try {
                    sessionHelper.login(vcenterInfo.getHostIp(), String.valueOf(vcenterInfo.getHostPort()),
                        vcenterInfo.getUserName(), CipherUtils.decryptString(vcenterInfo.getPassword()));
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
                String categoryid = vcsdkUtils.getCategoryId(sessionHelper);
                List<TagModel> tagModels = vcsdkUtils.getAllTagsByCategoryId(categoryid, sessionHelper);
                List<PbmProfile> pbmProfiles = vcsdkUtils.getAllSelfPolicyInallcontext();
                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.LIST_SERVICE_LEVEL_URL,
                    HttpMethod.GET, null);
                Object object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
                List<TagModel> alreadyhasList = new ArrayList<>();
                List<PbmProfile> alreadyhasPolicyList = new ArrayList<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject object1 = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
                    String name = object1.get(NAME_FIELD).getAsString();
                    boolean alreadyhas = false;
                    for (TagModel tagModel : tagModels) {
                        if (tagModel.getName().equalsIgnoreCase(name)) {
                            alreadyhasList.add(tagModel);
                            alreadyhas = true;
                            break;
                        }
                    }
                    boolean alreadyhasPolicy = false;
                    for (PbmProfile profile : pbmProfiles) {
                        if (profile.getName().equalsIgnoreCase(name)) {
                            alreadyhasPolicyList.add(profile);
                            alreadyhasPolicy = true;
                            break;
                        }
                    }
                    if (!alreadyhas) {
                        vcsdkUtils.createTag(name, sessionHelper);
                    }
                    if (!alreadyhasPolicy) {
                        vcsdkUtils.createPbmProfileInAllContext(VCSDKUtils.CATEGORY_NAME, name);
                    }
                }
                tagModels.removeAll(alreadyhasList);
                pbmProfiles.removeAll(alreadyhasPolicyList);
                vcsdkUtils.removePbmProfileInAllContext(pbmProfiles);
                vcsdkUtils.removeAllTags(tagModels, sessionHelper);
            } else {
                throw new VcenterException("There is no VCenter information in the database");
            }
        } catch (DmeException e) {
            log.error("list serviceLevel error", e);
            throw new DmeException(CODE_503, "update service level error" + e.getMessage());
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    /**
     * convert the api responseBody to SimpleServiceLevel Bean list
     *
     * @param object
     * @return java.util.List
     **/
    private List<SimpleServiceLevel> convertBean(Object object) {
        List<SimpleServiceLevel> ssls = new ArrayList<>();
        JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            SimpleServiceLevel ssl = new SimpleServiceLevel();
            JsonObject object1 = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
            String id = ToolUtils.jsonToOriginalStr(object1.get(ID_FIELD));
            String name = ToolUtils.jsonToOriginalStr(object1.get(NAME_FIELD));
            String descriptionn = ToolUtils.jsonToStr(object1.get("description"));
            String type = ToolUtils.jsonToOriginalStr(object1.get("type"));
            String protocol = ToolUtils.jsonToOriginalStr(object1.get("protocol"));
            double totalCapacity = ToolUtils.jsonToDou(object1.get("total_capacity"));
            double usedCapcity = ToolUtils.jsonToDou(object1.get("used_capacity"));
            double freeCapacity = ToolUtils.jsonToDou(object1.get("free_capacity"));
            ssl.setId(id);
            ssl.setName(name);
            ssl.setDescription(descriptionn);
            ssl.setType(type);
            ssl.setProtocol(protocol);
            ssl.setTotalCapacity(totalCapacity);
            ssl.setFreeCapacity(freeCapacity);
            ssl.setUsedCapacity(usedCapcity);
            JsonElement capObj = object1.get("capabilities");
            if (!ToolUtils.jsonIsNull(capObj)) {
                SimpleCapabilities scb = new SimpleCapabilities();
                JsonObject capJsonObj = capObj.getAsJsonObject();
                String resourceType = ToolUtils.jsonToOriginalStr(capJsonObj.get("resource_type"));
                boolean compression = ToolUtils.jsonToBoo(capJsonObj.get("compression"));
                scb.setResourceType(resourceType);
                scb.setCompression(compression);
                JsonElement smarttierObj = capJsonObj.get("smarttier");
                if (!ToolUtils.jsonIsNull(smarttierObj)) {
                    CapabilitiesSmarttier cbs = parseCapabilitiesSmarttier(smarttierObj);
                    scb.setSmarttier(cbs);
                }
                JsonElement qosObject = capJsonObj.get("qos");
                if (!ToolUtils.jsonIsNull(qosObject)) {
                    CapabilitiesQos cq = qosParse(qosObject);
                    scb.setQos(cq);
                }
                ssl.setCapabilities(scb);
            }
            ssls.add(ssl);
        }
        return ssls;
    }

    private CapabilitiesSmarttier parseCapabilitiesSmarttier(JsonElement smarttierObj) {
        CapabilitiesSmarttier cbs = new CapabilitiesSmarttier();
        JsonObject smartJsonObj = smarttierObj.getAsJsonObject();
        int policy = ToolUtils.jsonToInt(smartJsonObj.get("policy"), null);
        boolean enabled = ToolUtils.jsonToBoo(smartJsonObj.get("enabled"));
        cbs.setEnabled(enabled);
        cbs.setPolicy(policy);
        return cbs;
    }

    private CapabilitiesQos qosParse(JsonElement qosObject) {
        CapabilitiesQos cq = new CapabilitiesQos();
        JsonObject qosJsonObj = qosObject.getAsJsonObject();
        boolean enabled = ToolUtils.jsonToBoo(qosJsonObj.get("enabled"));
        cq.setEnabled(enabled);
        JsonElement qosParamObj = qosJsonObj.get("qos_param");
        if (!ToolUtils.jsonIsNull(qosParamObj)) {
            QosParam qp = new QosParam();
            JsonObject qosParamJsonObj = qosParamObj.getAsJsonObject();
            String latencyUnit = ToolUtils.jsonToStr(qosParamJsonObj.get("latencyUnit"));
            int latnecy = ToolUtils.jsonToInt(qosParamJsonObj.get("latency"));
            int minBandWidth = ToolUtils.jsonToInt(qosParamJsonObj.get("minBandWidth"));
            int minIops = ToolUtils.jsonToInt(qosParamJsonObj.get("minIOPS"));
            int maxBandWidth = ToolUtils.jsonToInt(qosParamJsonObj.get("maxBandWidth"));
            int maxIops = ToolUtils.jsonToInt(qosParamJsonObj.get("maxIOPS"));
            qp.setLatency(latnecy);
            qp.setLatencyUnit(latencyUnit);
            qp.setMinBandWidth(minBandWidth);
            qp.setMinIOPS(minIops);
            qp.setMaxBandWidth(maxBandWidth);
            qp.setMaxIOPS(maxIops);
            cq.setQosParam(qp);
        }
        return cq;
    }

    /**
     * 扫描服务等级 发现服务等级下的存储池,磁盘,(存储端口)
     *
     * @param serivceLevelId
     * @return java.util.List
     * @throws DmeException
     **/
    @Override
    public List<StoragePool> getStoragePoolInfosByServiceLevelId(String serivceLevelId) throws DmeException {
        List<StoragePool> storagePools = new ArrayList<>();
        String id = serivceLevelId;

        // servicLevelId对应的serviceLevelInstanceId
        Map<String, Map<String, Object>> serviceLevelMap = dmeRelationInstanceService.getServiceLevelInstance();
        if (null != serviceLevelMap && serviceLevelMap.size() > 0) {
            String serviceLevelInstanceId = serviceLevelMap.get(serivceLevelId).get("resId").toString();
            if (!StringUtils.isEmpty(serviceLevelInstanceId)) {
                id = serviceLevelInstanceId;
            }
        }

        // 1 获取serviceLevelId下的StoragePool实例集合
        List<String> storagePoolInstanceIds = getStoragePoolIdsByServiceLevelId(id);

        // 2 通过storagePoolInstanceId获取storagePoolId和storageDeviceId信息
        List<StoragePool> sps = new ArrayList<>();
        if (null != storagePoolInstanceIds && storagePoolInstanceIds.size() > 0) {
            for (String instanceId : storagePoolInstanceIds) {
                Object object = dmeRelationInstanceService.queryInstanceByInstanceNameId("SYS_StoragePool", instanceId);
                StoragePool sp = convertInstanceToStoragePool(object);
                sps.add(sp);
            }
        }

        // 3 通过storageDeviceId和storagePoolId获取storagePool信息(这里先获取存储设备下的所有存储池,再通过storagePoolId过滤)
        if (sps.size() > 0) {
            Map<String, Set<String>> storageDevicePoolIds = new HashMap<>();
            for (StoragePool sp : sps) {
                String storageDeviceId = sp.getStorageDeviceId();
                String poolInstanceId = sp.getStorageInstanceId();
                if (null == storageDevicePoolIds.get(storageDeviceId)) {
                    Set<String> poolIds = new HashSet<>();
                    poolIds.add(poolInstanceId);
                    storageDevicePoolIds.put(storageDeviceId, poolIds);
                } else {
                    storageDevicePoolIds.get(storageDeviceId).add(poolInstanceId);
                }
            }
            for (Map.Entry<String, Set<String>> entry : storageDevicePoolIds.entrySet()) {
                String storageDevcieId = entry.getKey();
                Set<String> storagePoolIds = entry.getValue();
                List<StoragePool> storageDevicePools = getStoragePoolInfosByStorageIdStoragePoolIds(storageDevcieId,
                    new ArrayList<>(storagePoolIds));
                if (null != storageDevicePoolIds && storageDevicePoolIds.size() > 0) {
                    storagePools.addAll(storageDevicePools);
                }
            }
        }
        return storagePools;
    }

    @Override
    public List<Volume> getVolumeInfosByServiceLevelId(String serviceLevelId) throws DmeException {
        List<Volume> volumes = new ArrayList<>();
        String url = DmeConstants.QUERY_SERVICE_LEVEL_VOLUME_URL.replace("{serviceLevelId}", serviceLevelId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.OK.value()) {
            return volumes;
        }
        Object object = responseEntity.getBody();
        if (object != null) {
            Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getLunInstance();
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            JsonArray array = jsonObject.get("volumes").getAsJsonArray();

            for (JsonElement je : array) {
                JsonObject element = je.getAsJsonObject();
                Volume volume = new Volume();
                volume.setId(ToolUtils.jsonToStr(element.get(ID_FIELD)));
                volume.setName(ToolUtils.jsonToStr(element.get(NAME_FIELD)));
                volume.setStatus(ToolUtils.jsonToStr(element.get("status")));
                volume.setAttached(ToolUtils.jsonToBoo(element.get("attached")));
                volume.setAlloctype(ToolUtils.jsonToStr(element.get("alloctype")));
                volume.setServiceLevelName(ToolUtils.jsonToStr(element.get("service_level_name")));
                volume.setStorageId(ToolUtils.jsonToStr(element.get("storage_id")));
                volume.setPoolRawId(ToolUtils.jsonToStr(element.get("pool_raw_id")));
                volume.setCapacityUsage(ToolUtils.jsonToStr(element.get("capacity_usage")));
                volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protectionStatus")));
                volume.setCapacity(ToolUtils.jsonToInt(element.get("capacity"), 0));

                String wwn = ToolUtils.jsonToStr(element.get("volume_wwn"));
                String instanceId = getInstancePropertyValue(sysLunMap, wwn, "wwn");
                if (!StringUtils.isEmpty(instanceId)) {
                    volume.setInstanceId(instanceId);
                }
                volumes.add(volume);
            }
        }
        return volumes;
    }

    private String getInstancePropertyValue(Map<String, Map<String, Object>> instanceMap, String id, String name) {
        String value = "";
        if (null != instanceMap) {
            Map<String, Object> instance = instanceMap.get(id);
            if (null != instance) {
                Object obj = instance.get(name);
                if (null != obj) {
                    value = obj.toString();
                }
            }
        }
        return value;
    }

    private List<StoragePool> getStoragePoolInfosByStorageIdStoragePoolIds(String storageDeviceId,
        List<String> storagePoolIds) throws DmeException {
        List<StoragePool> sps = new ArrayList<>();

        List<StoragePool> storagePools = dmeStorageService.getStoragePools(storageDeviceId, "all");

        for (StoragePool sp : storagePools) {
            String poolId = sp.getStorageInstanceId();
            if (storagePoolIds.contains(poolId)) {
                sps.add(sp);
            }
        }
        return sps;
    }

    /**
     * 服务等级 发现服务等级下的存储池 serviceLevelId和sourceInstanceId一样?
     *
     * @param serviceLevelId serviceLevelId
     * @return java.util.List
     * @throws DmeException DmeException
     **/
    public List<String> getStoragePoolIdsByServiceLevelId(String serviceLevelId) throws DmeException {
        String relatinName = "M_DjTierContainsStoragePool";
        return getContainIdsByRelationNameLevelId(relatinName, serviceLevelId);
    }

    /**
     * 服务等级 发现服务等级下的卷实例ID
     *
     * @param serviceLevelId serviceLevelId
     * @return java.util.List
     * @throws DmeException DmeException
     **/
    public List<String> getVolumeIdsByServiceLivelId(String serviceLevelId) throws DmeException {
        String relationName = "M_DjTierContainsLun";
        return getContainIdsByRelationNameLevelId(relationName, serviceLevelId);
    }

    private List<String> getContainIdsByRelationNameLevelId(String relationName, String serviceLevelId)
        throws DmeException {
        Set<String> ids = new HashSet<>();
        List<RelationInstance> ris = dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(
            relationName, serviceLevelId);
        if (null != ris && ris.size() > 0) {
            for (RelationInstance ri : ris) {
                ids.add(ri.getTargetInstanceId());
            }
        }
        return new ArrayList<>(ids);
    }

    /**
     * 将instance转换为storagepool信息
     *
     * @param instanceObj instanceObj
     * @return com.dmeplugin.dmestore.model.StoragePool
     **/
    private StoragePool convertInstanceToStoragePool(Object instanceObj) {
        StoragePool sp = new StoragePool();
        JsonObject jsonObject = new JsonParser().parse(instanceObj.toString()).getAsJsonObject();
        String name = ToolUtils.jsonToStr(jsonObject.get(NAME_FIELD));
        String poolId = ToolUtils.jsonToStr(jsonObject.get("poolId"));
        String storageDeviceId = ToolUtils.jsonToStr(jsonObject.get("storageDeviceId"));
        String storagePoolInstanceId = ToolUtils.jsonToStr(jsonObject.get(ID_FIELD));
        sp.setName(name);
        sp.setStoragePoolId(poolId);
        sp.setStorageInstanceId(storagePoolInstanceId);
        sp.setStorageDeviceId(storageDeviceId);
        return sp;
    }

    @Override
    public DmeDatasetsQueryResponse statLunDatasetsQuery(String serviceLevelId, String interval) throws DmeException {
        String data = excute(serviceLevelId, interval, "stat-lun");
        if (!StringUtils.isEmpty(data)) {
            return gson.fromJson(data, DmeDatasetsQueryResponse.class);
        }
        return null;
    }

    @Override
    public DmeDatasetsQueryResponse statStoragePoolDatasetsQuery(String serviceLevelId, String interval)
        throws DmeException {
        String data = excute(serviceLevelId, interval, "stat-storage-pool");
        if (!StringUtils.isEmpty(data)) {
            return gson.fromJson(data, DmeDatasetsQueryResponse.class);
        }
        return null;
    }

    @Override
    public DmeDatasetsQueryResponse lunPerformanceDatasetsQuery(String serviceLevelId, String interval)
        throws DmeException {
        String data = excute(serviceLevelId, interval, "perf-stat-lun-details");
        if (!StringUtils.isEmpty(data)) {
            return gson.fromJson(data, DmeDatasetsQueryResponse.class);
        }

        return null;
    }

    @Override
    public DmeDatasetsQueryResponse storagePoolPerformanceDatasetsQuery(String serviceLevelId, String interval)
        throws DmeException {
        String data = excute(serviceLevelId, interval, "perf-stat-storage-pool-details");
        if (!StringUtils.isEmpty(data)) {
            return gson.fromJson(data, DmeDatasetsQueryResponse.class);
        }

        return null;
    }

    private String excute(String serviceLevelId, String interval, String dataSetType) throws DmeException {
        // 封装查询body
        JsonObject queryBody = getDatasetsQueryBody(serviceLevelId, interval, dataSetType);
        String url = DmeConstants.DATASETS_QUERY_URL.replace("{dataSet}", dataSetType);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, queryBody.toString());
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            return responseEntity.getBody();
        }

        return null;
    }

    private Map<String, Long> parseTime(String interval) {
        long endTime = System.currentTimeMillis();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(endTime);

        // 开始结束时间计算, 默认为最近一个月
        switch (interval) {
            case "LAST_1_DAY":
                rightNow.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case "LAST_1_WEEK":
                rightNow.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case "LAST_1_MONTH":
                rightNow.add(Calendar.MONTH, -1);
                break;
            case "LAST_3_MONTH":
                rightNow.add(Calendar.MONTH, -3);
                break;
            case "LAST_6_MONTH":
                rightNow.add(Calendar.MONTH, -6);
                break;
            case "LAST_1_YEAR":
                rightNow.add(Calendar.YEAR, -1);
                break;
            default:
                break;
        }
        long beginTime = rightNow.getTimeInMillis();
        Map<String, Long> timeMap = new HashMap();
        timeMap.put("beginTime", beginTime);
        timeMap.put("endTime", endTime);

        return timeMap;
    }

    public JsonObject getDatasetsQueryBody(String serviceLevelId, String interval, String dataSetType) {
        Map<String, Long> timeMap = parseTime(interval);
        // timeRange
        JsonObject timeRange = new JsonObject();
        timeRange.addProperty("beginTime", timeMap.get("beginTime"));
        timeRange.addProperty("endTime", timeMap.get("endTime"));
        if ("stat-lun".equals(dataSetType) || "stat-storage-pool".equals(dataSetType)) {
            timeRange.addProperty("granularity", "1d");
        } else if ("perf-stat-lun-details".equals(dataSetType) || "perf-stat-storage-pool-details".equals(
            dataSetType)) {
            timeRange.addProperty("granularity", "30m");
        }

        // filters
        JsonObject filters = new JsonObject();
        JsonArray filtersDimensions = new JsonArray();
        JsonObject filtersDimension = new JsonObject();
        filtersDimension.addProperty("field", "dimensions.lun.tierNativeId");
        JsonArray values = new JsonArray();
        values.add(new JsonPrimitive(serviceLevelId));
        filtersDimension.add("values", values);
        filtersDimensions.add(filtersDimension);
        filters.add("dimensions", filtersDimensions);

        // dimensions
        JsonArray dimensions = new JsonArray();
        JsonObject dimension1 = new JsonObject();
        dimension1.addProperty("field", "dimensions.lun.tierNativeId");
        dimension1.addProperty("index", 1);
        JsonObject dimension2 = new JsonObject();
        dimension2.addProperty("field", "timestamp");
        dimension2.addProperty("index", 2);
        dimensions.add(dimension1);
        dimensions.add(dimension2);

        // metrics
        JsonArray metrics = new JsonArray();
        JsonObject totalCapacity = new JsonObject();
        totalCapacity.addProperty("field", "metrics.totalCapacity");
        totalCapacity.addProperty("aggType", "sum");

        JsonObject allocCapacity = new JsonObject();
        allocCapacity.addProperty("field", "metrics.allocCapacity");
        allocCapacity.addProperty("aggType", "sum");

        JsonObject usedCapacity = new JsonObject();
        usedCapacity.addProperty("field", "metrics.usedCapacity");
        usedCapacity.addProperty("aggType", "sum");

        JsonObject responseTime = new JsonObject();
        responseTime.addProperty("field", "metrics.responseTime");
        responseTime.addProperty("aggType", "max");

        JsonObject throughput = new JsonObject();
        throughput.addProperty("field", "metrics.throughput");
        throughput.addProperty("aggType", "sum");

        JsonObject bandwidth = new JsonObject();
        bandwidth.addProperty("field", "metrics.bandwidth");
        bandwidth.addProperty("aggType", "sum");

        metrics.add(totalCapacity);

        if ("stat-lun".equals(dataSetType)) {
            metrics.add(allocCapacity);
        } else if ("stat-storage-pool".equals(dataSetType)) {
            metrics.add(usedCapacity);
        } else if ("perf-stat-lun-details".equals(dataSetType)) {
            metrics.add(responseTime);
            metrics.add(throughput);
            metrics.add(bandwidth);
        } else if ("perf-stat-storage-pool-details".equals(dataSetType)) {
            metrics.add(throughput);
        }

        JsonObject returnBody = new JsonObject();
        returnBody.add("timeRange", timeRange);
        returnBody.add("filters", filters);
        returnBody.add("dimensions", dimensions);
        returnBody.add("metrics", metrics);

        return returnBody;
    }

}
