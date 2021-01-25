package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.RelationInstance;
import com.huawei.dmestore.utils.ToolUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
 * DMEOpenApiService
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public class DmeRelationInstanceServiceImpl implements DmeRelationInstanceService {
    private static final Logger LOG = LoggerFactory.getLogger(DmeRelationInstanceServiceImpl.class);

    private static final Map<String, Map<String, Object>> SERVICE_LEVEL_INSTANCE = new HashMap<>();

    private static final Map<String, Map<String, Object>> LUN_INSTANCE = new HashMap<>();

    private static final Map<String, Map<String, Object>> STORAGE_POOL_INSTANCE = new HashMap<>();

    private static final Map<String, Map<String, Object>> STORAGE_DEVCIE_INSTANCE = new HashMap<>();

    private static final String WWN_FIELD = "wwn";

    private static final String CODE = "code";

    private static final int CODE_VALUE_200 = 200;

    private static final String ID_FIELD = "id";

    private static final String NATIVE_ID_FIELD = "nativeId";

    private static final String RES_ID_FIELD = "resId";

    private static final String OBJ_LIST = "objList";

    private static final String MESSAGE = "message";

    private static final String NAME_FIELD = "name";

    private static final String SOURCE_INSTANCE_ID = "source_Instance_Id";

    private static final String RELATION_NAME = "{relationName}";

    private DmeAccessService dmeAccessService;

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    @Override
    public List<RelationInstance> queryRelationByRelationName(String relationName) throws DmeException {
        List<RelationInstance> ris = new ArrayList<>();
        Map<String, Object> remap = new HashMap<>();
        remap.put(CODE, CODE_VALUE_200);
        remap.put(MESSAGE, "queryRelationByRelationName success!");

        String url = DmeConstants.LIST_RELATION_URL.replace(RELATION_NAME, relationName);

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                ris = converRelations(responseEntity.getBody());
            }
        } catch (DmeException e) {
            throw new DmeException(e.getMessage());
        }
        return ris;
    }

    @Override
    public List<RelationInstance> queryRelationByRelationNameConditionSourceInstanceId(String relationName,
        String sourceInstanceId) throws DmeException {
        List<RelationInstance> ris = new ArrayList<>();
        Map<String, Object> remap = new HashMap<>();
        remap.put(CODE, CODE_VALUE_200);
        remap.put(MESSAGE, "queryRelationByRelationNameConditionSourceInstanceId success!");

        String url = DmeConstants.LIST_RELATION_URL;
        url = url.replace(RELATION_NAME, relationName);
        url = url + "?condition={json}";

        JsonObject consObj2 = new JsonObject();
        JsonObject simple2 = new JsonObject();
        simple2.addProperty(NAME_FIELD, SOURCE_INSTANCE_ID);
        simple2.addProperty("value", sourceInstanceId);
        consObj2.add("simple", simple2);
        JsonArray constraint = new JsonArray();
        constraint.add(consObj2);

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, constraint.toString());
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                ris = converRelations(responseEntity.getBody());
            }
        } catch (DmeException e) {
            throw new DmeException(e.getMessage());
        }
        return ris;
    }

    @Override
    public RelationInstance queryRelationByRelationNameInstanceId(String relationName, String instanceId)
        throws DmeException {
        RelationInstance ri = new RelationInstance();
        Map<String, Object> remap = new HashMap<>();
        remap.put(CODE, CODE_VALUE_200);
        remap.put(MESSAGE, "queryRelationByRelationName success!");

        String url = DmeConstants.QUERY_RELATION_URL;
        url = url.replace(RELATION_NAME, relationName);
        url = url.replace("{instanceId}", relationName);

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                ri = converRelation(responseEntity.getBody());
            }
        } catch (DmeException e) {
            throw new DmeException(e.getMessage());
        }
        return ri;
    }

    @Override
    public Object queryInstanceByInstanceNameId(String instanceName, String instanceId) throws DmeException {
        Object obj = new Object();
        Map<String, Object> remap = new HashMap<>();
        remap.put(CODE, CODE_VALUE_200);
        remap.put(MESSAGE, "queryInstanceByInstanceNameId success!");

        String url = DmeConstants.QUERY_INSTANCE_URL;
        url = url.replace("{className}", instanceName);
        url = url.replace("{instanceId}", instanceId);

        try {
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                obj = responseEntity.getBody();
            }
        } catch (DmeException e) {
            throw new DmeException(e.getMessage());
        }
        return obj;
    }

    public void listInstanceServiceLevel() {
        String instanceName = "SYS_DjTier";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (jsonObject != null) {
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> slMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                slMap.put(RES_ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(RES_ID_FIELD)));
                slMap.put(NAME_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(NAME_FIELD)));
                slMap.put(ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(ID_FIELD)));
                slMap.put(NATIVE_ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(NATIVE_ID_FIELD)));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get(NATIVE_ID_FIELD)), slMap);
            }
            if (map.size() > 0) {
                SERVICE_LEVEL_INSTANCE.clear();
                SERVICE_LEVEL_INSTANCE.putAll(map);
            }
        }
    }

    public void listInstanceLun() throws DmeException {
        String instanceName = "SYS_Lun";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (jsonObject != null && jsonObject.get("totalNum").getAsInt() > 0) {
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> lunMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                lunMap.put(RES_ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(RES_ID_FIELD)));
                lunMap.put(NAME_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(NAME_FIELD)));
                lunMap.put(ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(ID_FIELD)));
                lunMap.put(WWN_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(WWN_FIELD)));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get(WWN_FIELD)), lunMap);
            }
            if (map.size() > 0) {
                LUN_INSTANCE.clear();
                LUN_INSTANCE.putAll(map);
            }
        } else {
            LOG.error("listInstanceLun error!query instance error!className={}", instanceName);
            throw new DmeException("500", "No instance information was found!className=" + instanceName);
        }
    }

    public void listInstanceStoragePool() {
        String instanceName = "SYS_StoragePool";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (jsonObject != null) {
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> lunMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                lunMap.put(RES_ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(RES_ID_FIELD)));
                lunMap.put(NAME_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(NAME_FIELD)));
                lunMap.put(ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(ID_FIELD)));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get(ID_FIELD)), lunMap);
            }
            if (map.size() > 0) {
                STORAGE_POOL_INSTANCE.clear();
                STORAGE_POOL_INSTANCE.putAll(map);
            }
        }
    }

    public void listInstanceStorageDevcie() {
        String instanceName = "SYS_StorDevice";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (jsonObject != null) {
            JsonArray jsonArray = jsonObject.get(OBJ_LIST).getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> lunMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                lunMap.put(RES_ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(RES_ID_FIELD)));
                lunMap.put(NAME_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(NAME_FIELD)));
                lunMap.put(ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(ID_FIELD)));
                lunMap.put(NATIVE_ID_FIELD, ToolUtils.jsonToOriginalStr(slJson.get(NATIVE_ID_FIELD)));
                lunMap.put("sn", ToolUtils.jsonToOriginalStr(slJson.get("sn")));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get(NATIVE_ID_FIELD)), lunMap);
            }
            if (map.size() > 0) {
                STORAGE_DEVCIE_INSTANCE.clear();
                STORAGE_DEVCIE_INSTANCE.putAll(map);
            }
        }
    }

    @Override
    public Map<String, Map<String, Object>> getServiceLevelInstance() {
        if (SERVICE_LEVEL_INSTANCE.size() == 0) {
            listInstanceServiceLevel();
        }
        return SERVICE_LEVEL_INSTANCE;
    }

    @Override
    public Map<String, Map<String, Object>> getLunInstance() throws DmeException {
        if (LUN_INSTANCE.size() == 0) {
            listInstanceLun();
        }
        return LUN_INSTANCE;
    }

    @Override
    public Map<String, Map<String, Object>> getStoragePoolInstance() {
        if (STORAGE_POOL_INSTANCE.size() == 0) {
            listInstanceStoragePool();
        }
        return STORAGE_POOL_INSTANCE;
    }

    @Override
    public Map<String, Map<String, Object>> getStorageDeviceInstance() {
        if (STORAGE_DEVCIE_INSTANCE.size() == 0) {
            listInstanceStorageDevcie();
        }
        return STORAGE_DEVCIE_INSTANCE;
    }

    @Override
    public void refreshResourceInstance() throws DmeException {
        listInstanceStorageDevcie();
        listInstanceStoragePool();
        listInstanceLun();
        listInstanceServiceLevel();
    }

    private JsonObject listInstancdByInstanceName(String instanceName) {
        JsonObject jsonObject = null;
        String url = DmeConstants.LIST_INSTANCE_URL.replace("{className}", instanceName);
        try {
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity != null && responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                Object object = responseEntity.getBody();
                jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            }
        } catch (DmeException e) {
            LOG.warn("List instance error by instanceName:{},{}", instanceName, e);
        }
        return jsonObject;
    }

    private List<RelationInstance> converRelations(Object object) {
        List<RelationInstance> ris = new ArrayList<>();
        JsonObject bodyJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonArray objList = bodyJson.get(OBJ_LIST).getAsJsonArray();
        for (JsonElement element : objList) {
            RelationInstance ri = new RelationInstance();
            JsonObject objJson = element.getAsJsonObject();
            ri.setSourceInstanceId(ToolUtils.jsonToStr(objJson.get(SOURCE_INSTANCE_ID)));
            ri.setTargetInstanceId(ToolUtils.jsonToStr(objJson.get("target_Instance_Id")));
            ri.setRelationName(ToolUtils.jsonToStr(objJson.get("relation_Name")));
            ri.setId(ToolUtils.jsonToStr(objJson.get(ID_FIELD)));
            ri.setLastModified(ToolUtils.jsonToLon(objJson.get("last_Modified"), null));
            ri.setResId(ToolUtils.jsonToStr(objJson.get(RES_ID_FIELD)));
            ri.setRelationId(ToolUtils.jsonToInt(objJson.get("relation_Id"), 0));

            ris.add(ri);
        }
        return ris;
    }

    private RelationInstance converRelation(Object object) {
        RelationInstance ri = new RelationInstance();
        JsonObject objJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        if (objJson != null) {
            ri.setSourceInstanceId(ToolUtils.getStr(objJson.get(SOURCE_INSTANCE_ID)));
            ri.setTargetInstanceId(ToolUtils.getStr(objJson.get("target_Instance_Id")));
            ri.setRelationName(ToolUtils.getStr(objJson.get("relation_Name")));
            ri.setId(ToolUtils.getStr(objJson.get(ID_FIELD)));
            ri.setLastModified(ToolUtils.getLong(objJson.get("last_Modified")));
            ri.setResId(ToolUtils.getStr(objJson.get(RES_ID_FIELD)));
            ri.setRelationId(ToolUtils.getInt(objJson.get("relation_Id")));
        }
        return ri;
    }
}
