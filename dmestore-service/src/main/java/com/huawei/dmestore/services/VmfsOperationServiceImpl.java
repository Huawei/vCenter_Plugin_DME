package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.*;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VmfsOperationService
 *
 * @author lianqiang
 * @since 2020-09-09
 **/
public class VmfsOperationServiceImpl implements VmfsOperationService {
    private static final Logger LOG = LoggerFactory.getLogger(VmfsOperationServiceImpl.class);

    private static final int DEFAULT_CAPACITY = 16;

    private static final String CODE_403 = "403";

    private static final String CODE_20883 = "20883";

    private static final String CODE_503 = "503";

    private static final String TASK_ID = "task_id";

    private DmeAccessService dmeAccessService;

    private VmfsAccessServiceImpl vmfsAccessService;

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    private TaskService taskService;

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public VmfsAccessServiceImpl getVmfsAccessService() {
        return vmfsAccessService;
    }

    public void setVmfsAccessService(VmfsAccessServiceImpl vmfsAccessService) {
        this.vmfsAccessService = vmfsAccessService;
    }

    @Override
    public void updateVmfs(String volumeId, Map<String, Object> params) throws DmeException {
        Map<String, Object> volumeMap = new HashMap<>();
        Object serviceLevelName = params.get("service_level_name");
        if (StringUtils.isEmpty(serviceLevelName)) {
            Map<String, Object> customizeVolumeTuning = getCustomizeVolumeTuning(params);
            LOG.info("自定义方式创建vmfs{},服务等级：", serviceLevelName);
            volumeMap.put("tuning", customizeVolumeTuning);
        }

        Object newVoName = params.get("newVoName");
        if (!StringUtils.isEmpty(newVoName)) {
            volumeMap.put("name", newVoName.toString());
        }

        Map<String, Object> reqMap = new HashMap<>(DEFAULT_CAPACITY);
        reqMap.put("volume", volumeMap);
        String reqBody = gson.toJson(reqMap);

        String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
        try {
            Object oldDsName = params.get("oldDsName");
            Object newDsName = params.get("newDsName");
            if (StringUtils.isEmpty(oldDsName) || StringUtils.isEmpty(newDsName)) {
                throw new DmeException(CODE_403, "datastore params error");
            }
            Object dataStoreObjectId = params.get("dataStoreObjectId");
            String result = "";
            if (dataStoreObjectId != null) {
                result = vcsdkUtils.renameDataStore(newDsName.toString(), dataStoreObjectId.toString());
            }
            if (StringUtils.isEmpty(result) || "failed".equals(result)) {
                throw new DmeException(CODE_503, "vmware update VmfsDatastore failed!");
            }

            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, reqBody);
            int code = responseEntity.getStatusCodeValue();
            LOG.info("dme update vmfs,response:code={},response body={}", code, responseEntity.getBody());
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DmeException(CODE_503, "dme update VmfsDatastore failed!");
            }
            JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            Boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                LOG.error("update vmfs datastore error");
                throw new DmeException("400", "update vmfs datastore error");
            }
        } catch (DmeException e) {
            LOG.error("update vmfsDatastore error", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    private Map<String, Object> getCustomizeVolumeTuning(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        Object controlPolicy = params.get("control_policy");
        if (!StringUtils.isEmpty(controlPolicy)) {
            map.put("control_policy", Integer.valueOf(controlPolicy.toString()));
        }
        Object maxIops = params.get("max_iops");
        if (!StringUtils.isEmpty(maxIops)) {
            map.put("maxiops", Integer.valueOf(maxIops.toString()));
        }
        Object minIops = params.get("min_iops");
        if (!StringUtils.isEmpty(minIops)) {
            map.put("miniops", Integer.valueOf(minIops.toString()));
        }
        Object maxBandwidth = params.get("max_bandwidth");
        if (!StringUtils.isEmpty(maxBandwidth)) {
            map.put("maxbandwidth", Integer.valueOf(maxBandwidth.toString()));
        }
        Object minBandwidth = params.get("min_bandwidth");
        if (!StringUtils.isEmpty(minBandwidth)) {
            map.put("minbandwidth", Integer.valueOf(minBandwidth.toString()));
        }

        Object latency = params.get("latency");
        if (!StringUtils.isEmpty(latency)) {
            map.put("latency", Integer.valueOf(latency.toString()));
        }
        map.put("enabled", true);
        Map<String, Object> customizeVolumeTuning = new HashMap<>();
        customizeVolumeTuning.put("smartqos",map);
        return customizeVolumeTuning;
    }

    @Override
    public void expandVmfs(Map<String, String> volumemap) throws DmeException {
        Map<String, Object> reqBody = new HashMap<>(DEFAULT_CAPACITY);
        List<String> volumeIds = new ArrayList<>(DEFAULT_CAPACITY);

        String volumeId = volumemap.get("volume_id");
        String datastoreobjid = volumemap.get("obj_id");
        String voAddCapacity = volumemap.get("vo_add_capacity");
        if (!StringUtils.isEmpty(volumeId) && !StringUtils.isEmpty(voAddCapacity)) {
            volumeIds.add(volumeId);
        }
        volumeIds.add(volumeId);
        reqBody.put("volume_id", volumeId);
        reqBody.put("added_capacity", Integer.valueOf(volumemap.get("vo_add_capacity")));
        List<Object> reqList = new ArrayList<>(DEFAULT_CAPACITY);
        reqList.add(reqBody);
        Map<String, Object> reqMap = new HashMap<>(DEFAULT_CAPACITY);
        reqMap.put("volumes", reqList);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.API_VMFS_EXPAND,
                HttpMethod.POST, gson.toJson(reqMap));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DmeException(CODE_503, "expand vmfsDatastore failed !");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
            List<String> taskIds = new ArrayList<>(DEFAULT_CAPACITY);
            taskIds.add(taskId);
            Boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                throw new DmeException(CODE_503, "expand volume failed !");
            }
            String dsName = volumemap.get("ds_name");
            String result = null;
            if (!StringUtils.isEmpty(voAddCapacity) && !StringUtils.isEmpty(datastoreobjid)) {
                result = vcsdkUtils.expandVmfsDatastore(dsName, ToolUtils.getInt(voAddCapacity), datastoreobjid);
            }
            if ("failed".equals(result)) {
                throw new DmeException(CODE_403, "expand vmfsDatastore failed !");
            }

            // 刷新vCenter存储
            vcsdkUtils.refreshDatastore(datastoreobjid);
        } catch (DmeException e) {
            LOG.error("expand vmfsDatastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public void recycleVmfsCapacity(List<String> dsObjectIds) throws DmeException {
        try {
            String result = null;
            if (dsObjectIds != null && dsObjectIds.size() > 0) {
                for (int index = 0; index < dsObjectIds.size(); index++) {
                    result = vcsdkUtils.recycleVmfsCapacity(dsObjectIds.get(index));
                }
            }
            if (result == null || "error".equals(result)) {
                throw new DmeException(CODE_403, "recycle vmfsDatastore error");
            }
        } catch (VcenterException e) {
            LOG.error("recycle vmfsDatastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public boolean canRecycleVmfsCapacity(List<String> dsObjectIds) throws DmeException {

        boolean isThinVmdatastore = false;
        if (dsObjectIds != null && dsObjectIds.size() > 0) {
            for (int index = 0; index < dsObjectIds.size(); index++) {
                List<VmfsDatastoreVolumeDetail> detaillists = vmfsAccessService.volumeDetail(dsObjectIds.get(index));
                for (VmfsDatastoreVolumeDetail vmfsDatastoreVolumeDetail:detaillists)
                {
                    if ("thin".equalsIgnoreCase(vmfsDatastoreVolumeDetail.getProvisionType()))  {
                        isThinVmdatastore = true;
                        break;
                    }
                }
            }
        }
        return isThinVmdatastore;


    }

    @Override
    public void recycleVmfsCapacityByDataStoreIds(List<String> dsIds) throws DmeException {
        try {
            String result = null;
            if (dsIds != null && dsIds.size() > 0) {
                for (int index = 0; index < dsIds.size(); index++) {
                    result = vcsdkUtils.recycleVmfsCapacity(dsIds.get(index));
                }
            }
            if (result == null || "error".equals(result)) {
                throw new DmeException(CODE_403, "recycle vmfsDatastore error");
            }
        } catch (VcenterException e) {
            LOG.error("recycle vmfsDatastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public void updateVmfsServiceLevel(Map<String, Object> params) throws DmeException {
        if (params == null || params.size() == 0) {
            throw new DmeException(CODE_403, "params error,please check your params!");
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("service_level_id", params.get("service_level_id"));
        reqMap.put("volume_ids", params.get("volume_ids"));
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.API_SERVICELEVEL_UPDATE,
                HttpMethod.POST, gson.toJson(reqMap));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DmeException(CODE_503, "update vmfs service level error!");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String taskId = jsonObject.get(TASK_ID).getAsString();
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                throw new DmeException("400", "update vmfs service level failed");
            }
        } catch (DmeException e) {
            LOG.error("update vmfs service level error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public List<SimpleServiceLevel> listServiceLevelVmfs(Map<String, Object> params) throws DmeException {
        List<SimpleServiceLevel> simpleServiceLevels = new ArrayList<>(DEFAULT_CAPACITY);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.LIST_SERVICE_LEVEL_URL,
                HttpMethod.GET, gson.toJson(params));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list vmfs service level error !");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                SimpleServiceLevel simpleServiceLevel = new SimpleServiceLevel();
                parseBaseInfo(element, simpleServiceLevel);
                SimpleCapabilities capability = new SimpleCapabilities();
                JsonObject capabilities = element.get("capabilities").getAsJsonObject();
                capability.setResourceType(ToolUtils.jsonToStr(capabilities.get("resource_type")));
                capability.setCompression(ToolUtils.jsonToBoo(capabilities.get("compression")));
                capability.setDeduplication(ToolUtils.jsonToBoo(capabilities.get("deduplication")));
                CapabilitiesSmarttier smarttier = new CapabilitiesSmarttier();
                if (!"".equals(ToolUtils.jsonToStr(capabilities.get("smarttier")))) {
                    JsonObject smarttiers = capabilities.get("smarttier").getAsJsonObject();
                    smarttier.setPolicy(ToolUtils.jsonToInt(smarttiers.get("policy"), 0));
                    smarttier.setEnabled(ToolUtils.jsonToBoo(smarttiers.get("enabled")));
                }
                capability.setSmarttier(smarttier);

                CapabilitiesQos capabilitiesQos = new CapabilitiesQos();
                JsonObject qos = null;
                if (!"".equals(ToolUtils.getStr(capabilities.get("qos")))) {
                    qos = capabilities.get("qos").getAsJsonObject();
                    capabilitiesQos.setEnabled(ToolUtils.jsonToBoo(qos.get("enabled")));
                }
                SmartQos smartQos = new SmartQos();
                if (qos != null && !"".equals(ToolUtils.getStr(qos.get("qos_param")))) {
                    qosParse(qos, smartQos);
                }

                capabilitiesQos.setSmartQos(smartQos);
                capability.setQos(capabilitiesQos);
                simpleServiceLevel.setCapabilities(capability);
                simpleServiceLevels.add(simpleServiceLevel);
            }
        } catch (DmeException e) {
            LOG.error("list vmfs service level success !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return simpleServiceLevels;
    }

    private void qosParse(JsonObject qos, SmartQos smartQos) {
        JsonObject jsonObject1 = qos.get("qos_param").getAsJsonObject();
        smartQos.setLatency(ToolUtils.jsonToInt(jsonObject1.get("latency"), 0));
        smartQos.setMinbandwidth(ToolUtils.jsonToInt(jsonObject1.get("minBandWidth"), 0));
        smartQos.setMiniops(ToolUtils.jsonToInt(jsonObject1.get("minIOPS"), 0));
        smartQos.setLatencyUnit(ToolUtils.jsonToStr(jsonObject1.get("latencyUnit")));
    }

    private void parseBaseInfo(JsonObject element, SimpleServiceLevel simpleServiceLevel) {
        simpleServiceLevel.setId(ToolUtils.jsonToStr(element.get("id")));
        simpleServiceLevel.setName(ToolUtils.jsonToStr(element.get("name")));
        simpleServiceLevel.setDescription(ToolUtils.jsonToStr(element.get("description")));
        simpleServiceLevel.setType(ToolUtils.jsonToStr(element.get("type")));
        simpleServiceLevel.setProtocol(ToolUtils.jsonToStr(element.get("protocol")));
        simpleServiceLevel.setTotalCapacity(ToolUtils.jsonToDou(element.get("total_capacity"), 0.0));
        simpleServiceLevel.setFreeCapacity(ToolUtils.jsonToDou(element.get("free_capacity"), 0.0));
        simpleServiceLevel.setUsedCapacity(ToolUtils.jsonToDou(element.get("used_capacity"), 0.0));
    }
}
