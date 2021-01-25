package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.CreateVolumesRequest;
import com.huawei.dmestore.model.CustomizeVolumeTuningForCreate;
import com.huawei.dmestore.model.CustomizeVolumes;
import com.huawei.dmestore.model.CustomizeVolumesRequest;
import com.huawei.dmestore.model.ServiceVolumeBasicParams;
import com.huawei.dmestore.model.ServiceVolumeMapping;
import com.huawei.dmestore.model.SmartQosForRdmCreate;
import com.huawei.dmestore.model.VmRdmCreateBean;
import com.huawei.dmestore.utils.StringUtil;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VmRdmServiceImpl
 *
 * @author wangxiangyong
 * @since 2020-09-15
 **/
public class VmRdmServiceImpl implements VmRdmService {
    private static final Logger LOG = LoggerFactory.getLogger(VmRdmServiceImpl.class);

    private static final int MAP_DEFAULT_CAPACITY = 16;

    private static final int CONSTANT_1024 = 1024;

    private static final int THOUSAND = 1000;

    private static final String UNITTB = "TB";

    private static final String NAME_FIELD = "name";

    private static final String ID_FIELD = "id";

    private static final String CAPACITY = "capacity";

    private DmeAccessService dmeAccessService;

    private TaskService taskService;

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    private VmfsAccessService vmfsAccessService;

    public VmfsAccessService getVmfsAccessService() {
        return vmfsAccessService;
    }

    public void setVmfsAccessService(VmfsAccessService vmfsAccessService) {
        this.vmfsAccessService = vmfsAccessService;
    }

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

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void createRdm(String dataStoreObjectId, String vmObjectId, VmRdmCreateBean createBean) throws DmeException {
        createDmeRdm(createBean);
        Map<String, Object> paramMap = initParams(createBean);
        String requestVolumeName = (String) paramMap.get("requestVolumeName");
        int capacity = (int) paramMap.get(CAPACITY);
        JsonArray volumeArr = getDmeVolumeIdByName(requestVolumeName);
        List<String> volumeIds = new ArrayList<>();
        for (int index = 0; index < volumeArr.size(); index++) {
            volumeIds.add(volumeArr.get(index).getAsJsonObject().get(ID_FIELD).getAsString());
        }

        // vmware上虚拟机归属的主机查询
        Map<String, String> vcenterHostMap = vcsdkUtils.getHostByVmObjectId(vmObjectId);

        // vmware上已挂载存储的所有主机
        String hostsJsonStr = vcsdkUtils.getMountHostsByDsObjectId(dataStoreObjectId);
        List<Map<String, String>> hostListOnVmware = gson.fromJson(hostsJsonStr,
            new TypeToken<List<Map<String, String>>>() { }.getType());
        String hostIp = vcenterHostMap.get("hostName");
        String hostObjectId = vcenterHostMap.get("hostId");
        String hostId = getDmeHostId(hostIp, hostObjectId);
        boolean isFind = false;
        for(int index = 0; index < hostListOnVmware.size(); index ++){
            String tempHostObjectId = hostListOnVmware.get(index).get("hostId");
            if(hostObjectId.equals(tempHostObjectId)){
                isFind = true;
                break;
            }
        }
        if(!isFind){
            hostListOnVmware.add(vcenterHostMap);
        }
        List<String> dmeMapingHostIds = new ArrayList<>();
        if (paramMap.get("mapping") == null) {
            for(int index = 0; index < hostListOnVmware.size(); index ++){
                String tempHostIp = hostListOnVmware.get(index).get("hostName");
                String tempHostObjectId = hostListOnVmware.get(index).get("hostId");
                String tempDmeHostId = getDmeHostId(tempHostIp, tempHostObjectId);
                dmeMapingHostIds.add(tempDmeHostId);
                dmeAccessService.hostMapping(tempDmeHostId, volumeIds);
            }
        }
        vcsdkUtils.hostRescanVmfs(hostIp);
        vcsdkUtils.hostRescanHba(hostIp);
        Map<String, JsonObject> lunMap = getLunMap(volumeArr, volumeIds, hostIp, hostId);
        String errorMsg = "";
        int lunSize = lunMap.size();
        if (lunSize > 0) {
            List<String> failedVolumeIds = new ArrayList();
            for (Map.Entry<String, JsonObject> entry : lunMap.entrySet()) {
                String volumeId = entry.getKey();
                JsonObject object = entry.getValue();
                try {
                    vcsdkUtils.createDisk(dataStoreObjectId, vmObjectId, object.get("devicePath").getAsString(), capacity);
                } catch (VcenterException ex) {
                    failedVolumeIds.add(volumeId);
                    errorMsg = ex.getMessage();
                }
            }
            if (failedVolumeIds.size() > 0) {
                for(String dmeHostId : dmeMapingHostIds){
                    unMapping(dmeHostId, failedVolumeIds);
                }
                deleteVolumes(failedVolumeIds);
                if (failedVolumeIds.size() == lunSize) {
                    throw new DmeException(errorMsg);
                }
            }
        } else {
            for(String dmeHostId : dmeMapingHostIds){
                unMapping(dmeHostId, volumeIds);
            }
            deleteVolumes(volumeIds);
            throw new DmeException("No matching LUN information was found on the vCenter");
        }
    }

