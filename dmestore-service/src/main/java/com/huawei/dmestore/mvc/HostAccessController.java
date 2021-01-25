package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.EthPortInfo;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.HostAccessService;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * HostAccessController
 *
 * @author yy
 * @since 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accesshost")
public class HostAccessController extends BaseController {
    /**
     * LOG
     */
    public static final Logger LOG = LoggerFactory.getLogger(HostAccessController.class);

    private Gson gson = new Gson();

    @Autowired
    private HostAccessService hostAccessService;

    /**
     * configureIscsi
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/configureiscsi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean configureIscsi(@RequestBody Map<String, Object> params) {
        LOG.info("/configureiscsi=={}", gson.toJson(params));
        String failureStr = "";
        try {
            hostAccessService.configureIscsi(params);
            return success(null, "configure iscsi success");
        } catch (DmeException e) {
            LOG.error("configure iscsi failure:", e);
            failureStr = "configure iscsi failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * testConnectivity
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/testconnectivity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean testConnectivity(@RequestBody Map<String, Object> params) {
        LOG.info("/testconnectivity=={}", gson.toJson(params));
        String failureStr = "";
        try {
            List<EthPortInfo> lists = hostAccessService.testConnectivity(params);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("Test connectivity failure:", e);
            failureStr = "Test connectivity failure:" + e.toString();
        }
        return failure(failureStr);
    }
}
