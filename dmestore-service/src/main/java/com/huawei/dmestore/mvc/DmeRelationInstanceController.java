package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.DmeRelationInstanceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * DmeRelationInstanceController
 *
 * @author liuxh
 * @since 2020-09-15
 **/
@RestController
@RequestMapping(value = "/relationinstance")
public class DmeRelationInstanceController extends BaseController {
    /**
     * LOG
     */
    public static final Logger LOG = LoggerFactory.getLogger(DmeRelationInstanceController.class);

    @Autowired
    DmeRelationInstanceService dmeRelationInstanceService;

    /**
     * queryByRelationName
     *
     * @param relationName relationName
     * @return ResponseBodyBean
     */
    @GetMapping("/listbyrelationname")
    public ResponseBodyBean queryByRelationName(@RequestParam(name = "relationName") String relationName) {
        LOG.info("listbyrelationname:{}", relationName);
        try {
            return success(dmeRelationInstanceService.queryRelationByRelationName(relationName));
        } catch (DmeException e) {
            String errMsg = "queryRelationInstanceByRelationName error,relationName:" + relationName;
            return failure(errMsg);
        }
    }

    /**
     * queryByRelationNameInstanceId
     *
     * @param relationName relationName
     * @param instanceId   instanceId
     * @return ResponseBodyBean
     */
    @GetMapping("/getbyrelationnameinstanceid")
    public ResponseBodyBean queryByRelationNameInstanceId(@RequestParam(name = "relationName") String relationName,
                                                          @RequestParam(name = "instanceId") String instanceId) {
        LOG.info("getbyrelationname instanceid:{}, {}", relationName, instanceId);
        try {
            return success(dmeRelationInstanceService.queryRelationByRelationNameInstanceId(relationName, instanceId));
        } catch (DmeException e) {
            String errMsg = "queryRelationInstanceByRelationNameInstanceId error,relationName:" + relationName;
            return failure(errMsg);
        }
    }

    /**
     * queryByRelationNameInstanceIdCondition
     *
     * @param relationName relationName
     * @param instanceId   instanceId
     * @return ResponseBodyBean
     */
    @GetMapping("/getbyrelationnameinstanceidcondition")
    public ResponseBodyBean queryByRelationNameInstanceIdCondition(
        @RequestParam(name = "relationName") String relationName,
        @RequestParam(name = "instanceId") String instanceId) {
        LOG.info("getbyrelationname instanceid:{},{}", relationName, instanceId);
        try {
            return success(dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(relationName,
                instanceId));
        } catch (DmeException e) {
            String errMsg = "queryRelationInstanceByRelationNameInstanceId error,relationName:" + relationName + " , "
                + instanceId;
            return failure(errMsg);
        }
    }

    /**
     * refreshResourceInstance
     *
     * @return ResponseBodyBean
     * @throws DmeException DmeException
     */
    @GetMapping("/refreshresourceinstance")
    public ResponseBodyBean refreshResourceInstance() throws DmeException {
        LOG.info("refreshresourceinstance instance start!");
        dmeRelationInstanceService.refreshResourceInstance();
        return success(null, "refreshresourceinstance success");
    }
}
