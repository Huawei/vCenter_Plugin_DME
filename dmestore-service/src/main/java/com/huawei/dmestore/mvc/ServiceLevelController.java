package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.model.StoragePool;
import com.huawei.dmestore.model.Volume;
import com.huawei.dmestore.services.ServiceLevelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * ServiceLevelController
 *
 * @author liuxh
 * @since 2020-09-02
 **/
@RestController
@RequestMapping(value = "/servicelevel")
public class ServiceLevelController extends BaseController {
    /**
     * LOG
     **/
    public static final Logger LOG = LoggerFactory.getLogger(ServiceLevelController.class);

    @Autowired
    private ServiceLevelService serviceLevelService;

    /**
     * listServiceLevel
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listservicelevel", method = RequestMethod.POST)
    public ResponseBodyBean listServiceLevel(@RequestBody Map<String, Object> params) {
        try {
            return success(serviceLevelService.listServiceLevel(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * listStoragePoolsByServiceLevelId
     *
     * @param serviceLevelId serviceLevelId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listStoragePoolsByServiceLevelId", method = RequestMethod.POST)
    public ResponseBodyBean listStoragePoolsByServiceLevelId(@RequestBody String serviceLevelId) {
        String errMsg = "";
        try {
            List<StoragePool> storagePoolList = serviceLevelService.getStoragePoolInfosByServiceLevelId(serviceLevelId);
            if (null != storagePoolList && storagePoolList.size() > 0) {
                return success(storagePoolList);
            }
        } catch (DmeException e) {
            errMsg = e.getMessage();
        }
        return failure(errMsg);
    }

    /**
     * listVolumesByServiceLevelId
     *
     * @param serviceLevelId serviceLevelId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listVolumesByServiceLevelId", method = RequestMethod.POST)
    public ResponseBodyBean listVolumesByServiceLevelId(@RequestBody String serviceLevelId) {
        String errMsg = "";
        try {
            List<Volume> volumes = serviceLevelService.getVolumeInfosByServiceLevelId(serviceLevelId);
            if (null != volumes && volumes.size() > 0) {
                return success(volumes);
            }
        } catch (DmeException e) {
            errMsg = e.getMessage();
        }
        return failure(errMsg);
    }

    /**
     * manual update service level
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/manualupdate", method = RequestMethod.POST)
    public ResponseBodyBean manualupdate() {
        try {
            serviceLevelService.updateVmwarePolicy();
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * statLun
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/capacity/stat-lun", method = RequestMethod.GET)
    public ResponseBodyBean statLun(@RequestParam String serviceLevelId, @RequestParam String interval) {
        try {
            serviceLevelService.statLunDatasetsQuery(serviceLevelId, interval);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * stat-storage-pool
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/capacity/stat-storage-pool", method = RequestMethod.GET)
    public ResponseBodyBean statStoragePool(@RequestParam String serviceLevelId, @RequestParam String interval) {
        try {
            serviceLevelService.statStoragePoolDatasetsQuery(serviceLevelId, interval);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * perf-stat-lun-details
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/performance/perf-stat-lun-details", method = RequestMethod.GET)
    public ResponseBodyBean lunPerformanceDatasetsQuery(@RequestParam String serviceLevelId,
        @RequestParam String interval) {
        try {
            serviceLevelService.lunPerformanceDatasetsQuery(serviceLevelId, interval);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * perf-stat-storage-pool-details
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/performance/perf-stat-storage-pool-details", method = RequestMethod.GET)
    public ResponseBodyBean storagePoolPerformanceDatasetsQuery(@RequestParam String serviceLevelId,
        @RequestParam String interval) {
        try {
            serviceLevelService.storagePoolPerformanceDatasetsQuery(serviceLevelId, interval);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}
