package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.VmwareAccessService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * VmwareAccessController
 *
 * @author yy
 * @since 2020-09-03
 **/
@RestController
@RequestMapping(value = "/accessvmware")
public class VmwareAccessController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VmwareAccessController.class);

    @Autowired
    private VmwareAccessService vmwareAccessService;

    /**
     * Access hosts
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listhost", method = RequestMethod.GET)
    public ResponseBodyBean listHosts() {
        String failureStr = "";
        try {
            return success(vmwareAccessService.listHosts());
        } catch (DmeException e) {
            LOG.error("list vmware host failure:", e);
            failureStr = "list vmware host failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access hosts
     *
     * @param dataStoreObjectId dataStore ObjectId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/gethostsbydsobjectid", method = RequestMethod.GET)
    public ResponseBodyBean getHostsByDsObjectId(@RequestParam("dataStoreObjectId") String dataStoreObjectId) {
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getHostsByDsObjectId(dataStoreObjectId);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("getHostsByDsObjectId vmware host failure:", e);
            failureStr = "getHostsByDsObjectId vmware host failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access clusters
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listcluster", method = RequestMethod.GET)
    public ResponseBodyBean listClusters() {
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.listClusters();
            return success(lists);
        } catch (DmeException e) {
            failureStr = "list vmware cluster failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access clusters
     *
     * @param dataStoreObjectId dataStore ObjectId
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/getclustersbydsobjectid", method = RequestMethod.GET)
    public ResponseBodyBean getClustersByDsObjectId(@RequestParam("dataStoreObjectId") String dataStoreObjectId) {
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getClustersByDsObjectId(dataStoreObjectId);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("getClustersByDsObjectId vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * Access datastore
     *
     * @param hostObjectId host ObjectId
     * @param dataStoreType dataStore Type
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/getdatastoresbyhostobjectid", method = RequestMethod.GET)
    public ResponseBodyBean getDataStoresByHostObjectId(@RequestParam("hostObjectId") String hostObjectId,
        @RequestParam("dataStoreType") String dataStoreType) {
        LOG.info("accessvmware/listhostbystorageObjectId");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getDataStoresByHostObjectId(hostObjectId,
                dataStoreType);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("getDataStoresByHostObjectId vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * Access datastore
     *
     * @param clusterObjectId cluster ObjectId
     * @param dataStoreType dataStore Type
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/getdatastoresbyclusterobjectid", method = RequestMethod.GET)
    public ResponseBodyBean getDataStoresByClusterObjectId(@RequestParam("clusterObjectId") String clusterObjectId,
        @RequestParam("dataStoreType") String dataStoreType) {
        LOG.info("accessvmware/listhostbystorageObjectId");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getDataStoresByClusterObjectId(clusterObjectId,
                dataStoreType);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("getDataStoresByClusterObjectId vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * Get host's vmKernel IP,only provisioning provisioning
     *
     * @param hostObjectId host Object Id
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/getvmkernelipbyhostobjectid", method = RequestMethod.GET)
    public ResponseBodyBean getVmKernelIpByHostObjectId(@RequestParam("hostObjectId") String hostObjectId) {
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getVmKernelIpByHostObjectId(hostObjectId);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("get vmkernel ip by hostobjectid failure:", e);
            failureStr = "get vmkernel ip by hostobjectid failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access datastore
     *
     * @param hostObjectId host ObjectId
     * @param dataStoreType dataStore Type
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/getmountdatastoresbyhostobjectid", method = RequestMethod.GET)
    public ResponseBodyBean getMountDataStoresByHostObjectId(@RequestParam("hostObjectId") String hostObjectId,
        @RequestParam("dataStoreType") String dataStoreType) throws Exception {
        LOG.info("accessvmware/listMountDataStoreByHostObjectId");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getMountDataStoresByHostObjectId(hostObjectId,
                dataStoreType);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("getMountDataStoresByHostObjectId vmware host failure:", e);
            failureStr = "getMountDataStoresByHostObjectId vmware host failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access datastore
     *
     * @param clusterObjectId cluster ObjectId
     * @param dataStoreType dataStore Type
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/getmountdatastoresbyclusterobjectid", method = RequestMethod.GET)
    public ResponseBodyBean getMountDataStoresByClusterObjectId(@RequestParam("clusterObjectId") String clusterObjectId,
        @RequestParam("dataStoreType") String dataStoreType) throws Exception {
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getMountDataStoresByClusterObjectId(clusterObjectId,
                dataStoreType);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("getDataStoresByClusterObjectId vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * getRelationByObjId
     *
     * @param datastoreObjectId datastoreObjectId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/relation", method = RequestMethod.GET)
    public ResponseBodyBean getRelationByObjId(@RequestParam("datastoreObjectId") String datastoreObjectId) {
        try {
            return success(vmwareAccessService.getDmeVmwareRelationByDsId(datastoreObjectId));
        } catch (DmeSqlException e) {
            return failure("get dme volume by datastore failed!" + e.toString());
        }
    }
}
