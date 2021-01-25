package com.huawei.dmestore.mvc;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.CapacityAutonegotiation;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.NfsOperationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NfsOperationController
 *
 * @author liuxh
 * @since 2020-09-02
 **/
@RestController
@RequestMapping("/operatenfs")
public class NfsOperationController extends BaseController {
    /**
     * QOS策略上限标识
     */
    private static final String CONTROL_UP = "up";

    /**
     * QOS策略下限标识
     */
    private static final String CONTROL_LOW = "low";

    /**
     * nfsName
     */
    private static final String NFS_NAME_FIELD = "nfsName";

    /**
     * latency
     */
    private static final String LATENCY_FIELD = "latency";

    /**
     * allocation_type
     */
    private static final String ALLOCATION_TYPE_FIELD = "allocation_type";

    /**
     * compression_enabled
     */
    private static final String COMPRESSION_ENABLED_FIELD = "compression_enabled";

    /**
     * fsName
     */
    private static final String FSNAME_FIELD = "fsName";

    /**
     * thin
     */
    private static final String THIN_FIELD = "thin";

    /**
     * name
     */
    private static final String NAME_FIELD = "name";

    /**
     * deduplication_enabled
     */
    private static final String DEDUPLICATION_ENABLED_FIELD = "deduplication_enabled";

    /**
     * capacity_self_adjusting_mode
     */
    private static final String ADJUSTING_MODE_FIELD = "capacity_self_adjusting_mode";

    /**
     * auto_size_enable
     */
    private static final String AUTO_SIZE_ENABLE_FIELD = "auto_size_enable";

    /**
     * autoSizeEnable
     */
    private static final String AUTO_SIZE_ENABLE_REQUEST_FIELD = "autoSizeEnable";

    /**
     * 文件路径分隔符
     */
    private static final String FILE_SEPARATOR = "/";

    @Autowired
    private NfsOperationService nfsOperationService;

