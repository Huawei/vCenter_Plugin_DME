package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DmeIndicatorConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.SmartQos;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.model.TaskDetailInfo;
import com.huawei.dmestore.model.VmfsDataInfo;
import com.huawei.dmestore.model.VmfsDatastoreVolumeDetail;
import com.huawei.dmestore.utils.RestUtils;
import com.huawei.dmestore.utils.StringUtil;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import sun.rmi.runtime.Log;

/**
 * VmfsAccessServiceImpl
 *
 * @author yy
 * @since 2020-09-09
 **/
public class VmfsAccessServiceImpl implements VmfsAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(VmfsAccessServiceImpl.class);

    private static final String VOLUME_IDS = "volume_ids";

    private static final String HOST_OBJ_IDS = "hostObjIds";

    private static final String HOST_GROUP_ID = "hostGroupId";

    private static final String HOST_GROUP_ID1 = "hostgroup_id";

    private static final int DIVISOR_100 = 100;

    private static final int HTTP_SUCCESS = 2;

    private static final String DATASTORE_NAMES = "dataStoreNames";

    private static final String OBJECTID = "objectId";

    private static final String CAPACITY = "capacity";

    private static final String COUNT = "count";

    private static final String VOLUMEIDS = "volumeIds";

    private static final String HOST_NAME = "hostName";

    private static final String PORT_NAME = "port_name";

    private static final String NAME_FIELD = "name";

    private static final String ID_FIELD = "id";

    private static final String VOLUME_FIELD = "volume";

    private static final String SERVICE_LEVEL_NAME = "service_level_name";

    private static final String VOLUME_WWN = "volume_wwn";

    private static final String STORAGE_ID = "storage_id";

    private static final String HOSTID = "hostId";

    private static final String HOST = "host";

    private static final String CLUSTER_ID = "clusterId";

    private static final String TUNING = "tuning";

    private static final String SMARTQOS = "smartqos";

    private static final String MAXIOPS = "maxiops";

    private static final String MINIOPS = "miniops";

    private static final String MAXBANDWIDTH = "maxbandwidth";

    private static final String MINBANDWIDTH = "minbandwidth";

    private static final String LATENCY = "latency";

    private static final String MAX = "max";

    private static final String VOLUMENAME = "volumeName";

    private static final String VOLUME_NAME = "volume_name";

    private static final String SERVICE_LEVEL_ID = "service_level_id";

    private static final String POOL_RAW_ID = "pool_raw_id";

    private static final String HOST_ID = "host_id";

    private static final String MAPPING = "mapping";

    private static final String TASK_ID = "task_id";

    private static final String CONTROL_POLICY = "control_policy";

    private static final String ALLOCTYPE = "alloctype";

    private static final String SMARTTIER = "smartTier";

    private static final String WORKLOAD_TYPE_ID = "workload_type_id";

    private static final String DATASTORE_OBJECT_IDS = "dataStoreObjectIds";

    private static final String FIEL_SEPARATOR = "/";

    private static final int DEFAULT_LEN = 16;

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private DmeAccessService dmeAccessService;

    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    private DmeStorageService dmeStorageService;

    private TaskService taskService;

    private VCSDKUtils vcsdkUtils;

    private VCenterInfoService vcenterinfoservice;

    public void setVcenterinfoservice(VCenterInfoService vcenterinfoservice) {
        this.vcenterinfoservice = vcenterinfoservice;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    @Override
    public List<VmfsDataInfo> listVmfs() throws DmeException {
        List<VmfsDataInfo> relists = null;
        try {
            // 从关系表中取得DME卷与vcenter存储的对应关系
            List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(DmeConstants.STORE_TYPE_VMFS);
            if (dvrlist == null || dvrlist.size() == 0) {
                return relists;
            }

            // 整理数据
            Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);

            // 取得所有的存储设备
            List<Storage> storagemap = dmeStorageService.getStorages();

            // 整理数据
            Map<String, String> stoNameMap = getStorNameMap(storagemap);

            // 取得vcenter中的所有vmfs存储。
            String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(DmeConstants.STORE_TYPE_VMFS);
            if (!StringUtils.isEmpty(listStr)) {
                JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                if (jsonArray != null && jsonArray.size() > 0) {
                    relists = new ArrayList<>();
                    for (int index = 0; index < jsonArray.size(); index++) {
                        JsonObject jo = jsonArray.get(index).getAsJsonObject();

                        String vmwareStoreobjectid = ToolUtils.jsonToStr(jo.get(OBJECTID));
                        if (dvrMap != null && dvrMap.get(vmwareStoreobjectid) != null) {
                            VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                            double capacity = ToolUtils.getDouble(jo.get(CAPACITY)) / ToolUtils.GI;
                            double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                            double uncommitted = ToolUtils.getDouble(jo.get("uncommitted")) / ToolUtils.GI;

                            vmfsDataInfo.setName(ToolUtils.jsonToStr(jo.get(NAME_FIELD)));
                            vmfsDataInfo.setCapacity(capacity);
                            vmfsDataInfo.setFreeSpace(freeSpace);
                            vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);
                            vmfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get(OBJECTID)));

                            DmeVmwareRelation dvr = dvrMap.get(vmwareStoreobjectid);
                            getVmfsDetailFromDme(relists, stoNameMap, vmfsDataInfo, dvr.getVolumeId());
                        }
                    }
                }
            } else {
                LOG.info("list vmfs return empty");
            }
        } catch (DmeException e) {
            LOG.error("list vmfs error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    private void getVmfsDetailFromDme(List<VmfsDataInfo> relists, Map<String, String> stoNameMap,
        VmfsDataInfo vmfsDataInfo, String volumeId) {
        String detailedVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
        try {
            ResponseEntity responseEntity = dmeAccessService.access(detailedVolumeUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject voljson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonObject vjson2 = voljson.getAsJsonObject(VOLUME_FIELD);

                vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(vjson2.get(ID_FIELD)));
                vmfsDataInfo.setVolumeName(ToolUtils.jsonToStr(vjson2.get(NAME_FIELD)));
                vmfsDataInfo.setStatus(ToolUtils.jsonToStr(vjson2.get("status")));
                vmfsDataInfo.setServiceLevelName(ToolUtils.jsonToStr(vjson2.get(SERVICE_LEVEL_NAME)));
                vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(vjson2.get("protected")));
                vmfsDataInfo.setWwn(ToolUtils.jsonToStr(vjson2.get(VOLUME_WWN)));

                String storageId = ToolUtils.jsonToStr(vjson2.get(STORAGE_ID));
                vmfsDataInfo.setDeviceId(storageId);
                vmfsDataInfo.setDevice(stoNameMap == null ? "" : stoNameMap.get(storageId));

                parseTuning(vmfsDataInfo, vjson2);
                relists.add(vmfsDataInfo);
            }
        } catch (DmeException e) {
            LOG.error("get volume from dme error!errMsg={}", e.getMessage());
        }
    }

    private void parseTuning(VmfsDataInfo vmfsDataInfo, JsonObject vjson2) throws DmeSqlException {
        if (vjson2 != null && !ToolUtils.jsonIsNull(vjson2.get(TUNING))) {
            JsonObject tuning = vjson2.getAsJsonObject(TUNING);
            if (tuning != null && !ToolUtils.jsonIsNull(tuning.get(SMARTQOS))) {
                JsonObject smartqos = tuning.getAsJsonObject(SMARTQOS);
                boolean dorado = false;
                if (smartqos != null) {
                    vmfsDataInfo.setMaxIops(ToolUtils.jsonToInt(smartqos.get(MAXIOPS), null));
                    vmfsDataInfo.setMinIops(ToolUtils.jsonToInt(smartqos.get(MINIOPS), null));
                    vmfsDataInfo.setMaxBandwidth(ToolUtils.jsonToInt(smartqos.get(MAXBANDWIDTH), null));
                    vmfsDataInfo.setMinBandwidth(ToolUtils.jsonToInt(smartqos.get(MINBANDWIDTH), null));
                    String storageModel = getStorageModelByWwn(vmfsDataInfo.getWwn());
                    Float latency = ToolUtils.jsonToFloat(smartqos.get(LATENCY), null);
                    if (!StringUtils.isEmpty(storageModel)) {
                        dorado = ToolUtils.isDorado(storageModel);
                        if (dorado && latency != null) {
                            vmfsDataInfo.setLatency(latency / 1000);
                        }else{
                            vmfsDataInfo.setLatency(latency);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<VmfsDataInfo> listVmfsPerformance(List<String> wwns) throws DmeException {
        List<VmfsDataInfo> relists = null;
        try {
            if (wwns != null && wwns.size() > 0) {
                Map<String, Object> params = new HashMap<>(DEFAULT_LEN);
                params.put("obj_ids", wwns);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(params);
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    if (dataJson != null) {
                        relists = new ArrayList<>();
                        for (String wwn : wwns) {
                            JsonObject statisticObject = dataJson.getAsJsonObject(wwn);
                            VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                            vmfsDataInfo.setVolumeId(wwn);
                            vmfsDataInfo.setWwn(wwn);
                            vmfsDataInfo.setIops(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT, MAX), null));
                            vmfsDataInfo.setBandwidth(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH, MAX), null));
                            vmfsDataInfo.setReadResponseTime(ToolUtils.jsonToFloat(
                                ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_VOLUME_READRESPONSETIME, MAX), null));
                            vmfsDataInfo.setWriteResponseTime(ToolUtils.jsonToFloat(
                                ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITERESPONSETIME, MAX), null));
                            vmfsDataInfo.setLatency(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME, MAX), null));
                            relists.add(vmfsDataInfo);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list vmfs performance error:{}", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public void createVmfs(Map<String, Object> params) throws DmeException {
        if (params == null) {
            throw new DmeException("create vmfs params is null");
        }
        String objHostId = checkOrCreateToHostorHostGroup(params);
        if (StringUtils.isEmpty(objHostId)) {
            throw new DmeException("DME find or create host error!");
        }
        String taskId = "";
        if (params.get(DmeConstants.SERVICELEVELID) != null) {
            taskId = createVmfsByServiceLevel(params, objHostId);
        } else {
            // 非服务化的创建 taskId = createVmfsByUnServiceLevel(params, objHostId);
            taskId = createVmfsByUnServiceLevelNew(params, objHostId);
        }
        if (!StringUtils.isEmpty(taskId)) {
            // 查询看创建任务是否完成。
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            boolean isCreated = taskService.checkTaskStatus(taskIds);
            if (isCreated) {
                // DME创建完成，查找刚才创建好的卷
                String dmeHostId = null;
                String demHostGroupId = null;
                if (params.get(DmeConstants.HOST) != null) {
                    dmeHostId = objHostId;
                } else if (params.get(DmeConstants.CLUSTER) != null) {
                    demHostGroupId = objHostId;
                }
                List<Map<String, Object>> volumelist = getVolumeByName(ToolUtils.getStr(params.get(VOLUMENAME)),
                    dmeHostId, demHostGroupId, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)),
                    ToolUtils.getStr(params.get(STORAGE_ID)), ToolUtils.getStr(params.get(POOL_RAW_ID)));

                // 创建了几个卷，就创建几个VMFS，用卷的wwn去找到lun
                if (volumelist != null && volumelist.size() > 0) {
                    createOnVmware(params, volumelist);
                }
            } else {
                TaskDetailInfo taskinfo = taskService.queryTaskById(taskId);
                if (taskinfo != null) {
                    throw new DmeException(
                        "DME create vmfs volume error(task status info:" + "name:" + taskinfo.getTaskName() + ";status:"
                            + taskinfo.getStatus() + ";" + "progress:" + taskinfo.getProgress() + ";detail:"
                            + taskinfo.getDetail() + ")!");
                } else {
                    throw new DmeException("DME create vmfs volume error(task status is failure)!");
                }
            }
        } else {
            throw new DmeException("DME create vmfs volume error(task is null)!");
        }
    }

    private void createOnVmware(Map<String, Object> params, List<Map<String, Object>> volumelist) throws DmeException {
        VCenterInfo vcenterinfo = null;
        if (!StringUtils.isEmpty(params.get(DmeConstants.SERVICELEVELID))) {
            vcenterinfo = vcenterinfoservice.getVcenterInfo();
        }
        for (Map<String, Object> volumemap : volumelist) {
            // 创建vmware中的vmfs存储。
            params.put(VOLUME_WWN, volumemap.get(VOLUME_WWN));
            params.put(VOLUME_NAME, volumemap.get(VOLUME_NAME));
            String dataStoreStr = createVmfsOnVmware(params);
            if (!StringUtils.isEmpty(dataStoreStr)) {
                Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                    new TypeToken<Map<String, Object>>() { }.getType());
                if (dataStoreMap != null) {
                    // 将DME卷与vmfs的关系保存数据库,因为可以同时创建几个卷，无法在此得到对应关系，所以此处不再保存关系信息
                    saveDmeVmwareRalation(volumemap, dataStoreMap);

                    // 关联服务等级
                    if (!StringUtils.isEmpty(params.get(SERVICE_LEVEL_ID))) {
                        String serviceLevelName = ToolUtils.getStr(params.get(SERVICE_LEVEL_NAME));
                        vcsdkUtils.attachTag(ToolUtils.getStr(dataStoreMap.get("type")),
                            ToolUtils.getStr(dataStoreMap.get(ID_FIELD)), serviceLevelName, vcenterinfo);
                    }
                }
            } else {
                throw new DmeException("vmware create vmfs error:" + params.get(VOLUME_NAME));
            }
        }
    }

    private String createVmfsOnVmware(Map<String, Object> params) throws DmeException {
        // 在vmware创建存储
        String dataStoreStr = "";
        LOG.info("create vmfs on vmware begin !");
        try {
            if (params != null) {
                // 创建vmware中的vmfs存储。 cluster host
                String hostObjectId = ToolUtils.getStr(params.get(HOSTID));
                String clusterObjectId = ToolUtils.getStr(params.get(CLUSTER_ID));
                String datastoreName = ToolUtils.getStr(params.get(NAME_FIELD));
                int capacity = ToolUtils.getInt(params.get(CAPACITY));
                String existVolumeWwn = ToolUtils.getStr(params.get(VOLUME_WWN));
                String existVolumeName = ToolUtils.getStr(params.get(VOLUME_NAME));
                String volumeName = ToolUtils.getStr(params.get(VOLUMENAME));
                existVolumeName = existVolumeName.replaceAll(volumeName, "");

                // 根据后缀序号改变VMFS的名称
                datastoreName = datastoreName + existVolumeName;

                // 从主机或集群中找出最接近capacity的LUN
                Map<String, Object> hsdmap = null;
                if (params.get(DmeConstants.HOST) != null) {
                    hsdmap = vcsdkUtils.getLunsOnHost(hostObjectId, capacity, existVolumeWwn);
                } else if (params.get(DmeConstants.CLUSTER) != null) {
                    hsdmap = vcsdkUtils.getLunsOnCluster(clusterObjectId, capacity, existVolumeWwn);
                }

                int vmfsMajorVersion = ToolUtils.getInt(params.get("version"));
                int unmapGranularity = ToolUtils.getInt(params.get("spaceReclamationGranularity"));
                int blockSize = ToolUtils.getInt(params.get("blockSize"));
                String unmapPriority = ToolUtils.getStr(params.get("spaceReclamationPriority"));
                dataStoreStr = vcsdkUtils.createVmfsDataStore(hsdmap, capacity, datastoreName, vmfsMajorVersion,
                    blockSize, unmapGranularity, unmapPriority);

                // 如果创建成功，在集群中的其他主机上扫描并挂载datastore
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    vcsdkUtils.scanDataStore(clusterObjectId, null);
                }
            }
        } catch (DmeException e) {
            LOG.error("createVmfsOnVmware error:{}", e.toString());
            throw new DmeException(e.getMessage());
        }
        return dataStoreStr;
    }

    private String createVmfsByServiceLevel(Map<String, Object> params, String objhostid) throws DmeException {
        // 通过服务等级创建卷，返回任务ID
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.SERVICELEVELID) != null) {
                Map<String, Object> svbp = new HashMap<>();
                svbp.put(NAME_FIELD, ToolUtils.getStr(params.get(VOLUMENAME)));
                svbp.put(CAPACITY, ToolUtils.getInt(params.get(CAPACITY)));
                svbp.put(COUNT, ToolUtils.getInt(params.get(COUNT)));
                List<Map<String, Object>> volumes = new ArrayList<>();
                volumes.add(svbp);
                Map<String, Object> requestbody = new HashMap<>();
                requestbody.put("volumes", volumes);
                requestbody.put(SERVICE_LEVEL_ID, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)));

                Map<String, Object> mapping = new HashMap<>();
                if (!StringUtils.isEmpty(params.get(DmeConstants.HOST))) {
                    mapping.put(HOST_ID, objhostid);
                } else {
                    mapping.put(HOST_GROUP_ID1, objhostid);
                }
                requestbody.put(MAPPING, mapping);

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_VOLUME_BASE_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (jsonObject != null && !ToolUtils.jsonIsNull(jsonObject.get(DmeConstants.TASKID))) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                    }
                } else {
                    throw new DmeException(responseEntity.getBody().toString());
                }
            }
        } catch (DmeException e) {
            LOG.error("createVmfsByServiceLevel error:", e);
            throw new DmeException("create volume error:" + e.toString());
        }
        return taskId;
    }

    private String createVmfsByUnServiceLevelNew(Map<String, Object> params, String objhostid) throws DmeException {
        // 通过非服务化创建卷，返回任务ID
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.STORAGEID) != null) {
                Map<String, Object> requestbody = initCreateBody(params, objhostid);
                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (jsonObject != null && !ToolUtils.jsonIsNull(jsonObject.get(DmeConstants.TASKID))) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                    }
                } else {
                    throw new DmeException(
                        "create UNService Level volume error:" + responseEntity.getBody().toString());
                }
            } else {
                throw new DmeException("create UNService Level volume parameters error.");
            }
        } catch (DmeException e) {
            LOG.error("createVmfsUNServiceLevel error:", e);
            throw new DmeException("create UNService Level volume error:" + e.toString());
        }
        return taskId;
    }

    private Map<String, Object> initCreateBody(Map<String, Object> params, String objhostid) {
        Map<String, Object> requestbody = new HashMap<>();

        // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
        requestbody.put("pool_id", ToolUtils.getStr(params.get(POOL_RAW_ID)));
        requestbody.put(STORAGE_ID, ToolUtils.getStr(params.get(STORAGE_ID)));

        Map<String, Object> tuning = new HashMap<>();
        tuning.put("alloction_type", ToolUtils.getStr(params.get(ALLOCTYPE)));
        tuning.put("workload_type_raw_id", ToolUtils.getInt(params.get(WORKLOAD_TYPE_ID), null));
        String smartTier = ToolUtils.getStr(params.get(SMARTTIER));
        if(!StringUtils.isEmpty(smartTier)){
            tuning.put("smart_tier",  DmeConstants.SMART_TIER.get(smartTier));
        }
        if (params.get("qosFlag")!=null && (Boolean) params.get("qosFlag")==true){
            Map<String, Object> smartqos = new HashMap<>();
            smartqos.put(LATENCY, ToolUtils.getInt(params.get(LATENCY), null));
            smartqos.put("max_bandwidth", ToolUtils.getInt(params.get(MAXBANDWIDTH), null));
            smartqos.put("max_iops", ToolUtils.getInt(params.get(MAXIOPS), null));
            smartqos.put("min_bandwidth", ToolUtils.getInt(params.get(MINBANDWIDTH), null));
            smartqos.put("min_iops", ToolUtils.getInt(params.get(MINIOPS), null));
            if (!StringUtils.isEmpty(params.get(DmeConstants.CONTROLPOLICY))) {
                smartqos.put(DmeConstants.CONTROLPOLICY, params.get(DmeConstants.CONTROLPOLICY));
            }
            smartqos.put("enabled", true);
            tuning.put("smart_qos", smartqos);
        }
        if (!StringUtils.isEmpty(params.get(DmeConstants.ALLOCTYPE)) || !StringUtils.isEmpty(
            params.get(WORKLOAD_TYPE_ID)) || !StringUtils.isEmpty(params.get(CONTROL_POLICY))) {
            requestbody.put(TUNING, tuning);
        }

        Map<String, Object> vs = new HashMap<>();
        vs.put(NAME_FIELD, ToolUtils.getStr(params.get(VOLUMENAME)));
        vs.put(CAPACITY, ToolUtils.getInt(params.get(CAPACITY)));
        vs.put(COUNT, ToolUtils.getInt(params.get(COUNT)));
        List<Map<String, Object>> volumeSpecs = new ArrayList<>();
        volumeSpecs.add(vs);
        requestbody.put("lun_specs", volumeSpecs);

        Map<String, Object> mapping = new HashMap<>();
        if (!StringUtils.isEmpty(params.get(DmeConstants.HOST))) {
            mapping.put(HOST_ID, objhostid);
        } else {
            mapping.put(HOST_GROUP_ID1, objhostid);
        }
        requestbody.put(MAPPING, mapping);
        return requestbody;
    }

    private String checkToHost(String vmwareHostObjId) throws DmeException {
        if (StringUtils.isEmpty(vmwareHostObjId)) {
            return "";
        }

        // 判断vcenter主机在DME中是否存在
        String objId = "";
        try {
            // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(vmwareHostObjId);
            LOG.info("==hbas 0f host on vcenter==", gson.toJson(hbas));
            if (hbas == null || hbas.size() == 0) {
                throw new DmeException(vmwareHostObjId + " The host did not find a valid Hba");
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }
            LOG.info("==hostlist on vcenter==", gson.toJson(wwniqns));
            // 取出DME所有主机
            List<Map<String, Object>> dmeHostlist = dmeAccessService.getDmeHosts(null);
            if (dmeHostlist == null || dmeHostlist.size() == 0) {
                LOG.error("list dme hosts failed!dmeHostlist is null");
                return "";
            }
            for (Map<String, Object> hostmap : dmeHostlist) {
                if (hostmap != null && hostmap.get(ID_FIELD) != null) {
                    String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                    // 得到主机的启动器
                    List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                    LOG.info("==initiators 0f host on vcenter==", gson.toJson(initiators));
                    if (initiators != null && initiators.size() > 0) {
                        for (Map<String, Object> inimap : initiators) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            if (wwniqns.contains(portName)) {
                                objId = demHostId;
                                break;
                            }
                        }
                    }
                }

                // 如果已经找到的主机就不再循环
                if (!StringUtils.isEmpty(objId)) {
                    break;
                }
            }
        } catch (DmeException ex) {
            LOG.error("checkToHost error:", ex);
            throw new DmeException(ex.getMessage());
        }
        return objId;
    }

    @Override
    public String checkOrCreateToHost(String hostIp, String hostId) throws DmeException {
        // 判断主机在DME中是否存在 如果主机不存在就创建并得到主机ID
        String objId = "";
        try {
            if (StringUtils.isEmpty(hostId)) {
                return "";
            }

            // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(hostId);
            if (hbas == null || hbas.size() == 0) {
                throw new DmeException("The host did not find a valid Hba");
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }

            // 取出所有主机
            List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
            if (hostlist != null && hostlist.size() > 0) {
                for (Map<String, Object> hostmap : hostlist) {
                    if (hostmap == null || hostmap.get(ID_FIELD) == null) {
                        continue;
                    }

                    // 通过主机ID查到对应的主机的启动器
                    String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                    // 得到主机的启动器
                    List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                    if (initiators != null && initiators.size() > 0) {
                        for (Map<String, Object> inimap : initiators) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            if (wwniqns.contains(portName)) {
                                objId = demHostId;
                                break;
                            }
                        }
                    }

                    // 如果已经找到的主机就不再循环
                    if (!StringUtils.isEmpty(objId)) {
                        break;
                    }
                }
            }

            // 如果主机或主机不存在就创建并得到主机或主机组ID
            if (StringUtils.isEmpty(objId)) {
                objId = createHostOnDme(hostIp, hostId);
            }
        } catch (DmeException ex) {
            LOG.error("checkOrCreateToHost error:", ex);
            throw new DmeException(ex.getMessage());
        }
        return objId;
    }

    private String createHostOnDme(String hostIp, String hostId) throws DmeException {
        String hostObjId = "";
        Map<String, Object> params = new HashMap<>();
        params.put(HOST, hostIp);
        params.put(HOSTID, hostId);
        Map<String, Object> hostmap = dmeAccessService.createHost(params);
        if (hostmap != null && hostmap.get(DmeConstants.ID) != null) {
            hostObjId = hostmap.get(ID_FIELD).toString();
        }
        return hostObjId;
    }

    private String checkOrCreateToHostGroup(String clusterObjectId) throws DmeException {
        // 如果主机组不存在就创建并得到主机组ID 创建前要检查集群下的所有主机是否在DME中存在，只能通过id来创建主机组，如果集群有中文，dme中会创建失败
        String objId = "";
        try {
            // param str host: 主机  param str cluster: 集群
            // 如果主机或主机不存在就创建并得到主机或主机组ID 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            String clustername = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue();
            if (StringUtils.isEmpty(clusterObjectId)) {
                return "";
            }
            List<String> objIds = new ArrayList<>();

            // 检查集群对应的主机组在DME中是否存在
            List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clustername);
            LOG.info("==query host group of dme ==", gson.toJson(hostgrouplist));
            if (hostgrouplist != null && hostgrouplist.size() > 0) {
                for (Map<String, Object> hostgroupmap : hostgrouplist) {
                    if (hostgroupmap != null && hostgroupmap.get(NAME_FIELD) != null) {
                        if (clustername.equals(hostgroupmap.get(NAME_FIELD).toString())) {
                            String tmpObjId = ToolUtils.getStr(hostgroupmap.get(ID_FIELD));
                            objIds.add(tmpObjId);
                        }
                    }
                }
            }

            // 如果主机组id存在，就判断该主机组下的所有主机与集群下的主机是否一到处，如果不一致，不管是多还是少都算不一致，都需要重新创建主机组
            if (objIds != null && objIds.size() > 0) {
                for (String tmpObjId : objIds) {
                    objId = checkHostInHostGroup(clusterObjectId, tmpObjId);
                    if (!StringUtils.isEmpty(objId)) {
                        break;
                    }
                }
            }

            // 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            if (StringUtils.isEmpty(objId)) {
                objId = getOrCreateHostGroupId(clusterObjectId);
            }
        } catch (DmeException e) {
            LOG.error("checkOrCreateToHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String getOrCreateHostGroupId(String clusterObjectId) throws DmeException {
        String objId = "";

        // 取得集群下的所有主机
        String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
        if (!StringUtils.isEmpty(vmwarehosts)) {
            List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                new TypeToken<List<Map<String, String>>>() { }.getType());
            if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                // 分别检查每一个主机是否存在，如果不存在就创建
                List<String> hostlists = new ArrayList<>();
                for (Map<String, String> hostmap : vmwarehostlists) {
                    String tmpHostId = checkOrCreateToHost(ToolUtils.getStr(hostmap.get(HOST_NAME)),
                        ToolUtils.getStr(hostmap.get(HOSTID)));
                    if (!StringUtils.isEmpty(tmpHostId)) {
                        hostlists.add(tmpHostId);
                    }
                }

                // 在DME中创建主机组
                if (hostlists.size() == 0) {
                    return objId;
                }
                Map<String, Object> params = new HashMap<>();
                params.put("cluster", vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue());
                params.put("hostids", hostlists);
                Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                if (hostmap != null && hostmap.get(DmeConstants.ID) != null) {
                    objId = ToolUtils.getStr(hostmap.get(ID_FIELD));
                }
            }
        } else {
            new DmeException("the cluster has no host");
        }
        return objId;
    }

    private String checkToHostGroup(String clusterObjectId) throws DmeException {
        String objId = "";

        // 检查集群下的所有主机是否在DME中存在
        try {
            // param str host: 主机  param str cluster: 集群
            // 如果主机或主机不存在就创建并得到主机或主机组ID 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            String clustername = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue();
            if (!StringUtils.isEmpty(clustername)) {
                List<String> dmeHostGroupIds = new ArrayList<>();

                // 检查集群对应的主机组在DME中是否存在
                List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clustername);
                if (hostgrouplist != null && hostgrouplist.size() > 0) {
                    for (Map<String, Object> hostgroupmap : hostgrouplist) {
                        if (clustername.equals(hostgroupmap.get(NAME_FIELD).toString())) {
                            String tmpObjId = ToolUtils.getStr(hostgroupmap.get(ID_FIELD));
                            dmeHostGroupIds.add(tmpObjId);
                        }
                    }
                }

                // 如果主机组id存在，就判断该主机组下的所有主机与集群下的主机是否一到处，如果不一致，不管是多还是少都算不一致，都需要重新创建主机组
                if (dmeHostGroupIds != null && dmeHostGroupIds.size() > 0) {
                    for (String dmeHostGroupId : dmeHostGroupIds) {
                        objId = checkHostInHostGroup(clusterObjectId, dmeHostGroupId);
                        if (!StringUtils.isEmpty(objId)) {
                            break;
                        }
                    }
                }

                // 检查集群下的所有主机是否在DME中存在
                if (!StringUtils.isEmpty(objId)) {
                    return objId;
                }

                // 取得集群下的所有主机
                String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
                if (!StringUtils.isEmpty(vmwarehosts)) {
                    List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                        new TypeToken<List<Map<String, String>>>() { }.getType());

                    // 分别检查每一个主机是否存在，如果不存在就创建
                    List<String> hostlists = new ArrayList<>();
                    for (Map<String, String> hostmap : vmwarehostlists) {
                        String tmpHostId = checkToHost(ToolUtils.getStr(hostmap.get(HOSTID)));
                        if (!StringUtils.isEmpty(tmpHostId)) {
                            hostlists.add(tmpHostId);
                        }
                    }
                }
            }
        } catch (VcenterException e) {
            LOG.error("checkOrCreateToHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String checkHostInHostGroup(String vmwareClusterObjectId, String dmeHostGroupId) throws DmeException {
        String objId = "";
        try {
            if (StringUtils.isEmpty(vmwareClusterObjectId)) {
                LOG.error("vmware Cluster Object Id is null");
                return objId;
            }

            // 得到集群下所有的主机的hba
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByClusterObjectId(vmwareClusterObjectId);
            LOG.info("==host hba info on vcenter==", gson.toJson(hbas));
            if (hbas == null || hbas.size() == 0) {
                LOG.error("vmware Cluster hbas is null");
                return objId;
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }
            LOG.info("==wwn of host group==", wwniqns.toString() + "wwn size:" + wwniqns.size());
            List<Map<String, Object>> dmehosts = dmeAccessService.getDmeHostInHostGroup(dmeHostGroupId);
            LOG.info("==wwn of dme hosts==", dmehosts.toString() + "host size:" + dmehosts.size());
            if (dmehosts != null && dmehosts.size() > 0) {
                List<Map<String, Object>> initiators = new ArrayList<>();
                for (Map<String, Object> dmehost : dmehosts) {
                    // 得到主机的启动器
                    if (dmehost != null && dmehost.get(ID_FIELD) != null) {
                        String demHostId = ToolUtils.getStr(dmehost.get(ID_FIELD));
                        List<Map<String, Object>> subinitiators = dmeAccessService.getDmeHostInitiators(demHostId);
                        LOG.info("==initiators of host==", gson.toJson(initiators) + "host size:" + initiators.size());
                        if (subinitiators != null && subinitiators.size() > 0) {
                            initiators.addAll(subinitiators);
                        }
                    }
                }
                if (initiators.size() > 0) {
                    List<String> initiatorName = new ArrayList<>();
                    for (Map<String, Object> inimap : initiators) {
                        if (inimap != null && inimap.get(PORT_NAME) != null) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            initiatorName.add(portName);
                        }
                    }
                    boolean isCheckHbaInHostGroup = ToolUtils.compare(wwniqns, initiatorName);
                    LOG.info("==result of dme/vcenter wwn compare==", isCheckHbaInHostGroup);
                    if (isCheckHbaInHostGroup) {
                        objId = dmeHostGroupId;
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("checkHostInHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String checkOrCreateToHostorHostGroup(Map<String, Object> params) throws DmeException {
        // 根据参数选择检查主机或主机组的方法
        String objId = "";
        try {
            // param str host: 主机  param str cluster: 集群
            if (params != null && params.get(DmeConstants.HOST) != null) {
                objId = checkOrCreateToHost(ToolUtils.getStr(params.get(HOST)), ToolUtils.getStr(params.get(HOSTID)));
            } else if (params != null && params.get(DmeConstants.CLUSTER) != null) {
                objId = checkOrCreateToHostGroup(ToolUtils.getStr(params.get(CLUSTER_ID)));
            }
        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private List<Map<String, Object>> getVolumeByName(String volumeName, String hostId, String hostGroupId,
        String serviceLevelId, String storageId, String poolRawId) {
        // 根据卷名称,主机id,主机组id,服务等级id,存储设备ID，存储池ID 查询DME卷的信息
        List<Map<String, Object>> volumelist = null;
        String listVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + "?name=" + volumeName;
        if (!StringUtils.isEmpty(hostId)) {
            listVolumeUrl = listVolumeUrl + "&host_id=" + hostId;
        }
        if (!StringUtils.isEmpty(hostGroupId)) {
            listVolumeUrl = listVolumeUrl + "&hostgroup_id=" + hostGroupId;
        }
        if (!StringUtils.isEmpty(serviceLevelId)) {
            listVolumeUrl = listVolumeUrl + "&service_level_id=" + serviceLevelId;
        }
        if (!StringUtils.isEmpty(storageId)) {
            listVolumeUrl = listVolumeUrl + "&storage_id=" + storageId;
        }
        // todo 按照这个参数去查询存储池可用查到，如果按照这个参数的返回的结果去查询就查不到
        if (!StringUtils.isEmpty(poolRawId)) {
            listVolumeUrl = listVolumeUrl + "&id=" + poolRawId;
        }
        try {
            ResponseEntity responseEntity = dmeAccessService.access(listVolumeUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                volumelist = new ArrayList<>();
                JsonArray jsonArray = jsonObject.getAsJsonArray(DmeConstants.VOLUMES);
                for (int index = 0; index < jsonArray.size(); index++) {
                    JsonObject vjson = jsonArray.get(index).getAsJsonObject();
                    if (vjson != null) {
                        Map<String, Object> remap = new HashMap<>();
                        remap.put("storage_id", storageId);
                        remap.put("volume_id", ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                        remap.put(VOLUME_NAME, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                        remap.put(VOLUME_WWN, ToolUtils.jsonToStr(vjson.get(VOLUME_WWN)));
                        volumelist.add(remap);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("url:{},error msg:{}", listVolumeUrl, e.getMessage());
        }
        return volumelist;
    }

    private void saveDmeVmwareRalation(Map<String, Object> volumeMap, Map<String, Object> dataStoreMap)
        throws DmeException {
        // 保存卷与vmfs的关联关系
        if (volumeMap == null || volumeMap.get(DmeConstants.VOLUMEID) == null) {
            LOG.error("save Dme and Vmware's vmfs Ralation error: volume data is null");
            return;
        }
        if (dataStoreMap == null || dataStoreMap.get(DmeConstants.ID) == null) {
            LOG.error("save Dme and Vmware's vmfs Ralation error: dataStore data is null");
            return;
        }
        DmeVmwareRelation dvr = new DmeVmwareRelation();
        dvr.setStoreId(ToolUtils.getStr(dataStoreMap.get(OBJECTID)));
        dvr.setStoreName(ToolUtils.getStr(dataStoreMap.get(NAME_FIELD)));
        dvr.setVolumeId(ToolUtils.getStr(volumeMap.get("volume_id")));
        dvr.setVolumeName(ToolUtils.getStr(volumeMap.get(VOLUME_NAME)));
        dvr.setVolumeWwn(ToolUtils.getStr(volumeMap.get(VOLUME_WWN)));
        dvr.setStoreType(DmeConstants.STORE_TYPE_VMFS);
        // 服务等级方式创建vmfs 不需要判断存储类型
        String storageId = ToolUtils.getStr(volumeMap.get("storage_id"));
        if (!StringUtils.isEmpty(storageId)){
            dvr.setStorageType(getStorageModel(storageId));
        }
        List<DmeVmwareRelation> rallist = new ArrayList<>();
        rallist.add(dvr);
        dmeVmwareRalationDao.save(rallist);
        LOG.info("save DmeVmwareRalation success");
    }

    @Override
    public void mountVmfs(Map<String, Object> params) throws DmeException {
        if (params == null || params.size() == 0) {
            throw new DmeException("mount vmfs error, params is null");
        }
        String hostObjId = ToolUtils.getStr(params.get(HOSTID));
        String clusterObjId = ToolUtils.getStr(params.get(CLUSTER_ID));
        if (!StringUtils.isEmpty(hostObjId)) {
            String hostName = vcsdkUtils.getHostName(hostObjId);
            params.put(HOST, hostName);
        }
        if (!StringUtils.isEmpty(clusterObjId)) {
            String clusterName = vcsdkUtils.getClusterName(clusterObjId);
            params.put("cluster", clusterName);
        }

        // param str host: 主机  param str cluster: 集群  dataStoreObjectIds
        // 判断主机或主机组在DME中是否存在, 如果主机或主机不存在就创建并得到主机或主机组ID
        String objhostid = checkOrCreateToHostorHostGroup(params);
        if (StringUtils.isEmpty(objhostid)) {
            LOG.info("objhostid is null! mountVmfs failed!");
            return;
        }

        // 通过存储的objectid查询卷id
        if (params.get(DATASTORE_OBJECT_IDS) != null) {
            List<String> dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
            if (dataStoreObjectIds != null && dataStoreObjectIds.size() > 0) {
                getVolumIdFromLocal(params, dataStoreObjectIds);
            }
        }
        String taskId = "";
        if (params.get(DmeConstants.HOST) != null) {
            // 将卷挂载到主机DME
            LOG.info("mount Vmfs to host begin!");
            taskId = mountVmfsToHost(params, objhostid);
            LOG.info("mount Vmfs to host end!taskId={}", taskId);
        } else {
            // 将卷挂载到集群DME
            LOG.info("mount Vmfs to host group begin!");
            taskId = mountVmfsToHostGroup(params, objhostid);
            LOG.info("mount Vmfs to host group end!taskId={}", taskId);
        }
        if (StringUtils.isEmpty(taskId)) {
            throw new DmeException("DME mount vmfs volume error(task is null)!");
        }
        List<String> taskIds = new ArrayList<>();
        taskIds.add(taskId);
        boolean isMounted = taskService.checkTaskStatus(taskIds);
        if (isMounted) {
            LOG.info("vmware mount Vmfs begin!params={}", gson.toJson(params));
            mountVmfsOnVmware(params);
            LOG.info("vmware mount Vmfs end!");
        } else {
            LOG.info("DME mount Vmfs failed!taskId={}", taskId);
            throw new DmeException("DME mount vmfs volume error(task status)!");
        }
    }

    private void getVolumIdFromLocal(Map<String, Object> params, List<String> dataStoreObjectIds)
        throws DmeSqlException {
        List<String> volumeIds = new ArrayList<>();
        List<String> dataStoreNames = new ArrayList<>();
        for (String dsObjectId : dataStoreObjectIds) {
            DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
            if (dvr != null) {
                volumeIds.add(dvr.getVolumeId());
                dataStoreNames.add(dvr.getStoreName());
            }
        }
        if (volumeIds.size() > 0) {
            params.put(VOLUMEIDS, volumeIds);
            params.put(DATASTORE_NAMES, dataStoreNames);
        }
    }

    private void mountVmfsOnVmware(Map<String, Object> params) throws VcenterException {
        // 调用vCenter在主机上扫描卷和Datastore
        vcsdkUtils.scanDataStore(ToolUtils.getStr(params.get(CLUSTER_ID)), ToolUtils.getStr(params.get(HOSTID)));

        // 如果是需要扫描LUN来挂载，则需要执行下面的方法，dataStoreNames
        if (params.get(DmeConstants.DATASTORENAMES) != null) {
            List<String> dataStoreNames = (List<String>) params.get(DATASTORE_NAMES);
            if (dataStoreNames != null && dataStoreNames.size() > 0) {
                for (String dataStoreName : dataStoreNames) {
                    Map<String, Object> dsmap = new HashMap<>();
                    dsmap.put(NAME_FIELD, dataStoreName);

                    vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap), ToolUtils.getStr(params.get(CLUSTER_ID)),
                        ToolUtils.getStr(params.get(HOSTID)));
                }
            }
        }
    }

    private String mountVmfsToHost(Map<String, Object> params, String objhostid) {
        // 将卷挂载到主机DME
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.VOLUMEIDS) != null) {
                Map<String, Object> requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get(VOLUMEIDS);

                requestbody.put(VOLUME_IDS, volumeIds);
                requestbody.put(HOST_ID, objhostid);
                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_HOST_MAPPING_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.TASKID) != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                    }
                }
            } else {
                LOG.error("mountVmfsToHost error:volumeIds is null");
            }
        } catch (DmeException e) {
            LOG.error("mountVmfsToHost error:", e);
        }
        return taskId;
    }

    private String mountVmfsToHostGroup(Map<String, Object> params, String objhostid) {
        // 将卷挂载到集群DME
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.VOLUMEIDS) != null) {
                Map<String, Object> requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get(VOLUMEIDS);

                requestbody.put(VOLUME_IDS, volumeIds);
                requestbody.put(HOST_GROUP_ID1, objhostid);

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.MOUNT_VOLUME_TO_HOSTGROUP_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.TASKID) != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                        LOG.info("mountVmfsToHostGroup task_id===={}", taskId);
                    }
                }
            } else {
                LOG.error("mountVmfsToHost error:volumeIds is null");
            }
        } catch (DmeException e) {
            LOG.error("mountVmfsToHostGroup error:", e);
        }
        return taskId;
    }

    @Override
    public List<VmfsDatastoreVolumeDetail> volumeDetail(String storageObjectId) throws DmeException {
        List<VmfsDatastoreVolumeDetail> list = new ArrayList<>();
        List<String> volumeIds = dmeVmwareRalationDao.getVolumeIdsByStorageId(storageObjectId);
        LOG.error("get volume detail! volumeIds={}", gson.toJson(volumeIds));
        for (String volumeId : volumeIds) {
            // 调用DME接口获取卷详情
            String url = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
            LOG.info("get volume detail! url={}", url);
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / DIVISOR_100 != HTTP_SUCCESS) {
                LOG.info("get volume detail failed! response={}", gson.toJson(responseEntity));
                throw new DmeException(responseEntity.getBody());
            }
            String responseBody = responseEntity.getBody();
            JsonObject volume = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject(VOLUME_FIELD);
            if (volume.isJsonNull()) {
                continue;
            }
            LOG.info("volume detail! dme response={}", volume.toString());
            VmfsDatastoreVolumeDetail volumeDetail = new VmfsDatastoreVolumeDetail();
            volumeDetail.setWwn(volume.get(VOLUME_WWN).getAsString());
            volumeDetail.setName(volume.get(NAME_FIELD).getAsString());
            volumeDetail.setServiceLevel(ToolUtils.jsonToStr(volume.get(SERVICE_LEVEL_NAME), null));
            if (!volume.get(STORAGE_ID).isJsonNull()) {
                String storageId = volume.get(STORAGE_ID).getAsString();

                // 根据存储ID查询存储信息
                url = DmeConstants.DME_STORAGE_DETAIL_URL.replace("{storage_id}", storageId);
                responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / DIVISOR_100 == HTTP_SUCCESS) {
                    JsonObject storeageDetail = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                    volumeDetail.setStorage(storeageDetail.get(NAME_FIELD).getAsString());
                }
            }

            if (!volume.get(POOL_RAW_ID).isJsonNull()) {
                parseStoragePool(volume.get(POOL_RAW_ID).getAsString(), volumeDetail);
            }

            JsonObject tuning = volume.getAsJsonObject(TUNING);
            if (!tuning.isJsonNull()) {
                parseVolumeTuning(volumeDetail, tuning);
            }
            list.add(volumeDetail);
        }

        return list;
    }

    private void parseStoragePool(String poolId, VmfsDatastoreVolumeDetail volumeDetail) throws DmeException {
        String poolName = "";
        try {
            poolName = dmeStorageService.getStorageByPoolRawId(poolId);
        } catch (DmeException ex) {
            LOG.info("query datastore pool failed!{}", ex.getMessage());
        }
        LOG.info("query datastore pool success!poolName={}", poolName);
        volumeDetail.setStoragePool(poolName);
    }

    private void parseVolumeTuning(VmfsDatastoreVolumeDetail volumeDetail, JsonObject tuning) {
        // SmartTier
        volumeDetail.setSmartTier(ToolUtils.jsonToStr(tuning.get("smarttier")));

        // Tunning
        volumeDetail.setDudeplication(ToolUtils.jsonToBoo(tuning.get("dedupe_enabled")));
        volumeDetail.setProvisionType(ToolUtils.jsonToStr(tuning.get(ALLOCTYPE), null));
        volumeDetail.setCompression(ToolUtils.jsonToBoo(tuning.get("compression_enabled")));


        // 应用类型
        volumeDetail.setApplicationType(ToolUtils.jsonToStr(tuning.get(WORKLOAD_TYPE_ID), null));

        // Qos Policy
        if (!tuning.get(SMARTQOS).isJsonNull()) {
            JsonObject smartqos = tuning.getAsJsonObject(SMARTQOS);
            SmartQos smartQos = new SmartQos();
            Integer maxbandwidth = ToolUtils.jsonToInt(smartqos.get("maxbandwidth"),0);
            Integer minbandwidth = ToolUtils.jsonToInt(smartqos.get("minbandwidth"),0);
            if (maxbandwidth != 0 && minbandwidth != 0) {
                // 2表示保护上限 和  保护下线
                smartQos.setControlPolicy("2");
                volumeDetail.setControlPolicy("2");
            }else {
                smartQos.setControlPolicy(ToolUtils.jsonToStr(smartqos.get(CONTROL_POLICY), null));
                volumeDetail.setControlPolicy(ToolUtils.jsonToStr(smartqos.get(CONTROL_POLICY), null));
            }
            smartQos.setMaxbandwidth(maxbandwidth);
            smartQos.setMinbandwidth(minbandwidth);
            smartQos.setMaxiops(ToolUtils.jsonToInt(smartqos.get("maxiops"),0));
            smartQos.setMiniops(ToolUtils.jsonToInt(smartqos.get("miniops"),0));
            smartQos.setLatency(ToolUtils.jsonToInt(smartqos.get("latency"),0));
            volumeDetail.setSmartQos(smartQos);
        }
    }

    @Override
    public boolean scanVmfs() throws DmeException {
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(DmeConstants.STORE_TYPE_VMFS);
        if (StringUtils.isEmpty(listStr)) {
            LOG.info("===list vmfs datastore return empty");
            return false;
        }
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int index = 0; index < jsonArray.size(); index++) {
            JsonObject vmfsDatastore = jsonArray.get(index).getAsJsonObject();
            String storeType = DmeConstants.STORE_TYPE_VMFS;
            String vmfsDatastoreId = vmfsDatastore.get(OBJECTID).getAsString();
            String vmfsDatastoreName = vmfsDatastore.get(NAME_FIELD).getAsString();
            JsonArray wwnArray = vmfsDatastore.getAsJsonArray("vmfsWwnList");
            Map<String, String> storageIds = new HashMap<>();
            if (wwnArray == null || wwnArray.size() == 0) {
                continue;
            }

            for (int dex = 0; dex < wwnArray.size(); dex++) {
                String wwn = wwnArray.get(dex).getAsString();
                if (StringUtil.isBlank(wwn)) {
                    continue;
                }
                // 根据wwn从DME中查询卷信息
                String volumeUrlByName = DmeConstants.DME_VOLUME_BASE_URL + "?volume_wwn=" + wwn;
                ResponseEntity<String> responseEntity = dmeAccessService.access(volumeUrlByName, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / DIVISOR_100 != HTTP_SUCCESS) {
                    LOG.info(" Query DME volume failed! errorMsg:{}", responseEntity.toString());
                    continue;
                }
                JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                JsonElement volumesElement = jsonObject.get("volumes");
                if (!ToolUtils.jsonIsNull(volumesElement)) {
                    JsonArray volumeArray = volumesElement.getAsJsonArray();
                    if (volumeArray.size() > 0) {
                        JsonObject volumeObject = volumeArray.get(0).getAsJsonObject();
                        String storageId = ToolUtils.jsonToOriginalStr(volumeObject.get("storage_id"));
                        //根据存储Id 获取存储型号
                        String storageModel = "";
                        if (storageIds.get(storageId) == null) {
                            storageModel = getStorageModel(storageId);
                            storageIds.put(storageId, storageModel);
                        }else {
                            storageModel = storageIds.get(storageId);
                        }
                        DmeVmwareRelation relation = getDmeVmwareRelation(storeType, vmfsDatastoreId, vmfsDatastoreName,
                            volumeObject,storageModel);
                        relationList.add(relation);
                    }
                }
            }
        }

        if (relationList.size() > 0) {
            // 数据库处理
            return dmeVmwareRelationDbProcess(relationList, DmeConstants.STORE_TYPE_VMFS);
        }
        return true;
    }

    private DmeVmwareRelation getDmeVmwareRelation(String storeType, String vmfsDatastoreId, String vmfsDatastoreName,
                                                   JsonObject volumeObject, String storageModel) {
        String volumeId = ToolUtils.jsonToOriginalStr(volumeObject.get(ID_FIELD));
        String volumeName = ToolUtils.jsonToOriginalStr(volumeObject.get(NAME_FIELD));
        String volumeWwn = ToolUtils.jsonToOriginalStr(volumeObject.get(VOLUME_WWN));

        DmeVmwareRelation relation = new DmeVmwareRelation();
        relation.setStoreId(vmfsDatastoreId);
        relation.setStoreName(vmfsDatastoreName);
        relation.setVolumeId(volumeId);
        relation.setVolumeName(volumeName);
        relation.setVolumeWwn(volumeWwn);
        relation.setStoreType(storeType);
        relation.setStorageType(storageModel);
        relation.setState(1);
        return relation;
    }

    private boolean dmeVmwareRelationDbProcess(List<DmeVmwareRelation> relationList, String storeType)
        throws DmeSqlException {
        // 本地全量查询
        List<String> localWwns = dmeVmwareRalationDao.getAllWwnByType(storeType);

        List<DmeVmwareRelation> newList = new ArrayList<>();
        List<DmeVmwareRelation> upList = new ArrayList<>();
        for (DmeVmwareRelation relation : relationList) {
            String wwn = relation.getVolumeWwn();
            if (localWwns.contains(wwn)) {
                upList.add(relation);
                localWwns.remove(wwn);
            } else {
                newList.add(relation);
            }
        }

        // 更新
        if (!upList.isEmpty()) {
            dmeVmwareRalationDao.update(upList, storeType);
        }

        // 新增
        if (!newList.isEmpty()) {
            dmeVmwareRalationDao.save(newList);
        }

        // 删除
        if (!localWwns.isEmpty()) {
            dmeVmwareRalationDao.deleteByWwn(localWwns);
        }
        return true;
    }

    /**
     * vmfs 卸载
     *
     * @param params dataStoreObjectIds hostId hostGroupId由调用处传递过来
     * @throws DmeException DmeException
     */
    @Override
    public void unmountVmfs(Map<String, Object> params) throws DmeException {
        List<String> taskIds = new ArrayList<>();
        List<String> dataStoreObjectIds = null;
        String hostObjId = "";
        String clusterObjId = "";
        if (null != params && null != params.get(DATASTORE_OBJECT_IDS)) {
            dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
            if (dataStoreObjectIds.size() > 0) {
                List<String> volumeIds = new ArrayList<>();
                List<String> dataStoreNames = new ArrayList<>();
                for (String dsObjectId : dataStoreObjectIds) {
                    DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    if (dvr == null) {
                        scanVmfs();
                    }
                    dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    if (dvr != null) {
                        volumeIds.add(dvr.getVolumeId());
                        dataStoreNames.add(dvr.getStoreName());
                    }
                }
                if (volumeIds.size() > 0) {
                    params.put(VOLUMEIDS, volumeIds);
                    params.put(DATASTORE_NAMES, dataStoreNames);
                }
            } else {
                throw new DmeException("unmount volume params dataStoreObjectIds is null!");
            }
        }
        if (params != null && params.get(VOLUMEIDS) != null) {
            if (params.get(HOSTID) != null) {
                hostObjId = ToolUtils.getStr(params.get(HOSTID));
                Map<String, Object> hostMap = getDmeHostByHostObjeId(hostObjId);
                if (hostMap != null && hostMap.size() > 0) {
                    params.put(HOST_ID, ToolUtils.getStr(hostMap.get(ID_FIELD)));
                    String taskId = unmountHostGetTaskId(params);
                    if (!StringUtils.isEmpty(taskId)) {
                        taskIds.add(taskId);
                    }
                }
            }
            if (params.get(CLUSTER_ID) != null) {
                clusterObjId = ToolUtils.getStr(params.get(CLUSTER_ID));
                Map<String, Object> hostGroupMap = getDmeHostGroupByClusterObjId(clusterObjId);
                if (hostGroupMap != null && hostGroupMap.size() > 0) {
                    params.put(HOST_GROUP_ID1, ToolUtils.getStr(hostGroupMap.get(HOST_GROUP_ID)));
                    String taskId = unmountHostGroupGetTaskId(params);
                    if (!StringUtils.isEmpty(taskId)) {
                        taskIds.add(taskId);
                    }
                }
            }
        }

        // 获取卸载的任务完成后的状态,默认超时时间10分钟
        boolean isUnmounted = false;
        if (taskIds.size() > 0) {
            isUnmounted = taskService.checkTaskStatus(taskIds);
        }

        // 判断是否卸载vmfs上的全部主机或集群 若是 则删除dme侧的卷，目前卸载参数中的主机/集群只支持单个,所以vmfs只挂载了一个主机或集群dme侧就直接删除卷
        boolean isDeleteFalg = false;
        if (params.get(HOSTID) != null) {
            for (String dsObj : dataStoreObjectIds) {
                List<Map<String, Object>> hosts = getHostsByStorageId(dsObj);
                if (hosts != null && hosts.size() == 1) {
                    volumeDelete(params);
                    isDeleteFalg = true;
                    break;
                }
            }
        }
        if (!isDeleteFalg && params.get(CLUSTER_ID) != null) {
            for (String dsObjId : dataStoreObjectIds) {
                List<Map<String, Object>> hostGroups = getHostGroupsByStorageId(dsObjId);
                if (hostGroups != null && hostGroups.size() == 1) {
                    volumeDelete(params);
                    isDeleteFalg = true;
                    break;
                }
            }
        }
        if (!isUnmounted) {
            throw new DmeException(
                "unmount volume precondition unmount host and hostGroup error(task status),taskIds:(" + gson.toJson(
                    taskIds) + ")!");
        } else {
            // 如果是需要扫描LUN来卸载，则需要执行下面的方法，dataStoreNames
            if (params.get(DmeConstants.DATASTORENAMES) != null) {
                List<String> dataStoreNames = (List<String>) params.get(DATASTORE_NAMES);
                if (dataStoreNames != null && dataStoreNames.size() > 0) {
                    for (String dataStoreName : dataStoreNames) {
                        Map<String, Object> dsmap = new HashMap<>();
                        dsmap.put(NAME_FIELD, dataStoreName);
                        vcsdkUtils.unmountVmfsOnHostOrCluster(gson.toJson(dsmap), clusterObjId, hostObjId);
                    }
                }
            }
        }

        // 若卸载vmfs上的全部主机或集群 最后重新扫描 此步会自动删除vmfs
        if (isDeleteFalg) {
            vcsdkUtils.scanDataStore(clusterObjId, hostObjId);
        }
    }

    private List<String> unmountVmfs(String dsObjId, Map<String, Object> params) throws DmeException {
        List<String> taskIds = new ArrayList<>();

        // 获取vmfs关联的dme侧volume 并提取volumeId
        List<String> hostObjIds = new ArrayList<>();
        List<String> volumeIds = new ArrayList<>();
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjId);
        if (dvr != null) {
            volumeIds.add(dvr.getVolumeId());
        }

        // 没有指定集群 查询存储关联的所有集群
        List<Map<String, Object>> vcClusters = getHostGroupsByStorageId(dsObjId);
        if (volumeIds.size() > 0 && vcClusters != null && vcClusters.size() > 0) {
            for (Map<String, Object> vcCluster : vcClusters) {
                String hostGroupObjectId = ToolUtils.getStr(vcCluster.get(HOST_GROUP_ID));
                if (StringUtils.isEmpty(hostGroupObjectId)) {
                    continue;
                }
                Map<String, Object> hostGroupMap = getDmeHostGroupByClusterObjId(hostGroupObjectId);

                // vcenter的集群和dem的host的启动器一样
                if (hostGroupMap != null && hostGroupMap.size() > 0) {
                    String hostGroupIdDmeId = ToolUtils.getStr(hostGroupMap.get(HOST_GROUP_ID));

                    // 是否关联vm
                    if (dataStoreVmRelateHostOrCluster(dsObjId, null, hostGroupObjectId)) {
                        continue;
                    }
                    Map<String, Object> tempParams = new HashMap<>();
                    tempParams.put(HOST_GROUP_ID1, hostGroupIdDmeId);
                    tempParams.put(VOLUMEIDS, volumeIds);
                    String taskId = unmountHostGroupGetTaskId(tempParams);
                    taskIds.add(taskId);
                }
            }
        }

        // 没有指定主机 查询datastore下的主机 并过滤与vm有关联的
        List<Map<String, Object>> vcHosts = getHostsByStorageId(dsObjId);
        if (volumeIds.size() > 0 && vcHosts != null && vcHosts.size() > 0) {
            taskIds.addAll(unmountHostFromDme(dsObjId, hostObjIds, volumeIds, vcHosts));
        }

        // 提取datastore的hostid
        if (hostObjIds.size() > 0) {
            Object object = params.get(HOST_OBJ_IDS);
            if (object != null) {
                List<String> hostObjIdList = (List<String>) object;
                if (hostObjIdList != null && hostObjIdList.size() > 0) {
                    hostObjIds.addAll(hostObjIdList);
                }
            }
            params.put(HOST_OBJ_IDS, hostObjIds);
        }

        // 删除前的卸载 vcenter侧不扫描,执行删除时再扫描
        // host scan
        return taskIds;
    }

    private List<String> unmountHostFromDme(String dsObjId, List<String> hostObjIds, List<String> volumeIds,
        List<Map<String, Object>> vcHosts) throws DmeException {
        List<String> taskIds = new ArrayList<>();
        for (Map<String, Object> vcHost : vcHosts) {
            String hostObjectId = ToolUtils.getStr(vcHost.get(HOSTID));
            if (!StringUtils.isEmpty(hostObjectId)) {
                Map<String, Object> hostMap = getDmeHostByHostObjeId(hostObjectId);

                // vcenter的host和dem的host的启动器一样
                if (hostMap != null && hostMap.size() > 0) {
                    hostObjIds.add(hostObjectId);
                    String hostId = ToolUtils.getStr(hostMap.get(ID_FIELD));

                    // 是否关联vm
                    if (dataStoreVmRelateHostOrCluster(dsObjId, hostObjectId, null)) {
                        continue;
                    }
                    Map<String, Object> tempParams = new HashMap<>();
                    tempParams.put(HOST_ID, hostId);
                    tempParams.put(VOLUMEIDS, volumeIds);
                    String taskId = unmountHostGetTaskId(tempParams);
                    taskIds.add(taskId);
                }
            }
        }

        return taskIds;
    }

    // 删除前的卸载
    public List<String> unmountVmfsAll(Map<String, Object> params) throws DmeException {
        List<String> taskIds = new ArrayList<>();

        // 获取vmfs关联的dme侧volume 并提取volumeId
        if (params != null && params.get(DmeConstants.DATASTOREOBJECTIDS) != null) {
            List<String> dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
            List<String> dataStorageIds = new ArrayList<>();
            if (dataStoreObjectIds != null && dataStoreObjectIds.size() > 0) {
                List<String> volumeIds = new ArrayList<>();
                List<String> dataStoreNames = new ArrayList<>();
                for (String dsObjectId : dataStoreObjectIds) {
                    // 如果dsObject包含虚拟机 则不能删除
                    boolean isFoundVm = vcsdkUtils.hasVmOnDatastore(dsObjectId);
                    if (isFoundVm) {
                        LOG.info("vmfs delete,the vmfs:{} contain vm,can not delete!!!", dsObjectId);
                        continue;
                    }
                    DmeVmwareRelation dvr = null;
                    try {
                        dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    } catch (DmeSqlException e) {
                        LOG.info("getDmeVmwareRelationByDsId exception!dsId={}, msg={}", dsObjectId, e.getMessage());
                    }
                    if (dvr == null) {
                        scanVmfs();
                        dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    }
                    if (dvr != null) {
                        volumeIds.add(dvr.getVolumeId());
                        dataStoreNames.add(dvr.getStoreName());
                        dataStorageIds.add(dsObjectId);
                    }
                }
                if (volumeIds.size() > 0) {
                    params.put(VOLUMEIDS, volumeIds);
                    params.put(DATASTORE_NAMES, dataStoreNames);
                }

                // 没有满足条件的datastore 直接返回taskids(size=0)
                if (dataStorageIds.size() == 0) {
                    LOG.info("vmfs delete,all vmfs contain vm,can not delete!!!");
                    return taskIds;
                }
            }

            // 获取vcenter关联的所有hostId, dataStoreObjectIds只有一个值
            for (String dsObjectId : dataStorageIds) {
                List<String> unmountTaskIds = unmountVmfs(dsObjectId, params);
                if (unmountTaskIds != null && unmountTaskIds.size() > 0) {
                    taskIds.addAll(unmountTaskIds);
                }
            }
        }

        return taskIds;
    }

    @Override
    public void deleteVmfs(Map<String, Object> params) throws DmeException {
        // 先调卸载的接口 卸载是卸载所有所有主机和集群(dem侧主机,主机组)
        try {
            List<String> unmountTaskIds = unmountVmfsAll(params);
            if (unmountTaskIds != null && unmountTaskIds.size() > 0) {
                // 检测任务等待卸载完成后再删除,不用判断是否卸载成功
                taskService.checkTaskStatus(unmountTaskIds);
            }
        } catch (DmeException e) {
            LOG.error("delete volume precondition unmapping host and hostGroup error!");
            throw new DmeException("delete volume precondition unmapping host and hostGroup error!");
        }

        // DME侧卸载卷
        String taskId = volumeDeleteGetTaskId(params);
        if (!StringUtils.isEmpty(taskId)) {
            boolean isDmeDelete = taskService.checkTaskStatus(Arrays.asList(taskId));
            if (!isDmeDelete) {
                throw new DmeException(taskService.queryTaskById(taskId).getDetail());
            }
        } else {
            throw new DmeException("dme delete vmfs failed,task id is null!");
        }

        // vcenter侧 扫描
        List<String> hostObjIds = (List<String>) params.get(HOST_OBJ_IDS);
        if (hostObjIds != null && hostObjIds.size() > 0) {
            // 过滤重复的hostObjId
            hostObjIds = new ArrayList<>(new HashSet(hostObjIds));
            for (String hostObjId : hostObjIds) {
                vcsdkUtils.scanDataStore(null, hostObjId);
            }
        } else {
            // dme侧已删除(卸载) hostObjectIds参数为空，此时通过dsObjectId查询hostObjId,再扫描一次
            List<String> dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
            if (dataStoreObjectIds != null && dataStoreObjectIds.size() > 0) {
                for (String dsObjId : dataStoreObjectIds) {
                    String listStr = vcsdkUtils.getMountHostsByDsObjectId(dsObjId);
                    if (!StringUtils.isEmpty(listStr)) {
                        List<Map<String, String>> lists = gson.fromJson(listStr,
                            new TypeToken<List<Map<String, String>>>() { }.getType());
                        for (Map<String, String> hostMap : lists) {
                            String hostObjId = hostMap.get(HOSTID);
                            vcsdkUtils.scanDataStore(null, hostObjId);
                        }
                    }
                }
            }
        }

        // 重新扫描关联关系 更新数据库
        scanVmfs();
    }

    // DME侧从主机卸载卷
    private String unmountHostGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = hostUnmapping(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    // DME侧从主机组卸载卷
    private String unmountHostGroupGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = hostGroupUnmapping(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    // DME侧删除磁盘 获取任务ID
    private String volumeDeleteGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = volumeDelete(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    private ResponseEntity hostUnmapping(Map<String, Object> params) throws DmeException {
        String hostId = ToolUtils.getStr(params.get(HOST_ID));
        Object volumeIds = params.get(VOLUMEIDS);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(HOST_ID, hostId);
        requestbody.put(VOLUME_IDS, volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_HOST_UNMAPPING_URL, HttpMethod.POST,
            gson.toJson(requestbody));
        return responseEntity;
    }

    private ResponseEntity hostGroupUnmapping(Map<String, Object> params) throws DmeException {
        String hostGroupId = ToolUtils.getStr(params.get(HOST_GROUP_ID1));
        Object volumeIds = params.get(VOLUMEIDS);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(HOST_GROUP_ID1, hostGroupId);
        requestbody.put(VOLUME_IDS, volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.HOSTGROUP_UNMAPPING, HttpMethod.POST,
            gson.toJson(requestbody));
        return responseEntity;
    }

    private ResponseEntity volumeDelete(Map<String, Object> params) throws DmeException {
        Object volumeIds = params.get(VOLUMEIDS);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(VOLUME_IDS, volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_VOLUME_DELETE_URL, HttpMethod.POST,
            gson.toJson(requestbody));
        return responseEntity;
    }

    private Map<String, DmeVmwareRelation> getDvrMap(List<DmeVmwareRelation> dvrlist) {
        // 整理关系表数据
        Map<String, DmeVmwareRelation> remap = null;
        if (dvrlist != null && dvrlist.size() > 0) {
            remap = new HashMap<>();
            for (DmeVmwareRelation dvr : dvrlist) {
                remap.put(dvr.getStoreId(), dvr);
            }
        }
        return remap;
    }

    private Map<String, String> getStorNameMap(List<Storage> list) {
        // 整理存储信息
        Map<String, String> remap = null;
        if (list != null && list.size() > 0) {
            remap = new HashMap<>();
            for (Storage sr : list) {
                remap.put(sr.getId(), sr.getName());
            }
        }

        return remap;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public void setDataStoreStatisticHistoryService(DataStoreStatisticHistoryService dataStoreStatisticHistoryService) {
        this.dataStoreStatisticHistoryService = dataStoreStatisticHistoryService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    private String getTaskId(ResponseEntity responseEntity) {
        String taskId = "";
        if (responseEntity != null) {
            if (RestUtils.RES_STATE_I_202 == responseEntity.getStatusCodeValue()) {
                Object hostGroupBody = responseEntity.getBody();
                if (hostGroupBody != null) {
                    JsonObject hostJson = new JsonParser().parse(hostGroupBody.toString()).getAsJsonObject();
                    taskId = ToolUtils.jsonToStr(hostJson.get(TASK_ID));
                }
            }
        }
        return taskId;
    }

    @Override
    public List<Map<String, Object>> getHostsByStorageId(String storageId) throws DmeException {
        List<Map<String, Object>> hostMapList = new ArrayList<>(DEFAULT_LEN);

        // 先查询vcenter侧的主机
        String listStr = vcsdkUtils.getHostsByDsObjectId(storageId, true);

        // 获取已挂载的集群，找到对应的主机，用于排除主机
        List<Map<String, Object>> clustermaps = getHostGroupsByStorageId(storageId);
        Map<String, String> excludehostmap = new HashMap<>();
        for (Map<String, Object> clustermap : clustermaps) {
            String clusterid = String.valueOf(clustermap.get(HOST_GROUP_ID));
            String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterid);
            if (!StringUtils.isEmpty(vmwarehosts)) {
                List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                for (Map<String, String> vmwarehostmap : vmwarehostlists) {
                    excludehostmap.put(vmwarehostmap.get(HOSTID), "true");
                }
            }
        }
        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> hosts = gson.fromJson(listStr,
                new TypeToken<List<Map<String, String>>>() { }.getType());

            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> host : hosts) {
                String hostId = ToolUtils.getStr(host.get(HOSTID));

                // 排除已挂载在集群中的主机
                if (excludehostmap.get(hostId) == null) {
                    String hostNmme = ToolUtils.getStr(host.get(HOST_NAME));
                    String initiatorId = checkToHost(hostId);
                    if (!StringUtils.isEmpty(initiatorId)) {
                        Map<String, Object> tempMap = new HashMap<>();
                        tempMap.put(HOSTID, hostId);
                        tempMap.put(HOST_NAME, hostNmme);
                        hostMapList.add(tempMap);
                    }
                }
            }
        }
        return hostMapList;
    }

    @Override
    public List<Map<String, Object>> getHostGroupsByStorageId(String storageId) throws DmeException {
        List<Map<String, Object>> hostGroupMapList = new ArrayList<>();

        // 取得vcenter中的所有cluster
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(storageId);
        if (dvr == null) {
            throw new DmeException("存储关联关系为空");
        }
        List<String> hostgroupids = getDmeAttachHostGroupByVolumeId(dvr.getVolumeId());
        Map<String, String> mappeddmegroups = new HashMap<>();
        for (String hostgroupid : hostgroupids) {
            Map<String, Object> hostgroupmap = dmeAccessService.getDmeHostGroup(hostgroupid);
            mappeddmegroups.put(String.valueOf(hostgroupmap.get(NAME_FIELD)), "has");
        }
        String listStr = vcsdkUtils.getMountClustersByDsObjectId(storageId, mappeddmegroups);
        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> clusters = gson.fromJson(listStr,
                new TypeToken<List<Map<String, String>>>() { }.getType());

            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> cluster : clusters) {
                String clusterId = ToolUtils.getStr(cluster.get(CLUSTER_ID));
                String clusterNmme = ToolUtils.getStr(cluster.get("clusterName"));
                String initiatorId = checkToHostGroup(clusterId);
                if (!StringUtils.isEmpty(initiatorId)) {
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put(HOST_GROUP_ID, clusterId);
                    tempMap.put("hostGroupName", clusterNmme);
                    hostGroupMapList.add(tempMap);
                }
            }
        }
        return hostGroupMapList;
    }

    private List<String> getDmeAttachHostGroupByVolumeId(String volumeId) throws DmeException {
        String url;
        List<String> groupids = new ArrayList<>();
        url = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.OK.value()) {
            throw new DmeException("search host id error");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        JsonObject volume = jsonObject.get(VOLUME_FIELD).getAsJsonObject();
        JsonArray attachments = volume.get("attachments").getAsJsonArray();
        for (JsonElement jsonElement : attachments) {
            JsonObject element = jsonElement.getAsJsonObject();
            String attachedHostGroupId = ToolUtils.jsonToStr(element.get("attached_host_group"));
            if (!"".equalsIgnoreCase(attachedHostGroupId)) {
                groupids.add(attachedHostGroupId);
            }
        }

        return groupids;
    }

    // 通过vcenter的主机ID 查询dme侧的主机信息
    private Map<String, Object> getDmeHostByHostObjeId(String hostObjId) throws DmeException {
        Map<String, Object> hostInfo = new HashMap<>();
        Map<String, Object> hbaMap = vcsdkUtils.getHbaByHostObjectId(hostObjId);
        if (hbaMap == null || hbaMap.size() == 0) {
            return hostInfo;
        }
        String initiatorName = ToolUtils.getStr(hbaMap.get(NAME_FIELD));

        // 取出DME所有主机
        List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
        if (hostlist == null || hostlist.size() == 0) {
            return hostInfo;
        }
        for (Map<String, Object> hostmap : hostlist) {
            if (hostmap != null && hostmap.get(ID_FIELD) != null) {
                // 通过主机ID查到对应的主机的启动器
                String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                // 得到主机的启动器
                List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                if (initiators != null && initiators.size() > 0) {
                    for (Map<String, Object> inimap : initiators) {
                        String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                        if (initiatorName.equals(portName)) {
                            hostInfo = hostmap;
                            break;
                        }
                    }
                }
            }

            // 如果已经找到的主机就不再循环
            if (hostInfo.size() > 0) {
                break;
            }
        }
        return hostInfo;
    }

    // 通过vcenter的集群ID 查询dme侧的主机组信息
    private Map<String, Object> getDmeHostGroupByClusterObjId(String clusterObjId) throws DmeException {
        Map<String, Object> hostGroupMap = new HashMap<>();
        String initiatorId = checkToHostGroup(clusterObjId);
        if (!StringUtils.isEmpty(initiatorId)) {
            hostGroupMap.put(HOST_GROUP_ID, initiatorId);
        }
        return hostGroupMap;
    }

    public boolean isVmfs(String objectId) throws DmeSqlException {
        List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(DmeConstants.STORE_TYPE_VMFS);
        for (DmeVmwareRelation dmeVmwareRelation : dvrlist) {
            if (dmeVmwareRelation.getStoreId().equalsIgnoreCase(objectId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<VmfsDataInfo> queryVmfs(String dsObjId) throws Exception {
        List<VmfsDataInfo> relists = null;
        List<DmeVmwareRelation> dvrlist;
        try {
            // 从关系表中取得DME卷与vcenter存储的对应关系
            dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(DmeConstants.STORE_TYPE_VMFS);
        } catch (DmeSqlException ex) {
            LOG.error("query vmfs error:", ex);
            throw ex;
        }

        if (dvrlist.size() == 0) {
            LOG.info("Vmfs listStr=null");
            return relists;
        }

        // 整理数据
        Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);

        // 取得所有的存储设备
        List<Storage> storagemap = dmeStorageService.getStorages();

        // 整理数据
        Map<String, String> stoNameMap = getStorNameMap(storagemap);

        // 取得vcenter中的所有vmfs存储。
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(DmeConstants.STORE_TYPE_VMFS);
        if (StringUtils.isEmpty(listStr)) {
            return relists;
        }
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        if (jsonArray != null && jsonArray.size() > 0) {
            relists = new ArrayList<>();
            for (int index = 0; index < jsonArray.size(); index++) {
                JsonObject jo = jsonArray.get(index).getAsJsonObject();

                String vmwareStoreobjectid = ToolUtils.jsonToStr(jo.get(OBJECTID));
                if (!StringUtils.isEmpty(vmwareStoreobjectid) && vmwareStoreobjectid.equals(dsObjId)) {
                    // 对比数据库关系表中的数据，只显示关系表中的数据
                    if (dvrMap != null && dvrMap.get(vmwareStoreobjectid) != null) {
                        VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                        double capacity = ToolUtils.getDouble(jo.get(CAPACITY)) / ToolUtils.GI;
                        double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                        double uncommitted = ToolUtils.getDouble(jo.get("uncommitted")) / ToolUtils.GI;

                        vmfsDataInfo.setName(ToolUtils.jsonToStr(jo.get(NAME_FIELD)));
                        vmfsDataInfo.setCapacity(capacity);
                        vmfsDataInfo.setFreeSpace(freeSpace);
                        vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);
                        vmfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get(OBJECTID)));

                        DmeVmwareRelation dvr = dvrMap.get(vmwareStoreobjectid);
                        String volumeId = dvr.getVolumeId();

                        // 这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                        getVolumeDetail(relists, stoNameMap, vmfsDataInfo, volumeId);
                    }
                }
            }
        }
        return relists;
    }

    private void getVolumeDetail(List<VmfsDataInfo> relists, Map<String, String> stoNameMap, VmfsDataInfo vmfsDataInfo,
        String volumeId) {
        String detailedVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
        try {
            ResponseEntity responseEntity = dmeAccessService.access(detailedVolumeUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject voljson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonObject vjson2 = voljson.getAsJsonObject(VOLUME_FIELD);

                vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(vjson2.get(ID_FIELD)));
                vmfsDataInfo.setVolumeName(ToolUtils.jsonToStr(vjson2.get(NAME_FIELD)));
                vmfsDataInfo.setStatus(ToolUtils.jsonToStr(vjson2.get("status")));
                vmfsDataInfo.setServiceLevelName(ToolUtils.jsonToStr(vjson2.get(SERVICE_LEVEL_NAME)));
                vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(vjson2.get("protected")));
                vmfsDataInfo.setWwn(ToolUtils.jsonToStr(vjson2.get(VOLUME_WWN)));

                String storageId = ToolUtils.jsonToStr(vjson2.get(STORAGE_ID));
                vmfsDataInfo.setDeviceId(storageId);
                vmfsDataInfo.setDevice(stoNameMap == null ? "" : stoNameMap.get(storageId));

                parseTuning(vmfsDataInfo, vjson2);
                relists.add(vmfsDataInfo);
            }
        } catch (DmeException e) {
            LOG.error("DME link error url:{},error:{}", detailedVolumeUrl, e.getMessage());
        }
    }

    @Override
    public boolean queryDatastoreByName(String name) {
        boolean isFlag = true;
        try {
            String dataStoreName = dmeVmwareRalationDao.getDataStoreByName(name);
            if (!StringUtils.isEmpty(dataStoreName)) {
                isFlag = false;
            }
        } catch (DmeSqlException e) {
            isFlag = false;
        }
        return isFlag;
    }

    // 查询dataStrore上的vm 看是否关联了指定的主机或集群
    private boolean dataStoreVmRelateHostOrCluster(String dsObjid, String hostObjId, String clusterObjId) {
        // 1 查询datastore上的vm
        // 2 vm是否关联了hostObjId 或 clusterObjId (objId转id?)
        return false;
    }
    private String getStorageModel(String storageId) throws DmeException {
        return dmeStorageService.getStorageDetail(storageId).getModel();
    }

    private String getStorageModelByWwn(String wwn) throws DmeSqlException {
        return dmeVmwareRalationDao.getStorageModelByWwn(wwn);
    }
}
