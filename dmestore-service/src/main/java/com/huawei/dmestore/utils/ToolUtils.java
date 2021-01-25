package com.huawei.dmestore.utils;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.StorageTypeShow;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import springfox.documentation.spring.web.json.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * ToolUtils
 *
 * @author yy
 * @since 2020-09-15
 **/
public class ToolUtils {
    public static final String STORE_TYPE_NFS = "NFS";

    public static final String STORE_TYPE_ALL = "ALL";

    public static final int GI = 1024 * 1024 * 1024;

    private static final Logger LOG = LoggerFactory.getLogger(ToolUtils.class);

    private static final Gson GSON = new Gson();

    public static String getStr(Object obj) {
        return getStr(obj, null);
    }

    public static String getStr(Object obj, String defaultValue) {
        String re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.toString();
            }
        } catch (IllegalStateException e) {
            LOG.error("getStr error:{}", e.toString());
        }
        return re;
    }

    public static int getInt(Object obj) {
        int re = 0;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Integer.parseInt(obj.toString());
            }
        } catch (IllegalStateException e) {
            LOG.error("getInt error:{}", e.toString());
        }
        return re;
    }

    public static Integer getInt(Object obj, Integer defaultvalue) {
        Integer re = defaultvalue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Integer.parseInt(obj.toString());
            }
        } catch (IllegalStateException e) {
            LOG.error("getInt2 error:{}", e.toString());
        }
        return re;
    }

    public static double getDouble(Object obj) {
        double re = 0;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Double.parseDouble(obj.toString());
            }
        } catch (IllegalStateException e) {
            LOG.error("getDouble error:{}", e.toString());
        }
        return re;
    }

    public static long getLong(Object obj) {
        long re = 0L;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Long.parseLong(obj.toString());
            }
        } catch (IllegalStateException e) {
            LOG.error("getLong error:{}", e.toString());
        }
        return re;
    }

    public static String normalizeWwn(long wwn) {
        return  (Long.toHexString(wwn));
    }

    public static String jsonToStr(JsonElement obj) {
        String re = "";
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsString();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToStr error:{}", e.toString());
        }
        return re;
    }

    public static String jsonToOriginalStr(JsonElement obj) {
        String re = null;
        try {
            if (null != obj && !obj.isJsonNull()) {
                re = obj.getAsString();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToOriginalStr error:{}", e.toString());
        }
        return re;
    }

    public static String jsonToStr(JsonElement obj, String defaultValue) {
        String re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsString();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToStr2 error:{}", e.toString());
        }
        return re;
    }

    public static Integer jsonToInt(JsonElement obj, Integer defaultValue) {
        Integer re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsInt();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToInt error:{}", e.toString());
        }
        return re;
    }

    public static Integer jsonToInt(JsonElement obj) {
        Integer re = 0;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsInt();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToInt2 error:{}", e.toString());
        }
        return re;
    }

    public static Long jsonToLon(JsonElement obj, Long defaultValue) {
        Long re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsLong();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToLon error:{}", e.toString());
        }
        return re;
    }

    public static Double jsonToDou(JsonElement obj, Double defaultValue) {
        Double re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsDouble();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToDou error:{}", e.toString());
        }
        return re;
    }

    public static Double jsonToDou(JsonElement obj) {
        Double re = 0.00;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsDouble();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToDou2 error:{}", e.toString());
        }
        return re;
    }

    public static Float jsonToFloat(JsonElement obj, Float defaultValue) {
        Float re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsFloat();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToFloat error:{}", e.toString());
        }
        return re;
    }

    public static Float jsonToFloat(JsonElement obj) {
        Float re = 0.0F;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsFloat();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToFloat2 error:{}", e.toString());
        }
        return re;
    }

    public static boolean jsonToBoo(JsonElement obj) {
        boolean re = false;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsBoolean();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToBoo error:{}", e.toString());
        }
        return re;
    }

    public static Boolean jsonToBoo(JsonElement obj, Boolean defaultVal) {
        Boolean re = defaultVal;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsBoolean();
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToBoo error:{}", e.toString());
        }
        return re;
    }

    /**
     * 判断逻辑，只要vcenter中扫描的主机启动器包含dme主机中的启动器，符合主机一致性
     *
     * @param a vcenter 中扫描的主机中的启动器
     * @param b DME 中扫描的主机启动器
     * @param <T> 参数类型
     * @return boolean 包含或者不包含
     */
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        int count = 0;
        for (int index = 0; index < b.size(); index++) {
            if (a.contains(b.get(index))) {
                count++;
                if (count == b.size()) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean jsonIsNull(JsonElement obj) {
        boolean re = false;
        try {
            if (StringUtils.isEmpty(obj) || obj.isJsonNull() || "{}".equals(GSON.toJson(obj))) {
                re = true;
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonIsNull error:{}", e.toString());
        }
        return re;
    }

    public static String jsonToDateStr(JsonElement obj, String defaultValue) {
        String re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                    new Date(Long.parseLong(String.valueOf(obj.getAsBigInteger()))));
            }
        } catch (IllegalStateException e) {
            LOG.error("jsonToDateStr error:{}", e.toString());
        }
        return re;
    }

    public static JsonElement getStatistcValue(JsonObject statisticObject, String indicator, String type) {
        JsonElement object = null;
        if (statisticObject != null) {
            JsonElement indicatorEl = statisticObject.get(indicator);
            if (!ToolUtils.jsonIsNull(indicatorEl)) {
                JsonObject indicatorJson = indicatorEl.getAsJsonObject();
                JsonElement typeEl = indicatorJson.get(type);
                if (!ToolUtils.jsonIsNull(typeEl)) {
                    JsonObject typeJson = typeEl.getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> sets = typeJson.entrySet();
                    for (Map.Entry<String, JsonElement> set : sets) {
                        object = set.getValue();
                        break;
                    }
                }
            }
        }
        return object;
    }

    public static String getRequsetParams(String paramName, String paramValue) {
        JsonObject simple = new JsonObject();
        simple.addProperty("name", "dataStatus");
        simple.addProperty("operator", "equal");
        simple.addProperty("value", "normal");
        JsonObject consObj = new JsonObject();
        consObj.add("simple", simple);
        JsonArray constraint = new JsonArray();
        constraint.add(consObj);
        JsonObject simple1 = new JsonObject();
        simple1.addProperty("name", paramName);
        simple1.addProperty("operator", "equal");
        simple1.addProperty("value", paramValue);
        JsonObject consObj1 = new JsonObject();
        consObj1.add("simple", simple1);
        consObj1.addProperty("logOp", "and");
        constraint.add(consObj1);
        JsonObject condition = new JsonObject();
        condition.add("constraint", constraint);
        return condition.toString();
    }

    public static String getRequsetParams(Map<String,String> map) {
        JsonArray constraint = new JsonArray();
        for (Map.Entry<String,String> entry : map.entrySet()) {
            JsonObject simple = new JsonObject();
            simple.addProperty("name", "dataStatus");
            simple.addProperty("operator", "equal");
            simple.addProperty("value", "normal");
            JsonObject consObj = new JsonObject();
            consObj.add("simple", simple);
            JsonObject simple1 = new JsonObject();
            simple1.addProperty("name", entry.getKey());
            simple1.addProperty("operator", "equal");
            simple1.addProperty("value", entry.getValue());
            JsonObject consObj1 = new JsonObject();
            consObj1.add("simple", simple1);
            consObj1.addProperty("logOp", "and");
            constraint.add(consObj);
            constraint.add(consObj1);
        }
        JsonObject condition = new JsonObject();
        condition.add("constraint", constraint);
        return condition.toString();
    }

    public static StorageTypeShow getStorageTypeShow(String storageModel) throws DmeException {
        StorageTypeShow storageTypeShow = new StorageTypeShow();
        String[] model = storageModel.split(" ");
        List<String> list = Arrays.asList(model);
        if (list.contains("OceanStor")){
            // OceanStor Dorado 3000 V6
            storageTypeShow.setQosTag(1);
            storageTypeShow.setWorkLoadShow(1);
            storageTypeShow.setAllocationTypeShow(2);
            storageTypeShow.setOwnershipController(false);
            storageTypeShow.setStorageDetailTag(2);
            storageTypeShow.setDeduplicationShow(false);
            storageTypeShow.setCompressionShow(false);
            storageTypeShow.setCapacityInitialAllocation(false);
            storageTypeShow.setSmartTierShow(false);
            storageTypeShow.setPrefetchStrategyShow(false);
        }else {
            if (list.contains("V3") && list.get(0).contains("Dorado")){
                // Dorado v3
                storageTypeShow.setQosTag(3);
                storageTypeShow.setWorkLoadShow(1);
                storageTypeShow.setAllocationTypeShow(2);
                storageTypeShow.setOwnershipController(true);
                storageTypeShow.setStorageDetailTag(1);
                storageTypeShow.setDeduplicationShow(false);
                storageTypeShow.setCompressionShow(false);
                storageTypeShow.setCapacityInitialAllocation(false);
                storageTypeShow.setSmartTierShow(false);
                storageTypeShow.setPrefetchStrategyShow(false);
            }else{
                // v3/v5
                storageTypeShow.setQosTag(2);
                storageTypeShow.setWorkLoadShow(2);
                storageTypeShow.setAllocationTypeShow(1);
                storageTypeShow.setOwnershipController(true);
                storageTypeShow.setStorageDetailTag(2);
                storageTypeShow.setDeduplicationShow(true);
                storageTypeShow.setCompressionShow(true);
                storageTypeShow.setCapacityInitialAllocation(true);
                storageTypeShow.setSmartTierShow(true);
                storageTypeShow.setPrefetchStrategyShow(true);
            }
        }
        return storageTypeShow;
    }

    public static boolean isDorado(String storageModel) {
        String[] model = storageModel.split(" ");
        List<String> list = Arrays.asList(model);
        if (list.contains("OceanStor")){
            return true;
        }
        return false;
    }
}