    private Map<String, Object> initParams(VmRdmCreateBean createBean) {
        String requestVolumeName;
        int capacity;
        ServiceVolumeMapping mapping;
        ServiceVolumeBasicParams params;
        if (createBean.getCreateVolumesRequest() != null) {
            requestVolumeName = createBean.getCreateVolumesRequest().getVolumes().get(0).getName();
            params = createBean.getCreateVolumesRequest().getVolumes().get(0);
            capacity = params.getCapacity();
            if (UNITTB.equals(params.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            mapping = createBean.getCreateVolumesRequest().getMapping();
        } else {
            requestVolumeName = createBean.getCustomizeVolumesRequest()
                .getCustomizeVolumes()
                .getVolumeSpecs()
                .get(0)
                .getName();
            params = createBean.getCustomizeVolumesRequest().getCustomizeVolumes().getVolumeSpecs().get(0);
            capacity = params.getCapacity();
            if (UNITTB.equals(params.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            mapping = createBean.getCustomizeVolumesRequest().getMapping();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("requestVolumeName", requestVolumeName);
        map.put(CAPACITY, capacity);
        if (mapping != null) {
            map.put("mapping", mapping);
        }

        return map;
    }

    private JsonArray getDmeVolumeIdByName(String requestVolumeName) throws DmeException {
        String url = DmeConstants.DME_VOLUME_BASE_URL + "?name=" + requestVolumeName;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        JsonArray volumeArr = jsonObject.getAsJsonArray("volumes");
        return volumeArr;
    }

    private Map<String, JsonObject> getLunMap(JsonArray volumeArr, List<String> volumeIds, String hostIp, String hostId)
        throws DmeException {
        String lunStr = vcsdkUtils.getLunsOnHost(hostIp);
        if (StringUtil.isBlank(lunStr)) {
            // 将已经创建好的卷删除
            deleteVolumes(hostId, volumeIds);
            throw new DmeException("Failed to obtain the target LUN!");
        }
        LOG.info("get LUN information succeeded!");
        JsonArray lunArray = gson.fromJson(lunStr, JsonArray.class);
        Map<String, JsonObject> lunMap = new HashMap<>(MAP_DEFAULT_CAPACITY);
        for (int index = 0; index < lunArray.size(); index++) {
            JsonObject lunObject = lunArray.get(index).getAsJsonObject();
            for (int indexInner = 0; indexInner < volumeArr.size(); indexInner++) {
                JsonObject volume = volumeArr.get(indexInner).getAsJsonObject();
                String wwn = volume.get("volume_wwn").getAsString();
                if (ToolUtils.jsonToStr(lunObject.get("deviceName")).endsWith(wwn)) {
                    lunMap.put(ToolUtils.jsonToStr(volume.get(ID_FIELD)), lunObject);
                }
            }
        }
        return lunMap;
    }

    private String getDmeHostId(String hostIp, String hostObjectId) throws DmeException {
        String url = DmeConstants.DME_HOST_SUMMARY_URL;
        Map<String, String> queryHostParams = new HashMap<>(MAP_DEFAULT_CAPACITY);
        queryHostParams.put("ip", hostIp);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST,
            gson.toJson(queryHostParams));
        String hostId = null;
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            JsonObject dmeHost = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            if (dmeHost.get("total").getAsInt() > 0) {
                JsonObject hostObject = dmeHost.getAsJsonArray("hosts").get(0).getAsJsonObject();
                hostId = ToolUtils.jsonToStr(hostObject.get(ID_FIELD));
            }
        }

        if (hostId == null) {
            hostId = vmfsAccessService.checkOrCreateToHost(hostIp, hostObjectId);
        }
        return hostId;
    }

    private void deleteVolumes(String hostId, List<String> volumeIds) {
        try {
            dmeAccessService.unMapHost(hostId, volumeIds);
            dmeAccessService.deleteVolumes(volumeIds);
        } catch (DmeException ex) {
            LOG.error("delete volumes failed!");
        }
    }

    private void deleteVolumes(List<String> volumeIds) {
        try {
            dmeAccessService.deleteVolumes(volumeIds);
        } catch (DmeException ex) {
            LOG.error("delete volumes failed!");
        }
    }

    private void unMapping(String hostId, List<String> volumeIds) {
        try {
            dmeAccessService.unMapHost(hostId, volumeIds);
        } catch (DmeException ex) {
            LOG.error("unMapping volumes failed!");
        }
    }

    public void createDmeRdm(VmRdmCreateBean vmRdmCreateBean) throws DmeException {
        String taskId;

        // 通过服务等级创建卷
        if (vmRdmCreateBean.getCreateVolumesRequest() != null) {
            taskId = createDmeVolumeByServiceLevel(vmRdmCreateBean.getCreateVolumesRequest());
        } else {
            // 用户自定义创建卷
            taskId = createDmeVolumeByUnServiceLevelNew(vmRdmCreateBean.getCustomizeVolumesRequest());
        }
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            LOG.error("The DME volume is in abnormal condition!taskDetail={}", gson.toJson(taskDetail));
            throw new DmeException(taskDetail.get("detail_cn").getAsString());
        }
    }

    private String createDmeVolumeByServiceLevel(CreateVolumesRequest createVolumesRequest) throws DmeException {
        Map<String, Object> requestbody = new HashMap<>(MAP_DEFAULT_CAPACITY);
        List<ServiceVolumeBasicParams> requestVolumes = createVolumesRequest.getVolumes();
        List<Map<String, Object>> volumes = new ArrayList<>();
        for (ServiceVolumeBasicParams volume : requestVolumes) {
            Map<String, Object> svbp = new HashMap<>(MAP_DEFAULT_CAPACITY);
            svbp.put(NAME_FIELD, volume.getName());
            int capacity = volume.getCapacity();
            if (UNITTB.equals(volume.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            svbp.put(CAPACITY, capacity);
            svbp.put("count", volume.getCount());
            if (volume.getStartSuffix() != null && volume.getStartSuffix() > 0) {
                svbp.put("start_suffix", volume.getStartSuffix());
            }
            volumes.add(svbp);
        }

        requestbody.put("volumes", volumes);
        requestbody.put("service_level_id", createVolumesRequest.getServiceLevelId());
        String url = DmeConstants.DME_VOLUME_BASE_URL;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestbody));
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
            != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("Failed to create RDM by service_level!errorMsg:{}", responseEntity.getBody());
            throw new DmeException(responseEntity.getBody());
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    private String createDmeVolumeByUnServiceLevelNew(CustomizeVolumesRequest customizeVolumesRequest)
        throws DmeException {
        String ownerController = customizeVolumesRequest.getCustomizeVolumes().getOwnerController();

        // 归属控制器自动则不下发参数
        final String ownerControllerAuto = "0";
        if (ownerControllerAuto.equals(ownerController)) {
            customizeVolumesRequest.getCustomizeVolumes().setOwnerController(null);
        }

        // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
        Map<String, Object> requestbody = new HashMap<>(MAP_DEFAULT_CAPACITY);
        CustomizeVolumes customizeVolumes = customizeVolumesRequest.getCustomizeVolumes();
        putNotNull(requestbody, "initial_distribute_policy",
            DmeConstants.INITIAL_DISTRIBUTE_POLICY.get(customizeVolumes.getInitialDistributePolicy()));
        putNotNull(requestbody, "prefetch_value", customizeVolumes.getPrefetchValue());
        putNotNull(requestbody, "prefetch_policy",
            DmeConstants.PREFETCH_POLICY.get(customizeVolumes.getPrefetchPolicy()));
        putNotNull(requestbody, "owner_controller", customizeVolumes.getOwnerController());
        putNotNull(requestbody, "pool_id", customizeVolumes.getPoolRawId());
        putNotNull(requestbody, "storage_id", customizeVolumes.getStorageId());
        CustomizeVolumeTuningForCreate tuningBean = customizeVolumes.getTuning();
        if (tuningBean != null) {
            Map<String, Object> tuning = tuningParse(tuningBean);
            requestbody.put("tuning", tuning);
        }

        List<ServiceVolumeBasicParams> volumeSpecList = customizeVolumes.getVolumeSpecs();
        List<Map<String, Object>> volumeSpecs = new ArrayList<>();
        for (ServiceVolumeBasicParams volumeSpec : volumeSpecList) {
            Map<String, Object> vs = new HashMap<>(MAP_DEFAULT_CAPACITY);
            putNotNull(vs, NAME_FIELD, volumeSpec.getName());
            int capacity = volumeSpec.getCapacity();
            if (UNITTB.equals(volumeSpec.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            vs.put(CAPACITY, capacity);
            vs.put("count", volumeSpec.getCount());
            volumeSpecs.add(vs);
        }
        requestbody.put("lun_specs", volumeSpecs);
        String url = DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestbody));
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
            != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("Failed to create RDM on DME!errorMsg:{}", responseEntity.getBody());
            throw new DmeException(responseEntity.getBody());
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    private Map<String, Object> tuningParse(CustomizeVolumeTuningForCreate tuningBean) {
        Map<String, Object> tuning = new HashMap<>();
        putNotNull(tuning, "alloction_type", tuningBean.getAlloctype());
        putNotNull(tuning, "smart_tier", DmeConstants.SMART_TIER.get(tuningBean.getSmarttier()));
        putNotNull(tuning, "workload_type_raw_id", tuningBean.getWorkloadTypeId());
        putNotNull(tuning, "compression_enabled", tuningBean.getCompressionEnabled());
        putNotNull(tuning, "deduplication_enabled", tuningBean.getDedupeEnabled());

        SmartQosForRdmCreate smartqosBean = tuningBean.getSmartqos();
        if (smartqosBean != null) {
            Map<String, Object> smartqos = new HashMap<>(MAP_DEFAULT_CAPACITY);
            putNotNull(smartqos, "latency", smartqosBean.getLatency());
            putNotNull(smartqos, "max_bandwidth", smartqosBean.getMaxbandwidth());
            putNotNull(smartqos, "max_iops", smartqosBean.getMaxiops());
            putNotNull(smartqos, "min_bandwidth", smartqosBean.getMinbandwidth());
            putNotNull(smartqos, "min_iops", smartqosBean.getMiniops());
            tuning.put("smart_qos", smartqos);
        }

        return tuning;
    }

    @Override
    public List<Map<String, Object>> getAllDmeHost() throws DmeException {
        return dmeAccessService.getDmeHosts(null);
    }

    @Override
    public List<Object> getDatastoreMountsOnHost(String vmObjectId) throws DmeException {
        return vcsdkUtils.getDatastoreMountsOnHost(vmObjectId);
    }

    private void putNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            if (value instanceof String) {
                map.put(key, String.valueOf(value));
            }
            map.put(key, value);
        }
    }
}
