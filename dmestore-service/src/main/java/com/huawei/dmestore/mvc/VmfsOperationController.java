package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.VmfsOperationService;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * VmfsOperationController
 *
 * @author lianqiang
 * @since 2020-09-03
 **/
@RestController
@RequestMapping("/operatevmfs")
public class VmfsOperationController extends BaseController {
    /**
     * LOG
     **/
    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);

    private final Gson gson = new Gson();

    @Autowired
    private VmfsOperationService vmfsOperationService;

    /**
     * vmfs update
     *
     * @param volumeId required
     * @param params {control_policy,max_iops,max_bandwidth,newVoName,newDsName,
     * min_iops,min_bandwidth,String dataStoreObjectId,String service_level_name}
     *
     * @return ResponseBodyBean
     */
    @PutMapping("/updatevmfs")
    public ResponseBodyBean updateVmfs(final @RequestParam(value = "volumeId") String volumeId,
        final @RequestBody Map<String, Object> params) {
        try {
            vmfsOperationService.updateVmfs(volumeId, params);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * expand vmfs datastore
     *
     * @param volumes {int vo_add_capacity 扩容量,String hostObjectId 主机,String ds_name 存储名,String volume_id vmfs所在卷id}
     * @return ResponseBodyBean
     */
    @PostMapping("/expandvmfs")
    public ResponseBodyBean expandVmfs(final @RequestBody Map<String, String> volumes) {
        LOG.info("volumes=={}", gson.toJson(volumes));
        try {
            vmfsOperationService.expandVmfs(volumes);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * recycleVmfs
     *
     * @param dsObjectIds dsObjectIds
     * @return ResponseBodyBean
     */
    @PostMapping("/recyclevmfs")
    public ResponseBodyBean recycleVmfs(final @RequestBody List<String> dsObjectIds) {
        LOG.info("recyclevmfs=={}", gson.toJson(dsObjectIds));
        try {
            vmfsOperationService.canRecycleVmfsCapacity(dsObjectIds);
            vmfsOperationService.recycleVmfsCapacity(dsObjectIds);
            return success();
        } catch (DmeException e) {
            return failure(e.getCode(),e.getMessage());
        }
    }

    /**
     * recycleVmfsByDatastoreIds
     *
     * @param datastoreIds datastoreIds
     * @return ResponseBodyBean
     */
    @PostMapping("/recyclevmfsbydatastoreids")
    public ResponseBodyBean recycleVmfsByDatastoreIds(final @RequestBody List<String> datastoreIds) {
        LOG.info("recyclevmfsbydatastoreids=={}", gson.toJson(datastoreIds));
        try {
            boolean canrecycle=vmfsOperationService.canRecycleVmfsCapacity(datastoreIds);
            if (!canrecycle) {
                return failure("20883", "not support recycle thick vmfsDatastore ");
            }
            vmfsOperationService.recycleVmfsCapacityByDataStoreIds(datastoreIds);
            return success();
        } catch (DmeException e) {
            return failure(e.getCode(),e.getMessage());
        }
    }

    /**
     * canrecyclevmfsbydatastoreid
     *
     * @param datastoreId datastoreId
     * @return ResponseBodyBean
     */
    @PostMapping("/canrecyclevmfsbydatastoreid")
    public ResponseBodyBean canRecycleVmfsbyDatastoreid(final @RequestBody String datastoreId) {
        LOG.info("canrecyclevmfsbydatastoreid=={}", gson.toJson(datastoreId));
        try {
            List<String> datastoreids = new ArrayList<>();
            datastoreids.add(datastoreId);
            return success(vmfsOperationService.canRecycleVmfsCapacity(datastoreids));
        } catch (DmeException e) {
            return failure(e.getCode(),e.getMessage());
        }
    }

    /**
     * listServiceLevelVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @PutMapping("/listvmfsservicelevel")
    public ResponseBodyBean listServiceLevelVmfs(final @RequestBody(required = false) Map<String, Object> params) {
        LOG.info("recyclevmfs=={}", gson.toJson(params));
        try {
            return success(vmfsOperationService.listServiceLevelVmfs(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * {
     * "service_level_id" : "b1da0725-2456-4d37-9caf-ad0a4644d10e",
     * "attributes_auto_change" : true,
     * "volume_ids" : [ "a0da0725-2456-4d37-9caf-ad0a4644d10e" ]
     * }
     *
     * @param params {service_level_id,attributes_auto_change,volume_ids}
     * @return ResponseBodyBean
     */
    @PostMapping("updatevmfsservicelevel")
    public ResponseBodyBean updateServiceLevelVmfs(final @RequestBody Map<String, Object> params) {
        LOG.info("servicelevelvmfs=={}", gson.toJson(params));
        try {
            vmfsOperationService.updateVmfsServiceLevel(params);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}
