package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.model.VmfsDataInfo;
import com.huawei.dmestore.model.VmfsDatastoreVolumeDetail;
import com.huawei.dmestore.services.VmfsAccessService;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
 * ServiceLevelController
 *
 * @author yy
 * @since 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accessvmfs")
public class VmfsAccessController extends BaseController {
    /**
     * LOG
     **/
    public static final Logger LOG = LoggerFactory.getLogger(VmfsAccessController.class);

    private Gson gson = new Gson();

    @Autowired
    private VmfsAccessService vmfsAccessService;

    /**
     * listVmfs
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listvmfs", method = RequestMethod.GET)
    public ResponseBodyBean listVmfs() {
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.listVmfs();
            return success(lists);
        } catch (DmeException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * listVmfsPerformance
     *
     * @param wwns wwns
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listvmfsperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listVmfsPerformance(@RequestParam("wwns") List<String> wwns) {
        LOG.info("accessvmfs/listvmfsperformance volumeIds=={}", gson.toJson(wwns));
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.listVmfsPerformance(wwns);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("get vmfs performance failure:", e);
            failureStr = "get vmfs performance failure:" + e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * createVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/createvmfs", method = RequestMethod.POST)
    public ResponseBodyBean createVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/createvmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.createVmfs(params);
            return success(null, "Create vmfs success");
        } catch (DmeException e) {
            failureStr = "create vmfs failure:" + e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * mountVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/mountvmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/mountvmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.mountVmfs(params);
            return success(null, "Mount vmfs success");
        } catch (DmeException e) {
            LOG.error("mount vmfs failure:", e);
            failureStr = "mount vmfs failure:" + e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * unmountVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/ummountvmfs", method = RequestMethod.POST)
    public ResponseBodyBean unmountVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/unmountvmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.unmountVmfs(params);
            return success(null, "unmount vmfs success");
        } catch (DmeException e) {
            LOG.error("unmount vmfs failure:", e);
            failureStr = "unmount vmfs failure:" + e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * deleteVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/deletevmfs", method = RequestMethod.POST)
    public ResponseBodyBean deleteVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/deletevmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.deleteVmfs(params);
            return success();
        } catch (DmeException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * 根据存储ID查询关联的DME卷详细信息
     *
     * @param storageObjectId vCenter存储ID
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     * @author wangxy
     * @date 10:01 2020/10/14
     **/
    @RequestMapping(value = "/volume/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean volumeDetail(@PathVariable(value = "storageObjectId") String storageObjectId) {
        try {
            List<VmfsDatastoreVolumeDetail> detail = vmfsAccessService.volumeDetail(storageObjectId);
            return success(detail);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * VMFS扫描发现
     *
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     * @author wangxy
     * @date 10:25 2020/10/14
     **/
    @RequestMapping(value = "/scanvmfs", method = RequestMethod.GET)
    public ResponseBodyBean scanvmfs() {
        try {
            vmfsAccessService.scanVmfs();
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * 获取指定存储下的主机信息
     *
     * @param storageId 存储ID
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     * @author wangxy
     * @date 10:25 2020/10/14
     */
    @RequestMapping(value = "/gethostsbystorageid/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostsByStorageId(@PathVariable(value = "storageId") String storageId) {
        try {
            List<Map<String, Object>> hosts = vmfsAccessService.getHostsByStorageId(storageId);
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
            List<Map<String, Object>> hosts = vmfsAccessService.getHostGroupsByStorageId(storageId);
            return success(hosts);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * queryVmfs
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @return ResponseBodyBean
     * @throws Exception Exception
     */
    @RequestMapping(value = "/queryvmfs", method = RequestMethod.GET)
    public ResponseBodyBean queryVmfs(@RequestParam("dataStoreObjectId") String dataStoreObjectId) throws Exception {
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.queryVmfs(dataStoreObjectId);
            LOG.info("listvmfs lists=={}", gson.toJson(lists));
            return success(lists);
        } catch (DmeException e) {
            LOG.error("list vmfs failure:", e);
            failureStr = "list vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * queryDatastoreByName
     *
     * @param name name
     * @return ResponseBodyBean
     */
    @GetMapping("/querydatastorebyname")
    public ResponseBodyBean queryDatastoreByName(@RequestParam("name") String name) {
        return success(vmfsAccessService.queryDatastoreByName(name));
    }
}
