package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.NfsDataInfo;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.DmeNFSAccessService;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping(value = "/accessnfs")
public class NfsAccessController extends BaseController {
    /**
     * LOG
     */
    public static final Logger LOG = LoggerFactory.getLogger(NfsAccessController.class);

    private final Gson gson = new Gson();

    @Autowired
    private DmeNFSAccessService dmeNfsAccessService;

    /**
     * listNfs
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listnfs", method = RequestMethod.GET)
    public ResponseBodyBean listNfs() {
        String failureStr;
        try {
            List<NfsDataInfo> lists = dmeNfsAccessService.listNfs();
            return success(lists);
        } catch (DmeException e) {
            LOG.error("list nfs failure:", e);
            failureStr = "list nfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * listNfsPerformance
     *
     * @param fsIds fsIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listnfsperformance", method = RequestMethod.GET)
    public ResponseBodyBean listNfsPerformance(@RequestParam("fsIds") List<String> fsIds) {
        String failureStr;
        try {
            List<NfsDataInfo> lists = dmeNfsAccessService.listNfsPerformance(fsIds);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("get nfs performance failure:", e);
            failureStr = "get nfs performance failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * mountNfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/mountnfs", method = RequestMethod.POST)
    public ResponseBodyBean mountNfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessnfs/mountnfs=={}", gson.toJson(params));
        String failureStr;
        try {
            dmeNfsAccessService.mountNfs(params);
            return success(null, "Mount nfs success");
        } catch (DmeException e) {
            LOG.error("mount nfs failure:", e);
            failureStr = "mount nfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * unmountNfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/unmountnfs", method = RequestMethod.POST)
    public ResponseBodyBean unmountNfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessnfs/unmountnfs=={}", gson.toJson(params));
        String failureStr;
        try {
            dmeNfsAccessService.unmountNfs(params);
            return success(null, "unmount nfs success");
        } catch (DmeException e) {
            LOG.error("unmount nfs failure:", e);
            failureStr = "unmount nfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * deleteNfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/deletenfs", method = RequestMethod.POST)
    public ResponseBodyBean deleteNfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessnfs/deletenfs=={}", gson.toJson(params));
        String failureStr;
        try {
            dmeNfsAccessService.deleteNfs(params);
            return success(null, "delete nfs success");
        } catch (DmeException e) {
            LOG.error("delte nfs failure:", e);
            failureStr = "delete nfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * getHostsByStorageId
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/gethostsbystorageid/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostsByStorageId(@PathVariable(value = "storageId") String storageId) {
        try {
            List<Map<String, Object>> hosts = dmeNfsAccessService.getHostsMountDataStoreByDsObjectId(storageId);
            return success(hosts);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getHostGroupsByStorageId
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/gethostgroupsbystorageid/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostGroupsByStorageId(@PathVariable(value = "storageId") String storageId) {
        try {
            List<Map<String, Object>> hosts = dmeNfsAccessService.getClusterMountDataStoreByDsObjectId(storageId);
            return success(hosts);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}
