package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DmeIndicatorConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.RelationInstance;
import com.huawei.dmestore.utils.ToolUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DataStoreStatisticHistoryService
 *
 * @author liuxh
 * @since 2020-09-03
 **/
public class DataStoreStatisticHistoryServiceImpl implements DataStoreStatisticHistoryService {
    private static final Logger log = LoggerFactory.getLogger(DataStoreStatisticHistoryService.class);

    private static final String SYS_LUN_OBJTYPEID = "1125921381679104";

    private static final String OBJ_TYPE_ID_FIELD = "obj_type_id";

    private static final String BEGIN_TIME = "begin_time";

    private static final String END_TIME = "end_time";

    private static final String INDICATOR_IDS_FIELD = "indicator_ids";

    private static final String OBJ_IDS_FIELD = "obj_ids";

    private static final String RESID_FIELD = "resId";

    private static final String ERROR_MSG = "error_msg";

    private static final String RANGE = "range";

    private static final long TWENTY_FOUR_HOURS = 24 * 60 * 60 * 1000;

    private static final int MAX_OBJ_INDICATOR = 50;

    private static final String MAX_LABEL = "max";

    private static final String VOLUME_INSTANCE = "volume";

    private static final String INTERVAL = "interval";

    private Gson gson = new Gson();

    @Autowired
    private DmeAccessService dmeAccessService;

    @Autowired
    private DmeRelationInstanceService dmeRelationInstanceService;

    @Override
    public Map<String, Object> queryVmfsStatistic(Map<String, Object> params) throws DmeException {
        // 通过存储ID查卷ID 实际获取卷的性能数据
        return queryVolumeStatistic(params);
    }

    @Override
    public Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        List<String> instanceIds = new ArrayList<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        List<String> ids = (List<String>) params.get(OBJ_IDS_FIELD);
        if (null != ids && ids.size() > 0) {
            Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getLunInstance();
            for (String id : ids) {
                String instanceId = sysLunMap.get(id).get(RESID_FIELD).toString();
                if (!StringUtils.isEmpty(instanceId)) {
                    idInstancdIdMap.put(id, instanceId);
                    instanceIds.add(instanceId);
                }
            }
            if (instanceIds.size() > 0) {
                params.put(OBJ_IDS_FIELD, instanceIds);
            }
        }
        if (null == indicatorIds) {
            indicatorIds = initVolumeIndicator(true);
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }

