package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.DmeAccessService;
import com.google.gson.Gson;

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
 * DmeAccessController
 *
 * @author yy
 * @since 2020-12-01
 **/

@RestController
@RequestMapping(value = "/accessdme")
public class DmeAccessController extends BaseController {
    /**
     * LOG
     */
    public static final Logger LOG = LoggerFactory.getLogger(DmeAccessController.class);

    private Gson gson = new Gson();

    @Autowired
    private DmeAccessService dmeAccessService;

    /**
     * accessDme
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/access", method = RequestMethod.POST)
    public ResponseBodyBean accessDme(@RequestBody Map<String, Object> params) {
        LOG.info("accessdme/access params=={}", gson.toJson(params));
        try {
            dmeAccessService.accessDme(params);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * refreshDme
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/refreshaccess", method = RequestMethod.GET)
    public ResponseBodyBean refreshDme() {
        return success(dmeAccessService.refreshDme());
    }

    /**
     * getWorkLoads
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/getworkloads", method = RequestMethod.GET)
    public ResponseBodyBean getWorkLoads(@RequestParam("storageId") String storageId) {
        try {
            List<Map<String, Object>> lists = dmeAccessService.getWorkLoads(storageId);
            return success(lists);
        } catch (DmeException e) {
            return failure("get WorkLoads failure:" + e.toString());
        }
    }

    /**
     * scanDatastore
     *
     * @param storageType storageType
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/scandatastore", method = RequestMethod.GET)
    public ResponseBodyBean scanDatastore(@RequestParam("storageType") String storageType) {
        try {
            LOG.info("DmeAccessController scanDatastore begin!storageType={}", storageType);
            dmeAccessService.scanDatastore(storageType);
            LOG.info("DmeAccessController scanDatastore end!storageType={}", storageType);
            return success(null, "scan datastore complete!");
        } catch (DmeException e) {
            return failure("scan datastore failure:" + e.toString());
        }
    }

    /**
     * configureTaskTime
     *
     * @param taskId   taskId
     * @param taskCron taskCron
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/configuretasktime", method = RequestMethod.GET)
    public ResponseBodyBean configureTaskTime(@RequestParam("taskId") Integer taskId,
                                              @RequestParam("taskCron") String taskCron) {
        try {
            dmeAccessService.configureTaskTime(taskId, taskCron);
            return success(null, "configure task time complete!");
        } catch (DmeException e) {
            return failure("configure task time failure:" + e.toString());
        }
    }
}