    /**
     * createNfsDatastore
     *
     * @param requestParams requestParams
     * @return ResponseBodyBean
     */
    @PostMapping("/createnfsdatastore")
    public ResponseBodyBean createNfsDatastore(@RequestBody Map<String, Object> requestParams) {
        // 入参调整
        Map<String, Object> targetParams = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        Map<String, Object> createNfsShareParam = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        targetParams.put("storage_id", requestParams.get("storagId"));
        targetParams.put("storage_pool_id", requestParams.get("storagePoolId"));
        targetParams.put("pool_raw_id", requestParams.get("poolRawId"));
        targetParams.put("current_port_id", requestParams.get("currentPortId"));
        String accessModeDme = (String) requestParams.get("accessMode");
        if ("readWrite".equals(accessModeDme)) {
            accessModeDme = "read/write";
        } else {
            accessModeDme = "read-only";
        }
        targetParams.put("accessMode", requestParams.get("accessMode"));
        targetParams.put("accessModeDme", accessModeDme);
        Object nfsName = requestParams.get(NFS_NAME_FIELD);
        targetParams.put(NFS_NAME_FIELD, nfsName);
        targetParams.put("type", requestParams.get("type"));
        targetParams.put("securityType", requestParams.get("securityType"));
        Map<String, Object> filesystemSpec = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        filesystemSpec.put("capacity", requestParams.get("size"));
        filesystemSpec.put("count", 1);
        boolean sameName = (Boolean) requestParams.get("sameName");
        if (sameName) {
            createNfsShareParam.put(NAME_FIELD, FILE_SEPARATOR + nfsName);
            createNfsShareParam.put("share_path", FILE_SEPARATOR + nfsName + FILE_SEPARATOR);
            filesystemSpec.put(NAME_FIELD, nfsName);
            targetParams.put("exportPath", FILE_SEPARATOR + nfsName);
        } else {
            createNfsShareParam.put(NAME_FIELD, FILE_SEPARATOR + requestParams.get("shareName"));
            createNfsShareParam.put("share_path", FILE_SEPARATOR + requestParams.get(FSNAME_FIELD) + FILE_SEPARATOR);
            filesystemSpec.put(NAME_FIELD, requestParams.get(FSNAME_FIELD));
            targetParams.put("exportPath", FILE_SEPARATOR + requestParams.get(FSNAME_FIELD));
        }
        List<Map<String, Object>> filesystemSpecs = new ArrayList<>(DmeConstants.COLLECTION_CAPACITY_16);
        filesystemSpecs.add(filesystemSpec);
        targetParams.put("filesystem_specs", filesystemSpecs);
        Map<String, Object> nfsShareClientAddition = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        nfsShareClientAddition.put(NAME_FIELD, requestParams.get("vkernelIp"));
        nfsShareClientAddition.put("objectId", requestParams.get("hostObjectId"));
        createNfsShareParam.put("character_encoding", "utf-8");
        targetParams.put("create_nfs_share_param", createNfsShareParam);
        boolean advance = (Boolean) requestParams.get("advance");
        List<Map<String, Object>> nfsShareClientAdditions = new ArrayList<>(DmeConstants.COLLECTION_CAPACITY_16);
        nfsShareClientAdditions.add(nfsShareClientAddition);
        targetParams.put("nfs_share_client_addition", nfsShareClientAdditions);
        advanceExcute(requestParams, targetParams, advance);
        try {
            nfsOperationService.createNfsDatastore(targetParams);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * advanceExcute
     *
     * @param requestParams requestParams
     * @param targetParams targetParams
     * @param isAdvance isAdvance
     */
    private void advanceExcute(Map<String, Object> requestParams, Map<String, Object> targetParams, boolean isAdvance) {
        Map<String, Object> tuning = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        Map<String, Object> capacityAutonegotiation = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        String defaultCapacitymode = CapacityAutonegotiation.CAPACITY_MODE_OFF;
        if (isAdvance) {
            boolean qosFlag = (Boolean) requestParams.get("qosFlag");
            if (qosFlag) {
                Map<String, Object> qosPolicy = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                String contolPolicy = (String) requestParams.get("contolPolicy");
                if (CONTROL_UP.equals(contolPolicy)) {
                    qosPolicy.put("max_bandwidth", Integer.parseInt((String) requestParams.get("maxBandwidth")));
                    qosPolicy.put("max_iops", Integer.parseInt((String) requestParams.get("maxIops")));
                } else if (CONTROL_LOW.equals(contolPolicy)) {
                    qosPolicy.put("min_bandwidth", Integer.parseInt((String) requestParams.get("minBandwidth")));
                    qosPolicy.put("min_iops", Integer.parseInt((String) requestParams.get("minIops")));
                    qosPolicy.put(LATENCY_FIELD, Integer.parseInt((String) requestParams.get(LATENCY_FIELD)));
                }
                targetParams.put("qos_policy", qosPolicy);
            }
            boolean thin = (Boolean) requestParams.get(THIN_FIELD);
            if (thin) {
                tuning.put(ALLOCATION_TYPE_FIELD, THIN_FIELD);
            } else {
                tuning.put(ALLOCATION_TYPE_FIELD, "thick");
            }
            tuning.put(COMPRESSION_ENABLED_FIELD, requestParams.get("compressionEnabled"));
            tuning.put(DEDUPLICATION_ENABLED_FIELD, requestParams.get("deduplicationEnabled"));

            String capacitymode = (Boolean) requestParams.get(AUTO_SIZE_ENABLE_REQUEST_FIELD)
                ? CapacityAutonegotiation.CAPACITY_MODE_AUTO : defaultCapacitymode;
           if (!"grow-off".equalsIgnoreCase(capacitymode)){
                capacityAutonegotiation.put("capacity_recycle_mode","expand_capacity");
                capacityAutonegotiation.put("auto_grow_threshold_percent",85);
                capacityAutonegotiation.put("auto_shrink_threshold_percent",50);
                capacityAutonegotiation.put("max_auto_size", 16777216);
                capacityAutonegotiation.put("min_auto_size",16777216);
                capacityAutonegotiation.put("auto_size_increment",1024);
            }
            capacityAutonegotiation.put(ADJUSTING_MODE_FIELD, capacitymode);
            capacityAutonegotiation.put(AUTO_SIZE_ENABLE_FIELD, requestParams.get(AUTO_SIZE_ENABLE_REQUEST_FIELD));
        } else {
            tuning.put(ALLOCATION_TYPE_FIELD, THIN_FIELD);
            tuning.put(COMPRESSION_ENABLED_FIELD, false);
            tuning.put(DEDUPLICATION_ENABLED_FIELD, false);
            capacityAutonegotiation.put(AUTO_SIZE_ENABLE_FIELD, false);
            capacityAutonegotiation.put(ADJUSTING_MODE_FIELD, defaultCapacitymode);
        }
        targetParams.put("tuning", tuning);
        targetParams.put("capacity_autonegotiation", capacityAutonegotiation);
    }

    /**
     * updateNfsDatastore
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @PostMapping("/updatenfsdatastore")
    public ResponseBodyBean updateNfsDatastore(final @RequestBody Map<String, Object> params) {
        Map<String, Object> param = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        param.put("dataStoreObjectId", params.get("dataStoreObjectId"));
        param.put(NFS_NAME_FIELD, params.get(NFS_NAME_FIELD));
        param.put("file_system_id", params.get("fileSystemId"));
        param.put("nfs_share_id", params.get("shareId"));
        param.put("name", params.get("fsName"));
        Map<String, Object> capacityAutonegotiation = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        Object autoSizeEnable = params.get(AUTO_SIZE_ENABLE_REQUEST_FIELD);
        if (autoSizeEnable != null) {
            capacityAutonegotiation.put(AUTO_SIZE_ENABLE_FIELD, params.get(AUTO_SIZE_ENABLE_REQUEST_FIELD));
            String capacitymode = (Boolean)autoSizeEnable
                ? CapacityAutonegotiation.CAPACITY_MODE_AUTO
                : CapacityAutonegotiation.CAPACITY_MODE_OFF;
           if (!"grow-off".equalsIgnoreCase(capacitymode)){
                capacityAutonegotiation.put("capacity_recycle_mode","expand_capacity");
                capacityAutonegotiation.put("auto_grow_threshold_percent",85);
                capacityAutonegotiation.put("auto_shrink_threshold_percent",50);
                capacityAutonegotiation.put("max_auto_size", 3.6);
                capacityAutonegotiation.put("min_auto_size",3.0);
                capacityAutonegotiation.put("auto_size_increment",1024);
            }
            capacityAutonegotiation.put(ADJUSTING_MODE_FIELD, capacitymode);
            param.put("capacity_autonegotiation", capacityAutonegotiation);
        }
        Map<String, Object> tuning = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        tuning.put(DEDUPLICATION_ENABLED_FIELD, params.get("deduplicationEnabled"));
        tuning.put(COMPRESSION_ENABLED_FIELD, params.get("compressionEnabled"));
        Object thin = params.get(THIN_FIELD);
        if (thin != null) {
            if ((Boolean) thin) {
                tuning.put(ALLOCATION_TYPE_FIELD, THIN_FIELD);
            } else {
                tuning.put(ALLOCATION_TYPE_FIELD, "thick");
            }
            param.put("tuning", tuning);
        }
        boolean qosFlag = (boolean) params.get("qosFlag");
        if (qosFlag) {
            Map<String, Object> qosPolicy = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
            String contolPolicy = (String) params.get("contolPolicy");
            if (CONTROL_LOW.equals(contolPolicy)) {
                qosPolicy.put("min_bandwidth", Integer.parseInt((String) params.get("minBandwidth")));
                qosPolicy.put("min_iops", Integer.parseInt((String) params.get("minIops")));
                qosPolicy.put(LATENCY_FIELD, Integer.parseInt((String) params.get(LATENCY_FIELD)));
            } else if (CONTROL_UP.equals(contolPolicy)) {
                qosPolicy.put("max_bandwidth", Integer.parseInt((String) params.get("maxBandwidth")));
                qosPolicy.put("max_iops", Integer.parseInt((String) params.get("maxIops")));
            }
            qosPolicy.put("enabled", true);
            param.put("qos_policy", qosPolicy);
        }
        try {
            nfsOperationService.updateNfsDatastore(param);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * changeNfsCapacity
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @PutMapping("/changenfsdatastore")
    public ResponseBodyBean changeNfsCapacity(final @RequestBody Map<String, Object> params) {
        try {
            nfsOperationService.changeNfsCapacity(params);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getEditNfsStore
     *
     * @param storeObjectId storeObjectId
     * @return ResponseBodyBean
     */
    @GetMapping("/editnfsstore")
    public ResponseBodyBean getEditNfsStore(@RequestParam(name = "storeObjectId") String storeObjectId) {
        try {
            return success(nfsOperationService.getEditNfsStore(storeObjectId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}