        // SYS_Lun
        String objTypeId = SYS_LUN_OBJTYPEID;
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        return queryHistoryStatistic("queryVmfsStatisticCurrent", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryNfsStatistic(Map<String, Object> params) throws DmeException {
        return queryFsStatistic(params);
    }

    @Override
    public Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params) throws DmeException {
        // SYS_StorageFileSystem
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        if (null == indicatorIds) {
            indicatorIds = initFsIndicator(true);
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        String objTypeId = "1126179079716864";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        return queryHistoryStatistic("queryNfsStatisticCurrent", params, null);
    }

    @Override
    public Map<String, Object> queryVolumeStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamVolume(params, false);
        return queryHistoryStatistic(VOLUME_INSTANCE, params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryControllerStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamController(params, false);
        return queryHistoryStatistic("controller", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePortStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStoragePort(params, false);
        return queryHistoryStatistic("storagePort", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDiskStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStorageDisk(params, false);
        return queryHistoryStatistic("storageDisk", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryFsStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamFs(params, false);
        return queryHistoryStatistic("storageFileSystem", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryServiceLevelStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamServiceLevel(params);
        return queryHistoryStatistic("serviceLevel", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryServiceLevelLunStatistic(Map<String, Object> params) throws DmeException {
        // SYS_LUN
        Map<String, String> idInstanceIdMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        List<String> instanceIds = new ArrayList<>();
        String objTypeId = SYS_LUN_OBJTYPEID;
        String relationName = "M_DjTierContainsLun";
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        Object objIds = params.get(OBJ_IDS_FIELD);
        List<String> ids = getObjIds(objIds);
        if (ids.size() > 0) {
            for (String id : ids) {
                // 查询serviceLevel关联的lun实例ID
                String targetInstanceId = getTargetInstanceIdByRelationNameSourceId(relationName, id);
                if (!StringUtils.isEmpty(targetInstanceId)) {
                    idInstanceIdMap.put(id, targetInstanceId);
                    instanceIds.add(targetInstanceId);
                }
            }
            if (instanceIds.size() > 0) {
                params.put(OBJ_IDS_FIELD, instanceIds);
            }
        }
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelLunIndicator();
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return queryHistoryStatistic("serviceLevel Lun", params, idInstanceIdMap);
    }

    @Override
    public Map<String, Object> queryServiceLevelStoragePoolStatistic(Map<String, Object> params) throws DmeException {
        // SYS_StoragePool
        Map<String, String> idInstanceIdMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        List<String> instanceIds = new ArrayList<>();
        String objTypeId = "1125912791744512";
        String relationName = "M_DjTierContainsStoragePool";
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        Object objIds = params.get(OBJ_IDS_FIELD);
        List<String> ids = getObjIds(objIds);
        if (ids.size() > 0) {
            for (String id : ids) {
                // 查询serviceLevel关联的StoragePool的实例ID
                String targetInstanceId = getTargetInstanceIdByRelationNameSourceId(relationName, id);
                if (!StringUtils.isEmpty(targetInstanceId)) {
                    idInstanceIdMap.put(id, targetInstanceId);
                    instanceIds.add(targetInstanceId);
                }
            }
            if (instanceIds.size() > 0) {
                params.put(OBJ_IDS_FIELD, instanceIds);
            }
        }
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelStoragePoolIndicator();
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return queryHistoryStatistic("serviceLevel StroagePool", params, idInstanceIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePoolStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStoragePool(params);
        return queryHistoryStatistic("queryStoragePoolStatistic", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDevcieStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStorageDevice(params);
        return queryHistoryStatistic("queryStorageDevcieStatistic", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDevcieCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStorageDevice(params);
        return queryCurrentStatistic("StorageDevcie", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePoolCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStoragePool(params);
        return queryCurrentStatistic("StoragePool", params, idInstancdIdMap);
    }

    public Map<String, Object> queryVolumeCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamVolume(params, true);
        return queryCurrentStatistic(VOLUME_INSTANCE, params, idInstancdIdMap);
    }

    public Map<String, Object> queryServiceLevelCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamServiceLevel(params);
        return queryCurrentStatistic(VOLUME_INSTANCE, params, idInstancdIdMap);
    }

    public Map<String, Object> queryFsCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamFs(params, true);
        return queryCurrentStatistic(VOLUME_INSTANCE, params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryControllerCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamController(params, true);
        return queryCurrentStatistic("controller", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePortCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStoragePort(params, true);
        return queryCurrentStatistic("storageport", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDiskCurrentStatistic(Map<String, Object> params) throws DmeException {
        Map<String, String> idInstancdIdMap = initParamStorageDisk(params, true);
        return queryCurrentStatistic("storagedisk", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryHistoryStatistic(String relationOrInstance, Map<String, Object> params)
        throws DmeException {
        Map<String, Object> resultMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        if (!StringUtils.isEmpty(relationOrInstance)) {
            switch (relationOrInstance) {
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDEVICE:
                    resultMap = queryStorageDevcieStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPOOL:
                    resultMap = queryStoragePoolStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_LUN:
                    resultMap = queryVolumeStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_SERVICELEVEL:
                    resultMap = queryServiceLevelStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_FILESYSTEM:
                    resultMap = queryFsStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_CONTROLLER:
                    resultMap = queryControllerStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPORT:
                    resultMap = queryStoragePortStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDISK:
                    resultMap = queryStorageDiskStatistic(params);
                    break;
                default:
                    log.error("Unsupported historical performance data queries");
                    break;
            }
        }
        return resultMap;
    }

    private Map<String, Object> queryHistoryStatistic(String relationOrInstance, Map<String, Object> params,
        Map<String, String> idInstanceIdMap) throws DmeException {
        Map<String, Object> resultmap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        ResponseEntity responseEntity;
        JsonElement statisticElement;
        List<List<String>> objIdGroup = groupObjIds(params);
        if (null != objIdGroup && objIdGroup.size() > 0) {
            for (List<String> objids : objIdGroup) {
                params.put(OBJ_IDS_FIELD, objids);
                try {
                    responseEntity = queryStatistic(params);
                    if (null != responseEntity
                        && DmeConstants.HTTPS_STATUS_SUCCESS_200 == responseEntity.getStatusCodeValue()) {
                        Object body = responseEntity.getBody();
                        String bodyStr = replace(body.toString(), idInstanceIdMap);
                        JsonObject bodyJson = new JsonParser().parse(bodyStr).getAsJsonObject();
                        statisticElement = bodyJson.get("data");
                        if (ToolUtils.jsonIsNull(statisticElement)) {
                            continue;
                        }
                        Map<String, Object> objectMap = convertMap(statisticElement);
                        resultmap.putAll(objectMap);
                    } else {
                        log.error("{} statistic error,the params is:{}", relationOrInstance, gson.toJson(params));
                        throw new DmeException("503", relationOrInstance + " statistic error,the params is:{}");
                    }
                } catch (DmeException e) {
                    log.error("{} statistic exception.{}", relationOrInstance, e);
                    throw new DmeException("503", e.getMessage());
                }
            }
        }
        return resultmap;
    }

    @Override
    public Map<String, Object> queryCurrentStatistic(String relationOrInstance, Map<String, Object> params)
        throws DmeException {
        Map<String, Object> resultMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        if (!StringUtils.isEmpty(relationOrInstance)) {
            switch (relationOrInstance) {
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDEVICE:
                    resultMap = queryStorageDevcieCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPOOL:
                    resultMap = queryStoragePoolCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_LUN:
                    resultMap = queryVolumeCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_SERVICELEVEL:
                    resultMap = queryServiceLevelCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_FILESYSTEM:
                    resultMap = queryFsCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_CONTROLLER:
                    resultMap = queryControllerCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPORT:
                    resultMap = queryStoragePortCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDISK:
                    resultMap = queryStorageDiskCurrentStatistic(params);
                    break;
                default:
                    resultMap.put("code", Integer.valueOf(DmeConstants.ERROR_CODE_503));
                    resultMap.put("message",
                        relationOrInstance + " current statistic error, non-supported relation and instance!");
                    log.error("{} current statistic error, non-supported relation and instance.the params is:{}",
                        relationOrInstance, gson.toJson(params));
                    throw new DmeException(gson.toJson(resultMap));
            }
        }
        return resultMap;
    }

    private Map<String, Object> queryCurrentStatistic(String relationOrInstance, Map<String, Object> params,
        Map<String, String> idInstanceIdMap) {
        Map<String, Object> resultmap = new HashMap<>();
        String label = MAX_LABEL;
        ResponseEntity responseEntity;
        JsonElement statisticElement;
        List<List<String>> objIdGroup = groupObjIds(params);
        if (null != objIdGroup && objIdGroup.size() > 0) {
            for (List<String> objids : objIdGroup) {
                params.put(OBJ_IDS_FIELD, objids);
                try {
                    responseEntity = queryStatistic(params);
                    if (null != responseEntity
                        && DmeConstants.HTTPS_STATUS_SUCCESS_200 == responseEntity.getStatusCodeValue()) {
                        Object body = responseEntity.getBody();
                        String bodyStr = body.toString();
                        bodyStr = replace(bodyStr, idInstanceIdMap);
                        JsonObject bodyJson = new JsonParser().parse(bodyStr).getAsJsonObject();
                        statisticElement = bodyJson.get("data");
                        if (ToolUtils.jsonIsNull(statisticElement)) {
                            log.error(
                                relationOrInstance + "objid: " + gson.toJson(objids) + "currentStatistic is null:",
                                bodyJson.get(ERROR_MSG).getAsString());
                            continue;
                        }
                        Map<String, Object> objectMap = convertMap(statisticElement, label);
                        resultmap.putAll(objectMap);
                        if (null == objectMap || objectMap.size() == 0) {
                            log.error("{} current statistic error:{}", relationOrInstance,
                                bodyJson.get(ERROR_MSG).getAsString());
                        }
                    } else {
                        log.error("{} current statistic error,the params is:{}", relationOrInstance,
                            gson.toJson(params));
                    }
                } catch (DmeException e) {
                    log.error("{} current statistic exception.{}", relationOrInstance, e);
                }
            }
        }
        return resultmap;
    }

    // 预处理存储设备参数
    private Map<String, String> initParamStorageDevice(Map<String, Object> params) {
        if (null == params || params.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> idInstancdIdMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        List<String> instanceIds = new ArrayList<>();
        List<String> ids = (List<String>) params.get(OBJ_IDS_FIELD);
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        Map<String, Map<String, Object>> instanceMap = dmeRelationInstanceService.getStorageDeviceInstance();
        for (String id : ids) {
            String instanceId = instanceMap.get(id).get(RESID_FIELD).toString();
            if (!StringUtils.isEmpty(instanceId)) {
                idInstancdIdMap.put(id, instanceId);
                instanceIds.add(instanceId);
            }
        }
        if (instanceIds.size() > 0) {
            params.put(OBJ_IDS_FIELD, instanceIds);
        }
        if (null == indicatorIds) {
            indicatorIds = initStorageDeviceIndicator();
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }

        // SYS_StorDevice
        String objTypeId = "1125904201809920";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        return idInstancdIdMap;
    }

    private Map<String, String> initParamStoragePool(Map<String, Object> params) {
        if (null == params || params.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> idInstancdIdMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        List<String> instanceIds = new ArrayList<>();
        List<String> ids = (List<String>) params.get(OBJ_IDS_FIELD);
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getStoragePoolInstance();
        for (String id : ids) {
            String instanceId = sysLunMap.get(id).get(RESID_FIELD).toString();
            if (!StringUtils.isEmpty(instanceId)) {
                idInstancdIdMap.put(id, instanceId);
                instanceIds.add(instanceId);
            }
        }
        if (instanceIds.size() > 0) {
            params.put(OBJ_IDS_FIELD, instanceIds);
        }
        if (null == indicatorIds) {
            indicatorIds = initStoragePoolIndicator();
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }

        // SYS_StoragePool
        String objTypeId = "1125912791744512";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        return idInstancdIdMap;
    }

    // 预处理卷参数
    private Map<String, String> initParamVolume(Map<String, Object> params, boolean isCurrent) throws DmeException {
        if (null == params || params.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> idInstancdIdMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        List<String> instanceIds = new ArrayList<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        Object objIds = params.get(OBJ_IDS_FIELD);
        List<String> ids = getObjIds(objIds);
        if (ids.size() > 0) {
            // ids若为wwn的集合则转换为对应的instanceId集合,也有可能ids直接就是volume的instanceId集合
            Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getLunInstance();
            for (String id : ids) {
                String instanceId = sysLunMap.get(id).get(RESID_FIELD).toString();
                if (!StringUtils.isEmpty(instanceId)) {
                    idInstancdIdMap.put(id, instanceId);
                    instanceIds.add(instanceId);
                }
            }
            if (instanceIds.size() > 0) {
                params.put(OBJ_IDS_FIELD, instanceIds);
            }
        }

        // SYS_Lun
        String objTypeId = SYS_LUN_OBJTYPEID;
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initVolumeIndicator(isCurrent);
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return idInstancdIdMap;
    }

    private Map<String, String> initParamServiceLevel(Map<String, Object> params) {
        // SYS_DjTier
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        String objTypeId = "1126174784749568";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelIndicator();
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return idInstanceIdMap;
    }

    private Map<String, String> initParamFs(Map<String, Object> params, boolean isCurrent) {
        // SYS_StorageFileSystem
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        String objTypeId = "1126179079716864";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initFsIndicator(isCurrent);
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return idInstanceIdMap;
    }

    private Map<String, String> initParamController(Map<String, Object> params, boolean isCurrent) {
        // SYS_Contorller
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        String objTypeId = "1125908496777216";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initControllerIndicator(isCurrent);
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return idInstanceIdMap;
    }

    // 预处理存储端口参数(params中的obj_ids如果是instanceId 则不用做转换处理)
    private Map<String, String> initParamStoragePort(Map<String, Object> params, boolean isCurrent) {
        // SYS_StoragePort
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        String objTypeId = "1125925676646400";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initStoragePortIndicator(isCurrent);
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return idInstanceIdMap;
    }

    private Map<String, String> initParamStorageDisk(Map<String, Object> params, boolean isCurrent) {
        // SYS_StorageDisk
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        String objTypeId = "1125917086711808";
        params.put(OBJ_TYPE_ID_FIELD, objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initStorageDiskIndicator(isCurrent);
            params.put(INDICATOR_IDS_FIELD, indicatorIds);
        }
        return idInstanceIdMap;
    }

    private RelationInstance getInstance(String relationName, String sourceId) {
        RelationInstance relationInstance = null;
        try {
            List<RelationInstance> instances
                = dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(relationName,
                sourceId);
            if (instances.size() > 0) {
                relationInstance = instances.get(0);
            }
        } catch (DmeException e) {
            log.warn("get RelationInstance error!{}", e.getMessage());
        }
        return relationInstance;
    }

    private String getTargetInstanceIdByRelationNameSourceId(String relationName, String resourceId) {
        String sourceInstanceId = "";
        String targetInstanceId = "";
        Map<String, Map<String, Object>> serviceLevelInstance = dmeRelationInstanceService.getServiceLevelInstance();
        if (null != serviceLevelInstance) {
            Map<String, Object> slInstance = serviceLevelInstance.get(resourceId);
            if (null != slInstance) {
                Object sourceIdObj = slInstance.get(RESID_FIELD);
                if (null != sourceIdObj) {
                    sourceInstanceId = sourceIdObj.toString();
                }
            }
        }
        if (!StringUtils.isEmpty(sourceInstanceId)) {
            // 获取targetInstanceId
            RelationInstance instance = getInstance(relationName, sourceInstanceId);
            if (null != instance) {
                targetInstanceId = instance.getTargetInstanceId();
            }
        }
        return targetInstanceId;
    }

    // 从ObjectIds中提取id
    private List<String> getObjIds(Object objIds) {
        List<String> objectIds = new ArrayList<>();
        if (null != objIds) {
            JsonArray objIdJsonArray = new JsonParser().parse(objIds.toString()).getAsJsonArray();
            for (JsonElement element : objIdJsonArray) {
                String id = element.getAsString();
                objectIds.add(id);
            }
        }
        return objectIds;
    }

    // query statistic
    private ResponseEntity queryStatistic(Map<String, Object> paramMap) throws DmeException {
        Map<String, Object> params = initParams(paramMap);
        String objTypeId = params.get(OBJ_TYPE_ID_FIELD).toString();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        Object objIds = params.get(OBJ_IDS_FIELD);
        String interval = ToolUtils.getStr(params.get(INTERVAL));
        String range = (String) params.get(RANGE);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(OBJ_TYPE_ID_FIELD, objTypeId);
        requestbody.put(INDICATOR_IDS_FIELD, indicatorIds);
        requestbody.put(OBJ_IDS_FIELD, objIds);
        requestbody.put(INTERVAL, interval);
        requestbody.put(RANGE, range);
        if (RANG_BEGIN_END_TIME.equals(range)) {
            String beginTime = ToolUtils.getStr(params.get(BEGIN_TIME));
            String endTime = ToolUtils.getStr(params.get(END_TIME));
            requestbody.put(BEGIN_TIME, beginTime);
            requestbody.put(END_TIME, endTime);
        }
        log.info("query history performance params={}", gson.toJson(requestbody));
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.STATISTIC_QUERY, HttpMethod.POST,
            gson.toJson(requestbody));
        return responseEntity;
    }

    // 性能查询条件参数初始化
    private Map<String, Object> initParams(Map<String, Object> paramMap) {
        Map<String, Object> params = new HashMap<>();
        params.putAll(paramMap);
        String rang = (String) params.get(RANGE);
        String interval = ToolUtils.getStr(params.get(INTERVAL));
        long beginTime = ToolUtils.getLong(params.get(BEGIN_TIME));
        long endTime = ToolUtils.getLong(params.get(END_TIME));

        // 设置默认值
        if (StringUtils.isEmpty(rang)) {
            rang = RANGE_LAST_1_DAY;
            params.put(RANGE, rang);
        }

        if (RANG_BEGIN_END_TIME.equals(rang)) {
            // 时间范围为时间段 而未设置具体时间 则默认一天
            if (0 == endTime) {
                endTime = System.currentTimeMillis();
                params.put(END_TIME, endTime);
            }
            if (0 == beginTime) {
                beginTime = endTime - TWENTY_FOUR_HOURS;
                params.put(BEGIN_TIME, beginTime);
            }
        }
        if (StringUtils.isEmpty(interval)) {
            interval = getIntervalByRange(rang);
            params.put(INTERVAL, interval);
        }
        return params;
    }

    private String getIntervalByRange(String rang) {
        String interval = null;
        switch (rang) {
            case RANGE_LAST_5_MINUTE:
                interval = INTERVAL_ONE_MINUTE;
                break;
            case RANGE_LAST_1_HOUR:
                interval = INTERVAL_ONE_MINUTE;
                break;
            case RANGE_LAST_1_DAY:
                interval = INTERVAL_ONE_MINUTE;
                break;
            case RANGE_LAST_1_WEEK:
                interval = INTERVAL_HALF_HOUR;
                break;
            case RANGE_LAST_1_MONTH:
                interval = INTERVAL_HALF_HOUR;
                break;
            case RANGE_LAST_1_QUARTER:
                interval = INTERVAL_DAY;
                break;
            case RANGE_HALF_1_YEAR:
                interval = INTERVAL_DAY;
                break;
            case RANGE_LAST_1_YEAR:
                interval = INTERVAL_DAY;
                break;
            case RANG_BEGIN_END_TIME:
                interval = INTERVAL_ONE_MINUTE;
                break;
            default:
        }
        return interval;
    }

    // nfs的默认指标集合 目前取的DME存储设备的指标
    private List<String> initFsIndicator(boolean wetherCurrent) {
        List<String> indicators = new ArrayList<>();
        if (wetherCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_BANDWIDTH);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITEBANDWIDTH);
        }
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READRESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITERESPONSETIME);

        return indicators;
    }

    // volume默认指标集合
    private List<String> initVolumeIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITEBANDWIDTH);
        }
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READRESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITERESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME);
        return indicators;
    }

    // serviceLevel默认指标集合
    private List<String> initServiceLevelIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELECVEL_MAXRESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELEVEL_BANDWIDTHTIB);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELEVEL_THROUGHPUTTIB);
        return indicators;
    }

    // serivceLevelLun默认指标集合
    private List<String> initServiceLevelLunIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT);
        return indicators;
    }

    // serviceLevelStoragepool 默认指标集合
    private List<String> initServiceLevelStoragePoolIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_THROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_BANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_RESPONSETIME);
        return indicators;
    }

    // Storagepool 默认指标集合
    private List<String> initStoragePoolIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_THROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_RESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_BANDWIDTH);
        return indicators;
    }

    // StorageDevice 默认指标集合
    private List<String> initStorageDeviceIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_CPUUSAGE);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_RESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_BANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_READTHROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_WRITETHROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_READBANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_WRITEBANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_THROUGHPUT);
        return indicators;
    }

    // Controller 默认指标集合
    private List<String> initControllerIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_BANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_CPUUSAGE);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_RESPONSETIME);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_WRITEBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_READRESPONSETIME);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_WRITERESPONSETIME);
        }
        return indicators;
    }

    // StoragePort 默认指标集合
    private List<String> initStoragePortIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_BANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_UTILITY);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_RESPONSETIME);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_WRITEBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_READRESPONSETIME);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_WRITERESPONSETIME);
        }
        return indicators;
    }

    // StorageDisk 默认指标集合
    private List<String> initStorageDiskIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_BANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_UTILITY);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_BANDWIDTH);
        }
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_RESPONSETIME);
        return indicators;
    }

    private Map<String, Object> convertMap(JsonElement jsonElement) {
        Map<String, Object> objectMap = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> objectSet = jsonElement.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> objectEntry : objectSet) {
            String objectId = objectEntry.getKey();
            JsonElement objectElement = objectEntry.getValue();
            if (!ToolUtils.jsonIsNull(objectElement)) {
                Set<Map.Entry<String, JsonElement>> objectValueSet = objectElement.getAsJsonObject().entrySet();
                Map<String, Object> indicatorMap = new HashMap<>();
                for (Map.Entry<String, JsonElement> indicaterEntry : objectValueSet) {
                    Map<String, Object> indicatorValueMap = new HashMap<>();
                    JsonElement indicatorElement = indicaterEntry.getValue();
                    JsonObject indicatorValueObject = indicatorElement.getAsJsonObject();
                    JsonArray seriesArray = indicatorValueObject.get("series").getAsJsonArray();
                    if (null != seriesArray && seriesArray.size() > 0) {
                        seriesProcess(indicatorValueMap, seriesArray);
                    }
                    JsonElement minElement = indicatorValueObject.get("min");
                    if (!ToolUtils.jsonIsNull(minElement)) {
                        minProcess(indicatorValueMap, minElement);
                    }
                    JsonElement maxElement = indicatorValueObject.get(MAX_LABEL);
                    if (!ToolUtils.jsonIsNull(maxElement)) {
                        maxProcess(indicatorValueMap, maxElement);
                    }
                    JsonElement avgElement = indicatorValueObject.get("avg");
                    if (!ToolUtils.jsonIsNull(avgElement)) {
                        avgProcess(indicatorValueMap, avgElement);
                    }
                    indicatorMap.put(indicaterEntry.getKey(), indicatorValueMap);
                }
                objectMap.put(objectId, indicatorMap);
            }
        }
        return objectMap;
    }

    // 消息转换 object-map 提取指定的标签
    private Map<String, Object> convertMap(JsonElement jsonElement, String label) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!ToolUtils.jsonIsNull(jsonElement)) {
            Set<Map.Entry<String, JsonElement>> objectSet = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> objectEntry : objectSet) {
                String objectId = objectEntry.getKey();
                JsonElement objectElement = objectEntry.getValue();
                if (!ToolUtils.jsonIsNull(objectElement)) {
                    Set<Map.Entry<String, JsonElement>> objectValueSet = objectElement.getAsJsonObject().entrySet();
                    Map<String, Object> indicatorMap = new HashMap<>();
                    for (Map.Entry<String, JsonElement> indicaterEntry : objectValueSet) {
                        String indicatoerId = indicaterEntry.getKey();
                        JsonElement indicatorElement = indicaterEntry.getValue();
                        JsonObject indicatorValueObject = indicatorElement.getAsJsonObject();
                        JsonElement maxElement = indicatorValueObject.get(label);
                        if (!ToolUtils.jsonIsNull(maxElement)) {
                            Set<Map.Entry<String, JsonElement>> maxSet = maxElement.getAsJsonObject().entrySet();
                            for (Map.Entry<String, JsonElement> maxEntry : maxSet) {
                                String value = ToolUtils.jsonToStr(maxEntry.getValue());
                                indicatorMap.put(indicatoerId, value);
                                break;
                            }
                        }
                    }
                    objectMap.put(objectId, indicatorMap);
                }
            }
        }
        return objectMap;
    }

    private void seriesProcess(Map<String, Object> indicatorValueMap, JsonArray seriesArray) {
        List<Map<String, String>> seriesList = new ArrayList<>();
        for (JsonElement serieCellElemt : seriesArray) {
            Map<String, String> seriesMap = new HashMap<>();
            if (!ToolUtils.jsonIsNull(serieCellElemt)) {
                Set<Map.Entry<String, JsonElement>> cellSet = serieCellElemt.getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> cellEntry : cellSet) {
                    String time = cellEntry.getKey();
                    String value = cellEntry.getValue().getAsString();
                    seriesMap.put(time, value);
                    seriesList.add(seriesMap);
                    break;
                }
            }
        }
        indicatorValueMap.put("series", seriesList);
    }

    private void minProcess(Map<String, Object> indicatorValueMap, JsonElement minElement) {
        Map<String, String> minValueMap = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> minSet = minElement.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> minEntry : minSet) {
            String time = minEntry.getKey();
            String value = minEntry.getValue().getAsString();
            minValueMap.put(time, value);
            break;
        }
        indicatorValueMap.put("min", minValueMap);
    }

    private void maxProcess(Map<String, Object> indicatorValueMap, JsonElement maxElement) {
        Map<String, String> maxValueMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        Set<Map.Entry<String, JsonElement>> maxSet = maxElement.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> maxEntry : maxSet) {
            String time = maxEntry.getKey();
            String value = maxEntry.getValue().getAsString();
            maxValueMap.put(time, value);
            break;
        }
        indicatorValueMap.put(MAX_LABEL, maxValueMap);
    }

    private void avgProcess(Map<String, Object> indicatorValueMap, JsonElement avgElement) {
        Map<String, String> avgMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        Set<Map.Entry<String, JsonElement>> avgSet = avgElement.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> avgEntry : avgSet) {
            String time = avgEntry.getKey();
            String value = avgEntry.getValue().getAsString();
            avgMap.put(time, value);
            break;
        }
        indicatorValueMap.put("avg", avgMap);
    }

    // 将性能数据结果中的instanceId转换为参数传递的id
    private String replace(String source, Map<String, String> idInstanceIdMap) {
        String result = source;
        if (!StringUtils.isEmpty(result) && null != idInstanceIdMap && idInstanceIdMap.size() > 0) {
            for (Map.Entry<String, String> entry : idInstanceIdMap.entrySet()) {
                String id = entry.getKey();
                String instanceId = entry.getValue();
                if (!StringUtils.isEmpty(instanceId) && !StringUtils.isEmpty(id)) {
                    result = result.replace(instanceId, id);
                }
            }
        }
        return result;
    }

    private List<List<String>> groupObjIds(Map<String, Object> params) {
        int maxObjIndicator = MAX_OBJ_INDICATOR;
        List<List<String>> objGroup = new ArrayList<>();
        List<String> objIds = (List<String>) params.get(OBJ_IDS_FIELD);
        List<String> indicatorIds = (List<String>) params.get(INDICATOR_IDS_FIELD);
        int objSize = objIds.size();
        int indicatorSize = indicatorIds.size();
        if (objSize * indicatorSize > maxObjIndicator) {
            int length = maxObjIndicator / indicatorSize;
            int count = 0;
            List<String> list = null;
            for (String objId : objIds) {
                if (0 == count % length) {
                    list = new ArrayList<>();
                    objGroup.add(list);
                }
                list.add(objId);
                count++;
            }
        } else {
            objGroup.add(objIds);
        }
        return objGroup;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public void setDmeRelationInstanceService(DmeRelationInstanceService dmeRelationInstanceService) {
        this.dmeRelationInstanceService = dmeRelationInstanceService;
    }
}
